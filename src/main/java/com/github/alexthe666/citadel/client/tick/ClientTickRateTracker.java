package com.github.alexthe666.citadel.client.tick;

import com.github.alexthe666.citadel.server.tick.TickRateTracker;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public class ClientTickRateTracker extends TickRateTracker {
    public static final Logger LOGGER = LogManager.getLogger("citadel-client-tick");
    private static final Map<MinecraftClient, ClientTickRateTracker> dataMap = new HashMap<>();

    public MinecraftClient client;

    private static final float MS_PER_TICK = 50f;

    public ClientTickRateTracker(MinecraftClient client) {
        this.client = client;

    }

    public void syncFromServer(NbtCompound tag){
        tickRateModifierList.clear();
        fromTag(tag);
    }

    public static ClientTickRateTracker getForClient(MinecraftClient minecraft){
        if(!dataMap.containsKey(minecraft)){
            ClientTickRateTracker tracker = new ClientTickRateTracker(minecraft);
            dataMap.put(minecraft, tracker);
            return tracker;
        }
        return dataMap.get(minecraft);
    }

    public void masterTick(){
        super.masterTick();
        client.renderTickCounter.tickTime = getClientTickRate();
    }

    public float getClientTickRate(){
        float f = MS_PER_TICK;
        for(TickRateModifier modifier : tickRateModifierList){
            if(modifier.appliesTo(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ())){
                f *= modifier.getTickRateMultiplier();
            }
        }
        return Math.max(1F, f * getEntityTickLengthModifier(MinecraftClient.getInstance().player));
    }

    public float modifySoundPitch(SoundInstance soundInstance) {
        float f = 1.0F;
        for(TickRateModifier modifier : tickRateModifierList){
            if(modifier.appliesTo(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ())){
                f /= modifier.getTickRateMultiplier();
            }
        }
        return Math.max(1F, f * getEntityTickLengthModifier(MinecraftClient.getInstance().player));
    }

    @Override
    public void tickEntityAtCustomRate(Entity entity) {
        if(entity.getWorld().isClient && entity.getWorld() instanceof ClientWorld){
            ((ClientWorld)entity.getWorld()).tickEntity(entity);
        }
    }
}
