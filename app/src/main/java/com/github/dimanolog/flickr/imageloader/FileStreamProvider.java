package com.github.dimanolog.flickr.imageloader;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileStreamProvider  {

    private static final int BUFFER_SIZE = 4096;

    public InputStream get(File file) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
        return inputStream;
    }
}
