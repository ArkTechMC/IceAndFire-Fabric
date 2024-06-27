package com.iafenvoy.iceandfire.item.block;

import com.iafenvoy.iceandfire.entity.EntityGhost;
import com.iafenvoy.iceandfire.registry.IafEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;

import java.util.concurrent.ThreadLocalRandom;

public class BlockGraveyardSoil extends Block {
    public BlockGraveyardSoil() {
        super(Settings.create().mapColor(MapColor.DIRT_BROWN).sounds(BlockSoundGroup.GRAVEL).strength(5, 1F).ticksRandomly());
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isClient) {
            if (!worldIn.isRegionLoaded(pos.add(-3, -3, -3), pos.add(3, 3, 3))) return;
            if (!worldIn.isDay() && !worldIn.getBlockState(pos.up()).isOpaque() && rand.nextInt(9) == 0 && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
                int checkRange = 32;
                int k = worldIn.getNonSpectatingEntities(EntityGhost.class, (new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).expand(checkRange)).size();
                if (k < 10) {
                    EntityGhost ghost = IafEntities.GHOST.create(worldIn);
                    assert ghost != null;
                    ghost.updatePositionAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, ThreadLocalRandom.current().nextFloat() * 360F, 0);
                    ghost.initialize(worldIn, worldIn.getLocalDifficulty(pos), SpawnReason.SPAWNER, null, null);
                    worldIn.spawnEntity(ghost);
                    ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                    ghost.setPositionTarget(pos, 16);
                }
            }
        }
    }
}
