package com.example.demo.Model.Exceptions;

public class MusicNotFoundException extends RuntimeException {

    public MusicNotFoundException(Integer id) {
        super("Could not find music " + id);
    }
}