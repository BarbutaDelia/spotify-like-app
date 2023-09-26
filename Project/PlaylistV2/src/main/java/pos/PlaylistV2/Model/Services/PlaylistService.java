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
import pos.PlaylistV2.Model.Exceptions.MusicNotFound;
import pos.PlaylistV2.Model.Exceptions.PlaylistNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pos.PlaylistV2.View.DTOs.MusicDto;
import pos.PlaylistV2.View.DTOs.PlaylistDto;
import pos.PlaylistV2.View.DTOs.PlaylistPOJO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pos.PlaylistV2.Model.Util.Conversion.getMusicDtosForPlaylistPOJO;

@Service
@Transactional
public class PlaylistService {
    @Autowired
    private pos.PlaylistV2.Model.Repositories.PlaylistRepository PlaylistRepository;

    public List<Playlist> listAllPlaylists() {
        return PlaylistRepository.findAll();
    }

    public void savePlaylist(Playlist Playlist) {
        PlaylistRepository.save(Playlist);
    }

    public Playlist getPlaylist(String id) {
        if(PlaylistRepository.findById(id).isPresent())
            return PlaylistRepository.findById(id).get();
        else
            throw new PlaylistNotFoundException(id);
    }

    public void updatePlaylist(Playlist playlist, PlaylistPOJO playlistPOJO, String operation){
        if(!Objects.equals(playlist.getUser_id(), playlistPOJO.getUser_id()))
            playlist.setUser_id(playlistPOJO.getUser_id());
        if(!Objects.equals(playlist.getName(), playlistPOJO.getName()))
            playlist.setName(playlistPOJO.getName());
        if(Objects.equals(operation, "add")){
            List<MusicDto> songs = getMusicDtosForPlaylistPOJO(playlistPOJO);
            for (MusicDto song : songs) {
                if(!playlist.getSongs().contains(song))
                    playlist.addSong(song);
            }
        }
        if(Objects.equals(operation, "remove")){
            List<MusicDto> songs = getMusicDtosForPlaylistPOJO(playlistPOJO);
            for (MusicDto song : songs) {
                if(playlist.getSongs().contains(song))
                    playlist.removeSong(song);
            }
        }
        PlaylistRepository.save(playlist);
    }

    public void deletePlaylist(String id) {
        if(PlaylistRepository.findById(id).isPresent())
            PlaylistRepository.deleteById(id);
        else
            throw new PlaylistNotFoundException(id);
    }

}
