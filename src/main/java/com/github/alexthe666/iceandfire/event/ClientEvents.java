package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.client.render.entity.RenderChain;
import com.github.alexthe666.iceandfire.client.render.tile.RenderFrozenState;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.WorldEventContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mod.EventBusSubscriber(modid = IceAndFire.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    private static final Identifier SIREN_SHADER = new Identifier(IceAndFire.MOD_ID,"shaders/post/siren.json");

    private final Random rand = new Random();

    private static boolean shouldCancelRender(LivingEntity living) {
        if (living.getVehicle() != null && living.getVehicle() instanceof EntityDragonBase) {
            return ClientProxy.currentDragonRiders.contains(living.getUuid()) || living == MinecraftClient.getInstance().player && MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderWorldLastEvent(@NotNull final RenderLevelStageEvent event)
    {
        WorldEventContext.INSTANCE.renderWorldLastEvent(event);
    }

    @SubscribeEvent
    public void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player.getVehicle() != null) {
            if (player.getVehicle() instanceof EntityDragonBase) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                float scale = ((EntityDragonBase) player.getVehicle()).getRenderSize() / 3;
                if (MinecraftClient.getInstance().options.getPerspective() == Perspective.THIRD_PERSON_BACK ||
                        MinecraftClient.getInstance().options.getPerspective() == Perspective.THIRD_PERSON_FRONT) {
                    if (currentView == 1) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 1.2F), 0F, 0);
                    } else if (currentView == 2) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 3F), 0F, 0);
                    } else if (currentView == 3) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 5F), 0F, 0);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (event.getEntity() instanceof ICustomMoveController) {
            Entity entity = event.getEntity();
            ICustomMoveController moveController = ((Entity & ICustomMoveController) event.getEntity());
            if (entity.getVehicle() != null && entity.getVehicle() == mc.player) {
                byte previousState = moveController.getControlState();
                moveController.dismount(mc.options.sneakKey.isPressed());
                byte controlState = moveController.getControlState();
                if (controlState != previousState) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
                }
            }
        }
        if (event.getEntity() instanceof PlayerEntity player) {
            if (player.getWorld().isClient) {

                if (player.getVehicle() instanceof ICustomMoveController) {
                    Entity entity = player.getVehicle();
                    ICustomMoveController moveController = ((Entity & ICustomMoveController) player.getVehicle());
                    byte previousState = moveController.getControlState();
                    moveController.up(mc.options.jumpKey.isPressed());
                    moveController.down(IafKeybindRegistry.dragon_down.isPressed());
                    moveController.attack(IafKeybindRegistry.dragon_strike.isPressed());
                    moveController.dismount(mc.options.sneakKey.isPressed());
                    moveController.strike(IafKeybindRegistry.dragon_fireAttack.isPressed());
                    byte controlState = moveController.getControlState();
                    if (controlState != previousState) {
                        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
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

            if (player.getWorld().isClient) {
                GameRenderer renderer = MinecraftClient.getInstance().gameRenderer;

                EntityDataProvider.getCapability(player).ifPresent(data -> {
                    if (IafConfig.sirenShader && data.sirenData.charmedBy == null && renderer.currentEffect() != null) {
                        if (SIREN_SHADER.toString().equals(renderer.currentEffect().getName()))
                            renderer.shutdownEffect();
                    }

                    if (data.sirenData.charmedBy == null) {
                        return;
                    }

                    if (IafConfig.sirenShader && !data.sirenData.isCharmed && renderer.currentEffect() != null && SIREN_SHADER.toString().equals(renderer.currentEffect().getName())) {
                        renderer.shutdownEffect();
                    }

                if (data.sirenData.isCharmed) {
                    if (player.level().isClientSide && rand.nextInt(40) == 0) {
                        IceAndFire.PROXY.spawnParticle(EnumParticles.Siren_Appearance, player.getX(), player.getY(), player.getZ(), data.sirenData.charmedBy.getHairColor(), 0, 0);
                    }

                        if (IafConfig.sirenShader && renderer.currentEffect() == null) {
                            renderer.loadEffect(SIREN_SHADER);
                        }

                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if (shouldCancelRender(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        if (shouldCancelRender(event.getEntity())) {
            event.setCanceled(true);
        }

        LivingEntity entity = event.getEntity();

        EntityDataProvider.getCapability(entity).ifPresent(data -> {
            for (LivingEntity target : data.miscData.getTargetedByScepter()) {
                CockatriceBeamRender.render(entity, target, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
            }

            if (data.frozenData.isFrozen) {
                RenderFrozenState.render(event.getEntity(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), data.frozenData.frozenTicks);
            }

            RenderChain.render(entity, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), data.chainData.getChainedTo());
        });
    }

    @SubscribeEvent
    public void onGuiOpened(ScreenEvent.Opening event) {
        if (IafConfig.customMainMenu && event.getScreen() instanceof TitleScreen && !(event.getScreen() instanceof IceAndFireMainMenu)) {
            event.setNewScreen(new IceAndFireMainMenu());
        }
    }

    // TODO: add this to client side config
    public final boolean AUTO_ADAPT_3RD_PERSON = true;

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        if (event.getEntityBeingMounted() instanceof EntityDragonBase dragon && event.getLevel().isClientSide && event.getEntityMounting() == MinecraftClient.getInstance().player) {
            if (dragon.isTamed() && dragon.isOwner(MinecraftClient.getInstance().player)) {
                if (AUTO_ADAPT_3RD_PERSON) {
                    // Auto adjust 3rd person camera's according to dragon's size
                    IceAndFire.PROXY.setDragon3rdPersonView(2);
                }
                if (IafConfig.dragonAuto3rdPerson) {
                    if (event.isDismounting()) {
                        MinecraftClient.getInstance().options.setPerspective(Perspective.values()[IceAndFire.PROXY.getPreviousViewType()]);
                    } else {
                        IceAndFire.PROXY.setPreviousViewType(MinecraftClient.getInstance().options.getPerspective().ordinal());
                        MinecraftClient.getInstance().options.setPerspective(Perspective.values()[1]);
                    }
                }
            }
        }
    }
}