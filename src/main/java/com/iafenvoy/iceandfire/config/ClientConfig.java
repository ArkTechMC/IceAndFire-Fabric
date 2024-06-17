package com.iafenvoy.iceandfire.config;

import io.github.fabricators_of_create.porting_lib.config.ModConfigSpec;

public class ClientConfig {
    public final ModConfigSpec.BooleanValue customMainMenu;
    public final ModConfigSpec.BooleanValue useVanillaFont;
    public final ModConfigSpec.BooleanValue dragonAuto3rdPerson;

    public ClientConfig(final ModConfigSpec.Builder builder) {
        builder.push("general");
        this.customMainMenu = buildBoolean(builder, "Custom main menu", true, "Whether to display the dragon on the main menu or not");
        this.dragonAuto3rdPerson = buildBoolean(builder, "Auto 3rd person when riding dragon", true, "True if riding dragons should make the player take a 3rd person view automatically.");
        this.useVanillaFont = buildBoolean(builder, "Use Vanilla Font", false, "Whether to use the vanilla font in the bestiary or not");
    }

    private static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder builder, String name, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ModConfigSpec.IntValue buildInt(ModConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ModConfigSpec.DoubleValue buildDouble(ModConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
