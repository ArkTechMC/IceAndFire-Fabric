package com.github.alexthe666.citadel.server.tick;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.message.SyncClientTickRateMessage;
import com.github.alexthe666.citadel.server.world.CitadelServerData;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifier;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifierType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ServerTickRateTracker extends TickRateTracker {
    public static final Logger LOGGER = LogManager.getLogger("citadel-server-tick");

    public MinecraftServer server;

    public ServerTickRateTracker(MinecraftServer server) {
        this.server = server;
    }

    public ServerTickRateTracker(MinecraftServer server, NbtCompound tag) {
        this(server);
        fromTag(tag);
    }

    public void addTickRateModifier(TickRateModifier modifier) {
        tickRateModifierList.add(modifier);
        sync();
    }
    @Override
    public void tickEntityAtCustomRate(Entity entity) {
        if(!entity.method_48926().isClient && entity.method_48926() instanceof ServerWorld){
            ((ServerWorld)entity.method_48926()).tickEntity(entity);
        }
    }

    @Override
    protected void sync() {
        Citadel.sendMSGToAll(new SyncClientTickRateMessage(toTag()));
    }

    public int getServerTickLengthMs() {
        int i = MinecraftServer.MS_PER_TICK;
        for (TickRateModifier modifier : tickRateModifierList) {
            if (modifier.getType() == TickRateModifierType.GLOBAL) {
                i *= modifier.getTickRateMultiplier();
            }
        }
        if (i <= 0) {
            return 1;
        }
        return i;
    }

    public static ServerTickRateTracker getForServer(MinecraftServer server) {
        return CitadelServerData.get(server).getOrCreateTickRateTracker();
    }

    public static void modifyTickRate(World level, TickRateModifier modifier) {
        if (level instanceof ServerWorld serverLevel) {
            getForServer(serverLevel.getServer()).addTickRateModifier(modifier);
        }
    }
}
