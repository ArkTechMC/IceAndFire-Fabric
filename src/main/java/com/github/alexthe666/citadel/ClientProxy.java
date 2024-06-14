package com.github.alexthe666.citadel;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.tick.ClientTickRateTracker;
import com.github.alexthe666.citadel.server.event.EventChangeEntityTickRate;
import dev.arktechmc.iafextra.event.EventBus;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTooltipBorderColorCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy extends ServerProxy {
    public ClientProxy() {
        super();
    }

    public static RenderTooltipBorderColorCallback.BorderColorEntry renderTooltipColor(ItemStack stack, int originalBorderColorStart, int originalBorderColorEnd) {
        return new RenderTooltipBorderColorCallback.BorderColorEntry(originalBorderColorStart, originalBorderColorEnd);
    }

    @Override
    public float getMouseOverProgress(ItemStack itemStack) {
        return super.getMouseOverProgress(itemStack);
    }

    @Override
    public void handleAnimationPacket(int entityId, int index) {
        if (MinecraftClient.getInstance().world != null) {
            IAnimatedEntity entity = (IAnimatedEntity) MinecraftClient.getInstance().world.getEntityById(entityId);
            if (entity != null) {
                if (index == -1) {
                    entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
                } else {
                    entity.setAnimation(entity.getAnimations()[index]);
                }
                entity.setAnimationTick(0);
            }
        }
    }

    @Override
    public void handlePropertiesPacket(String propertyID, NbtCompound compound, int entityID) {
        super.handlePropertiesPacket(propertyID, compound, entityID);
    }


    @Override
    public void handleClientTickRatePacket(NbtCompound compound) {
        ClientTickRateTracker.getForClient(MinecraftClient.getInstance()).syncFromServer(compound);
    }

    @Override
    public void openBookGUI(ItemStack book) {
    }

    public boolean isGamePaused() {
        return MinecraftClient.getInstance().isPaused();
    }

    public PlayerEntity getClientSidePlayer() {
        return MinecraftClient.getInstance().player;
    }

    public boolean canEntityTickClient(World level, Entity entity) {
        ClientTickRateTracker tracker = ClientTickRateTracker.getForClient(MinecraftClient.getInstance());
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
        return true;
    }
}
