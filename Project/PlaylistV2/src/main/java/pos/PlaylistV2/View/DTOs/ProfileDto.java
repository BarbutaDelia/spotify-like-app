package pos.PlaylistV2.View.DTOs;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pos.PlaylistV2.Model.Entities.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto extends RepresentationModel<ProfileDto> {
    private String id;
    private Integer user_id;
    private String first_name;
    private String last_name;
    private String email;
    private List<MusicDto> likedMusic = new ArrayList<>();
    private List<Playlist> playlists = new ArrayList<>();
    //Dto cu subplaylist unde am nume si id
}
