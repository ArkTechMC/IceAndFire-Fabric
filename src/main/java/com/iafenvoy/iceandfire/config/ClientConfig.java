package com.iafenvoy.iceandfire.config;

public class ClientConfig {
    private static ClientConfig INSTANCE = null;
    public boolean customMainMenu = true;
    //TODO
    public boolean dragonAuto3rdPerson = false;

    public static ClientConfig getInstance() {
        if (INSTANCE == null)
            INSTANCE = ConfigLoader.load(ClientConfig.class, "./config/iceandfire/client.json", new ClientConfig());
        return INSTANCE;
    }
}
