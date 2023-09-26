package com.example.demo.Model.Entities;


import com.example.demo.Model.Enums.Genre;
import com.example.demo.Model.Enums.Type;
import org.springframework.hateoas.RepresentationModel;

import java.util.*;

import javax.persistence.*;


@Entity
@Table(name="music")
public class Music{

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Genre music_genre;
    private Integer year_of_release;
    @Enumerated(EnumType.STRING)
    private Type music_type;

    @ManyToOne(cascade={CascadeType.DETACH})
    @JoinColumn(name="album_id")
    private Music album;

    @OneToMany(mappedBy="album")
    private List<Music> albumSongs = new ArrayList<Music>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    @JoinTable(name = "music_artists",
            joinColumns = @JoinColumn(name = "music_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private List<Artist> artists;

//    @ManyToMany(mappedBy = "music")
//    private Collection<Artist> artists;

//    public Collection<Artist> getArtists() {
//        return artists;
//    }
//
//    public void setArtists(Collection<Artist> artists) {
//        this.artists = artists;
//    }
//
//    public void addArtist(Artist artist){
//        this.artists.add(artist);
//    }

    public Music() {}

    public Music(String name, Genre music_genre, Integer year_of_release, Type music_type, Music album) {
        this.name = name;
        this.music_genre = music_genre;
        this.year_of_release = year_of_release;
        this.music_type = music_type;
        this.album = album;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getMusic_genre() {
        return music_genre;
    }

    public void setMusic_genre(Genre music_genre) {
        this.music_genre = music_genre;
    }

    public Integer getYear_of_release() {
        return year_of_release;
    }

    public void setYear_of_release(Integer year_of_release) {
        this.year_of_release = year_of_release;
    }

    public Type getMusic_type() {
        return music_type;
    }

    public void setMusic_type(Type music_type) {
        this.music_type = music_type;
    }
    //    public List<Music> getalbumSongs() {
//        return albumSongs;
//    }

    public void addAlbumSong(Music music) {
        this.albumSongs.add(music);
    }

    public Music getAlbum() {
        return album;
    }

    public void setAlbum(Music album_id) {
        this.album = album_id;
    }

    public void setAlbumSongs(List<Music> albumSongs) {
        this.albumSongs = albumSongs;
    }

    @Override
    public boolean equals(Object m) {

        if (this == m)
            return true;
        if (!(m instanceof Music music))
            return false;
        return Objects.equals(this.id, music.id) && Objects.equals(this.name, music.name)
                && Objects.equals(this.music_genre, music.music_genre) && Objects.equals(this.year_of_release, music.year_of_release)
                && Objects.equals(this.music_type, music.music_type) && Objects.equals(this.album, music.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.music_genre, this.year_of_release, this.music_type, this.album);
    }

    @Override
    public String toString() {
        return "Music{" + "id=" + this.id + ", name= " + this.name + ", genre= " + this.music_genre + ", year of release = " + this.year_of_release + ", type = " + this.music_type + ", album_id = " + this.album + '}';
    }
}