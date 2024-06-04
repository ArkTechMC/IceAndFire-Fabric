package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.EGG_IN_ICE;

public class BlockEggInIce extends BlockWithEntity {
    public Item itemBlock;

    @SuppressWarnings("deprecation")
    public BlockEggInIce() {
        super(
                Settings
                        .create()
                        .mapColor(MapColor.PALE_PURPLE)
                        .nonOpaque()
                        .dynamicBounds()
                        .strength(0.5F)
                        .dynamicBounds()
                        .sounds(BlockSoundGroup.GLASS)
        );
    }

    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityEggInIce(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return checkType(entityType, EGG_IN_ICE.get(), TileEntityEggInIce::tickEgg);
    }

    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void afterBreak(@NotNull World worldIn, PlayerEntity player, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity te, @NotNull ItemStack stack) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        player.addExhaustion(0.005F);
    }

    @Override
    public void onBreak(World worldIn, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull PlayerEntity player) {
        if (worldIn.getBlockEntity(pos) != null) {
            if (worldIn.getBlockEntity(pos) instanceof TileEntityEggInIce tile) {
                tile.spawnEgg();
            }
        }
    }

}
