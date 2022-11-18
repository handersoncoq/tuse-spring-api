package com.tuse.tuse.utilities;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {super("User made an unauthorized request");}
    public UnauthorizedException(String s) {
        super(s);
    }
}
