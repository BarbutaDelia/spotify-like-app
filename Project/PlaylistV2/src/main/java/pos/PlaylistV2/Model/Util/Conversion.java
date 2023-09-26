package pos.PlaylistV2.Model.Util;

import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pos.PlaylistV2.Model.Entities.Playlist;
import pos.PlaylistV2.Model.Entities.Profile;
import pos.PlaylistV2.Model.Exceptions.MusicNotFound;
import pos.PlaylistV2.View.DTOs.*;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

public class Conversion {
    public static Playlist getPlaylistFromPOJO(PlaylistPOJO playlistPOJO) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        Playlist p = new Playlist(playlistPOJO.getId(), playlistPOJO.getUser_id(), playlistPOJO.getName(), new ArrayList<>());
        for(Integer songId : playlistPOJO.getSong_ids()) {
            try {
                String url = "http://localhost:8080/api/songs/" + songId;
                ResponseEntity<Object> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()),
                        Object.class
                );
                org.json.JSONObject j = new JSONObject(new Gson().toJson(response.getBody()));
                try {
                    String name = j.getString("name");
                    String link = j.getJSONObject("_links").getJSONObject("self").getString("href");
                    MusicDto musicDto = new MusicDto(songId, name, link);
                    p.addSong(musicDto);
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            catch(HttpClientErrorException m){
                throw new MusicNotFound(songId);
            }
        }
        return p;
    }

    public static PlaylistDto getDtoFromPlaylist(Playlist playlist){
        return new PlaylistDto(playlist.getId(), playlist.getUser_id(), playlist.getName(), playlist.getSongs());
    }

    public static List<PlaylistDto> getDtosWithHATEOASFromPlaylists(List<Playlist> playlists)
    {
        List<PlaylistDto> playlistsDto = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistsDto.add(getDtoFromPlaylist(playlist));
        }
        return playlistsDto;
    }

    public static List<MusicDto> getMusicDtosForPlaylistPOJO(PlaylistPOJO playlistPOJO){
        List<MusicDto> songs = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        for(Integer songId : playlistPOJO.getSong_ids()) {
            try {
                String url = "http://localhost:8080/api/songs/" + songId;
                ResponseEntity<Object> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()),
                        Object.class
                );
                org.json.JSONObject j = new JSONObject(new Gson().toJson(response.getBody()));
                try {
                    String name = j.getString("name");
                    String link = j.getJSONObject("_links").getJSONObject("self").getString("href");
                    MusicDto musicDto = new MusicDto(songId, name, link);
                    songs.add(musicDto);
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            catch(HttpClientErrorException | JSONException m){
                throw new MusicNotFound(songId);
            }
        }
        return songs;
    }

    public static List<MusicDto> getMusicDtosForProfilePOJO(ProfilePOJO profilePOJO){
        List<MusicDto> songs = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        for(Integer songId : profilePOJO.getLikedMusic_ids()) {
            try {
                String url = "http://localhost:8080/api/songs/" + songId;
                ResponseEntity<Object> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()),
                        Object.class
                );
                org.json.JSONObject j = new JSONObject(new Gson().toJson(response.getBody()));
                try {
                    String name = j.getString("name");
                    String link = j.getJSONObject("_links").getJSONObject("self").getString("href");
                    MusicDto musicDto = new MusicDto(songId, name, link);
                    songs.add(musicDto);
                } catch (JSONException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            catch(HttpClientErrorException | JSONException m){
                throw new MusicNotFound(songId);
            }
        }
        return songs;
    }

    public static Profile getProfileFromPOJO(ProfilePOJO profilePOJO){
        RestTemplate restTemplate = new RestTemplate();
        if(profilePOJO.getLikedMusic_ids().size() == 0 && profilePOJO.getPlaylist_ids().size() == 0) {
            Profile profile = new Profile(profilePOJO.getId(), profilePOJO.getUser_id(), profilePOJO.getFirst_name().get(), profilePOJO.getLast_name().get(), profilePOJO.getEmail().get(), new ArrayList<>(), new ArrayList<>());
            return profile;
        }
        return null;
    }

    public static ProfileDto getDtoFromProfile(Profile profile){
        return new ProfileDto(profile.getId(), profile.getUser_id(), profile.getFirst_name(), profile.getLast_name(), profile.getEmail(), profile.getLikedMusic(), profile.getPlaylists());
    }

    public static List<ProfileDto> getDtosWithHATEOASFromProfiles(List<Profile> profiles)
    {
        List<ProfileDto> profilesDto = new ArrayList<>();
        for (Profile profile : profiles) {
            profilesDto.add(getDtoFromProfile(profile));
        }
        return profilesDto;
    }

}
