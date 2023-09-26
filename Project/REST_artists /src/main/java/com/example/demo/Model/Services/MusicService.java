package com.example.demo.Model.Services;
import com.example.demo.Model.Entities.Artist;
import com.example.demo.Model.Entities.Music;
import com.example.demo.Model.Exceptions.CollectionOfMusicNotFoundException;
import com.example.demo.Model.Exceptions.MusicNotFoundException;
import com.example.demo.Model.Repositories.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MusicService {
    @Autowired
    private MusicRepository musicRepository;

    public List<Music> listAllMusic() {
        if(!musicRepository.findAll().isEmpty())
            return musicRepository.findAll();
        else
            throw new CollectionOfMusicNotFoundException();
    }

    public void saveMusic(Music music) {
        musicRepository.save(music);
    }

    public Music getMusic(Integer id) {
        if(musicRepository.findById(id).isPresent())
            return musicRepository.findById(id).get();
        else
            throw new MusicNotFoundException(id);
    }

    public void deleteMusic(Integer id) {
        if(musicRepository.findById(id).isPresent())
            musicRepository.deleteById(id);
        else
            throw new MusicNotFoundException(id);
    }

    public List<Music> listAllMusicByName(String name){
        return musicRepository.findByName(name);
    }

    public List<Music> listAllMusicByPartialName(String name){
        return musicRepository.findByNameContaining(name);
    }


}
