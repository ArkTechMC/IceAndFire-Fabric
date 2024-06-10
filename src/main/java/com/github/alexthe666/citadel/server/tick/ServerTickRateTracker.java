package com.github.alexthe666.citadel.server.tick;

import com.github.alexthe666.citadel.server.message.SyncClientTickRateMessage;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifier;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifierType;
import com.github.alexthe666.citadel.server.world.CitadelServerData;
import dev.arktechmc.iafextra.StaticVariables;
import dev.arktechmc.iafextra.network.IafServerNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerTickRateTracker extends TickRateTracker {
    public static final Logger LOGGER = LogManager.getLogger("citadel-server-tick");

    public MinecraftServer server;

    public ServerTickRateTracker(MinecraftServer server) {
        this.server = server;
    }

    public ServerTickRateTracker(MinecraftServer server, NbtCompound tag) {
        this(server);
        this.fromTag(tag);
    }

    public static ServerTickRateTracker getForServer(MinecraftServer server) {
        return CitadelServerData.get(server).getOrCreateTickRateTracker();
    }

    public static void modifyTickRate(World level, TickRateModifier modifier) {
        if (level instanceof ServerWorld serverLevel) {
            getForServer(serverLevel.getServer()).addTickRateModifier(modifier);
        }
    }

    public void addTickRateModifier(TickRateModifier modifier) {
        this.tickRateModifierList.add(modifier);
        this.sync();
    }

    @Override
    public void tickEntityAtCustomRate(Entity entity) {
        if (!entity.getWorld().isClient && entity.getWorld() instanceof ServerWorld) {
            ((ServerWorld) entity.getWorld()).tickEntity(entity);
        }
    }

    @Override
    protected void sync() {
        IafServerNetworkHandler.sendToAll(new SyncClientTickRateMessage(this.toTag()));
    }

    public int getServerTickLengthMs() {
        int i = (int) StaticVariables.MSPT;
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (modifier.getType() == TickRateModifierType.GLOBAL) {
                i *= modifier.getTickRateMultiplier();
            }
        }
        if (i <= 0) {
            return 1;
        }
        return i;
    }
}
