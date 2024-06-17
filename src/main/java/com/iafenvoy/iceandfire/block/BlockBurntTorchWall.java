package com.iafenvoy.iceandfire.block;

import com.iafenvoy.iceandfire.block.util.IDreadBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BlockBurntTorchWall extends WallTorchBlock implements IDreadBlock {
    public BlockBurntTorchWall() {
        super(Settings.create().mapColor(MapColor.OAK_TAN).burnable().luminance((state) -> 0).sounds(BlockSoundGroup.WOOD).nonOpaque().dynamicBounds().noCollision(), DustParticleEffect.DEFAULT);
        //                    .lootFrom(IafBlockRegistry.BURNT_TORCH)
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
    }
}