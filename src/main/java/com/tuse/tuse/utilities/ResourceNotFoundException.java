package com.tuse.tuse.utilities;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {super("Requested Resource could not be found");}
    public ResourceNotFoundException(String s) {
        super(s);
    }
}
