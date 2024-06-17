package com.iafenvoy.iceandfire.entity.block;

import com.iafenvoy.iceandfire.entity.util.DreadSpawnerBaseLogic;
import com.iafenvoy.iceandfire.registry.IafBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;

public class BlockEntityDreadSpawner extends MobSpawnerBlockEntity {
    private final BlockEntityType<?> type;
    private final DreadSpawnerBaseLogic spawner = new DreadSpawnerBaseLogic() {
        @Override
        public void sendStatus(World world, BlockPos pos, int status) {
            world.addSyncedBlockEvent(pos, Blocks.SPAWNER, status, 0);
        }

        @Override
        public void setSpawnEntry(World world, BlockPos pos, MobSpawnerEntry spawnEntry) {
            super.setSpawnEntry(world, pos, spawnEntry);
            if (world != null) {
                BlockState blockstate = world.getBlockState(pos);
                world.updateListeners(pos, blockstate, blockstate, 4);
            }
        }
    };

    public BlockEntityDreadSpawner(BlockPos pos, BlockState state) {
        super(pos, state);
        this.type = IafBlockEntities.DREAD_SPAWNER;
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        this.spawner.readNbt(this.world, this.pos, compound);
    }

    public NbtCompound save(NbtCompound compound) {
        super.writeNbt(compound);
        this.spawner.writeNbt(compound);
        return compound;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
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
    public MobSpawnerLogic getLogic() {
        return this.spawner;
    }

    @Override
    public BlockEntityType<?> getType() {
        return this.type != null ? this.type : super.getType();
    }
}