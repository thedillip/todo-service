package com.todo.util;

public class AppConstants {
    private AppConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TODO_CACHE_KEY = "todos";
    public static final String KEEP_NOTE_MAP_NAME = "keepNoteCache";
}