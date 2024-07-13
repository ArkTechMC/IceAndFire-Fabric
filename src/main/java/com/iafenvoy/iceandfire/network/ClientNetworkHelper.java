package com.iafenvoy.iceandfire.network;

import com.iafenvoy.uranus.animation.IAnimatedEntity;
import com.iafenvoy.uranus.client.tick.ClientTickRateTracker;
import com.iafenvoy.iceandfire.StaticVariables;
import com.iafenvoy.iceandfire.entity.EntityDeathWorm;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.block.BlockEntityJar;
import com.iafenvoy.iceandfire.entity.block.BlockEntityPixieHouse;
import com.iafenvoy.iceandfire.entity.block.BlockEntityPodium;
import com.iafenvoy.iceandfire.entity.util.ISyncMount;
import com.iafenvoy.uranus.network.PacketBufferUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ClientNetworkHelper {
    public static void registerReceivers() {
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
            Identifier particleId = new Identifier(buf.readString());
            final double x = buf.readDouble(), y = buf.readDouble(), z = buf.readDouble();
            final double velocityX = buf.readDouble(), velocityY = buf.readDouble(), velocityZ = buf.readDouble();
            client.execute(() -> client.world.addParticle((DefaultParticleType) Registries.PARTICLE_TYPE.get(particleId), x, y, z, velocityX, velocityY, velocityZ));
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
                IAnimatedEntity entity = (IAnimatedEntity) client.world.getEntityById(entityID);
                if (entity != null) {
                    if (index == -1) entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                    else entity.setAnimation(entity.getAnimations()[index]);
                    entity.setAnimationTick(0);
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(StaticVariables.SYNC_CLIENT_TICK, (client, handler, buf, responseSender) -> {
            ClientTickRateTracker.getForClient(client).syncFromServer(buf.readNbt());
        });
    }
}
