package com.github.alexthe666.iceandfire.client;

import com.github.alexthe666.iceandfire.CommonProxy;
import com.github.alexthe666.iceandfire.client.gui.GuiMyrmexAddRoom;
import com.github.alexthe666.iceandfire.client.gui.GuiMyrmexStaff;
import com.github.alexthe666.iceandfire.client.gui.bestiary.GuiBestiary;
import com.github.alexthe666.iceandfire.client.particle.*;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClientProxy extends CommonProxy {

    public static Set<UUID> currentDragonRiders = new HashSet<UUID>();
    private static MyrmexHive referedClientHive = null;
    private int previousViewType = 0;
    private int thirdPersonViewDragon = 0;
    private Entity referencedMob = null;
    private BlockEntity referencedTE = null;

    public static MyrmexHive getReferedClientHive() {
        return referedClientHive;
    }

    @Override
    public void setReferencedHive(MyrmexHive hive) {
        referedClientHive = hive;
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {

    }

    @Environment(EnvType.CLIENT)
    @Override
    public void spawnDragonParticle(final EnumParticles name, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return;
        }
        net.minecraft.client.particle.Particle particle = null;
        if (name == EnumParticles.DragonFire) {
            particle = new ParticleDragonFlame(world, x, y, z, motX, motY, motZ, entityDragonBase, 0);
        } else if (name == EnumParticles.DragonIce) {
            particle = new ParticleDragonFrost(world, x, y, z, motX, motY, motZ, entityDragonBase, 0);
        }
        if (particle != null) {
            MinecraftClient.getInstance().particleManager.addParticle(particle);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void spawnParticle(final EnumParticles name, double x, double y, double z, double motX, double motY, double motZ, float size) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return;
        }
        Particle particle = switch (name) {
            case DragonFire -> new ParticleDragonFlame(world, x, y, z, motX, motY, motZ, size);
            case DragonIce -> new ParticleDragonFrost(world, x, y, z, motX, motY, motZ, size);
            case Dread_Torch -> new ParticleDreadTorch(world, x, y, z, motX, motY, motZ, size);
            case Dread_Portal -> new ParticleDreadPortal(world, x, y, z, motX, motY, motZ, size);
            case Blood -> new ParticleBlood(world, x, y, z);
            case If_Pixie -> new ParticlePixieDust(world, x, y, z, (float) motX, (float) motY, (float) motZ);
            case Siren_Appearance -> new ParticleSirenAppearance(world, x, y, z, (int) motX);
            case Ghost_Appearance -> new ParticleGhostAppearance(world, x, y, z, (int) motX);
            case Siren_Music -> new ParticleSirenMusic(world, x, y, z, motX, motY, motZ, 1);
            case Serpent_Bubble -> new ParticleSerpentBubble(world, x, y, z, motX, motY, motZ, 1);
            case Hydra -> new ParticleHydraBreath(world, x, y, z, (float) motX, (float) motY, (float) motZ);
            default -> null;
        };
        if (particle != null) {
            MinecraftClient.getInstance().particleManager.addParticle(particle);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void openBestiaryGui(ItemStack book) {
        MinecraftClient.getInstance().setScreen(new GuiBestiary(book));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void openMyrmexStaffGui(ItemStack staff) {
        MinecraftClient.getInstance().setScreen(new GuiMyrmexStaff(staff));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, Direction facing) {
        MinecraftClient.getInstance().setScreen(new GuiMyrmexAddRoom(staff, pos, facing));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Object getFontRenderer() {
        return MinecraftClient.getInstance().textRenderer;
    }

    @Override
    public int getDragon3rdPersonView() {
        return this.thirdPersonViewDragon;
    }

    @Override
    public void setDragon3rdPersonView(int view) {
        this.thirdPersonViewDragon = view;
    }

    @Override
    public int getPreviousViewType() {
        return this.previousViewType;
    }

    @Override
    public void setPreviousViewType(int view) {
        this.previousViewType = view;
    }

    @Override
    public void updateDragonArmorRender(String clear) {
        LayerDragonArmor.clearCache(clear);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean shouldSeeBestiaryContents() {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 344);
    }

    @Override
    public Entity getReferencedMob() {
        return this.referencedMob;
    }

    @Override
    public void setReferencedMob(Entity dragonBase) {
        this.referencedMob = dragonBase;
    }

    @Override
    public BlockEntity getRefrencedTE() {
        return this.referencedTE;
    }

    @Override
    public void setRefrencedTE(BlockEntity tileEntity) {
        this.referencedTE = tileEntity;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public PlayerEntity getClientSidePlayer() {
        return MinecraftClient.getInstance().player;
    }
}
