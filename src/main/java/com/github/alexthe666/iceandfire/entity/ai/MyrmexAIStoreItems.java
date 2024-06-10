package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathFindingStatus;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.block.BlockMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MyrmexAIStoreItems extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private BlockPos nextRoom = null;
    private BlockPos nextCocoon = null;
    private BlockPos mainRoom = null;
    private boolean first = true; //first stage - enter the main hive room then storage room
    private PathResult path;

    public MyrmexAIStoreItems(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.myrmex.canMove() || this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker) this.myrmex).holdingBaby() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigation().isIdle() || this.myrmex.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            return false;
        }
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.hasVehicle()) {
            return false;
        }
        if (this.myrmex.getWaitTicks() > 0) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            return false;
        }
        if (!this.myrmex.isInHive()) {
            return false;
        }
        this.first = true;
        this.mainRoom = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), village.getCenter());

        this.nextRoom = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), village.getRandomRoom(WorldGenMyrmexHive.RoomType.FOOD, this.myrmex.getRandom(), this.myrmex.getBlockPos()));
        this.nextCocoon = this.getNearbyCocoon(this.nextRoom);
        if (this.nextCocoon == null) {
            this.myrmex.setWaitTicks(20 + ThreadLocalRandom.current().nextInt(40));
        }
        this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.mainRoom.getX() + 0.5D, this.mainRoom.getY() + 0.5D, this.mainRoom.getZ() + 0.5D, this.movementSpeed);
        return this.nextCocoon != null;

    }

    @Override
    public boolean shouldContinue() {
        return !this.myrmex.getStackInHand(Hand.MAIN_HAND).isEmpty() && this.nextCocoon != null && this.isUseableCocoon(this.nextCocoon) && !this.myrmex.isCloseEnoughToTarget(this.nextCocoon, 3) && this.myrmex.shouldEnterHive();
    }

    @Override
    public void tick() {
        if (this.first && this.mainRoom != null) {
            if (this.myrmex.isCloseEnoughToTarget(this.mainRoom, 10)) {
                this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.nextCocoon.getX() + 0.5D, this.nextCocoon.getY() + 0.5D, this.nextCocoon.getZ() + 0.5D, this.movementSpeed);
                this.first = false;
            } else if (!this.myrmex.pathReachesTarget(this.path, this.mainRoom, 9)) {
                //Simple way to stop executing this task
                this.nextCocoon = null;
            }
        }

        if (!this.first && this.nextCocoon != null) {
            final double dist = 9.0D; // 3 * 3
            if (this.myrmex.isCloseEnoughToTarget(this.nextCocoon, dist) && !this.myrmex.getStackInHand(Hand.MAIN_HAND).isEmpty() && this.isUseableCocoon(this.nextCocoon)) {
                TileEntityMyrmexCocoon cocoon = (TileEntityMyrmexCocoon) this.myrmex.getWorld().getBlockEntity(this.nextCocoon);
                ItemStack itemstack = this.myrmex.getStackInHand(Hand.MAIN_HAND);
                if (!itemstack.isEmpty()) {
                    for (int i = 0; i < cocoon.size(); ++i) {
                        if (!itemstack.isEmpty()) {
                            ItemStack cocoonStack = cocoon.getStack(i);
                            if (cocoonStack.isEmpty()) {
                                cocoon.setStack(i, itemstack.copy());
                                cocoon.markDirty();

                                this.myrmex.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                                this.myrmex.isEnteringHive = false;
                                return;
                            } else if (cocoonStack.getItem() == itemstack.getItem()) {
                                final int j = Math.min(cocoon.getMaxCountPerStack(), cocoonStack.getMaxCount());
                                final int k = Math.min(itemstack.getCount(), j - cocoonStack.getCount());
                                if (k > 0) {
                                    cocoonStack.increment(k);
                                    itemstack.decrement(k);

                                    if (itemstack.isEmpty()) {
                                        cocoon.markDirty();
                                    }
                                    this.myrmex.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                                    this.myrmex.isEnteringHive = false;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            //In case the myrmex isn't close enough to the cocoon and walked to it's destination try a different one
            else if (!this.myrmex.getStackInHand(Hand.MAIN_HAND).isEmpty() && this.path.getStatus() == PathFindingStatus.COMPLETE && !this.myrmex.pathReachesTarget(this.path, this.nextCocoon, dist)) {
                this.nextCocoon = this.getNearbyCocoon(this.nextRoom);
                if (this.nextCocoon != null) {
                    this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.nextCocoon.getX() + 0.5D, this.nextCocoon.getY() + 0.5D, this.nextCocoon.getZ() + 0.5D, this.movementSpeed);
                }
            } else if (this.myrmex.pathReachesTarget(this.path, this.nextCocoon, dist) && this.path.isCancelled()) {
                this.stop();
            }
        }
    }

    @Override
    public void stop() {
        this.nextRoom = null;
        this.nextCocoon = null;
        this.mainRoom = null;
        this.first = true;
    }

    public BlockPos getNearbyCocoon(BlockPos roomCenter) {
        int RADIUS_XZ = 15;
        int RADIUS_Y = 7;
        List<BlockPos> closeCocoons = new ArrayList<>();
        BlockPos.stream(roomCenter.add(-RADIUS_XZ, -RADIUS_Y, -RADIUS_XZ), roomCenter.add(RADIUS_XZ, RADIUS_Y, RADIUS_XZ)).forEach(blockpos -> {
            BlockEntity te = this.myrmex.getWorld().getBlockEntity(blockpos);
            if (te instanceof TileEntityMyrmexCocoon) {
                if (!((TileEntityMyrmexCocoon) te).isFull(this.myrmex.getStackInHand(Hand.MAIN_HAND))) {
                    closeCocoons.add(te.getPos());
                }
            }
        });
        if (closeCocoons.isEmpty()) {
            return null;
        }
        return closeCocoons.get(this.myrmex.getRandom().nextInt(Math.max(closeCocoons.size() - 1, 1)));
    }

    public boolean isUseableCocoon(BlockPos blockpos) {
        if (this.myrmex.getWorld().getBlockState(blockpos).getBlock() instanceof BlockMyrmexCocoon && this.myrmex.getWorld().getBlockEntity(blockpos) != null && this.myrmex.getWorld().getBlockEntity(blockpos) instanceof TileEntityMyrmexCocoon) {
            return !((TileEntityMyrmexCocoon) this.myrmex.getWorld().getBlockEntity(blockpos)).isFull(this.myrmex.getStackInHand(Hand.MAIN_HAND));
        }
        return false;
    }
}