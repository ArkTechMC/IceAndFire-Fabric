package com.github.alexthe666.iceandfire.config;

import io.github.fabricators_of_create.porting_lib.config.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHolder {

    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec SERVER_SPEC;
    public static final ClientConfig CLIENT;
    public static final ServerConfig SERVER;

    static {
        {
            final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
        {
            final Pair<ServerConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
    }
}