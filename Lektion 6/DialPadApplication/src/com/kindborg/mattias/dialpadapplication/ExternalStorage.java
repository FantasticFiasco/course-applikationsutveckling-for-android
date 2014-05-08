package com.kindborg.mattias.dialpadapplication;

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
}