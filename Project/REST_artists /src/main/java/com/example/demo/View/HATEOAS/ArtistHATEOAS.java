package com.example.demo.View.HATEOAS;

import com.example.demo.Controller.ArtistController;
import com.example.demo.View.DTOs.ArtistDto;
import com.example.demo.View.DTOs.ArtistDto;
import com.example.demo.View.DTOs.MusicDto;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ArtistHATEOAS {
    public static void createHATEOAS(ArtistDto artist) {
        artist.add(linkTo(methodOn(ArtistController.class).get(artist.getUuid())).withSelfRel());
        artist.add(linkTo(methodOn(ArtistController.class).listArtists(Optional.empty(), Optional.empty())).withRel("parent"));
    }

    public static Link createHATEOAS(List<ArtistDto> artist) {
        artist.forEach(ArtistHATEOAS::createHATEOAS);
        return linkTo(methodOn(ArtistController.class).listArtists(Optional.empty(), Optional.empty())).withSelfRel();
    }

    public static Link createHATEOASForSongsOfArtist(String uuid) {
        return linkTo(methodOn(ArtistController.class).getSongsForArtist(uuid)).withRel("parent");
    }

    public static Link createHATEOASForSongsOfArtist() {
        return linkTo(methodOn(ArtistController.class).listArtists(Optional.empty(), Optional.empty())).withRel("parent");
    }
}
