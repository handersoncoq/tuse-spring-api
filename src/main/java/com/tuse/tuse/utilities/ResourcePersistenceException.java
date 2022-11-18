package com.tuse.tuse.utilities;

public class ResourcePersistenceException extends RuntimeException {
    public ResourcePersistenceException() {super("Resource could not be persisted to the database");}
    public ResourcePersistenceException(String s) {
        super(s);
    }
}