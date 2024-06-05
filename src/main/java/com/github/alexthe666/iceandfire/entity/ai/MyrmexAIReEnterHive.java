package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MyrmexAIReEnterHive extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private PathResult path;
    private BlockPos currentTarget = BlockPos.ORIGIN;
    private Phases currentPhase = Phases.GOTOENTRANCE;

    private enum Phases {
        GOTOENTRANCE,
        GOTOEXIT,
        GOTOCENTER
    }

    private MyrmexHive hive;

    public MyrmexAIReEnterHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.myrmex.canMove() || this.myrmex.shouldLeaveHive() || !this.myrmex.shouldEnterHive() || this.currentPhase != Phases.GOTOENTRANCE) {
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.myrmex.getWorld()).getNearestHive(this.myrmex.getBlockPos(), 500);
        }
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate) || this.myrmex.hasVehicle()) {
            return false;
        }
        if (village == null || this.myrmex.isInHive()) {
            return false;
        } else {
            this.hive = village;
            this.currentTarget = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), this.hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRandom(), false));
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 1);
            this.currentPhase = Phases.GOTOENTRANCE;
            return this.path != null;
        }
    }

    @Override
    public void tick() {
        //Fallback for if for some reason the myrmex can't reach the entrance try a different one (random)
        if (this.currentPhase == Phases.GOTOENTRANCE && !this.myrmex.pathReachesTarget(this.path, this.currentTarget, 12)) {
            this.currentTarget = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), this.hive.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRandom(), true));
            this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), this.movementSpeed);
        }
        if (this.currentPhase == Phases.GOTOENTRANCE && this.myrmex.isCloseEnoughToTarget(this.currentTarget, 12)) {
            if (this.hive != null) {
                this.currentTarget = this.hive.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.getRandom());
                this.currentPhase = Phases.GOTOEXIT;
                this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 1);
            }
        }
        if (this.currentPhase == Phases.GOTOEXIT && this.myrmex.isCloseEnoughToTarget(this.currentTarget, 12)) {
            if (this.hive != null) {
                this.currentTarget = MyrmexHive.getGroundedPos(this.myrmex.getWorld(), this.hive.getCenter());
                this.currentPhase = Phases.GOTOCENTER;
                this.path = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToXYZ(this.currentTarget.getX(), this.currentTarget.getY(), this.currentTarget.getZ(), 1);
            }
        }
        this.myrmex.isEnteringHive = !this.myrmex.isCloseEnoughToTarget(this.currentTarget, 14) && this.currentPhase != Phases.GOTOCENTER;
    }

    @Override
    public boolean shouldContinue() {
        return !(this.myrmex.isCloseEnoughToTarget(this.currentTarget, 9) && this.currentPhase != Phases.GOTOCENTER);
    }

    @Override
    public void stop() {
        this.currentTarget = BlockPos.ORIGIN;
        this.currentPhase = Phases.GOTOENTRANCE;
    }
}