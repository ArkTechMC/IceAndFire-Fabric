package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TileEntityDreadSpawner extends MobSpawnerBlockEntity {
    private final BlockEntityType<?> type;
    private final DreadSpawnerBaseLogic spawner = new DreadSpawnerBaseLogic() {
        @Override
        public void sendStatus(World p_155767_, @NotNull BlockPos p_155768_, int p_155769_) {
            p_155767_.addSyncedBlockEvent(p_155768_, Blocks.SPAWNER, p_155769_, 0);
        }

        @Override
        public void setSpawnEntry(World p_155771_, @NotNull BlockPos p_155772_, @NotNull MobSpawnerEntry p_155773_) {
            super.setSpawnEntry(p_155771_, p_155772_, p_155773_);
            if (p_155771_ != null) {
                BlockState blockstate = p_155771_.getBlockState(p_155772_);
                p_155771_.updateListeners(p_155772_, blockstate, blockstate, 4);
            }

        }
    };

    public TileEntityDreadSpawner(BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = IafTileEntityRegistry.DREAD_SPAWNER.get();
    }

    public static void clientTick(World p_155755_, BlockPos p_155756_, BlockState p_155757_, TileEntityDreadSpawner p_155758_) {
        p_155758_.spawner.clientTick(p_155755_, p_155756_);
    }

    public static void serverTick(World p_155762_, BlockPos p_155763_, BlockState p_155764_, TileEntityDreadSpawner p_155765_) {
        p_155765_.spawner.serverTick((ServerWorld) p_155762_, p_155763_);
    }

    @Override
    public void readNbt(@NotNull NbtCompound p_155760_) {
        super.readNbt(p_155760_);
        this.spawner.readNbt(this.world, this.pos, p_155760_);
    }

    public NbtCompound save(NbtCompound p_59795_) {
        super.writeNbt(p_59795_);
        this.spawner.writeNbt(p_59795_);
        return p_59795_;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt() {
        NbtCompound compoundtag = this.save(new NbtCompound());
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
    }

    @Override
    public boolean onSyncedBlockEvent(int p_59797_, int p_59798_) {
        return this.spawner.handleStatus(this.world, p_59797_) || super.onSyncedBlockEvent(p_59797_, p_59798_);
    }

    @Override
    public boolean copyItemDataRequiresOperator() {
        return true;
    }

    @Override
    public @NotNull MobSpawnerLogic getLogic() {
        return this.spawner;
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return this.type != null ? this.type : super.getType();
    }

}