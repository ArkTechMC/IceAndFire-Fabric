package com.iafenvoy.iafextra.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.HitResult;

@Cancelable
public class ProjectileImpactEvent extends Event {
    private final HitResult ray;
    private final ProjectileEntity projectile;

    public ProjectileImpactEvent(ProjectileEntity projectile, HitResult ray) {
        this.ray = ray;
        this.projectile = projectile;
    }

    public HitResult getRayTraceResult() {
        return this.ray;
    }

    public ProjectileEntity getProjectile() {
        return this.projectile;
    }
}
