package com.example.demo.Model.Repositories;

import com.example.demo.Model.Entities.Artist;
import com.example.demo.Model.Entities.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Integer>{
    List<Music> findByName(String name);
    List<Music> findByNameContaining(String name);
}