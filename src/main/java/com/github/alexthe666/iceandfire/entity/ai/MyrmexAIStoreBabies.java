package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MyrmexAIStoreBabies extends Goal {
    private final EntityMyrmexWorker myrmex;
    private BlockPos nextRoom = BlockPos.ORIGIN;

    public MyrmexAIStoreBabies(EntityMyrmexWorker entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.myrmex.canMove() || !this.myrmex.holdingBaby() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigation().isIdle() || this.myrmex.canSeeSky())
            return false;
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) return false;
        else {
            this.nextRoom = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), village.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.myrmex.getRandom(), this.myrmex.getBlockPos())).up();
            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.myrmex.holdingBaby() && !this.myrmex.getNavigation().isIdle() && this.myrmex.squaredDistanceTo(this.nextRoom.getX() + 0.5D, this.nextRoom.getY() + 0.5D, this.nextRoom.getZ() + 0.5D) > 3 && this.myrmex.shouldEnterHive();
    }

    @Override
    public void start() {
        this.myrmex.getNavigation().startMovingTo(this.nextRoom.getX(), this.nextRoom.getY(), this.nextRoom.getZ(), 1.5F);
    }

    @Override
    public void tick() {
        if (this.nextRoom != null && this.myrmex.squaredDistanceTo(this.nextRoom.getX() + 0.5D, this.nextRoom.getY() + 0.5D, this.nextRoom.getZ() + 0.5D) < 4 && this.myrmex.holdingBaby())
            if (!this.myrmex.getPassengerList().isEmpty())
                for (Entity entity : this.myrmex.getPassengerList()) {
                    entity.stopRiding();
                    this.stop();
                    entity.copyPositionAndRotation(this.myrmex);
                }
    }

    @Override
    public void stop() {
        this.nextRoom = BlockPos.ORIGIN;
        this.myrmex.getNavigation().stop();
    }
}