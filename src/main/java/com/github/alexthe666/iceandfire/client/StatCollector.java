package com.github.alexthe666.iceandfire.client;

import net.minecraft.client.resource.language.I18n;

/*
    Legacy translations helper
 */
public class StatCollector {
    public static String translateToLocal(String s) {
        return I18n.translate(s);
    }
}
