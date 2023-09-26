package com.example.demo.Model.Exceptions;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException(Integer id) {
        super("Invalid album_id  " + id);
    }
}