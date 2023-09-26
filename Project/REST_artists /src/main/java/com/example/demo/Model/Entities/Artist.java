package com.example.demo.Model.Entities;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="artists")
public class Artist {

    private @Id String uuid;
    private String name;
    private Boolean is_active;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    @JoinTable(name = "music_artists",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id"))
    private Collection<Music> songs;

    public Collection<Music> getSongs() {
        return songs;
    }

    public void setSongs(Collection<Music> songs) {
        this.songs = songs;
    }

    public void addSong(Music song){
        if(!this.songs.contains(song))
            this.songs.add(song);
    }

    public void removeSong(Music song){
        if(!this.songs.contains(song))
            this.songs.remove(song);
    }


//    @ManyToMany(fetch = FetchType.LAZY,
//            cascade = {
//                    CascadeType.PERSIST,
//                    CascadeType.MERGE
//            })
//    @JoinTable(name = "music_artists",
//            joinColumns = { @JoinColumn(name = "artist_id") },
//            inverseJoinColumns = { @JoinColumn(name = "music_id") })
//
//    private Set<Music> songs = new HashSet<>();
//
//    public void addSong(Music song) {
//        this.songs.add(song);
//        song.getArtists().add(this);
//    }
//
//    public void removeSong(Integer songId) {
//        Music song = this.songs.stream().filter(t -> Objects.equals(t.getId(), songId)).findFirst().orElse(null);
//        if (song != null) {
//            this.songs.remove(song);
//            song.getArtists().remove(this);
//        }
//    }

    public Artist() {}

    public Artist(String uuid, String name, Boolean is_active) {
        this.uuid = uuid;
        this.name = name;
        this.is_active = is_active;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    @Override
    public boolean equals(Object a) {
        if (this == a)
            return true;
        if (!(a instanceof Artist artist))
            return false;
        return Objects.equals(this.uuid, artist.uuid) && Objects.equals(this.name, artist.name)
                && Objects.equals(this.is_active, artist.is_active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uuid, this.name, this.is_active);
    }

    @Override
    public String toString() {
        return "Music{" + "uuid=" + this.uuid + ", name= " + this.name + ", is_active = " + this.is_active + '}';
    }
}