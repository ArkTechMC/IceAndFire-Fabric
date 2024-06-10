package com.github.alexthe666.citadel;

import com.github.alexthe666.citadel.server.event.EventChangeEntityTickRate;
import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import dev.arktechmc.iafextra.event.EventBus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ServerProxy {


    public ServerProxy() {
    }

    public void onPreInit() {
    }

    public void handleAnimationPacket(int entityId, int index) {

    }

    public void handlePropertiesPacket(String propertyID, NbtCompound compound, int entityID) {
    }

    public void handleClientTickRatePacket(NbtCompound compound) {
    }


    public void openBookGUI(ItemStack book) {
    }

    public void onClientInit() {
    }

    /*
        Biome gen example. Place
        ExpandedBiomes.addExpandedBiome(Biomes.WARPED_FOREST, LevelStem.OVERWORLD);
        In mod's constructor in order to work before trying something similar to this.

    @SubscribeEvent
    public void onReplaceBiome(EventReplaceBiome event){
        if(event.weirdness > 0.5F && event.weirdness < 1F && event.depth > 0.2F && event.depth < 0.9F){
            event.setResult(Event.Result.ALLOW);
            event.setBiomeToGenerate(event.getBiomeSource().getResourceKeyMap().get(Biomes.WARPED_FOREST));
        }
    }
    */

    public boolean canEntityTickClient(World level, Entity entity) {
        return true;
    }

    public boolean canEntityTickServer(World level, Entity entity) {
        if (level instanceof ServerWorld) {
            ServerTickRateTracker tracker = ServerTickRateTracker.getForServer(((ServerWorld) level).getServer());
            if (tracker.isTickingHandled(entity)) {
                return false;
            } else if (!tracker.hasNormalTickRate(entity)) {
                EventChangeEntityTickRate event = new EventChangeEntityTickRate(entity, tracker.getEntityTickLengthModifier(entity));
                EventBus.post(event);
                if (event.isCanceled()) {
                    return true;
                } else {
                    tracker.addTickBlockedEntity(entity);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGamePaused() {
        return false;
    }

    public float getMouseOverProgress(ItemStack itemStack) {
        return 0.0F;
    }

    public PlayerEntity getClientSidePlayer() {
        return null;
    }
}
