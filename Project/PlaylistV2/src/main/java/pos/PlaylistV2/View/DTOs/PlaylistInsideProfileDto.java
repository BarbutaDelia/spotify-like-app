package pos.PlaylistV2.View.DTOs;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistInsideProfileDto {
    private List<String> playlist_ids;
}
