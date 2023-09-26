package com.example.demo.Model.Repositories;

import com.example.demo.Model.Entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, String>{
    List<Artist> findByName(String name);
    List<Artist> findByNameContaining(String name);
}