package com.github.alexthe666.citadel;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.gui.GuiCitadelBook;
import com.github.alexthe666.citadel.client.tick.ClientTickRateTracker;
import com.github.alexthe666.citadel.item.ItemWithHoverAnimation;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.event.EventChangeEntityTickRate;
import dev.arktechmc.iafextra.event.EventBus;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTooltipBorderColorCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy extends ServerProxy {
    private final Map<ItemStack, Float> prevMouseOverProgresses = new HashMap<>();
    private final Map<ItemStack, Float> mouseOverProgresses = new HashMap<>();
    private static ItemStack lastHoveredItem = null;

    public ClientProxy() {
        super();
    }

    public static RenderTooltipBorderColorCallback.BorderColorEntry renderTooltipColor(ItemStack stack, int originalBorderColorStart, int originalBorderColorEnd) {
        if (stack.getItem() instanceof ItemWithHoverAnimation hoverOver && hoverOver.canHoverOver(stack)) {
            lastHoveredItem = stack;
        } else {
            lastHoveredItem = null;
        }
        return new RenderTooltipBorderColorCallback.BorderColorEntry(originalBorderColorStart, originalBorderColorEnd);
    }

    @Override
    public float getMouseOverProgress(ItemStack itemStack) {
        float prev = this.prevMouseOverProgresses.getOrDefault(itemStack, 0F);
        float current = this.mouseOverProgresses.getOrDefault(itemStack, 0F);
        float lerped = prev + (current - prev) * MinecraftClient.getInstance().getTickDelta();
        float maxTime = 5F;
        if (itemStack.getItem() instanceof ItemWithHoverAnimation hoverOver) {
            maxTime = hoverOver.getMaxHoverOverTime(itemStack);
        }
        return lerped / maxTime;
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
        if (compound == null || MinecraftClient.getInstance().world == null) {
            return;
        }
        Entity entity = MinecraftClient.getInstance().world.getEntityById(entityID);
        if ((propertyID.equals("CitadelPatreonConfig") || propertyID.equals("CitadelTagUpdate")) && entity instanceof LivingEntity) {
            CitadelEntityData.setCitadelTag((LivingEntity) entity, compound);
        }
    }


    @Override
    public void handleClientTickRatePacket(NbtCompound compound) {
        ClientTickRateTracker.getForClient(MinecraftClient.getInstance()).syncFromServer(compound);
    }

    @Override
    public void openBookGUI(ItemStack book) {
        MinecraftClient.getInstance().setScreen(new GuiCitadelBook(book));
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
