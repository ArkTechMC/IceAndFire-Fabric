package com.iafenvoy.iceandfire;

import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.config.IafConfigSerializer;
import com.iafenvoy.iceandfire.entity.*;
import com.iafenvoy.iceandfire.entity.util.ISyncMount;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.event.EntityEvents;
import com.iafenvoy.iceandfire.event.LivingEntityEvents;
import com.iafenvoy.iceandfire.event.PlayerEvents;
import com.iafenvoy.iceandfire.event.ServerEvents;
import com.iafenvoy.iceandfire.registry.*;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class IceAndFire implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "iceandfire";
    public static final String MOD_NAME = "Ice And Fire";
    public static final String VERSION;

    static {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(IceAndFire.MOD_ID);
        if (container.isPresent()) VERSION = container.get().getMetadata().getVersion().getFriendlyString();
        else VERSION = "Unknown";
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(IafConfig.class, (config, aClass) -> new IafConfigSerializer());

        IafItems.init();
        IafBlocks.init();
        IafEntities.init();
        IafSounds.init();
        IafTrades.init();
        IafRecipes.init();
        IafLoots.init();
        IafFeatures.init();
        IafBlockEntities.init();
        IafBannerPatterns.init();
        IafPlacementFilters.init();
        IafStructureTypes.init();
        IafStructures.init();
        IafScreenHandlers.init();
        IafRecipeSerializers.init();
        IafProcessors.init();
        IafItemGroups.init();
        IafParticles.init();

        PlayerBlockBreakEvents.AFTER.register(ServerEvents::onBreakBlock);
        UseEntityCallback.EVENT.register(ServerEvents::onEntityInteract);
        UseItemCallback.EVENT.register(ServerEvents::onEntityUseItem);
        UseBlockCallback.EVENT.register(ServerEvents::onPlayerRightClick);
        ServerLivingEntityEvents.AFTER_DEATH.register(ServerEvents::onEntityDie);
        AttackEntityCallback.EVENT.register(ServerEvents::onPlayerAttack);
        PlayerEvents.LOGGED_OUT.register(ServerEvents::onPlayerLeaveEvent);
        EntityEvents.ON_JOIN_WORLD.register(ServerEvents::onEntityJoinWorld);
        EntityEvents.START_TRACKING_TAIL.register(ServerEvents::onLivingSetTarget);
        LivingEntityEvents.DAMAGE.register(ServerEvents::onEntityDamage);
        LivingEntityEvents.FALL.register(ServerEvents::onEntityFall);

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
                        if (dragon.isOwner(player)) {
                            dragon.setControlState(controlState);
                        }
                    } else if (entity instanceof EntityHippogryph hippogryph) {
                        if (hippogryph.isOwner(player)) {
                            hippogryph.setControlState(controlState);
                        }
                    } else if (entity instanceof EntityHippocampus hippo) {
                        if (hippo.isOwner(player)) {
                            hippo.setControlState(controlState);
                        }

                        hippo.setPos(pos.getX(), pos.getY(), pos.getZ());
                    } else if (entity instanceof EntityDeathWorm deathWorm) {
                        deathWorm.setControlState(controlState);
                        deathWorm.setPos(pos.getX(), pos.getY(), pos.getZ());
                    } else if (entity instanceof EntityAmphithere amphithere) {
                        if (amphithere.isOwner(player)) {
                            amphithere.setControlState(controlState);
                        }

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
    }
}