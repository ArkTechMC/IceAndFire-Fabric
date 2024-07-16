package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;
import com.iafenvoy.iceandfire.entity.EntityMyrmexQueen;
import com.iafenvoy.iceandfire.entity.EntityMyrmexWorker;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import com.iafenvoy.uranus.object.entity.pathfinding.raycoms.AdvancedPathNavigate;
import com.iafenvoy.uranus.object.entity.pathfinding.raycoms.PathResult;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MyrmexAILeaveHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private PathResult path;
    private BlockPos nextEntrance = BlockPos.ORIGIN;

    public MyrmexAILeaveHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.myrmex instanceof EntityMyrmexQueen) return false;
        //If it's riding something don't execute
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.hasVehicle())
            return false;
        if (this.myrmex.isBaby()) return false;
        if (!this.myrmex.canMove() || !this.myrmex.shouldLeaveHive() || this.myrmex.shouldEnterHive() || !this.myrmex.isInHive() || this.myrmex instanceof EntityMyrmexWorker && (((EntityMyrmexWorker) this.myrmex).holdingSomething() || !this.myrmex.getStackInHand(Hand.MAIN_HAND).isEmpty()) || this.myrmex.isEnteringHive)
            return false;
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.getWorld()).getNearestHive(this.myrmex.getBlockPos(), 1000);
        if (village == null) return false;
        else {
            this.nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRandom(), true));
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.nextEntrance.getX(), this.nextEntrance.getY(), this.nextEntrance.getZ(), this.movementSpeed);
            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        if (this.myrmex.isCloseEnoughToTarget(this.nextEntrance, 12) || this.myrmex.shouldEnterHive())
            return false;
        return this.myrmex.shouldLeaveHive();
    }

    @Override
    public void tick() {
        //If the path has been created but the destination couldn't be reached
        //or if the myrmex has reached the end of the path but isn't close enough to the entrance for some reason
        if (!this.myrmex.pathReachesTarget(this.path, this.nextEntrance, 12)) {
            MyrmexHive village = MyrmexWorldData.get(this.myrmex.getWorld()).getNearestHive(this.myrmex.getBlockPos(), 1000);
            this.nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRandom(), true));
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.nextEntrance.getX(), this.nextEntrance.getY() + 1, this.nextEntrance.getZ(), this.movementSpeed);
        }
    }


    @Override
    public void start() {
    }

    @Override
    public void stop() {
        this.nextEntrance = BlockPos.ORIGIN;
        this.myrmex.getNavigation().stop();
    }
}