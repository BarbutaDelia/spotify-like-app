package com.example.demo.Model.Util;

import com.example.demo.Controller.MusicController;
import com.example.demo.Model.Entities.Artist;
import com.example.demo.Model.Entities.Music;
import com.example.demo.View.DTOs.ArtistDto;
import com.example.demo.View.DTOs.MusicDto;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.example.demo.View.HATEOAS.MusicHATEOAS.createHATEOAS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class Conversion {
    public static Music getMusicFromDto(MusicDto musicDto, Music album){
        return new Music(musicDto.getName(), musicDto.getMusic_genre(), musicDto.getYear_of_release(), musicDto.getMusic_type(), album);
    }

    public static MusicDto getDtoFromMusic(Music music){
        if(music.getAlbum()!= null)
            return new MusicDto(music.getId(), music.getName(), music.getMusic_genre(), music.getYear_of_release(), music.getMusic_type(), music.getAlbum().getId());
        else
            return new MusicDto(music.getId(), music.getName(), music.getMusic_genre(), music.getYear_of_release(), music.getMusic_type(), null);

    }

    public static Artist getArtistFromDto(ArtistDto artistDto){
        return new Artist(artistDto.getUuid(), artistDto.getName(), artistDto.getIs_active());
    }

    public static ArtistDto getDtoFromArtist(Artist artist){
        return new ArtistDto(artist.getUuid(), artist.getName(), artist.getIs_active());
    }

    public static ResponseEntity<?> getDtoFromMusicListWithPagination(@RequestParam Optional<Integer> page, @RequestParam(defaultValue = "10") Optional<Integer> items_per_page, List<Music> songs, List<MusicDto> songsDto, Integer nrOfPages) {
        if(songs.size() > items_per_page.get()) {
            for (Integer i = 0; i < items_per_page.get(); i++) {
                try {
                    songsDto.add(getDtoFromMusic(songs.get((page.get() - 1) * items_per_page.get() + i)));
                } catch (IndexOutOfBoundsException e) {
                    if(songsDto.isEmpty()){
                        Link parent = linkTo(methodOn(MusicController.class).listMusic(Optional.of(Integer.parseInt(page.get().toString()) - 1), items_per_page, Optional.empty(), Optional.empty())).withRel("parent");
                        //TODO aici trebuie modificat! sa dai link la pagina anterioara ca parent
                        return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
                    }
                }
            }
        }
        else {
            for (Music music : songs) {
                songsDto.add(getDtoFromMusic(music));
            }
        }
        List<Link> allSongsLinks = createHATEOAS(songsDto, page, items_per_page, nrOfPages);
        return new ResponseEntity<>(List.of(songsDto, allSongsLinks), HttpStatus.OK);
    }

    public static List<ArtistDto> getListOfArtistDtosFromArtists(List<Artist> artists){
        List<ArtistDto> artistsDto = new ArrayList<>();
        for (Artist artist : artists) {
            artistsDto.add(getDtoFromArtist(artist));
        }
        return artistsDto;
    }

    public static List<MusicDto> getListOfMusicDtosFromSongs(Collection<Music> songs){
        List<MusicDto> artistSongsDto = new ArrayList<>();
        for (Music song : songs) {
            artistSongsDto.add(getDtoFromMusic(song));
        }
        return artistSongsDto;
    }
}
