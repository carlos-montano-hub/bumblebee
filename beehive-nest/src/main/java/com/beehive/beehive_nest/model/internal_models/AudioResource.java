package com.beehive.beehive_nest.model.internal_models;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;

public class AudioResource extends InputStreamResource {
    private final String filename;

    public AudioResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return -1; // unknown length
    }
}
