package pos.PlaylistV2.Model.Entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.hateoas.RepresentationModel;
import pos.PlaylistV2.View.DTOs.MusicDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    private @Id
    String id;
    private Integer user_id;
    private String name;
    private List<MusicDto> songs = new ArrayList<>();

    public void addSong(MusicDto song){
        songs.add(song);
    }

    public void removeSong(MusicDto song){
        songs.remove(song);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Playlist))
            return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(this.id, playlist.id) && Objects.equals(this.name, playlist.name)
                 && Objects.equals(this.songs, playlist.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.songs);
    }

    @Override
    public String toString() {
        StringBuilder resBuilder = new StringBuilder("Playlist{" + "id=" + this.id + ", name='" + this.name + '\'');
        for (MusicDto song : songs)
        {
            resBuilder.append(" song_id = ").append(song.getId()).append("\n");
            resBuilder.append(" song_name = ").append(song.getName()).append("\n");
            resBuilder.append(" link = ").append(song.getSelfLink()).append("\n");
        }
        String res = resBuilder.toString();

        res +=  '}';
        return res;
    }
}