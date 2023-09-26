package pos.PlaylistV2.Model.Services;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pos.PlaylistV2.Model.Entities.Playlist;
import pos.PlaylistV2.Model.Entities.Profile;
import pos.PlaylistV2.Model.Exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pos.PlaylistV2.Model.Repositories.ProfileRepository;
import pos.PlaylistV2.View.DTOs.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pos.PlaylistV2.Model.Util.Conversion.*;

@Service
@Transactional
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public List<Profile> listAllProfiles() {
        return profileRepository.findAll();
    }

    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public Profile getProfile(String id) {
        if(profileRepository.findById(id).isPresent())
            return profileRepository.findById(id).get();
        else
            throw new ProfileNotFoundException(id);
    }

    public Profile addNewProfile(ProfilePOJO profilePOJO){

        if(profilePOJO.getUser_id() == null) {
            throw new IncorrectReqBodyException("user_id not present in request body!");
        }
        if(profilePOJO.getLikedMusic_ids().size() != 0)
        {
            throw new UnprocessableReqBody();
        }
        Profile profile = getProfileFromPOJO(profilePOJO);
        profileRepository.save(profile);
        return profile;
    }

    public void updateProfile(Profile profile, ProfilePOJO profilePOJO, String operation, List<Playlist> playlists){
        System.out.println(profilePOJO.getId() + " " +  profilePOJO.getFirst_name() + profilePOJO.getUser_id());
        if(profilePOJO.getFirst_name() != null) {
            if (profilePOJO.getFirst_name().isPresent())
                if (!Objects.equals(profile.getFirst_name(), profilePOJO.getFirst_name().get()))
                    profile.setFirst_name(profilePOJO.getFirst_name().get());
        }
        if(profilePOJO.getLast_name() != null) {
            if (profilePOJO.getLast_name().isPresent())
                if (!Objects.equals(profile.getLast_name(), profilePOJO.getLast_name().get()))
                    profile.setLast_name(profilePOJO.getLast_name().get());
        }
        if(profilePOJO.getEmail() != null) {
            if (profilePOJO.getEmail().isPresent())
                if (!Objects.equals(profile.getEmail(), profilePOJO.getEmail().get()))
                    profile.setEmail(profilePOJO.getEmail().get());
        }

        if(Objects.equals(operation, "add")){
            List<MusicDto> songs = getMusicDtosForProfilePOJO(profilePOJO);
            for (MusicDto song : songs) {
                if(!profile.getLikedMusic().contains(song))
                    profile.addSongToLikedMusic(song);
            }
            for (Playlist p : playlists) {
                if(!profile.getPlaylists().contains(p))
                    profile.addPlaylist(p);
            }
        }
        if(Objects.equals(operation, "remove")){
            List<MusicDto> songs = getMusicDtosForProfilePOJO(profilePOJO);
            for (MusicDto song : songs) {
                if(profile.getLikedMusic().contains(song))
                    profile.removeSongFromLikedMusic(song);
            }
            for (Playlist p : playlists) {
                if(profile.getPlaylists().contains(p))
                    profile.removePlaylist(p);
            }
        }
        profileRepository.save(profile);
    }

//    public void updatePlaylistInsideProfile(Profile profile, List<Playlist> playlists, String operation){
//        if(Objects.equals(operation, "add")){
//            for (Playlist p : playlists) {
//                if(!profile.getPlaylists().contains(p))
//                    profile.addPlaylist(p);
//            }
//        }
//        if(Objects.equals(operation, "remove")){
//            for (Playlist p : playlists) {
//                if(profile.getPlaylists().contains(p))
//                    profile.removePlaylist(p);
//            }
//        }
//        if(!Objects.equals(operation, "add" ) && !Objects.equals(operation, "remove")){
//            throw new IncorrectOperationException();
//        }
//        profileRepository.save(profile);
//    }

    public void deleteProfile(String id) {
        if(profileRepository.findById(id).isPresent())
            profileRepository.deleteById(id);
        else
            throw new ProfileNotFoundException(id);
    }

}
