package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.event.ClientEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public final class IafKeybindings {
    public static KeyBinding dragon_fireAttack = new KeyBinding("key.dragon_fireAttack", GLFW.GLFW_KEY_R, "key.categories.gameplay");
    public static KeyBinding dragon_strike = new KeyBinding("key.dragon_strike", GLFW.GLFW_KEY_G, "key.categories.gameplay");
    public static KeyBinding dragon_down = new KeyBinding("key.dragon_down", GLFW.GLFW_KEY_X, "key.categories.gameplay");
    public static KeyBinding dragon_change_view = new KeyBinding("key.dragon_change_view", GLFW.GLFW_KEY_F7, "key.categories.gameplay");

    public static void init() {
        // Minecraft instance is null during data gen
        if (MinecraftClient.getInstance() == null) return;
        KeyBindingHelper.registerKeyBinding(dragon_fireAttack);
        KeyBindingHelper.registerKeyBinding(dragon_strike);
        KeyBindingHelper.registerKeyBinding(dragon_down);
        KeyBindingHelper.registerKeyBinding(dragon_change_view);
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (dragon_change_view.wasPressed()) {
                if (ClientEvents.currentView + 1 > 3) ClientEvents.currentView = 0;
                else ClientEvents.currentView++;
            }
        });
    }
}
