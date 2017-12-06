/*
 * Copyright (c) 2017 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package com.github.dimanolog.flickr.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.github.dimanolog.flickr.util.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Shnipko Dmitry
 */
public class DiskLruCache {

    private static final String TAG = DiskLruCache.class.getSimpleName();
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 80;
    private static final int BUFFER_SIZE = 4096;
    private static final String IMAGE_CACHE_DIR_NAME = "IMAGE_CACHE";
    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024;
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private final File cacheDir;
    private final long cacheSize;

    public DiskLruCache(Context pContext) {
        this.cacheDir = pContext.getCacheDir();
        if (!cacheDir.exists()) {
            boolean mkdir = this.cacheDir.mkdirs();
            if (!mkdir) {
                throw new IllegalStateException("Can't create dir for images");
            }

        }
        this.cacheDir.setWritable(true);

        if (!this.cacheDir.canWrite()) {
            throw new IllegalStateException("Can't write into dir for images");
        }

        cacheSize = calculateDiskCacheSize(this.cacheDir);
        freeSpaceIfRequired();

    }


    public File get(String imageUri) {
        final String fileName = MD5.hash(imageUri);
        File[] files = cacheDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return fileName.equals(name);
            }
        });

        if (files != null && files.length == 1) {
            File imageFile = files[0];
            imageFile.setLastModified(System.currentTimeMillis());
            return imageFile;
        }
        return null;
    }


    public void add(String imageUri, Bitmap bitmap) {
        File imageFile = get(imageUri);
        if (imageFile == null) {
            String fileName = MD5.hash(imageUri);
            imageFile = new File(cacheDir, fileName);

            freeSpaceIfRequired();
            OutputStream os = null;
            FileOutputStream out = null;
            try {
                if(imageFile.createNewFile())
                {
                    out = new FileOutputStream(imageFile);
                    os = new BufferedOutputStream(out, BUFFER_SIZE);

                    boolean savedSuccessfully = bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, os);
                    if(savedSuccessfully) {
                        imageFile.setLastModified(System.currentTimeMillis());
                    }
                }
            } catch (IOException e) {
                Log.d(TAG, "cant create new Image file");
            } finally {
                IOUtils.close(os);
                IOUtils.close(out);
            }
        }
    }


    private void freeSpaceIfRequired() {
        Log.d(TAG, "freeSpaceIfRequired() called");
        long currentCacheSize = getCurrentCacheSize();
        if (currentCacheSize > cacheSize) {
            File[] files = cacheDir.listFiles();
            Arrays.sort(files, new Comparator<File>() {

                @Override
                public int compare(File lhs, File rhs) {
                    return Long.valueOf(lhs.lastModified()).compareTo(rhs.lastModified());
                }
            });
            int i = 0;
            do {
                final File f = files[i];
                final long length = f.length();
                if (f.delete()) {
                    currentCacheSize -= length;
                }
                i++;
                Log.d(TAG, "freeSpaceIfRequired: after delete " + currentCacheSize);
            } while (currentCacheSize > cacheSize);
        }
        Log.d(TAG, "freeSpaceIfRequired() returned: " + currentCacheSize);
    }

   private long getCurrentCacheSize() {
        long length = 0;
        for (File file : cacheDir.listFiles()) {
            if (file.isFile())
                length += file.length();
        }
        return length;
    }


    private static long calculateDiskCacheSize(File dir) {
        long size = MIN_DISK_CACHE_SIZE;

        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            size = available / 50;
        } catch (IllegalArgumentException ignored) {
        }

//        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
        return 40772;
    }
}

