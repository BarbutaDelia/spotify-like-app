package com.example.demo.Model.Services;
import com.example.demo.Model.Entities.Artist;
import com.example.demo.Model.Exceptions.ArtistNotFoundException;
import com.example.demo.Model.Exceptions.MusicNotFoundException;
import com.example.demo.Model.Repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    public List<Artist> listAllArtists() {
        return artistRepository.findAll();
    }

    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }

    public Artist getArtist(String uuid) {
        if(artistRepository.findById(uuid).isPresent())
            return artistRepository.findById(uuid).get();
        else
            throw new ArtistNotFoundException(uuid);
    }

    public void deleteArtist(String uuid) {
        if(artistRepository.findById(uuid).isPresent())
            artistRepository.deleteById(uuid);
        else
            throw new ArtistNotFoundException(uuid);
    }

    public List<Artist> listAllArtistsByName(String name){
        return artistRepository.findByName(name);
    }

    public List<Artist> listAllArtistsByPartialName(String name){
        return artistRepository.findByNameContaining(name);
    }

}
