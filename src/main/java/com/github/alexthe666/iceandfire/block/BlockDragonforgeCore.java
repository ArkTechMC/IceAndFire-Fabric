package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.DRAGONFORGE_CORE;

public class BlockDragonforgeCore extends BlockWithEntity implements IDragonProof, INoTab {
    private final int dragonType;
    private final boolean activated;

    public BlockDragonforgeCore(int dragonType, boolean activated) {
        super(Settings.create().mapColor(MapColor.IRON_GRAY).dynamicBounds().strength(40, 500).sounds(BlockSoundGroup.METAL).luminance((state) -> activated ? 15 : 0));

        this.dragonType = dragonType;
        this.activated = activated;
    }

    static String name(int dragonType, boolean activated) {
        return "dragonforge_%s_core%s".formatted(DragonType.getNameFromInt(dragonType), activated ? "" : "_disabled");
    }

    public static void setState(int dragonType, boolean active, World worldIn, BlockPos pos) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);

        if (active) {
            if (dragonType == 0) {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get().getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get().getDefaultState(), 3);
            } else if (dragonType == 1) {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.get().getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.get().getDefaultState(), 3);
            } else if (dragonType == 2) {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get().getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get().getDefaultState(), 3);
            }
        } else {
            if (dragonType == 0) {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().getDefaultState(), 3);
            } else if (dragonType == 1) {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().getDefaultState(), 3);
            } else if (dragonType == 2) {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().getDefaultState(), 3);
            }
        }

        if (tileentity != null) {
            tileentity.cancelRemoval();
            worldIn.addBlockEntity(tileentity);
        }
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, PlayerEntity player, @NotNull Hand handIn, @NotNull BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null)
                player.openHandledScreen(screenHandlerFactory);
        }
        return ActionResult.SUCCESS;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        if (this.dragonType == 0) {
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().asItem());
        }
        if (this.dragonType == 1) {
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().asItem());
        }
        if (this.dragonType == 2) {
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().asItem());
        }
        return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().asItem());
    }

    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileEntityDragonforge) {
            ItemScatterer.spawn(worldIn, pos, (TileEntityDragonforge) tileentity);
            worldIn.updateComparators(pos, this);
            worldIn.removeBlockEntity(pos);
        }
    }

    @Override
    public int getComparatorOutput(@NotNull BlockState blockState, World worldIn, @NotNull BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(worldIn.getBlockEntity(pos));
    }

    @Override
    public boolean hasComparatorOutput(@NotNull BlockState state) {
        return true;
    }


    @Override
    public boolean shouldBeInTab() {
        return !this.activated;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return checkType(entityType, DRAGONFORGE_CORE.get(), TileEntityDragonforge::tick);
    }

    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityDragonforge(pos, state, this.dragonType);
    }
}
