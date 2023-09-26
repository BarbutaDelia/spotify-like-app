package pos.PlaylistV2.Controller;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.PlaylistV2.Model.Entities.Playlist;
import pos.PlaylistV2.Model.Entities.Profile;
import pos.PlaylistV2.Model.Exceptions.*;
import pos.PlaylistV2.Model.Services.AuthorizationService;
import pos.PlaylistV2.Model.Services.PlaylistService;
import pos.PlaylistV2.Model.Services.ProfileService;
import pos.PlaylistV2.View.DTOs.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static pos.PlaylistV2.Model.Util.Conversion.*;
import static pos.PlaylistV2.View.HATEOAS.ProfileHATEOAS.createHATEOAS;



@RestController
//@CrossOrigin(origins = "http://localhost:3000")//, methods = RequestMethod.PATCH)
@RequestMapping("api/profiles")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private AuthorizationService authorizationService;

//    @CrossOrigin
//    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
//    public ResponseEntity handlePreflight(HttpServletResponse response) {
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Methods", "PATCH");
//        response.addHeader("Access-Control-Allow-Headers", "content-type, authorization");
//        return ResponseEntity.ok().build();
//    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("")
    public ResponseEntity<?> listProfiles() {
        try {
            List<Profile> profiles = profileService.listAllProfiles();
            List<ProfileDto> profilesDto = getDtosWithHATEOASFromProfiles(profiles);
            Link allSongsLinks = createHATEOAS(profilesDto);
            return new ResponseEntity<>(List.of(profilesDto, allSongsLinks), HttpStatus.OK);
        } catch (CollectionOfProfilesNotFound e) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        try {
            Profile profile = profileService.getProfile(id);
            ProfileDto profileDto = getDtoFromProfile(profile);
            createHATEOAS(profileDto);
            return new ResponseEntity<>(profileDto, HttpStatus.OK);
        } catch (ProfileNotFoundException e) {
            Link parent = linkTo(methodOn(ProfileController.class).listProfiles()).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody ProfilePOJO profilePOJO, @RequestHeader (name="Authorization") String token) {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            Integer id = Integer.valueOf(auth.split("-")[0]);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("client") || !id.equals(profilePOJO.getUser_id()))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            Profile profile = profileService.addNewProfile(profilePOJO);
            ProfileDto profileDto = getDtoFromProfile(profile);
            createHATEOAS(profileDto);
            return new ResponseEntity<>(profileDto, HttpStatus.CREATED);
        }catch(IncorrectReqBodyException e){
            //pt user_id null
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        catch (UnprocessableReqBody e)
        {
            //daca liked music sau playlists nu au size-ul 0
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

//    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.PATCH, allowedHeaders = "*")
//@CrossOrigin(
//        // Access-Control-Allow-Origin
//        origins = { "*" },
//
//        // Alternative to origins that supports more flexible originpatterns.
//        // Please, see CorsConfiguration.setAllowedOriginPatterns(List)for details.
//        // originPatterns = { "" },
//
//        // Access-Control-Allow-Credentials
//        allowCredentials = "false",
//
//        // Access-Control-Allow-Headers
//        allowedHeaders = { "*" },
//
//        // Access-Control-Expose-Headers
//        exposedHeaders = { "*" },
//
//        // Access-Control-Max-Age
//        maxAge = 60 * 30,
//
//        // Access-Control-Allow-Methods
//        methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH}
//)
    @CrossOrigin(origins = "http://localhost:3000")
    @PatchMapping(value="/{id}")
    public ResponseEntity<?> updatePlaylist(@PathVariable String id, @RequestBody ProfilePOJO profilePOJO, @RequestParam(required=false) String operation, @RequestHeader (name="Authorization") String token)
    {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            Integer idUser = Integer.valueOf(auth.split("-")[0]);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("client") || !idUser.equals(profilePOJO.getUser_id()))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            Profile profile = profileService.getProfile(id);
            List<Playlist> playlists = new ArrayList<>();
            for(String playlistId: profilePOJO.getPlaylist_ids()){
                playlists.add(playlistService.getPlaylist(playlistId));
            }
            profileService.updateProfile(profile, profilePOJO, operation, playlists);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch  (ProfileNotFoundException e) {
            Link parent = linkTo(methodOn(ProfileController.class).listProfiles()).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        } catch(PlaylistNotFoundException | MusicNotFound e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    //metoda asta exista doar pentru ca nu am reusit sa elimin cors-ul pentru metoda patch de mai sus
    //ea nu respecta standardul si nu ar fi existat niciodata, daca nu era problema cors-ului
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value="/{id}")
    public ResponseEntity<?> updatePlaylistWrongWayToWorkWithReact(@PathVariable String id, @RequestBody ProfilePOJO profilePOJO, @RequestParam(required=false) String operation, @RequestHeader (name="Authorization") String token)
    {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            Integer idUser = Integer.valueOf(auth.split("-")[0]);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("client") || !idUser.equals(profilePOJO.getUser_id()))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            Profile profile = profileService.getProfile(id);
            List<Playlist> playlists = new ArrayList<>();
            for(String playlistId: profilePOJO.getPlaylist_ids()){
                playlists.add(playlistService.getPlaylist(playlistId));
            }
            profileService.updateProfile(profile, profilePOJO, operation, playlists);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch  (ProfileNotFoundException e) {
            Link parent = linkTo(methodOn(ProfileController.class).listProfiles()).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        } catch(PlaylistNotFoundException | MusicNotFound e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, @RequestHeader (name="Authorization") String token) {
        try {
            String auth = authorizationService.AuthorizeRequest(token);
            Integer idUser = Integer.valueOf(auth.split("-")[0]);
            String role = auth.split("-")[1].replace("[", "").replace("]", "").replace("'", "");
            if(!role.equals("client"))
                return new ResponseEntity<>("FORBIDDEN", HttpStatus.FORBIDDEN);
        }
        catch(AuthorizationFailedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        try {
            profileService.deleteProfile(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(ProfileNotFoundException e){
            Link parent = linkTo(methodOn(ProfileController.class).listProfiles()).withRel("parent");
            return new ResponseEntity<>(parent, HttpStatus.NOT_FOUND);
        }
    }
}
