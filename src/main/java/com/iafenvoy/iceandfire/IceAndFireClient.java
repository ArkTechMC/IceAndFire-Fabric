package com.iafenvoy.iceandfire;

import com.iafenvoy.citadel.animation.IAnimatedEntity;
import com.iafenvoy.citadel.client.tick.ClientTickRateTracker;
import com.iafenvoy.iceandfire.entity.EntityDeathWorm;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.block.BlockEntityJar;
import com.iafenvoy.iceandfire.entity.block.BlockEntityPixieHouse;
import com.iafenvoy.iceandfire.entity.block.BlockEntityPodium;
import com.iafenvoy.iceandfire.entity.util.ISyncMount;
import com.iafenvoy.iceandfire.enums.EnumDragonArmor;
import com.iafenvoy.iceandfire.enums.EnumSeaSerpent;
import com.iafenvoy.iceandfire.enums.EnumTroll;
import com.iafenvoy.iceandfire.network.PacketBufferUtils;
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
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class IceAndFireClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
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
        for (EnumDragonArmor armor : EnumDragonArmor.values())
            ArmorRenderer.register(new ScaleArmorRenderer(), armor.helmet, armor.chestplate, armor.leggings, armor.boots);
        for (EnumSeaSerpent seaSerpent : EnumSeaSerpent.values())
            ArmorRenderer.register(new SeaSerpentArmorRenderer(), seaSerpent.helmet, seaSerpent.chestplate, seaSerpent.leggings, seaSerpent.boots);
        for (EnumTroll troll : EnumTroll.values())
            ArmorRenderer.register(new TrollArmorRenderer(), troll.helmet, troll.chestplate, troll.leggings, troll.boots);
        for (EnumTroll.Weapon weapon : EnumTroll.Weapon.values())
            BuiltinItemRendererRegistry.INSTANCE.register(weapon.item, new TrollWeaponRenderer());

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

        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.DEATH_WORM_HITBOX, (client, handler, buf, responseSender) -> {
            PlayerEntity player = client.player;
            if (player != null) {
                Entity entity = player.getWorld().getEntityById(buf.readInt());
                if (entity instanceof EntityDeathWorm deathWorm)
                    deathWorm.initSegments(buf.readFloat());
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.DRAGON_SET_BURN_BLOCK, (client, handler, buf, responseSender) -> {
            PlayerEntity player = client.player;
            if (player != null) {
                Entity entity = player.getWorld().getEntityById(buf.readInt());
                if (entity instanceof EntityDragonBase dragon) {
                    dragon.setBreathingFire(buf.readBoolean());
                    dragon.burningTarget = new BlockPos(buf.readBlockPos());
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.PARTICLE_SPAWN, (client, handler, buf, responseSender) -> {
            assert client.world != null;
            client.world.addParticle((DefaultParticleType) Registries.PARTICLE_TYPE.get(new Identifier(buf.readString())), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.START_RIDING_MOB_S2C, (client, handler, buf, responseSender) -> {
            int dragonId = buf.readInt();
            boolean ride = buf.readBoolean();
            boolean baby = buf.readBoolean();
            PlayerEntity player = client.player;
            if (player != null) {
                Entity entity = player.getWorld().getEntityById(dragonId);
                if (entity instanceof ISyncMount && entity instanceof TameableEntity tamable) {
                    if (tamable.isOwner(player) && tamable.distanceTo(player) < 14) {
                        if (ride) {
                            if (baby) tamable.startRiding(player, true);
                            else player.startRiding(tamable, true);
                        } else {
                            if (baby) tamable.stopRiding();
                            else player.stopRiding();
                        }
                    }
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.UPDATE_PIXIE_HOUSE, (client, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            boolean hasPixie = buf.readBoolean();
            int pixieType = buf.readInt();
            PlayerEntity player = client.player;
            if (player != null) {
                BlockEntity blockEntity = player.getWorld().getBlockEntity(blockPos);
                if (blockEntity instanceof BlockEntityPixieHouse house) {
                    house.hasPixie = hasPixie;
                    house.pixieType = pixieType;
                } else if (blockEntity instanceof BlockEntityJar jar) {
                    jar.hasPixie = hasPixie;
                    jar.pixieType = pixieType;
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.UPDATE_PIXIE_JAR, (client, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            boolean isProducing = buf.readBoolean();
            PlayerEntity player = client.player;
            if (player != null) {
                if (player.getWorld().getBlockEntity(blockPos) instanceof BlockEntityJar jar)
                    jar.hasProduced = isProducing;
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.UPDATE_PODIUM, (client, handler, buf, responseSender) -> {
            BlockPos blockPos = buf.readBlockPos();
            ItemStack heldStack = PacketBufferUtils.readItemStack(buf);
            PlayerEntity player = client.player;
            if (player != null)
                if (player.getWorld().getBlockEntity(blockPos) instanceof BlockEntityPodium podium)
                    podium.setStack(0, heldStack);
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.ANIMATION, (client, handler, buf, responseSender) -> {
            int entityID = buf.readInt();
            int index = buf.readInt();
            if (client.world != null) {
                IAnimatedEntity entity = (IAnimatedEntity) MinecraftClient.getInstance().world.getEntityById(entityID);
                if (entity != null) {
                    if (index == -1) entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                    else entity.setAnimation(entity.getAnimations()[index]);
                    entity.setAnimationTick(0);
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.SYNC_CLIENT_TICK, (client, handler, buf, responseSender) -> {
            ClientTickRateTracker.getForClient(MinecraftClient.getInstance()).syncFromServer(buf.readNbt());
        });
    }
}
