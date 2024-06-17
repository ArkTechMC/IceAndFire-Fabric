package com.iafenvoy.iceandfire;

import com.iafenvoy.iceandfire.client.model.util.DragonAnimationsLibrary;
import com.iafenvoy.iceandfire.client.model.util.EnumDragonModelTypes;
import com.iafenvoy.iceandfire.client.model.util.EnumDragonPoses;
import com.iafenvoy.iceandfire.client.model.util.EnumSeaSerpentAnimations;
import com.iafenvoy.iceandfire.event.ClientEvents;
import com.iafenvoy.iceandfire.network.IafClientNetworkHandler;
import com.iafenvoy.iceandfire.registry.*;
import com.iafenvoy.iceandfire.render.*;
import io.github.fabricators_of_create.porting_lib.event.client.LivingEntityRenderEvents;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTooltipBorderColorCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

@Environment(EnvType.CLIENT)
public class IceAndFireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LivingEntityRenderEvents.POST.register(ClientEvents::onPostRenderLiving);
        RenderTooltipBorderColorCallback.EVENT.register((stack, originalBorderColorStart, originalBorderColorEnd) -> new RenderTooltipBorderColorCallback.BorderColorEntry(originalBorderColorStart, originalBorderColorEnd));

        IafScreenHandlers.registerGui();
        EnumSeaSerpentAnimations.initializeSerpentModels();
        DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());
        IafEntities.registerRenderers();
        IafBlockEntities.registerRenderers();
        IafBlocks.registerRenderLayers();
        IafItems.registerModelPredicates();
        IafKeybindings.init();
        IafParticles.registerParticleRenderers();

        ArmorRenderer.register(new CopperArmorRenderer(), IafItems.COPPER_HELMET, IafItems.COPPER_CHESTPLATE, IafItems.COPPER_LEGGINGS, IafItems.COPPER_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_WHITE_HELMET, IafItems.DEATHWORM_WHITE_CHESTPLATE, IafItems.DEATHWORM_WHITE_LEGGINGS, IafItems.DEATHWORM_WHITE_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_YELLOW_HELMET, IafItems.DEATHWORM_YELLOW_CHESTPLATE, IafItems.DEATHWORM_YELLOW_LEGGINGS, IafItems.DEATHWORM_YELLOW_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_RED_HELMET, IafItems.DEATHWORM_RED_CHESTPLATE, IafItems.DEATHWORM_RED_LEGGINGS, IafItems.DEATHWORM_RED_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_FIRE_HELMET, IafItems.DRAGONSTEEL_FIRE_CHESTPLATE, IafItems.DRAGONSTEEL_FIRE_LEGGINGS, IafItems.DRAGONSTEEL_FIRE_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_ICE_HELMET, IafItems.DRAGONSTEEL_ICE_CHESTPLATE, IafItems.DRAGONSTEEL_ICE_LEGGINGS, IafItems.DRAGONSTEEL_ICE_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_LIGHTNING_HELMET, IafItems.DRAGONSTEEL_LIGHTNING_CHESTPLATE, IafItems.DRAGONSTEEL_LIGHTNING_LEGGINGS, IafItems.DRAGONSTEEL_LIGHTNING_BOOTS);
        ArmorRenderer.register(new SilverArmorRenderer(), IafItems.SILVER_HELMET, IafItems.SILVER_CHESTPLATE, IafItems.SILVER_LEGGINGS, IafItems.SILVER_BOOTS);
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.DEATHWORM_GAUNTLET_RED, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.DEATHWORM_GAUNTLET_YELLOW, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.DEATHWORM_GAUNTLET_WHITE, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.GORGON_HEAD, new GorgonHeadRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.TIDE_TRIDENT, new TideTridentRenderer());

        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_BIRCH, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_OAK, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_DARK_OAK, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_SPRUCE, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_MUSHROOM_RED, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_MUSHROOM_BROWN, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.DREAD_PORTAL, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.GHOST_CHEST, new TEISRItemRenderer());

        IafClientNetworkHandler.register();
    }
}
