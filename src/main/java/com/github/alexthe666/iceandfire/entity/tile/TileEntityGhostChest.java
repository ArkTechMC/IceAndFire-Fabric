package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class TileEntityGhostChest extends ChestBlockEntity {

    public TileEntityGhostChest(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.GHOST_CHEST.get(), pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        super.onOpen(player);
        assert this.world != null;
        if (this.world.getDifficulty() != Difficulty.PEACEFUL) {
            EntityGhost ghost = IafEntityRegistry.GHOST.get().create(this.world);
            assert ghost != null;
            ghost.updatePositionAndAngles(this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F,
                    ThreadLocalRandom.current().nextFloat() * 360F, 0);
            if (!this.world.isClient) {
                ghost.initialize((ServerWorld) this.world, this.world.getLocalDifficulty(this.pos), SpawnReason.SPAWNER, null, null);
                if (!player.isCreative()) {
                    ghost.setTarget(player);
                }
                ghost.setPersistent();
                this.world.spawnEntity(ghost);
            }
            ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
            ghost.setPositionTarget(this.pos, 4);
            ghost.setFromChest(true);
        }
    }

    @Override
    protected void onViewerCountUpdate(World level, BlockPos pos, BlockState state, int p_155336_, int p_155337_) {
        super.onViewerCountUpdate(level, pos, state, p_155336_, p_155337_);
        level.updateNeighborsAlways(pos.down(), state.getBlock());
    }
}
