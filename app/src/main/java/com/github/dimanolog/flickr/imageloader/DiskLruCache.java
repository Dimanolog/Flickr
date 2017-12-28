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
import android.os.StatFs;
import android.util.Log;

import com.github.dimanolog.flickr.util.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Shnipko Dmitry
 */
//TODO add to imageView tag or solve bug with another way
public class DiskLruCache {

    private static final String TAG = DiskLruCache.class.getSimpleName();
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 80;
    private static final int BUFFER_SIZE = 4096;
    private static final String IMAGE_CACHE_DIR_NAME = "IMAGE_CACHE";
    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024;
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final int CLEAN_DISK_SIZE = 5 * 1024 * 1024;

    private final File mCacheDir;
    private final long mCacheSize;

    private volatile long mCurrentCacheSize;

    public DiskLruCache(Context pContext) {
        this.mCacheDir = pContext.getCacheDir();
        if (!mCacheDir.exists()) {
            boolean mkdir = this.mCacheDir.mkdirs();
            if (!mkdir) {
                throw new IllegalStateException("Can't create dir for images");
            }

        }
        this.mCacheDir.setWritable(true);

        if (!this.mCacheDir.canWrite()) {
            throw new IllegalStateException("Can't write into dir for images");
        }

        mCacheSize = calculateDiskCacheSize(this.mCacheDir);

        freeSpaceIfRequired();
    }


    public File get(String imageUri) {
        final String fileName = MD5.hash(imageUri);
        File[] files = mCacheDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return fileName.equals(name);
            }
        });

        if (files != null && files.length == 1) {
            File imageFile = files[0];
            imageFile.setLastModified(System.currentTimeMillis());
            Log.d(TAG, String.format("success return file from cache: %s, %s", imageFile.getName(), imageUri));
            return imageFile;
        }
        return null;
    }


    public void add(final String imageUri, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = MD5.hash(imageUri);
                File imageFile = new File(fileName);
                if (!imageFile.exists()) {
                    imageFile = new File(mCacheDir, fileName + "temp");

                    OutputStream os = null;
                    FileOutputStream out = null;
                    try {
                        if (imageFile.createNewFile()) {
                            out = new FileOutputStream(imageFile);
                            os = new BufferedOutputStream(out, BUFFER_SIZE);

                            boolean savedSuccessfully = bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, os);
                            if (savedSuccessfully) {
                                imageFile.setLastModified(System.currentTimeMillis());
                                imageFile.renameTo(new File(fileName));
                                long current = mCurrentCacheSize + imageFile.length();
                                if (current > mCacheSize) {
                                    freeSpaceIfRequired();
                                }
                                Log.d(TAG, "success create new image file in cache" + imageFile.getName());
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
        }).start();
    }


    private synchronized void freeSpaceIfRequired() {
        Log.d(TAG, "freeSpaceIfRequired() called");
        long currentCacheSize = getCurrentCacheSize();

        if (currentCacheSize > mCacheSize) {
            long targetSize = mCacheSize;
            if (mCacheSize > CLEAN_DISK_SIZE) {
                targetSize = mCacheSize - CLEAN_DISK_SIZE;
            }
            File[] files = mCacheDir.listFiles();
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
            } while (currentCacheSize > targetSize);
            mCurrentCacheSize=getCurrentCacheSize();
        }
        Log.d(TAG, "freeSpaceIfRequired() returned: " + currentCacheSize);
    }

    private long getCurrentCacheSize() {
        long length = 0;
        for (File file : mCacheDir.listFiles()) {
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

        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);

    }
}

