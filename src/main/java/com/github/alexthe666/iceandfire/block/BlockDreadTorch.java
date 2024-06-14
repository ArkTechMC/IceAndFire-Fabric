package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.particle.IafParticleRegistry;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockDreadTorch extends TorchBlock implements IDreadBlock, IWallBlock {

    public BlockDreadTorch() {
        super(
                Settings
                        .create()
                        .mapColor(MapColor.OAK_TAN)
                        .instrument(Instrument.BASS)
                        .burnable()
                        .luminance((state) -> 5)
                        .sounds(BlockSoundGroup.STONE)
                        .nonOpaque()
                        .dynamicBounds()
                        .noCollision(),
                DustParticleEffect.DEFAULT
        );
    }

    @Override
    public void randomDisplayTick(@NotNull BlockState stateIn, @NotNull World worldIn, BlockPos pos, @NotNull Random rand) {
        // Direction Direction = stateIn.get(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.6D;
        double d2 = (double) pos.getZ() + 0.5D;
        double d3 = 0.22D;
        double d4 = 0.27D;
        worldIn.addParticle(IafParticleRegistry.DREAD_TORCH, d0, d1, d2, 0.0D, 0.0D, 0.0D);
  /*   if (Direction.getAxis().isHorizontal()) {
            Direction Direction1 = Direction.getOpposite();
            //worldIn.spawnParticle(ParticleTypes.SMOKE_NORMAL, d0 + 0.27D * (double)Direction1.getXOffset(), d1 + 0.22D, d2 + 0.27D * (double)Direction1.getZOffset(), 0.0D, 0.0D, 0.0D);
            IceAndFire.PROXY.spawnParticle("dread_torch", d0 + 0.27D * (double) Direction1.getXOffset(), d1 + 0.22D, d2 + 0.27D * (double) Direction1.getZOffset(), 0.0D, 0.0D, 0.0D);

        } else {
            //worldIn.spawnParticle(ParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }*/
    }

    @Override
    public Block wallBlock() {
        return IafBlockRegistry.DREAD_TORCH_WALL.get();
    }
}