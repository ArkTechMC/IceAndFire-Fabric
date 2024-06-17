package com.iafenvoy.iceandfire.config;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class ConfigLoader {
    private static final Gson GSON = new Gson();

    public static <T> T load(Class<T> clazz, String path, T defaultValue) {
        try {
            FileInputStream stream = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(stream);
            return GSON.fromJson(reader, clazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                FileUtils.write(new File(path), GSON.toJson(defaultValue));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return defaultValue;
        }
    }
}
