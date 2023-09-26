package com.example.demo.Model.Exceptions;

public class ArtistNotFoundException extends RuntimeException {

    public ArtistNotFoundException(String uuid) {
        super("Could not find artist " + uuid);
    }
}