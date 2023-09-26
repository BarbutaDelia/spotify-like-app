package com.example.demo.View.DTOs;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class SongOfArtistDto {
    private List<Integer> song_ids = new ArrayList<>();

    public SongOfArtistDto(List<Integer> song_ids){
        this.song_ids.addAll(song_ids);
    }

    public SongOfArtistDto(){
        this.song_ids = new ArrayList<>();
    }
}
