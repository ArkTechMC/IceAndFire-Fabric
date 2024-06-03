package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockDreadTorchWall extends WallTorchBlock implements IDreadBlock {

    public BlockDreadTorchWall() {
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
                .noCollision()
                .dropsLike(IafBlockRegistry.DREAD_TORCH.get()),
            DustParticleEffect.DEFAULT
        );
    }

    @Override
    public void randomDisplayTick(BlockState stateIn, @NotNull World worldIn, BlockPos pos, @NotNull Random rand) {
        Direction direction = stateIn.get(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;
        double d3 = 0.22D;
        double d4 = 0.27D;
        Direction direction1 = direction.getOpposite();
        IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, d0 + 0.27D * (double) direction1.getOffsetX(), d1 + 0.22D, d2 + 0.27D * (double) direction1.getOffsetZ(), 0.0D, 0.0D, 0.0D);
    }
}