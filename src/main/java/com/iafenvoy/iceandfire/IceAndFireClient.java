package com.iafenvoy.iceandfire;

import com.iafenvoy.iceandfire.config.IafClientConfig;
import com.iafenvoy.iceandfire.data.DragonArmor;
import com.iafenvoy.iceandfire.data.SeaSerpent;
import com.iafenvoy.iceandfire.data.TrollType;
import com.iafenvoy.iceandfire.event.ClientEvents;
import com.iafenvoy.iceandfire.network.ClientNetworkHelper;
import com.iafenvoy.iceandfire.registry.*;
import com.iafenvoy.iceandfire.render.TEISRItemRenderer;
import com.iafenvoy.iceandfire.render.TideTridentRenderer;
import com.iafenvoy.iceandfire.render.TrollWeaponRenderer;
import com.iafenvoy.iceandfire.render.armor.*;
import com.iafenvoy.iceandfire.render.item.DeathwormGauntletRenderer;
import com.iafenvoy.iceandfire.render.item.GorgonHeadRenderer;
import com.iafenvoy.iceandfire.render.model.util.DragonAnimationsLibrary;
import com.iafenvoy.iceandfire.render.model.util.EnumDragonModelTypes;
import com.iafenvoy.iceandfire.render.model.util.EnumDragonPoses;
import com.iafenvoy.iceandfire.render.model.util.EnumSeaSerpentAnimations;
import com.iafenvoy.jupiter.malilib.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

@Environment(EnvType.CLIENT)
public class IceAndFireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigManager.getInstance().registerConfigHandler(IafClientConfig.INSTANCE);

        IafScreenHandlers.registerGui();
        EnumSeaSerpentAnimations.initializeSerpentModels();
        DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());
        IafRenderers.registerRenderers();
        IafBlockEntities.registerRenderers();
        IafBlocks.registerRenderLayers();
        IafItems.registerModelPredicates();
        IafKeybindings.init();
        IafRenderers.registerParticleRenderers();

        ArmorRenderer.register(new CopperArmorRenderer(), IafItems.COPPER_HELMET, IafItems.COPPER_CHESTPLATE, IafItems.COPPER_LEGGINGS, IafItems.COPPER_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_WHITE_HELMET, IafItems.DEATHWORM_WHITE_CHESTPLATE, IafItems.DEATHWORM_WHITE_LEGGINGS, IafItems.DEATHWORM_WHITE_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_YELLOW_HELMET, IafItems.DEATHWORM_YELLOW_CHESTPLATE, IafItems.DEATHWORM_YELLOW_LEGGINGS, IafItems.DEATHWORM_YELLOW_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_RED_HELMET, IafItems.DEATHWORM_RED_CHESTPLATE, IafItems.DEATHWORM_RED_LEGGINGS, IafItems.DEATHWORM_RED_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_FIRE_HELMET, IafItems.DRAGONSTEEL_FIRE_CHESTPLATE, IafItems.DRAGONSTEEL_FIRE_LEGGINGS, IafItems.DRAGONSTEEL_FIRE_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_ICE_HELMET, IafItems.DRAGONSTEEL_ICE_CHESTPLATE, IafItems.DRAGONSTEEL_ICE_LEGGINGS, IafItems.DRAGONSTEEL_ICE_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_LIGHTNING_HELMET, IafItems.DRAGONSTEEL_LIGHTNING_CHESTPLATE, IafItems.DRAGONSTEEL_LIGHTNING_LEGGINGS, IafItems.DRAGONSTEEL_LIGHTNING_BOOTS);
        ArmorRenderer.register(new SilverArmorRenderer(), IafItems.SILVER_HELMET, IafItems.SILVER_CHESTPLATE, IafItems.SILVER_LEGGINGS, IafItems.SILVER_BOOTS);
        for (DragonArmor armor : DragonArmor.values())
            ArmorRenderer.register(new ScaleArmorRenderer(), armor.helmet, armor.chestplate, armor.leggings, armor.boots);
        for (SeaSerpent seaSerpent : SeaSerpent.values())
            ArmorRenderer.register(new SeaSerpentArmorRenderer(), seaSerpent.helmet, seaSerpent.chestplate, seaSerpent.leggings, seaSerpent.boots);
        for (TrollType troll : TrollType.values())
            ArmorRenderer.register(new TrollArmorRenderer(), troll.helmet, troll.chestplate, troll.leggings, troll.boots);
        for (TrollType.BuiltinWeapon weapon : TrollType.BuiltinWeapon.values())
            BuiltinItemRendererRegistry.INSTANCE.register(weapon.getItem(), new TrollWeaponRenderer());

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

        UseEntityCallback.EVENT.register(ClientEvents::onEntityInteract);

        ClientNetworkHelper.registerReceivers();
    }
}
