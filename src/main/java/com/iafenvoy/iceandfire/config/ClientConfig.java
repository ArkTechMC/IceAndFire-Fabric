package com.iafenvoy.iceandfire.config;

import com.iafenvoy.iceandfire.IceAndFire;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientConfig {
    private static ClientConfig INSTANCE = null;
    private static final int CURRENT_VERSION = 1;
    private static final String configPath = "./config/iceandfire/client.json";
    private static final String backupPath = "./config/iceandfire/backup/";
    // Version key for identify
    public int version = CURRENT_VERSION;

    public final boolean customMainMenu = true;
    //TODO
    public final boolean dragonAuto3rdPerson = false;

    public static ClientConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = ConfigLoader.load(ClientConfig.class, "./config/iceandfire/client.json", new ClientConfig());
            if (INSTANCE.version != CURRENT_VERSION) {
                IceAndFire.LOGGER.warn("Wrong client config version {} for mod Ice And Fire! Automatically transform to version {} and backup old one.", INSTANCE.version, CURRENT_VERSION);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                ConfigLoader.copy(configPath, backupPath + "client_" + sdf.format(new Date()) + ".json");
                ConfigLoader.save(configPath, INSTANCE = new ClientConfig());
            }
        }
        return INSTANCE;
    }
}
