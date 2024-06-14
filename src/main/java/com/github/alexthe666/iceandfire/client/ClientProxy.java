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

    public static final Set<UUID> currentDragonRiders = new HashSet<>();
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
