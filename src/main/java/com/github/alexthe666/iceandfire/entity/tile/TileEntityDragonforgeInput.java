package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.block.BlockDragonForgeInput;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class TileEntityDragonforgeInput extends BlockEntity {
    private static final int LURE_DISTANCE = 50;
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private int ticksSinceDragonFire;
    private TileEntityDragonforge core = null;

    public TileEntityDragonforgeInput(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_INPUT.get(), pos, state);
    }

    public static void tick(final World level, final BlockPos position, final BlockState state, final TileEntityDragonforgeInput forgeInput) {
        if (forgeInput.core == null) {
            forgeInput.core = forgeInput.getConnectedTileEntity(position);
        }

        if (forgeInput.ticksSinceDragonFire > 0) {
            forgeInput.ticksSinceDragonFire--;
        }

        if ((forgeInput.ticksSinceDragonFire == 0 || forgeInput.core == null) && forgeInput.isActive()) {
            BlockEntity tileentity = level.getBlockEntity(position);
            level.setBlockState(position, forgeInput.getDeactivatedState());
            if (tileentity != null) {
                tileentity.cancelRemoval();
                level.addBlockEntity(tileentity);
            }
        }

        if (forgeInput.isAssembled()) {
            forgeInput.lureDragons();
        }
    }

    public void onHitWithFlame() {
        if (this.core != null) {
            this.core.transferPower(1);
        }
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbtWithIdentifyingData();
    }

    protected void lureDragons() {
        Vec3d targetPosition = new Vec3d(
                this.getPos().getX() + 0.5F,
                this.getPos().getY() + 0.5F,
                this.getPos().getZ() + 0.5F
        );

        Box searchArea = new Box(
                (double) this.pos.getX() - LURE_DISTANCE,
                (double) this.pos.getY() - LURE_DISTANCE,
                (double) this.pos.getZ() - LURE_DISTANCE,
                (double) this.pos.getX() + LURE_DISTANCE,
                (double) this.pos.getY() + LURE_DISTANCE,
                (double) this.pos.getZ() + LURE_DISTANCE
        );

        boolean dragonSelected = false;

        assert this.world != null;
        for (EntityDragonBase dragon : this.world.getNonSpectatingEntities(EntityDragonBase.class, searchArea)) {
            if (!dragonSelected && /* Dragon Checks */ this.getDragonType() == dragon.dragonType.getIntFromType() && (dragon.isChained() || dragon.isTamed()) && this.canSeeInput(dragon, targetPosition)) {
                dragon.burningTarget = this.pos;
                dragonSelected = true;
            } else if (dragon.burningTarget == this.pos) {
                dragon.burningTarget = null;
                dragon.setBreathingFire(false);
            }
        }
    }

    public boolean isAssembled() {
        return (this.core != null && this.core.assembled() && this.core.canSmelt());
    }

    public void resetCore() {
        this.core = null;
    }

    private boolean canSeeInput(EntityDragonBase dragon, Vec3d target) {
        if (target != null) {
            assert this.world != null;
            HitResult rayTrace = this.world.raycast(new RaycastContext(dragon.getHeadPosition(), target, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, dragon));
            double distance = dragon.getHeadPosition().distanceTo(rayTrace.getPos());

            return distance < 10.0F + dragon.getWidth();
        }

        return false;
    }

    private BlockState getDeactivatedState() {
        return switch (this.getDragonType()) {
            case 1 ->
                    IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get().getDefaultState().with(BlockDragonForgeInput.ACTIVE, false);
            case 2 ->
                    IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get().getDefaultState().with(BlockDragonForgeInput.ACTIVE, false);
            default ->
                    IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get().getDefaultState().with(BlockDragonForgeInput.ACTIVE, false);
        };
    }

    private int getDragonType() {
        assert this.world != null;
        BlockState state = this.world.getBlockState(this.pos);

        if (state.getBlock() == IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get()) {
            return 0;
        } else if (state.getBlock() == IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get()) {
            return 1;
        } else if (state.getBlock() == IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get()) {
            return 2;
        }

        return 0;
    }

    private boolean isActive() {
        assert this.world != null;
        BlockState state = this.world.getBlockState(this.pos);
        return state.getBlock() instanceof BlockDragonForgeInput && state.get(BlockDragonForgeInput.ACTIVE);
    }

    private TileEntityDragonforge getConnectedTileEntity(final BlockPos position) {
        for (Direction facing : HORIZONTALS) {
            assert this.world != null;
            if (this.world.getBlockEntity(position.offset(facing)) instanceof TileEntityDragonforge forge) {
                return forge;
            }
        }

        return null;
    }
}
