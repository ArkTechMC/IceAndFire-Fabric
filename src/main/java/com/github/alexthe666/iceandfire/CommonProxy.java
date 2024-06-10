package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CommonProxy {
    public static void loadConfig() {
        // Rebake the configs when they change
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            IafConfig.bakeClient();
        } else {
            // We only need to initialize the biome config on the server
            BiomeConfig.init();
            IafConfig.bakeServer();
        }
    }

    public void setReferencedHive(MyrmexHive hive) {

    }

    public void preInit() {

    }

    public void init() {

    }

    public void postInit() {
    }

    public void spawnParticle(EnumParticles name, double x, double y, double z, double motX, double motY, double motZ) {
        this.spawnParticle(name, x, y, z, motX, motY, motZ, 1.0F);
    }

    public void spawnDragonParticle(EnumParticles name, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase) {
    }

    public void spawnParticle(EnumParticles name, double x, double y, double z, double motX, double motY, double motZ, float size) {
    }

    public void openBestiaryGui(ItemStack book) {
    }

    public void openMyrmexStaffGui(ItemStack staff) {
    }

    public Object getFontRenderer() {
        return null;
    }

    public int getDragon3rdPersonView() {
        return 0;
    }

    public void setDragon3rdPersonView(int view) {
    }

    public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, Direction facing) {
    }

    public int getPreviousViewType() {
        return 0;
    }

    public void setPreviousViewType(int view) {
    }

    public void updateDragonArmorRender(String clear) {
    }

    public boolean shouldSeeBestiaryContents() {
        return true;
    }

    public Entity getReferencedMob() {
        return null;
    }

    public void setReferencedMob(Entity dragonBase) {
    }

    public BlockEntity getRefrencedTE() {
        return null;
    }

    public void setRefrencedTE(BlockEntity tileEntity) {
    }

    public PlayerEntity getClientSidePlayer() {
        return null;
    }
}
