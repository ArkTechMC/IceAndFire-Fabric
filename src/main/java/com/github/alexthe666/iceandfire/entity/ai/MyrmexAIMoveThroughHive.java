package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MyrmexAIMoveThroughHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private BlockPos nextRoom = BlockPos.ORIGIN;
    private PathResult path;

    public MyrmexAIMoveThroughHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.myrmex.canMove() || this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker) this.myrmex).holdingSomething() || !this.myrmex.shouldMoveThroughHive() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigation().isIdle() || this.myrmex.canSeeSky()) {
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.getWorld()).getNearestHive(this.myrmex.getBlockPos(), 300);
        if (village == null) {
            village = this.myrmex.getHive();
        }
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.hasVehicle()) {
            return false;
        }
        if (village == null) {
            return false;
        } else {
            this.nextRoom = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), village.getRandomRoom(this.myrmex.getRandom(), this.myrmex.getBlockPos()));
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.nextRoom.getX(), this.nextRoom.getY(), this.nextRoom.getZ(), this.movementSpeed);
            return this.path != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.myrmex.shouldLeaveHive() && !this.myrmex.isCloseEnoughToTarget(this.nextRoom, 3) && this.myrmex.shouldEnterHive() && !(this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker) this.myrmex).holdingBaby());
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        this.nextRoom = BlockPos.ORIGIN;

    }
}