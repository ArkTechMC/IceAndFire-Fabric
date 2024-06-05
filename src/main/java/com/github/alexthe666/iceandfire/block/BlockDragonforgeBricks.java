package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeBrick;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockDragonforgeBricks extends BlockWithEntity implements IDragonProof {

    public static final BooleanProperty GRILL = BooleanProperty.of("grill");
    private final int isFire;

    public BlockDragonforgeBricks(int isFire) {
        super(
                Settings
                        .create()
                        .mapColor(MapColor.STONE_GRAY)
                        .instrument(Instrument.BASEDRUM)
                        .dynamicBounds()
                        .strength(40, 500)
                        .sounds(BlockSoundGroup.METAL)
        );

        this.isFire = isFire;
        this.setDefaultState(this.getStateManager().getDefaultState().with(GRILL, Boolean.FALSE));
    }

    static String name(int dragonType) {
        return "dragonforge_%s_brick".formatted(DragonType.getNameFromInt(dragonType));
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand handIn, BlockHitResult resultIn) {
        if (this.getConnectedTileEntity(worldIn, resultIn.getBlockPos()) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, resultIn.getBlockPos());
            if (forge != null && forge.fireType == this.isFire) {
                if (worldIn.isClient) {
                    IceAndFire.PROXY.setRefrencedTE(worldIn.getBlockEntity(forge.getPos()));
                } else {
                    NamedScreenHandlerFactory inamedcontainerprovider = this.createScreenHandlerFactory(forge.getCachedState(), worldIn, forge.getPos());
                    if (inamedcontainerprovider != null) {
                        player.openHandledScreen(inamedcontainerprovider);
                    }
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    private TileEntityDragonforge getConnectedTileEntity(World worldIn, BlockPos pos) {
        for (Direction facing : Direction.values()) {
            if (worldIn.getBlockEntity(pos.offset(facing)) != null && worldIn.getBlockEntity(pos.offset(facing)) instanceof TileEntityDragonforge forge) {
                if (forge.assembled()) {
                    return forge;
                }
            }
        }
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(GRILL);
    }

    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityDragonforgeBrick(pos, state);
    }
}
