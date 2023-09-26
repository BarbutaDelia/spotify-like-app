package pos.PlaylistV2.View.DTOs;

import lombok.*;
import org.springframework.data.annotation.Id;
import pos.PlaylistV2.Model.Entities.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class ProfilePOJO {
    private String id;
    private Integer user_id;
    private Optional<String> first_name;
    private Optional<String> last_name;
    private Optional<String> email;
    private List<Integer> likedMusic_ids = new ArrayList<>();
    private List<String> playlist_ids = new ArrayList<>();

    public ProfilePOJO(String id, Integer user_id, Optional<String> first_name, Optional<String> last_name, Optional<String> email, List<Integer> likedMusic, List<String> playlists){
        this.id = id;
        this.user_id = user_id;
        if(first_name != null) {
            if (first_name.isPresent())
                this.first_name = Optional.of(first_name).get();
        }
        if(last_name != null) {
            if (last_name.isPresent())
                this.last_name = Optional.of(last_name).get();
        }
        if(email != null) {
            if (email.isPresent())
                this.email = Optional.of(email).get();
        }
        this.likedMusic_ids = likedMusic;
        this.playlist_ids = playlists;
    }

}
