package com.github.alexthe666.iceandfire.registry;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class IafKeybindings {
    public static KeyBinding dragon_fireAttack;
    public static KeyBinding dragon_strike;
    public static KeyBinding dragon_down;

    public static void init() {
        // Minecraft instance is null during data gen
        if (MinecraftClient.getInstance() == null)
            return;
        dragon_fireAttack = new KeyBinding("key.dragon_fireAttack", 82, "key.categories.gameplay");
        dragon_strike = new KeyBinding("key.dragon_strike", 71, "key.categories.gameplay");
        dragon_down = new KeyBinding("key.dragon_down", 88, "key.categories.gameplay");
        KeyBindingHelper.registerKeyBinding(dragon_fireAttack);
        KeyBindingHelper.registerKeyBinding(dragon_strike);
        KeyBindingHelper.registerKeyBinding(dragon_down);
    }
}
