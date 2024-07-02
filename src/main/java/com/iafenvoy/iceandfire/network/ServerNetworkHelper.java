package com.iafenvoy.iceandfire.network;

import com.iafenvoy.iceandfire.StaticVariables;
import com.iafenvoy.iceandfire.entity.*;
import com.iafenvoy.iceandfire.entity.util.ISyncMount;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.event.ServerEvents;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ServerNetworkHelper {
    public static void sendToAll(Identifier id, PacketByteBuf buf) {
        if (StaticVariables.server != null)
            for (ServerPlayerEntity player : StaticVariables.server.getPlayerManager().getPlayerList())
                ServerPlayNetworking.send(player, id, buf);
    }

    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.MYRMEX_SYNC, (server, player, handler, buf, responseSender) -> {
            MyrmexHive serverHive = MyrmexHive.fromNBT(buf.readNbt());
            NbtCompound tag = new NbtCompound();
            serverHive.writeVillageDataToNBT(tag);
            serverHive.readVillageDataFromNBT(tag);
            if (player != null) {
                MyrmexHive realHive = MyrmexWorldData.get(player.getWorld()).getHiveFromUUID(serverHive.hiveUUID);
                realHive.readVillageDataFromNBT(serverHive.toNBT());
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.DRAGON_CONTROL, (server, player, handler, buf, responseSender) -> {
            int dragonId = buf.readInt();
            byte controlState = buf.readByte();
            BlockPos pos = buf.readBlockPos();

            if (player != null) {
                Entity entity = player.getWorld().getEntityById(dragonId);
                if (ServerEvents.isRidingOrBeingRiddenBy(entity, player)) {
                        /*
                            For some of these entities the `setPos` is handled in `Entity#move`
                            Doing it here would cause server-side movement checks to fail (resulting in "moved wrongly" messages)
                        */
                    if (entity instanceof EntityDragonBase dragon) {
                        if (dragon.isOwner(player))
                            dragon.setControlState(controlState);
                    } else if (entity instanceof EntityHippogryph hippogryph) {
                        if (hippogryph.isOwner(player))
                            hippogryph.setControlState(controlState);
                    } else if (entity instanceof EntityHippocampus hippo) {
                        if (hippo.isOwner(player))
                            hippo.setControlState(controlState);
                        hippo.setPos(pos.getX(), pos.getY(), pos.getZ());
                    } else if (entity instanceof EntityDeathWorm deathWorm) {
                        deathWorm.setControlState(controlState);
                        deathWorm.setPos(pos.getX(), pos.getY(), pos.getZ());
                    } else if (entity instanceof EntityAmphithere amphithere) {
                        if (amphithere.isOwner(player))
                            amphithere.setControlState(controlState);
                        // TODO :: Is this handled by Entity#move due to recent changes?
                        amphithere.setPos(pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.MULTIPART_INTERACT, (server, player, handler, buf, responseSender) -> {
            int creatureID = buf.readInt();
            float dmg = buf.readFloat();

            if (player != null) {
                Entity entity = player.getWorld().getEntityById(creatureID);
                if (entity instanceof LivingEntity livingEntity) {
                    double dist = player.distanceTo(livingEntity);
                    if (dist < 100) {
                        if (dmg > 0F) livingEntity.damage(player.getWorld().damageSources.mobAttack(player), dmg);
                        else livingEntity.interact(player, Hand.MAIN_HAND);
                    }
                }
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.PLAYER_HIT_MULTIPART, (server, player, handler, buf, responseSender) -> {
            if (player != null) {
                Entity entity = player.getWorld().getEntityById(buf.readInt());
                if (entity instanceof LivingEntity livingEntity) {
                    double dist = player.distanceTo(livingEntity);
                    if (dist < 100) {
                        player.attack(livingEntity);
                        if (livingEntity instanceof EntityHydra hydra)
                            hydra.triggerHeadFlags(buf.readInt());
                    }
                }
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(StaticVariables.START_RIDING_MOB_C2S, (server, player, handler, buf, responseSender) -> {
            int dragonId = buf.readInt();
            boolean ride = buf.readBoolean();
            boolean baby = buf.readBoolean();
            if (player != null) {
                Entity entity = player.getWorld().getEntityById(dragonId);
                if (entity instanceof ISyncMount && entity instanceof TameableEntity tamable)
                    if (tamable.isOwner(player) && tamable.distanceTo(player) < 14)
                        if (ride) {
                            if (baby) tamable.startRiding(player, true);
                            else player.startRiding(tamable, true);
                        } else {
                            if (baby) tamable.stopRiding();
                            else player.stopRiding();
                        }
            }
        });
    }
}
