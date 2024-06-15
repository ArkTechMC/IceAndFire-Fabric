package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
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

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.DRAGONFORGE_INPUT;

public class BlockDragonforgeInput extends BlockWithEntity implements IDragonProof {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    private final int dragonType;

    public BlockDragonforgeInput(int dragonType) {
        super(Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).dynamicBounds().strength(40, 500).sounds(BlockSoundGroup.METAL)
        );

        this.dragonType = dragonType;
        this.setDefaultState(this.getStateManager().getDefaultState().with(ACTIVE, Boolean.FALSE));
    }

    static String name(int dragonType) {
        return "dragonforge_%s_input".formatted(DragonType.getNameFromInt(dragonType));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (this.getConnectedTileEntity(world, hitResult.getBlockPos()) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(world, hitResult.getBlockPos());
            if (forge != null && forge.getPropertyDelegate().fireType == this.dragonType) {
                if (!world.isClient) {
                    NamedScreenHandlerFactory inamedcontainerprovider = this.createScreenHandlerFactory(forge.getCachedState(), world, forge.getPos());
                    if (inamedcontainerprovider != null) {
                        player.openHandledScreen(inamedcontainerprovider);
                    }
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    private TileEntityDragonforge getConnectedTileEntity(World world, BlockPos pos) {
        for (Direction facing : Direction.values())
            if (world.getBlockEntity(pos.offset(facing)) != null && world.getBlockEntity(pos.offset(facing)) instanceof TileEntityDragonforge)
                return (TileEntityDragonforge) world.getBlockEntity(pos.offset(facing));
        return null;
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(ACTIVE, meta > 0);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public int getMetaFromState(BlockState state) {
        return state.get(ACTIVE) ? 1 : 0;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> entityType) {
        return world.isClient ? null : checkType(entityType, DRAGONFORGE_INPUT.get(), TileEntityDragonforgeInput::tick);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDragonforgeInput(pos, state);
    }
}
