package com.iafenvoy.iceandfire.config;

import com.google.gson.Gson;
import com.iafenvoy.iceandfire.IceAndFire;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigLoader {
    private static final Gson GSON = new Gson();

    public static <T> T load(Class<T> clazz, String path, T defaultValue) {
        try {
            FileInputStream stream = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(stream);
            return GSON.fromJson(reader, clazz);
        } catch (FileNotFoundException e) {
            IceAndFire.LOGGER.error(e);
            try {
                FileUtils.write(new File(path), GSON.toJson(defaultValue), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                IceAndFire.LOGGER.error(ex);
            }
            return defaultValue;
        }
    }
}
