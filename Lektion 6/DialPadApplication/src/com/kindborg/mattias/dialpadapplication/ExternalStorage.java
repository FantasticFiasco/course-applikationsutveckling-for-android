package com.kindborg.mattias.dialpadapplication;

import java.io.*;
import java.util.*;

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
     * Gets a value indicating whether file represented by specified file name
     * exists.
     */
    public static boolean fileExists(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.isFile();
    }

    /**
     * Gets a value indicating whether directory represented by directory name
     * exists.
     */
    public static boolean directoryExists(String directoryName) {
        File file = new File(directoryName);
        return file.exists() && file.isDirectory();
    }

    /**
     * Ensures that specified directory exists.
     */
    public static void ensureDirectoryExists(String path) {
        new File(path).mkdirs();
    }

    /**
     * Gets the directories in specified path.
     */
    public static String[] getDirectories(String path) {
        List<String> directoryList = new ArrayList<String>();
        File pathFile = new File(path);

        if (pathFile.exists()) {
            for (File file : pathFile.listFiles()) {
                if (file.isDirectory()) {
                    directoryList.add(file.getAbsolutePath());
                }
            }
        }

        String[] directories = new String[directoryList.size()];
        return directoryList.toArray(directories);
    }

    /**
     * Gets the names of specified directories.
     */
    public static String[] getDirectoryNames(String[] directoryPaths) {
        List<String> directoryNameList = new ArrayList<String>();

        for (String directoryPath : directoryPaths) {
            directoryNameList.add(new File(directoryPath).getName());
        }

        String[] directoryNames = new String[directoryNameList.size()];
        return directoryNameList.toArray(directoryNames);
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