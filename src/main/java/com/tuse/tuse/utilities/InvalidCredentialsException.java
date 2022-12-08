package com.tuse.tuse.utilities;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {super("Invalid credentials");}
    public InvalidCredentialsException(String s) {
        super(s);
    }
}
