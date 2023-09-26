package pos.PlaylistV2.Controller;

import org.json.JSONException;
import org.springframework.hateoas.Link;
import pos.PlaylistV2.Model.Entities.Playlist;
import pos.PlaylistV2.Model.Exceptions.AuthorizationFailedException;
import pos.PlaylistV2.Model.Exceptions.CollectionOfPlaylistsNotFound;
import pos.PlaylistV2.Model.Exceptions.MusicNotFound;
import pos.PlaylistV2.Model.Exceptions.PlaylistNotFoundException;
import pos.PlaylistV2.Model.Services.AuthorizationService;
import pos.PlaylistV2.Model.Services.PlaylistService;
import pos.PlaylistV2.View.DTOs.PlaylistDto;
import pos.PlaylistV2.View.DTOs.PlaylistPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static pos.PlaylistV2.Model.Util.Conversion.*;
import static pos.PlaylistV2.View.HATEOAS.PlaylistHATEOAS.createHATEOAS;

@RestController
@RequestMapping("api/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("")
    public ResponseEntity<?> listPlaylists() {
        try {
            List<Playlist> playlists = playlistService.listAllPlaylists();
            List<PlaylistDto> playlistsDto = getDtosWithHATEOASFromPlaylists(playlists);
            Link allSongsLinks = createHATEOAS(playlistsDto);
            return new ResponseEntity<>(List.of(playlistsDto, allSongsLinks), HttpStatus.OK);
        } catch (CollectionOfPlaylistsNotFound e) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        try {
            Playlist playlist = playlistService.getPlaylist(id);
            PlaylistDto playlistDto = getDtoFromPlaylist(playlist);
            createHATEOAS(playlistDto);
            return new ResponseEntity<>(playlistDto, HttpStatus.OK);
        } catch (PlaylistNotFoundException e) {
            Link parent = linkTo(methodOn(PlaylistController.class).listPlaylists()).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody PlaylistPOJO playlistPOJO, @RequestHeader (name="Authorization") String token) {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            Integer id = Integer.valueOf(auth.split("-")[0]);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("client") || !id.equals(playlistPOJO.getUser_id()))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            Playlist playlist = getPlaylistFromPOJO(playlistPOJO);
            playlistService.savePlaylist(playlist);
            PlaylistDto playlistDto = getDtoFromPlaylist(playlist);
            createHATEOAS(playlistDto);
            return new ResponseEntity<>(playlistDto, HttpStatus.CREATED);
        }catch(MusicNotFound | JSONException e){
            //aici e atomica tranzactia, ori reuseste adaugarea, ori nu se intampla nimic
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping(value="/{id}")
    public ResponseEntity<?> updatePlaylist(@PathVariable String id, @RequestBody PlaylistPOJO playlistPOJO, @RequestParam(required=false) String operation, @RequestHeader (name="Authorization") String token)
    {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            Integer idUser = Integer.valueOf(auth.split("-")[0]);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("client") || !idUser.equals(playlistPOJO.getUser_id()))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            Playlist playlist = playlistService.getPlaylist(id);
            playlistService.updatePlaylist(playlist, playlistPOJO, operation);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (PlaylistNotFoundException e) {
            Link parent = linkTo(methodOn(PlaylistController.class).listPlaylists()).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
        catch(MusicNotFound e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, @RequestHeader (name="Authorization") String token) {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("client"))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            playlistService.deletePlaylist(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(PlaylistNotFoundException e){
            Link parent = linkTo(methodOn(PlaylistController.class).listPlaylists()).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }

}
