package com.kindborg.mattias.dialpadapplication;

import java.io.*;

import android.os.*;

/**
 * Class responsible for handling the external storage.
 */
public class ExternalStorage {

    /**
     * Gets a value indicating whether external storage is in a readable state.
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) ||
            state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    /**
     * Gets a value indicating whether external storage is in a writable state.
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Gets a value indicating whether file represented by specified absolute
     * file name exists.
     */
    public static boolean fileExists(String absoluteFileName) {
        File file = new File(absoluteFileName);
        return file.exists() && file.isFile();
    }

    /**
     * Create a path pointing to specified path on the external storage.
     */
    public static String createPath(String path) {
        File file = new File(
            Environment.getExternalStorageDirectory(),
            path);

        return file.getAbsolutePath();
    }

    /**
     * Combines specified directory name with specified file name.
     */
    public static String combine(String directoryName, String fileName) {
        return directoryName + File.separator + fileName;
    }
}