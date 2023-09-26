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
public class ArtistDto extends RepresentationModel<ArtistDto> {
    private String uuid;
    private String name;
    private Boolean is_active;

    public ArtistDto(String uuid, String name, Boolean isActive){
        this.uuid = uuid;
        this.name = name;
        this.is_active = isActive;
    }
}
//TODO cred ca trebuie <Artist>
