package com.iafenvoy.iceandfire.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.iafenvoy.iceandfire.IceAndFire;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IafConfigSerializer implements ConfigSerializer<IafConfig> {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Path getConfigPath() {
        return Path.of(IafConfig.configPath);
    }

    @Override
    public void serialize(IafConfig config) throws SerializationException {
        Path configPath = this.getConfigPath();
        try {
            Files.createDirectories(configPath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            GSON.toJson(config, writer);
            writer.close();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public IafConfig deserialize() throws SerializationException {
        Path configPath = this.getConfigPath();
        if (Files.exists(configPath)) {
            try {
                BufferedReader reader = Files.newBufferedReader(configPath);
                IafConfig ret = GSON.fromJson(reader, IafConfig.class);
                reader.close();
                if (ret.version != IafConfig.CURRENT_VERSION) {
                    IceAndFire.LOGGER.warn("Wrong config version {} for mod {}! Automatically transform to version {} and backup old one.", ret.version, IceAndFire.MOD_NAME, IafConfig.CURRENT_VERSION);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                    FileUtils.copyFile(configPath.toFile(), new File(IafConfig.backupPath + "common_" + sdf.format(new Date()) + ".json"));
                    ret = this.createDefault();
                    FileUtils.write(configPath.toFile(), GSON.toJson(reader), StandardCharsets.UTF_8);
                } else IceAndFire.LOGGER.info("{} config version match.", IceAndFire.MOD_NAME);
                return ret;
            } catch (JsonParseException | IOException e) {
                throw new ConfigSerializer.SerializationException(e);
            }
        } else return this.createDefault();
    }

    @Override
    public IafConfig createDefault() {
        return new IafConfig();
    }
}
