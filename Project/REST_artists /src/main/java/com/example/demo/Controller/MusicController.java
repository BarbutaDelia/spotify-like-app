package com.example.demo.Controller;

import com.example.demo.Model.Entities.Artist;
import com.example.demo.Model.Enums.Type;
import com.example.demo.Model.Entities.Music;
import com.example.demo.Model.Exceptions.AlbumNotFoundException;
import com.example.demo.Model.Exceptions.AuthorizationFailedException;
import com.example.demo.Model.Exceptions.CollectionOfMusicNotFoundException;
import com.example.demo.Model.Exceptions.MusicNotFoundException;
import com.example.demo.Model.Services.AuthorizationService;
import com.example.demo.View.DTOs.ArtistDto;
import com.example.demo.View.DTOs.MusicDto;
import com.example.demo.Model.Services.MusicService;
import com.example.demo.View.HATEOAS.ArtistHATEOAS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.demo.Model.Util.Conversion.*;
import static com.example.demo.View.HATEOAS.MusicHATEOAS.createHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/songs")
public class MusicController {
    @Autowired
    private MusicService musicService;

    @Autowired
    private AuthorizationService authorizationService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("")
    public ResponseEntity<?> listMusic(@RequestParam Optional<Integer> page, @RequestParam(defaultValue = "10") Optional<Integer> items_per_page, @RequestParam Optional<String> name, @RequestParam Optional<String> match) {
        // cazul in care nu avem paginare si nu cautam dupa nume
        if(page.isEmpty() && name.isEmpty() && match.isEmpty()) {
            try {
                List<Music> songs = musicService.listAllMusic();
                List<MusicDto> songsDto = new ArrayList<>();
                for (Music song : songs) {
                    songsDto.add(getDtoFromMusic(song));
                }
                Link allSongsLinks = createHATEOAS(songsDto);
                return new ResponseEntity<>(List.of(songsDto, allSongsLinks), HttpStatus.OK);
            } catch (CollectionOfMusicNotFoundException e) {
                return new ResponseEntity<>(List.of(), HttpStatus.OK);
            }
        }
        //cazul in care nu avem paginare si facem cautare exacta dupa nume
        if(name.isPresent() && match.isPresent() && page.isEmpty()){
            List<Music> songs = musicService.listAllMusicByName(name.get());
            List<MusicDto> songsDto = new ArrayList<>();
                for (Music music : songs) {
                    songsDto.add(getDtoFromMusic(music));
            }
            Link allSongsLinks = createHATEOAS(songsDto, page, items_per_page, name, match);
            return new ResponseEntity<>(List.of(songsDto, allSongsLinks), HttpStatus.OK);
        }

        //cazul in care nu avem paginare si facem cautare partiala dupa nume
        if(name.isPresent() && match.isEmpty() && page.isEmpty()){
            List<Music> songs = musicService.listAllMusicByPartialName(name.get());
            List<MusicDto> songsDto = new ArrayList<>();
            for (Music music : songs) {
                songsDto.add(getDtoFromMusic(music));
            }
            Link allSongsLinks = createHATEOAS(songsDto, page, items_per_page, name, match);
            return new ResponseEntity<>(List.of(songsDto, allSongsLinks), HttpStatus.OK);
        }

        //cazul in care avem paginare si facem cautare exacta dupa nume
        if(name.isPresent() && match.isPresent() && page.isPresent()){
            List<Music> songs = musicService.listAllMusicByName(name.get());
            List<MusicDto> songsDto = new ArrayList<>();
            Integer nrOfPages = Integer.parseInt(songs.size()  + "") / items_per_page.get() + Integer.parseInt("1");
            return getDtoFromMusicListWithPagination(page, items_per_page, songs, songsDto, nrOfPages);
        }

        //cazul in care avem paginare si facem cautare partiala dupa nume
        if(name.isPresent() && match.isEmpty() && page.isPresent()){
            List<Music> songs = musicService.listAllMusicByPartialName(name.get());
            Integer nrOfPages = Integer.parseInt(songs.size()  + "") / items_per_page.get() + Integer.parseInt("1");
            List<MusicDto> songsDto = new ArrayList<>();
            return getDtoFromMusicListWithPagination(page, items_per_page, songs, songsDto, nrOfPages);
        }

        //cazul in care avem paginare si nu facem nici o cautare dupa nume
        else{
            System.out.println(items_per_page);
            try {
                List<Music> songs = musicService.listAllMusic();
                Integer nrOfPages = Integer.parseInt(songs.size()  + "") / items_per_page.get() + Integer.parseInt("1");
                List<MusicDto> songsDto = new ArrayList<>();
                return getDtoFromMusicListWithPagination(page, items_per_page, songs, songsDto, nrOfPages);
            } catch (CollectionOfMusicNotFoundException e) {
                return new ResponseEntity<>(List.of(), HttpStatus.OK);
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            Music music = musicService.getMusic(id);
            MusicDto musicDto = getDtoFromMusic(music);
            createHATEOAS(musicDto);
            return new ResponseEntity<>(musicDto, HttpStatus.OK);
        } catch (MusicNotFoundException e) {
            Link parent = linkTo(methodOn(MusicController.class).listMusic(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody MusicDto musicDto, @RequestHeader (name="Authorization") String token) {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("content manager") && !role.equals("artist") )
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        if(musicDto.getYear_of_release() > Integer.parseInt(format.format(new Date()))){
            return new ResponseEntity<>("Year cannot be in the future", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if(musicDto.getAlbum_id() != null) {
            try {
                Music album = musicService.getMusic(musicDto.getAlbum_id());
                if (musicDto.getMusic_type() == Type.album)
                    return new ResponseEntity<>("Album cannot reference another album.", HttpStatus.CONFLICT);
                if (musicDto.getMusic_type() == Type.single)
                    return new ResponseEntity<>("Single cannot belong to an album.", HttpStatus.CONFLICT);
                if (album.getMusic_type() == Type.album) {
                    Music music = getMusicFromDto(musicDto, album);
                    try {
                        musicService.saveMusic(music);
                    }
                    catch(DataAccessException e){
                        //reprez nu coresp cu ce e in baza de date
                        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
                    }
                    createHATEOAS(musicDto);
                    return new ResponseEntity<>(musicDto.getLinks(), HttpStatus.CREATED);
                }
            }
            catch(MusicNotFoundException e){
                return new ResponseEntity<>("Invalid album_id.", HttpStatus.CONFLICT);
            }
        }
        Music music = getMusicFromDto(musicDto, null);
        musicService.saveMusic(music);
        MusicDto musicDtofinal = getDtoFromMusic(music);
        createHATEOAS(musicDtofinal);
        return new ResponseEntity<>(musicDtofinal, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, @RequestHeader (name="Authorization") String token) {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("content manager") && !role.equals("artist") )
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            musicService.deleteMusic(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(MusicNotFoundException e){
            Link parent = linkTo(methodOn(MusicController.class).listMusic(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }
}
