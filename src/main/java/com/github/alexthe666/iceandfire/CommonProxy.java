package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.config.ConfigHolder;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = IceAndFire.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {
    public static void loadConfig() {
        final IafConfig config =new IafConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            IafConfig.bakeClient(config);
        } else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            // We only need to initialize the biome config on the server
            BiomeConfig.init();
            IafConfig.bakeServer(config);
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

    public void setup() {
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
    }

    public void clientInit() {
    }
}
