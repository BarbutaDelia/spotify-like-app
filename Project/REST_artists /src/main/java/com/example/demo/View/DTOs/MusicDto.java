package com.example.demo.View.DTOs;

import com.example.demo.Model.Entities.Music;
import com.example.demo.Model.Enums.Genre;
import com.example.demo.Model.Enums.Type;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Data
@Getter
@Setter
public class MusicDto extends RepresentationModel<MusicDto> {
    private Integer id;
    private String name;
    private Genre music_genre;
    private Integer year_of_release;
    private Type music_type;
    private Integer album_id;

    public MusicDto(Integer id, String name, Genre music_genre, Integer year_of_release, Type music_type, Integer album_id){
        this.id = id;
        this.name = name;
        this.music_genre = music_genre;
        this.year_of_release = year_of_release;
        this.music_type = music_type;
        this.album_id = album_id;
    }
}