package pos.PlaylistV2.View.DTOs;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDto extends RepresentationModel<PlaylistDto> {
    private String id;
    private Integer user_id;
    private String name;
    private List<MusicDto> songs;

}

