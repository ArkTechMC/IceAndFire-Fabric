package dev.arktechmc.iafextra;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.ClientProxy;
import com.github.alexthe666.citadel.client.render.CitadelLecternRenderer;
import com.github.alexthe666.citadel.client.render.pathfinding.WorldEventContext;
import com.github.alexthe666.iceandfire.client.IafClientSetup;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.event.ClientEvents;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import dev.arktechmc.iafextra.network.IafClientNetworkHandler;
import dev.arktechmc.iafextra.render.*;
import io.github.fabricators_of_create.porting_lib.event.client.LivingEntityRenderEvents;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTooltipBorderColorCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class IceAndFireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(Citadel.LECTERN_BE.get(), CitadelLecternRenderer::new);

        LivingEntityRenderEvents.PRE.register(ClientEvents::onPreRenderLiving);
        LivingEntityRenderEvents.POST.register(ClientEvents::onPostRenderLiving);
        RenderTooltipBorderColorCallback.EVENT.register(ClientProxy::renderTooltipColor);

        IafClientSetup.clientInit();
        IafClientSetup.setupClient();
        IafKeybindRegistry.init();

        WorldRenderEvents.LAST.register(WorldEventContext.INSTANCE::renderWorldLastEvent);
        ArmorRenderer.register(new CopperArmorRenderer(), IafItemRegistry.COPPER_HELMET.get(), IafItemRegistry.COPPER_CHESTPLATE.get(), IafItemRegistry.COPPER_LEGGINGS.get(), IafItemRegistry.COPPER_BOOTS.get());
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_WHITE_HELMET.get(), IafItemRegistry.DEATHWORM_WHITE_CHESTPLATE.get(), IafItemRegistry.DEATHWORM_WHITE_LEGGINGS.get(), IafItemRegistry.DEATHWORM_WHITE_BOOTS.get());
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_YELLOW_HELMET.get(), IafItemRegistry.DEATHWORM_YELLOW_CHESTPLATE.get(), IafItemRegistry.DEATHWORM_YELLOW_LEGGINGS.get(), IafItemRegistry.DEATHWORM_YELLOW_BOOTS.get());
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItemRegistry.DEATHWORM_RED_HELMET.get(), IafItemRegistry.DEATHWORM_RED_CHESTPLATE.get(), IafItemRegistry.DEATHWORM_RED_LEGGINGS.get(), IafItemRegistry.DEATHWORM_RED_BOOTS.get());
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_FIRE_HELMET.get(), IafItemRegistry.DRAGONSTEEL_FIRE_CHESTPLATE.get(), IafItemRegistry.DRAGONSTEEL_FIRE_LEGGINGS.get(), IafItemRegistry.DRAGONSTEEL_FIRE_BOOTS.get());
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_ICE_HELMET.get(), IafItemRegistry.DRAGONSTEEL_ICE_CHESTPLATE.get(), IafItemRegistry.DRAGONSTEEL_ICE_LEGGINGS.get(), IafItemRegistry.DRAGONSTEEL_ICE_BOOTS.get());
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_HELMET.get(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_CHESTPLATE.get(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_LEGGINGS.get(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_BOOTS.get());
        ArmorRenderer.register(new SilverArmorRenderer(), IafItemRegistry.SILVER_HELMET.get(), IafItemRegistry.SILVER_CHESTPLATE.get(), IafItemRegistry.SILVER_LEGGINGS.get(), IafItemRegistry.SILVER_BOOTS.get());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.DEATHWORM_GAUNTLET_RED.get(), new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.DEATHWORM_GAUNTLET_YELLOW.get(), new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.DEATHWORM_GAUNTLET_WHITE.get(), new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.GORGON_HEAD.get(), new GorgonHeadRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItemRegistry.TIDE_TRIDENT.get(), new TideTridentRenderer());

        IafClientNetworkHandler.register();
    }
}