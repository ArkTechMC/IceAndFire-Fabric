package com.github.alexthe666.citadel;

import com.github.alexthe666.citadel.server.event.EventChangeEntityTickRate;
import com.github.alexthe666.citadel.server.world.CitadelServerData;
import com.github.alexthe666.citadel.server.world.ModifiableTickRateServer;
import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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

    public Object getISTERProperties() {
        return null;
    }

    public void onClientInit() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onServerTick(TickEvent.ServerTickEvent event) {
        ServerTickRateTracker tickRateTracker = CitadelServerData.get(event.getServer()).getOrCreateTickRateTracker();
        if (event.getServer() instanceof ModifiableTickRateServer modifiableServer && event.phase == TickEvent.Phase.START) {
            long l = tickRateTracker.getServerTickLengthMs();
            if (l == MinecraftServer.MS_PER_TICK) {
                modifiableServer.resetGlobalTickLengthMs();
            } else {
                modifiableServer.setGlobalTickLengthMs(tickRateTracker.getServerTickLengthMs());
            }
            tickRateTracker.masterTick();
        }
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
                MinecraftForge.EVENT_BUS.post(event);
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
