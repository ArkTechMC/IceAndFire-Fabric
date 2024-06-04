package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.PIXIE_JAR;

public class BlockJar extends BlockWithEntity {
    protected static final VoxelShape AABB = Block.createCuboidShape(3, 0, 3, 13, 16, 13);
    public Item itemBlock;
    private final boolean empty;
    private final int pixieType;

    public BlockJar(int pixieType) {
        super(
                pixieType != -1 ?
                        Settings
                                .create()
                                .mapColor(MapColor.CLEAR)
                                .instrument(Instrument.HAT)
                                .nonOpaque()
                                .dynamicBounds()
                                .strength(1, 2)
                                .sounds(BlockSoundGroup.GLASS)
                                .luminance((state) -> 10)
                                .dropsLike(IafBlockRegistry.JAR_EMPTY.get())
                        : Settings
                        .create()
                        .mapColor(MapColor.CLEAR)
                        .instrument(Instrument.HAT)
                        .nonOpaque()
                        .dynamicBounds()
                        .strength(1, 2)
                        .sounds(BlockSoundGroup.GLASS)
        );

        this.empty = pixieType == -1;
        this.pixieType = pixieType;
    }

    static String name(int pixieType) {
        if (pixieType == -1)
            return "pixie_jar_empty";
        return "pixie_jar_%d".formatted(pixieType);
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(@NotNull BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return AABB;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return AABB;
    }


    @Override
    public void onStateReplaced(@NotNull BlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        dropPixie(worldIn, pos);
        super.onStateReplaced(state, worldIn, pos, newState, isMoving);
    }

    public void dropPixie(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie) {
            ((TileEntityJar) world.getBlockEntity(pos)).releasePixie();
        }
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand handIn, @NotNull BlockHitResult resultIn) {
        if (!empty && world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie && ((TileEntityJar) world.getBlockEntity(pos)).hasProduced) {
            ((TileEntityJar) world.getBlockEntity(pos)).hasProduced = false;
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(IafItemRegistry.PIXIE_DUST.get()));
            if (!world.isClient) {
                world.spawnEntity(item);
            }
            world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundCategory.NEUTRAL, 1, 1, false);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }


    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onPlaced(World world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        if (world.getBlockEntity(pos) instanceof TileEntityJar jar) {
            if (!empty) {
                jar.hasPixie = true;
                jar.pixieType = pixieType;
            } else {
                jar.hasPixie = false;
            }
            jar.markDirty();
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return checkType(entityType, PIXIE_JAR.get(), TileEntityJar::tick);
    }

    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityJar(pos, state, empty);
    }
}
