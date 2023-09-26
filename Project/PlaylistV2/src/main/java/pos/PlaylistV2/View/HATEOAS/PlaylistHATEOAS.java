package pos.PlaylistV2.View.HATEOAS;

import org.springframework.hateoas.Link;
import pos.PlaylistV2.Controller.PlaylistController;
import pos.PlaylistV2.View.DTOs.PlaylistDto;
import pos.PlaylistV2.View.DTOs.PlaylistPOJO;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class PlaylistHATEOAS {
    public static void createHATEOAS(PlaylistDto playlist){
        playlist.add(linkTo(methodOn(PlaylistController.class).get(playlist.getId())).withSelfRel());
        playlist.add(linkTo(methodOn(PlaylistController.class).listPlaylists()).withRel("parent"));
    }
    public static Link createHATEOAS(List<PlaylistDto> playlists){
        playlists.forEach(PlaylistHATEOAS::createHATEOAS);
        return linkTo(methodOn(PlaylistController.class).listPlaylists()).withSelfRel();
    }
}
