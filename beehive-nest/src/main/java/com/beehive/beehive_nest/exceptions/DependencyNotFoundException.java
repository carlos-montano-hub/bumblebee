package com.beehive.beehive_nest.exceptions;

public class DependencyNotFoundException extends RuntimeException {
    public DependencyNotFoundException(String message) {
        super(message);
    }
}
