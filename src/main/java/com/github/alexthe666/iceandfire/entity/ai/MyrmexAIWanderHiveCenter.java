package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class MyrmexAIWanderHiveCenter extends Goal {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos target = BlockPos.ORIGIN;

    public MyrmexAIWanderHiveCenter(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.myrmex.canMove() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigation().isIdle() || this.myrmex.canSeeSky())
            return false;
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.getWorld()).getNearestHive(this.myrmex.getBlockPos(), 300);
        if (village == null)
            village = this.myrmex.getHive();
        if (village == null) return false;
        else {
            this.target = this.getNearPos(MyrmexHive.getGroundedPos(this.myrmex.getWorld(), village.getCenter()));
            this.path = this.myrmex.getNavigation().findPathTo(this.target, 0);
            return this.path != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.myrmex.getNavigation().isIdle() && this.myrmex.squaredDistanceTo(this.target.getX() + 0.5D, this.target.getY() + 0.5D, this.target.getZ() + 0.5D) > 3 && this.myrmex.shouldEnterHive();
    }

    @Override
    public void start() {
        this.myrmex.getNavigation().startMovingAlong(this.path, this.movementSpeed);
    }

    @Override
    public void stop() {
        this.target = BlockPos.ORIGIN;
        this.myrmex.getNavigation().startMovingAlong(null, this.movementSpeed);
    }

    public BlockPos getNearPos(BlockPos pos) {
        return pos.add(this.myrmex.getRandom().nextInt(8) - 4, 0, this.myrmex.getRandom().nextInt(8) - 4);
    }
}