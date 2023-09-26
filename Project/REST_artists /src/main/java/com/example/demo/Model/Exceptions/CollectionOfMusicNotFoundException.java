package com.example.demo.Model.Exceptions;

public class CollectionOfMusicNotFoundException extends RuntimeException {

    public CollectionOfMusicNotFoundException() {
        super("Could not find music.");
    }
}
