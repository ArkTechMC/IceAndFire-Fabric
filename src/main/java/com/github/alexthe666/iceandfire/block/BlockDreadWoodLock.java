package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
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

public class BlockDreadWoodLock extends Block implements IDragonProof, IDreadBlock {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.of("player_placed");

    public BlockDreadWoodLock() {
        super(Settings.create().mapColor(MapColor.OAK_TAN).instrument(Instrument.BASS).burnable().strength(-1.0F, 1000000F).sounds(BlockSoundGroup.WOOD));
        this.setDefaultState(this.getStateManager().getDefaultState().with(PLAYER_PLACED, Boolean.FALSE));
    }

    @SuppressWarnings("deprecation")
    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView worldIn, BlockPos pos) {
        if (state.get(PLAYER_PLACED)) {
            float f = 8f;
            //Code from super method
            return player.getDigSpeed(state, pos) / f / (float) 30;
        }
        return super.calcBlockBreakingDelta(state, player, worldIn, pos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() == IafItemRegistry.DREAD_KEY.get()) {
            if (!player.isCreative())
                stack.decrement(1);
            this.deleteNearbyWood(world, pos, pos);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.BLOCKS, 1, 1, false);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1, 2, false);
        }
        return ActionResult.SUCCESS;
    }

    private void deleteNearbyWood(World world, BlockPos pos, BlockPos startPos) {
        if (pos.getSquaredDistance(startPos) < 32)
            if (world.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS.get() || world.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS_LOCK.get()) {
                world.breakBlock(pos, false);
                for (Direction facing : Direction.values())
                    this.deleteNearbyWood(world, pos.offset(facing), startPos);
            }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }
}
