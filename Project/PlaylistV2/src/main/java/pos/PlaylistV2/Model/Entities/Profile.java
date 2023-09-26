package pos.PlaylistV2.Model.Entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import pos.PlaylistV2.View.DTOs.MusicDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private String id;
    private Integer user_id;
    private String first_name;
    private String last_name;
    private String email;
    private List<MusicDto> likedMusic = new ArrayList<>();
    private List<Playlist> playlists = new ArrayList<>();

    public void addSongToLikedMusic(MusicDto music){
        likedMusic.add(music);
    }
    public void removeSongFromLikedMusic(MusicDto music){
        likedMusic.remove(music);
    }
    public void addPlaylist(Playlist p){
        playlists.add(p);
    }
    public void removePlaylist(Playlist p){
        playlists.remove(p);
    }

//    public Profile(Integer idUser, Optional<String> firstName,Optional<String> lastName,Optional<String> email)
//    {
//        this.idUser = idUser;
//
//        if(firstName != null) {
//            if (firstName.isPresent() && !firstName.isEmpty()) {
//                this.firstName = Optional.of(firstName).get();
//            }
//        }
//        if(lastName != null) {
//            if(lastName.isPresent() && !lastName.isEmpty()) {
//                this.lastName = Optional.of(lastName).get();
//            }
//        }
//        if(email != null) {
//            if(email.isPresent() && !email.isEmpty()) {
//                this.email = Optional.of(email).get();
//            }
//        }
//    }
}
