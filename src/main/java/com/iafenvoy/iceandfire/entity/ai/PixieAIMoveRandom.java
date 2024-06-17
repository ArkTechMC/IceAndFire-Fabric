package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityPixie;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;

import java.util.EnumSet;

public class PixieAIMoveRandom extends Goal {
    final EntityPixie pixie;
    final Random random;
    BlockPos target;

    public PixieAIMoveRandom(EntityPixie entityPixieIn) {
        this.pixie = entityPixieIn;
        this.random = entityPixieIn.getRandom();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        this.target = EntityPixie.getPositionRelativetoGround(this.pixie, this.pixie.getWorld(), this.pixie.getX() + this.random.nextInt(15) - 7, this.pixie.getZ() + this.random.nextInt(15) - 7, this.random);
        return !this.pixie.isOwnerClose() && !this.pixie.isPixieSitting() && this.isDirectPathBetweenPoints(this.pixie.getBlockPos(), this.target) && !this.pixie.getMoveControl().isMoving() && this.random.nextInt(4) == 0 && this.pixie.getHousePos() == null;
    }

    protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
        return this.pixie.getWorld().raycast(
                new RaycastContext(new Vec3d(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D),
                        new Vec3d(posVec32.getX() + 0.5D, posVec32.getY() + this.pixie.getHeight() * 0.5D, posVec32.getZ() + 0.5D),
                        RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.pixie)).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void tick() {
        if (!this.isDirectPathBetweenPoints(this.pixie.getBlockPos(), this.target))
            this.target = EntityPixie.getPositionRelativetoGround(this.pixie, this.pixie.getWorld(), this.pixie.getX() + this.random.nextInt(15) - 7, this.pixie.getZ() + this.random.nextInt(15) - 7, this.random);
        if (this.pixie.getWorld().isAir(this.target)) {
            this.pixie.getMoveControl().moveTo(this.target.getX() + 0.5D, this.target.getY() + 0.5D, this.target.getZ() + 0.5D, 0.25D);
            if (this.pixie.getTarget() == null)
                this.pixie.getLookControl().lookAt(this.target.getX() + 0.5D, this.target.getY() + 0.5D, this.target.getZ() + 0.5D, 180.0F, 20.0F);
        }
    }
}
