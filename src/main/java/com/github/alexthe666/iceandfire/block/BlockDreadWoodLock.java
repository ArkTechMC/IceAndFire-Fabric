package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockDreadWoodLock extends Block implements IDragonProof, IDreadBlock {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.of("player_placed");

    public BlockDreadWoodLock() {
        super(
            Settings
                .create()
                .mapColor(MapColor.OAK_TAN)
                .instrument(Instrument.BASS)
                .burnable()
                .strength(-1.0F, 1000000F)
                .sounds(BlockSoundGroup.WOOD)
        );
        this.setDefaultState(this.getStateManager().getDefaultState().with(PLAYER_PLACED, Boolean.FALSE));
    }

    @SuppressWarnings("deprecation")
    @Override
    public float calcBlockBreakingDelta(BlockState state, @NotNull PlayerEntity player, @NotNull BlockView worldIn, @NotNull BlockPos pos) {
        if (state.get(PLAYER_PLACED)) {
            float f = 8f;
            //Code from super method
            return player.getDigSpeed(state, pos) / f / (float) 30;
        }
        return super.calcBlockBreakingDelta(state, player, worldIn, pos);
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, @NotNull World worldIn, @NotNull BlockPos pos, PlayerEntity player, @NotNull Hand handIn, @NotNull BlockHitResult resultIn) {
        ItemStack stack = player.getStackInHand(handIn);
        if (stack.getItem() == IafItemRegistry.DREAD_KEY.get()) {
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            deleteNearbyWood(worldIn, pos, pos);
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.BLOCKS, 1, 1, false);
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1, 2, false);
        }
        return ActionResult.SUCCESS;
    }

    private void deleteNearbyWood(World worldIn, BlockPos pos, BlockPos startPos) {
        if (pos.getSquaredDistance(startPos) < 32) {
            if (worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS.get() || worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS_LOCK.get()) {
                worldIn.breakBlock(pos, false);
                for (Direction facing : Direction.values()) {
                    deleteNearbyWood(worldIn, pos.offset(facing), startPos);
                }
            }
        }
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }

    public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return this.getDefaultState().with(PLAYER_PLACED, true);
    }
}
