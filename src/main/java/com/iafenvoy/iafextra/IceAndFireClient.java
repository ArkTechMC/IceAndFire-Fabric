package com.iafenvoy.iafextra;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.WorldEventContext;
import com.iafenvoy.iafextra.network.IafClientNetworkHandler;
import com.iafenvoy.iafextra.render.CopperArmorRenderer;
import com.iafenvoy.iafextra.render.DeathWormArmorRenderer;
import com.iafenvoy.iafextra.render.DragonSteelArmorRenderer;
import com.iafenvoy.iafextra.render.SilverArmorRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class IceAndFireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WorldRenderEvents.LAST.register(WorldEventContext.INSTANCE::renderWorldLastEvent);
        ArmorRenderer.register(new CopperArmorRenderer(), IafItemRegistry.COPPER_HELMET.get(), IafItemRegistry.COPPER_CHESTPLATE.get(), IafItemRegistry.COPPER_LEGGINGS.get(), IafItemRegistry.COPPER_BOOTS.get());
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_WHITE_HELMET.get(), IafItemRegistry.DEATHWORM_WHITE_CHESTPLATE.get(), IafItemRegistry.DEATHWORM_WHITE_LEGGINGS.get(), IafItemRegistry.DEATHWORM_WHITE_BOOTS.get());
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_YELLOW_HELMET.get(), IafItemRegistry.DEATHWORM_YELLOW_CHESTPLATE.get(), IafItemRegistry.DEATHWORM_YELLOW_LEGGINGS.get(), IafItemRegistry.DEATHWORM_YELLOW_BOOTS.get());
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_RED_HELMET.get(), IafItemRegistry.DEATHWORM_RED_CHESTPLATE.get(), IafItemRegistry.DEATHWORM_RED_LEGGINGS.get(), IafItemRegistry.DEATHWORM_RED_BOOTS.get());
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_FIRE_HELMET.get(), IafItemRegistry.DRAGONSTEEL_FIRE_CHESTPLATE.get(), IafItemRegistry.DRAGONSTEEL_FIRE_LEGGINGS.get(), IafItemRegistry.DRAGONSTEEL_FIRE_BOOTS.get());
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_ICE_HELMET.get(), IafItemRegistry.DRAGONSTEEL_ICE_CHESTPLATE.get(), IafItemRegistry.DRAGONSTEEL_ICE_LEGGINGS.get(), IafItemRegistry.DRAGONSTEEL_ICE_BOOTS.get());
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_HELMET.get(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_CHESTPLATE.get(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_LEGGINGS.get(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_BOOTS.get());
        ArmorRenderer.register(new SilverArmorRenderer(), IafItemRegistry.SILVER_HELMET.get(), IafItemRegistry.SILVER_CHESTPLATE.get(), IafItemRegistry.SILVER_LEGGINGS.get(), IafItemRegistry.SILVER_BOOTS.get());

        IafClientNetworkHandler.register();
    }
}
