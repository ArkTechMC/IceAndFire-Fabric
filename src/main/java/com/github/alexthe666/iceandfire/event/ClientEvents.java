package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.client.render.entity.RenderChain;
import com.github.alexthe666.iceandfire.client.render.tile.RenderFrozenState;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import dev.arktechmc.iafextra.data.EntityDataComponent;
import dev.arktechmc.iafextra.network.IafClientNetworkHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ClientEvents {
//    public static boolean onCameraSetup(CameraSetupCallback.CameraInfo info) {
//        PlayerEntity player = MinecraftClient.getInstance().player;
//        if (player.getVehicle() != null) {
//            if (player.getVehicle() instanceof EntityDragonBase) {
//                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
//                float scale = ((EntityDragonBase) player.getVehicle()).getRenderSize() / 3;
//                if (MinecraftClient.getInstance().options.getPerspective() == Perspective.THIRD_PERSON_BACK ||
//                        MinecraftClient.getInstance().options.getPerspective() == Perspective.THIRD_PERSON_FRONT) {
//                    if (currentView == 1) {
//                        info.camera.move(-info.camera.getMaxZoom(scale * 1.2F), 0F, 0);
//                    } else if (currentView == 2) {
//                        info.camera.move(-info.camera.getMaxZoom(scale * 3F), 0F, 0);
//                    } else if (currentView == 3) {
//                        info.camera.move(-info.camera.getMaxZoom(scale * 5F), 0F, 0);
//                    }
//                }
//            }
//        }
//    }

    private static boolean shouldCancelRender(LivingEntity living) {
        if (living.getVehicle() != null && living.getVehicle() instanceof EntityDragonBase) {
            return ClientProxy.currentDragonRiders.contains(living.getUuid()) || living == MinecraftClient.getInstance().player && MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
        }
        return false;
    }

    public static void onLivingUpdate(LivingEntity entity) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (entity instanceof ICustomMoveController moveController) {
            if (entity.getVehicle() != null && entity.getVehicle() == mc.player) {
                byte previousState = moveController.getControlState();
                moveController.dismount(mc.options.sneakKey.isPressed());
                byte controlState = moveController.getControlState();
                if (controlState != previousState) {
                    IafClientNetworkHandler.send(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
                }
            }
        }
        if (entity instanceof PlayerEntity player) {
            if (player.getWorld().isClient) {

                if (player.getVehicle() instanceof ICustomMoveController) {
                    Entity vehicle = player.getVehicle();
                    ICustomMoveController moveController = ((Entity & ICustomMoveController) player.getVehicle());
                    byte previousState = moveController.getControlState();
                    moveController.up(mc.options.jumpKey.isPressed());
                    moveController.down(IafKeybindRegistry.dragon_down.isPressed());
                    moveController.attack(IafKeybindRegistry.dragon_strike.isPressed());
                    moveController.dismount(mc.options.sneakKey.isPressed());
                    moveController.strike(IafKeybindRegistry.dragon_fireAttack.isPressed());
                    byte controlState = moveController.getControlState();
                    if (controlState != previousState) {
                        IafClientNetworkHandler.send(new MessageDragonControl(vehicle.getId(), controlState, vehicle.getX(), vehicle.getY(), vehicle.getZ()));
                    }
                }
            }
            if (player.getWorld().isClient && IafKeybindRegistry.dragon_change_view.isPressed()) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                if (currentView + 1 > 3) {
                    currentView = 0;
                } else {
                    currentView++;
                }
                IceAndFire.PROXY.setDragon3rdPersonView(currentView);
            }
        }
    }

    public static boolean onPreRenderLiving(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialRenderTick, MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
        return shouldCancelRender(entity);
    }

    public static void onPostRenderLiving(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialRenderTick, MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
        if (shouldCancelRender(entity)) return;
        EntityDataComponent data = EntityDataComponent.ENTITY_DATA_COMPONENT.get(entity);
        for (LivingEntity target : data.miscData.getTargetedByScepter()) {
            CockatriceBeamRender.render(entity, target, matrixStack, buffers, partialRenderTick);
        }
        if (data.frozenData.isFrozen) {
            RenderFrozenState.render(entity, matrixStack, buffers, light, data.frozenData.frozenTicks);
        }
        RenderChain.render(entity, partialRenderTick, matrixStack, buffers, light, data.chainData.getChainedTo());
    }

//    @SubscribeEvent
//    public void onEntityMount(EntityMountEvent event) {
//        if (event.getEntityBeingMounted() instanceof EntityDragonBase dragon && event.getLevel().isClientSide && event.getEntityMounting() == MinecraftClient.getInstance().player) {
//            if (dragon.isTamed() && dragon.isOwner(MinecraftClient.getInstance().player)) {
//                if (this.AUTO_ADAPT_3RD_PERSON) {
//                    // Auto adjust 3rd person camera's according to dragon's size
//                    IceAndFire.PROXY.setDragon3rdPersonView(2);
//                }
//                if (IafConfig.dragonAuto3rdPerson) {
//                    if (event.isDismounting()) {
//                        MinecraftClient.getInstance().options.setPerspective(Perspective.values()[IceAndFire.PROXY.getPreviousViewType()]);
//                    } else {
//                        IceAndFire.PROXY.setPreviousViewType(MinecraftClient.getInstance().options.getPerspective().ordinal());
//                        MinecraftClient.getInstance().options.setPerspective(Perspective.values()[1]);
//                    }
//                }
//            }
//        }
//    }
}