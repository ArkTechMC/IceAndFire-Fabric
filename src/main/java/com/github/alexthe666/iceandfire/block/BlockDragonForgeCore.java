package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
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

public class BlockDragonForgeCore extends BlockWithEntity implements IDragonProof, INoTab {
    private final int isFire;
    private final boolean activated;

    public BlockDragonForgeCore(int isFire, boolean activated) {
        super(Settings.create().mapColor(MapColor.IRON_GRAY).dynamicBounds().strength(40, 500).sounds(BlockSoundGroup.METAL).luminance((state) -> activated ? 15 : 0));
        this.isFire = isFire;
        this.activated = activated;
    }

    static String name(int dragonType, boolean activated) {
        return "dragonforge_%s_core%s".formatted(DragonType.getNameFromInt(dragonType), activated ? "" : "_disabled");
    }

    public static void setState(int dragonType, boolean active, World worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (active) {
            switch (dragonType) {
                case 0 -> worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.getDefaultState(), 3);
                case 1 -> worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.getDefaultState(), 3);
                case 2 ->
                        worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.getDefaultState(), 3);
            }
        } else {
            switch (dragonType) {
                case 0 ->
                        worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.getDefaultState(), 3);
                case 1 ->
                        worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.getDefaultState(), 3);
                case 2 ->
                        worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.getDefaultState(), 3);
            }
        }
        if (blockEntity != null) {
            blockEntity.cancelRemoval();
            worldIn.addBlockEntity(blockEntity);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (!player.isSneaking()) {
            if (!world.isClient) {
                NamedScreenHandlerFactory screenHandlerFactory = this.createScreenHandlerFactory(state, world, pos);
                if (screenHandlerFactory != null)
                    player.openHandledScreen(screenHandlerFactory);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    public ItemStack getItem(World world, BlockPos pos, BlockState state) {
        return switch (this.isFire) {
            case 1 -> new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.asItem());
            case 2 -> new ItemStack(IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.asItem());
            default -> new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.asItem());
        };
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityDragonforge) {
            ItemScatterer.spawn(world, pos, (TileEntityDragonforge) blockEntity);
            world.updateComparators(pos, this);
            world.removeBlockEntity(pos);
        }
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }


    @Override
    public boolean shouldBeInTab() {
        return !this.activated;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> entityType) {
        return checkType(entityType, IafTileEntityRegistry.DRAGONFORGE_CORE, TileEntityDragonforge::tick);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDragonforge(pos, state, this.isFire);
    }
}
