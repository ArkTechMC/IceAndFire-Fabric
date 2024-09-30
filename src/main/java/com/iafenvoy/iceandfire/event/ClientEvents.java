package com.iafenvoy.iceandfire.event;

import com.iafenvoy.iceandfire.StaticVariables;
import com.iafenvoy.iceandfire.config.IafClientConfig;
import com.iafenvoy.iceandfire.data.component.EntityDataComponent;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.EntityMultipartPart;
import com.iafenvoy.iceandfire.entity.util.ICustomMoveController;
import com.iafenvoy.iceandfire.particle.CockatriceBeamRender;
import com.iafenvoy.iceandfire.registry.IafKeybindings;
import com.iafenvoy.iceandfire.registry.IafParticles;
import com.iafenvoy.iceandfire.render.block.RenderFrozenState;
import com.iafenvoy.iceandfire.render.entity.RenderChain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    private static final Identifier SIREN_SHADER = new Identifier("iceandfire", "shaders/post/siren.json");
    public static int currentView = 0;

    public static void onCameraSetup(Camera camera) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && player.getVehicle() instanceof EntityDragonBase) {
            float scale = ((EntityDragonBase) player.getVehicle()).getRenderSize() / 3;
            if (MinecraftClient.getInstance().options.getPerspective() == Perspective.THIRD_PERSON_BACK ||
                    MinecraftClient.getInstance().options.getPerspective() == Perspective.THIRD_PERSON_FRONT) {
                if (currentView == 1) {
                    camera.moveBy(-camera.clipToSpace(scale * 1.2F), 0F, 0);
                } else if (currentView == 2) {
                    camera.moveBy(-camera.clipToSpace(scale * 3F), 0F, 0);
                } else if (currentView == 3) {
                    camera.moveBy(-camera.clipToSpace(scale * 5F), 0F, 0);
                }
            }
        }
    }

    public static ActionResult onEntityInteract(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        // Hook multipart
        if (entity instanceof EntityMultipartPart) return ActionResult.SUCCESS;
        return ActionResult.PASS;
    }

    public static void onLivingUpdate(LivingEntity entity) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (entity instanceof ICustomMoveController moveController) {
            if (entity.getVehicle() != null && entity.getVehicle() == mc.player) {
                byte previousState = moveController.getControlState();
                moveController.dismount(mc.options.sneakKey.isPressed());
                byte controlState = moveController.getControlState();
                if (controlState != previousState) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(entity.getId()).writeByte(controlState);
                    buf.writeBlockPos(entity.getBlockPos());
                    ClientPlayNetworking.send(StaticVariables.DRAGON_CONTROL, buf);
                }
            }
        }
        if (entity instanceof PlayerEntity player && player.getWorld().isClient) {
            if (player.getVehicle() instanceof ICustomMoveController controller) {
                Entity vehicle = player.getVehicle();
                byte previousState = controller.getControlState();
                controller.up(mc.options.jumpKey.isPressed());
                controller.down(IafKeybindings.dragon_down.isPressed());
                controller.attack(IafKeybindings.dragon_strike.isPressed());
                controller.dismount(mc.options.sneakKey.isPressed());
                controller.strike(IafKeybindings.dragon_fireAttack.isPressed());
                byte controlState = controller.getControlState();
                if (controlState != previousState) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(vehicle.getId()).writeByte(controlState);
                    buf.writeBlockPos(vehicle.getBlockPos());
                    ClientPlayNetworking.send(StaticVariables.DRAGON_CONTROL, buf);
                }
            }
            GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;
            EntityDataComponent data = EntityDataComponent.get(player);
            if (IafClientConfig.INSTANCE.sirenShader.getBooleanValue() && data.sirenData.charmedBy == null && renderer.getPostProcessor() != null)
                if (SIREN_SHADER.toString().equals(renderer.getPostProcessor().getName()))
                    renderer.disablePostProcessor();
            if (data.sirenData.charmedBy == null) return;
            if (IafClientConfig.INSTANCE.sirenShader.getBooleanValue() && !data.sirenData.isCharmed && renderer.getPostProcessor() != null && SIREN_SHADER.toString().equals(renderer.getPostProcessor().getName()))
                renderer.disablePostProcessor();
            if (data.sirenData.isCharmed) {
                if (entity.getRandom().nextInt(40) == 0)
                    entity.getWorld().addParticle(IafParticles.SIREN_APPEARANCE, player.getX(), player.getY(), player.getZ(), data.sirenData.charmedBy.getHairColor(), 0, 0);
                if (IafClientConfig.INSTANCE.sirenShader.getBooleanValue() && renderer.getPostProcessor() == null)
                    renderer.loadPostProcessor(SIREN_SHADER);
            }
        }
    }

    public static void onPostRenderLiving(LivingEntity entity, float partialRenderTick, MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
        EntityDataComponent data = EntityDataComponent.get(entity);
        data.miscData.checkScepterTarget();
        for (LivingEntity target : data.miscData.getTargetedByScepter())
            CockatriceBeamRender.render(entity, target, matrixStack, buffers, partialRenderTick);
        if (data.frozenData.isFrozen)
            RenderFrozenState.render(entity, matrixStack, buffers, light, data.frozenData.frozenTicks);
        RenderChain.render(entity, matrixStack, buffers, light, data.chainData.getChainedTo());
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