package com.iafenvoy.iceandfire.config;

import com.google.gson.JsonObject;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.jupiter.config.FileConfigContainer;
import com.iafenvoy.jupiter.malilib.config.options.ConfigBoolean;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IafClientConfig extends FileConfigContainer {
    public static final IafClientConfig INSTANCE = new IafClientConfig();
    public static final int CURRENT_VERSION = 0;
    public final ConfigBoolean customMainMenu = new ConfigBoolean("iceandfire.customMainMenu", true);
    public final ConfigBoolean dragonAuto3rdPerson = new ConfigBoolean("iceandfire.dragonAuto3rdPerson", false);

    public IafClientConfig() {
        super(new Identifier("config.iceandfire.client"), "screen.iceandfire.client.title", "./config/iceandfire/iaf-client.json");
    }

    @Override
    public void init() {
        this.createTab("client", "iceandfire.client")
                .addConfig(this.customMainMenu)
                .addConfig(this.dragonAuto3rdPerson);
    }

    @Override
    protected boolean shouldLoad(JsonObject obj) {
        int version = obj.get("version").getAsInt();
        if (version != CURRENT_VERSION) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                FileUtils.copyFile(new File(this.path), new File(IafCommonConfig.backupPath + "iceandfire_client_" + sdf.format(new Date()) + ".json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            IceAndFire.LOGGER.info("Wrong config version {} for mod {}! Automatically use version {} and backup old one.", version, IceAndFire.MOD_NAME, CURRENT_VERSION);
            return false;
        } else IceAndFire.LOGGER.info("{} config version match.", IceAndFire.MOD_NAME);
        return true;
    }

    @Override
    protected void writeCustomData(JsonObject obj) {
        obj.addProperty("version", CURRENT_VERSION);
    }
}
