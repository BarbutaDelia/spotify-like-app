package com.example.demo.Controller;

import com.example.demo.Model.Entities.Artist;
import com.example.demo.Model.Entities.Music;
import com.example.demo.Model.Exceptions.*;
import com.example.demo.Model.Services.ArtistService;
import com.example.demo.Model.Services.AuthorizationService;
import com.example.demo.Model.Services.MusicService;
import com.example.demo.View.DTOs.ArtistDto;
import com.example.demo.View.DTOs.ArtistDtoIn;
import com.example.demo.View.DTOs.MusicDto;
import com.example.demo.View.DTOs.SongOfArtistDto;
import com.example.demo.View.HATEOAS.ArtistHATEOAS;
import com.example.demo.View.HATEOAS.MusicHATEOAS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.View.HATEOAS.ArtistHATEOAS.createHATEOASForSongsOfArtist;
import static com.example.demo.View.HATEOAS.MusicHATEOAS.createHATEOAS;

import java.util.*;

import static com.example.demo.Model.Util.Conversion.*;
import static com.example.demo.View.HATEOAS.ArtistHATEOAS.createHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/artists")
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @Autowired
    private MusicService musicService;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("")
    public ResponseEntity<?> listArtists(@RequestParam Optional<String> name, @RequestParam Optional<String> match) {
        if(name.isEmpty() && match.isEmpty()) {
            try {
                List<Artist> artists = artistService.listAllArtists();
                List<ArtistDto> artistsDto = getListOfArtistDtosFromArtists(artists);
                Link allSongsLinks = createHATEOAS(artistsDto);
                return new ResponseEntity<>(List.of(artistsDto, allSongsLinks), HttpStatus.OK);
            } catch (CollectionOfArtistsNotFound e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        if(name.isPresent() && match.isPresent()){
            List<Artist> artists = artistService.listAllArtistsByName(name.get());
            List<ArtistDto> artistsDto = getListOfArtistDtosFromArtists(artists);
            Link allSongsLinks = createHATEOAS(artistsDto);
            return new ResponseEntity<>(List.of(artistsDto, allSongsLinks), HttpStatus.OK);
        }
        if(name.isPresent()){
            List<Artist> artists = artistService.listAllArtistsByPartialName(name.get());
            List<ArtistDto> artistsDto = getListOfArtistDtosFromArtists(artists);
            Link allSongsLinks = createHATEOAS(artistsDto);
            return new ResponseEntity<>(List.of(artistsDto, allSongsLinks), HttpStatus.OK);
        }
        return new ResponseEntity<>(List.of(), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> get(@PathVariable String uuid) {
        try {
            Artist artist = artistService.getArtist(uuid);
            ArtistDto artistDto = getDtoFromArtist(artist);
            createHATEOAS(artistDto);
            return new ResponseEntity<>(artistDto, HttpStatus.OK);
        } catch (ArtistNotFoundException e) {
            Link parent = linkTo(methodOn(ArtistController.class).listArtists(Optional.empty(), Optional.empty())).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{uuid}")
    public ResponseEntity<?> update(@RequestBody ArtistDtoIn artistDtoIn, @PathVariable String uuid, @RequestHeader (name="Authorization") String token) {
//        System.out.println(token);
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("content manager"))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        ArtistDto artistDto = new ArtistDto(uuid, artistDtoIn.getName(), artistDtoIn.getIs_active());
        try {
            artistService.getArtist(uuid);
            artistDto.setUuid(uuid);
            Artist newArtist = getArtistFromDto(artistDto);
            artistService.saveArtist(newArtist);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //nu ii dai repr
        }
        catch(ArtistNotFoundException a){
            Artist artist = getArtistFromDto(artistDto);
            artist.setUuid(uuid);
            artistService.saveArtist(artist);
            createHATEOAS(artistDto);
            return new ResponseEntity<>(artistDto, HttpStatus.CREATED); //ii dai reprezentarea
        }
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(@PathVariable String uuid, @RequestHeader (name="Authorization") String token) {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("content manager"))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            artistService.deleteArtist(uuid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(ArtistNotFoundException e){
            Link parent = linkTo(methodOn(ArtistController.class).listArtists(Optional.empty(), Optional.empty())).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{uuid}/songs")
    public ResponseEntity<?> getSongsForArtist(@PathVariable String uuid) {
        try {
            Artist artist = artistService.getArtist(uuid);
            Collection<Music> artistSongs = artist.getSongs();
            List<MusicDto> artistSongsDto = getListOfMusicDtosFromSongs(artistSongs);
            createHATEOAS(artistSongsDto);
            Link self = linkTo(methodOn(ArtistController.class).getSongsForArtist(uuid)).withRel("self");
            Link parent = linkTo(methodOn(ArtistController.class).get(uuid)).withRel("parent");
            return new ResponseEntity<>(List.of(artistSongsDto, List.of(self, parent)), HttpStatus.OK);
        } catch (ArtistNotFoundException e) {
            Link parent = linkTo(methodOn(ArtistController.class).get(uuid)).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{uuid}/songs/{song_id}")
    public ResponseEntity<?> addSongToArtist(@PathVariable String uuid, @PathVariable String song_id, @RequestHeader (name="Authorization") String token) {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("content manager"))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            Artist artist = artistService.getArtist(uuid);
            try {
                Music music = musicService.getMusic(Integer.parseInt(song_id));
                artist.addSong(music);
                artistService.saveArtist(artist);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (MusicNotFoundException e) {
                Link parentLink = createHATEOASForSongsOfArtist(uuid);
                return new ResponseEntity<>(parentLink, HttpStatus.NOT_FOUND);
            }
        }
        catch(ArtistNotFoundException e){
            Link parentLink = createHATEOASForSongsOfArtist();
            return new ResponseEntity<>(parentLink, HttpStatus.NOT_FOUND);
        }
    }
}
