package com.iafenvoy.iceandfire.data.component;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafWorld;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PortalDataComponent implements ComponentV3, AutoSyncedComponent, CommonTickingComponent {
    protected static final ComponentKey<PortalDataComponent> COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(IceAndFire.MOD_ID, "portal_data"), PortalDataComponent.class);
    private final PlayerEntity player;
    private boolean teleported = false;
    private int teleportTick = -1;
    private int renderTick = 0;

    public PortalDataComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void tick() {
        World world = this.player.getWorld();
        if (!this.teleported && this.teleportTick == 0 && world instanceof ServerWorld serverWorld) {
            this.teleported = true;
            MinecraftServer server = serverWorld.getServer();
            Vec3d pos = this.player.getPos();
            if (world.getRegistryKey().getValue().equals(IafWorld.DREAD_LAND.getValue())) {
                this.player.moveToWorld(server.getWorld(World.OVERWORLD));
                this.player.setPosition(pos);
            } else {
                ServerWorld dreadLand = server.getWorld(IafWorld.DREAD_LAND);
                if (dreadLand == null) return;
                this.player.moveToWorld(dreadLand);
                this.player.setPosition(pos);
                if (!dreadLand.getBlockState(this.player.getBlockPos()).isOf(IafBlocks.DREAD_PORTAL))
                    server.getStructureTemplateManager().getTemplate(new Identifier(IceAndFire.MOD_ID, "dread_exit_portal")).ifPresent(structureTemplate -> structureTemplate.place(dreadLand, this.player.getBlockPos(), new BlockPos(-2, -1, -2), new StructurePlacementData(), dreadLand.random, 2));
            }
        }
        if (world.getBlockState(this.player.getBlockPos()).isOf(IafBlocks.DREAD_PORTAL)) {
            if (this.renderTick < this.player.getMinFreezeDamageTicks()) this.renderTick++;
            if (this.teleportTick > 0) this.teleportTick--;
            else if (this.teleportTick == -1) this.teleportTick = 100;
        } else {
            if (this.renderTick > 0) this.renderTick--;
            this.teleported = false;
            this.teleportTick = -1;
        }
        COMPONENT.sync(this.player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.setTeleported(tag.getBoolean("teleported"));
        this.setTeleportTick(tag.getInt("teleport_tick"));
        this.setRenderTick(tag.getInt("render_tick"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("teleported", this.isTeleported());
        tag.putInt("teleport_tick", this.getTeleportTick());
        tag.putInt("render_tick", this.getRenderTick());
    }

    public boolean isTeleported() {
        return this.teleported;
    }

    public int getTeleportTick() {
        return this.teleportTick;
    }

    public int getRenderTick() {
        return this.renderTick;
    }

    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    public void setTeleportTick(int teleportTick) {
        this.teleportTick = teleportTick;
    }

    public void setRenderTick(int renderTick) {
        this.renderTick = renderTick;
    }

    public static PortalDataComponent get(PlayerEntity player) {
        return COMPONENT.get(player);
    }
}
