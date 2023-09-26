package com.example.demo.View.HATEOAS;

import com.example.demo.Controller.ArtistController;
import com.example.demo.Controller.MusicController;
import com.example.demo.Model.Entities.Artist;
import com.example.demo.Model.Entities.Music;
import com.example.demo.View.DTOs.ArtistDto;
import com.example.demo.View.DTOs.MusicDto;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class MusicHATEOAS {
    public static void createHATEOAS(MusicDto music){
        music.add(linkTo(methodOn(MusicController.class).get(music.getId())).withSelfRel());
        music.add(linkTo(methodOn(MusicController.class).listMusic(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())).withRel("parent"));
    }

    public static void createHATEOAS(MusicDto music, Optional page, Optional items_per_page, Optional name, Optional match){
        music.add(linkTo(methodOn(MusicController.class).get(music.getId())).withSelfRel());
        music.add(linkTo(methodOn(MusicController.class).listMusic(page, items_per_page, name, match)).withRel("parent"));
    }

    public static void createHATEOAS(MusicDto music, Optional page, Optional items_per_page){
        music.add(linkTo(methodOn(MusicController.class).get(music.getId())).withSelfRel());
        music.add(linkTo(methodOn(MusicController.class).listMusic(page, items_per_page, Optional.empty(), Optional.empty())).withRel("parent"));
    }

    public static Link createHATEOAS(List<MusicDto> music){
        music.forEach(MusicHATEOAS::createHATEOAS);
        return linkTo(methodOn(MusicController.class).listMusic(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())).withSelfRel();
    }

    public static Link createHATEOAS(List<MusicDto> music, Optional page, Optional items_per_page, Optional name, Optional match){
        music.forEach(MusicHATEOAS::createHATEOAS);
        return linkTo(methodOn(MusicController.class).listMusic(page, items_per_page, name, match)).withSelfRel();
    }

//    public static Link createHATEOAS(List<MusicDto> music, ArtistDto artistDto){
//        music.forEach(MusicHATEOAS::createHATEOASForSongsOfArtist);
//        return linkTo(methodOn(ArtistController.class).getSongsForArtist(artistDto.getUuid()).withSelfRel();
//    }
//
//    public static void createHATEOASForSongsOfArtist(MusicDto song, ArtistDto artist) {
//        song.add(linkTo(methodOn(ArtistController.class).getSongsForArtist(artist.getUuid())).withSelfRel());
//        song.add(linkTo(methodOn(ArtistController.class).get(artist.getUuid())).withRel("parent"));
//    }

    public static ArrayList<Link> createHATEOASForMoreLinks(List<MusicDto> music, Optional page, Optional items_per_page, Optional name, Optional match){
        ArrayList<Link> links = new ArrayList<Link>();
        music.forEach(MusicHATEOAS::createHATEOAS);
        Link link1 =  linkTo(methodOn(MusicController.class).listMusic(page, items_per_page, name, match)).withSelfRel();
        Link prevLink = linkTo(methodOn(MusicController.class).listMusic(page, items_per_page, name, match)).withRel("prevLink");
        links.add(link1);
        links.add(prevLink);
        return links;
    }

    public static ArrayList<Link> createHATEOAS(List<MusicDto> music, Optional page, Optional items_per_page, Integer nrOfPages){
        ArrayList<Link> links = new ArrayList<>();
        music.forEach(MusicHATEOAS::createHATEOAS);
        Link link1 = linkTo(methodOn(MusicController.class).listMusic(page, items_per_page, Optional.empty(), Optional.empty())).withSelfRel();
        Link link2 = linkTo(methodOn(MusicController.class).listMusic(Optional.of(Integer.parseInt(page.get().toString()) - 1), items_per_page, Optional.empty(), Optional.empty())).withRel("previousLink");
        Link link3 = linkTo(methodOn(MusicController.class).listMusic(Optional.of(Integer.parseInt(page.get().toString()) + 1), items_per_page, Optional.empty(), Optional.empty())).withRel("nextLink");
        links.add(link1);
        if(Integer.parseInt(page.get().toString()) != 1) {
            links.add(link2);
        }
        if(Integer.parseInt(page.get().toString()) != nrOfPages) {
            links.add(link3);
        }
        return links;
    }

}
