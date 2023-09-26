package com.example.demo.Model.Exceptions;

public class CollectionOfArtistsNotFound extends RuntimeException{
    public CollectionOfArtistsNotFound() {
        super("Could not find artists.");
    }
}
