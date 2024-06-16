package dev.arktechmc.iafextra;

import com.github.alexthe666.citadel.client.render.pathfinding.WorldEventContext;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.client.model.util.DragonAnimationsLibrary;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonModelTypes;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonPoses;
import com.github.alexthe666.iceandfire.client.model.util.EnumSeaSerpentAnimations;
import com.github.alexthe666.iceandfire.client.particle.IafParticleRegistry;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.event.ClientEvents;
import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import dev.arktechmc.iafextra.network.IafClientNetworkHandler;
import dev.arktechmc.iafextra.render.*;
import io.github.fabricators_of_create.porting_lib.event.client.LivingEntityRenderEvents;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTooltipBorderColorCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

@Environment(EnvType.CLIENT)
public class IceAndFireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LivingEntityRenderEvents.POST.register(ClientEvents::onPostRenderLiving);
        RenderTooltipBorderColorCallback.EVENT.register((stack, originalBorderColorStart, originalBorderColorEnd) -> new RenderTooltipBorderColorCallback.BorderColorEntry(originalBorderColorStart, originalBorderColorEnd));

        IafContainerRegistry.registerGui();
        EnumSeaSerpentAnimations.initializeSerpentModels();
        DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());
        IafEntityRegistry.registerRenderers();
        IafTileEntityRegistry.registerRenderers();
        IafBlockRegistry.registerRenderLayers();
        IafItemRegistry.registerModelPredicates();
        IafKeybindRegistry.init();
        IafParticleRegistry.registerParticleRenderers();

        WorldRenderEvents.LAST.register(WorldEventContext.INSTANCE::renderWorldLastEvent);
        ArmorRenderer.register(new CopperArmorRenderer(), IafItemRegistry.COPPER_HELMET, IafItemRegistry.COPPER_CHESTPLATE, IafItemRegistry.COPPER_LEGGINGS, IafItemRegistry.COPPER_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_WHITE_HELMET, IafItemRegistry.DEATHWORM_WHITE_CHESTPLATE, IafItemRegistry.DEATHWORM_WHITE_LEGGINGS, IafItemRegistry.DEATHWORM_WHITE_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_YELLOW_HELMET, IafItemRegistry.DEATHWORM_YELLOW_CHESTPLATE, IafItemRegistry.DEATHWORM_YELLOW_LEGGINGS, IafItemRegistry.DEATHWORM_YELLOW_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_RED_HELMET, IafItemRegistry.DEATHWORM_RED_CHESTPLATE, IafItemRegistry.DEATHWORM_RED_LEGGINGS, IafItemRegistry.DEATHWORM_RED_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_FIRE_HELMET, IafItemRegistry.DRAGONSTEEL_FIRE_CHESTPLATE, IafItemRegistry.DRAGONSTEEL_FIRE_LEGGINGS, IafItemRegistry.DRAGONSTEEL_FIRE_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_ICE_HELMET, IafItemRegistry.DRAGONSTEEL_ICE_CHESTPLATE, IafItemRegistry.DRAGONSTEEL_ICE_LEGGINGS, IafItemRegistry.DRAGONSTEEL_ICE_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_HELMET, IafItemRegistry.DRAGONSTEEL_LIGHTNING_CHESTPLATE, IafItemRegistry.DRAGONSTEEL_LIGHTNING_LEGGINGS, IafItemRegistry.DRAGONSTEEL_LIGHTNING_BOOTS);
        ArmorRenderer.register(new SilverArmorRenderer(), IafItemRegistry.SILVER_HELMET, IafItemRegistry.SILVER_CHESTPLATE, IafItemRegistry.SILVER_LEGGINGS, IafItemRegistry.SILVER_BOOTS);
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.DEATHWORM_GAUNTLET_RED, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.DEATHWORM_GAUNTLET_YELLOW, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.DEATHWORM_GAUNTLET_WHITE, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.GORGON_HEAD, new GorgonHeadRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.TIDE_TRIDENT, new TideTridentRenderer());

        BuiltinItemRendererRegistry.INSTANCE.register(IafBlockRegistry.PIXIE_HOUSE_BIRCH, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlockRegistry.PIXIE_HOUSE_OAK, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlockRegistry.PIXIE_HOUSE_DARK_OAK, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlockRegistry.PIXIE_HOUSE_SPRUCE, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlockRegistry.DREAD_PORTAL, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlockRegistry.GHOST_CHEST, new TEISRItemRenderer());

        IafClientNetworkHandler.register();
    }
}
