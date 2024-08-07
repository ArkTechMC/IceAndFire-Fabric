package com.iafenvoy.iceandfire.render.model;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.entity.EntityDragonEgg;
import com.iafenvoy.iceandfire.entity.block.BlockEntityEggInIce;
import com.iafenvoy.iceandfire.enums.DragonType;
import com.iafenvoy.uranus.client.model.AdvancedEntityModel;
import com.iafenvoy.uranus.client.model.AdvancedModelBox;
import com.iafenvoy.uranus.client.model.basic.BasicModelPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;

public class ModelDragonEgg<T extends LivingEntity> extends AdvancedEntityModel<T> {
    public final AdvancedModelBox Egg1;
    public final AdvancedModelBox Egg2;
    public final AdvancedModelBox Egg3;
    public final AdvancedModelBox Egg4;

    public ModelDragonEgg() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.Egg3 = new AdvancedModelBox(this, 0, 0);
        this.Egg3.setPos(0.0F, 0.0F, 0.0F);
        this.Egg3.addBox(-2.5F, -4.6F, -2.5F, 5, 5, 5, 0.0F);
        this.Egg2 = new AdvancedModelBox(this, 22, 2);
        this.Egg2.setPos(0.0F, 0.0F, 0.0F);
        this.Egg2.addBox(-2.5F, -0.6F, -2.5F, 5, 5, 5, 0.0F);
        this.Egg1 = new AdvancedModelBox(this, 0, 12);
        this.Egg1.setPos(0.0F, 19.6F, 0.0F);
        this.Egg1.addBox(-3.0F, -2.8F, -3.0F, 6, 6, 6, 0.0F);
        this.Egg4 = new AdvancedModelBox(this, 28, 16);
        this.Egg4.setPos(0.0F, -0.9F, 0.0F);
        this.Egg4.addBox(-2.0F, -4.8F, -2.0F, 4, 4, 4, 0.0F);
        this.Egg1.addChild(this.Egg3);
        this.Egg1.addChild(this.Egg2);
        this.Egg3.addChild(this.Egg4);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Egg1);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Egg1, this.Egg2, this.Egg3, this.Egg4);
    }

    @Override
    public void setAngles(LivingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.resetToDefaultPose();
        this.Egg1.setPos(0.0F, 19.6F, 0.0F);
        this.Egg4.setPos(0.0F, -0.9F, 0.0F);
        if (entity instanceof EntityDragonEgg egg) {
            boolean isLocationValid = false;
            if (egg.getEggType().dragonType() == DragonType.FIRE)
                isLocationValid = egg.getWorld().getBlockState(egg.getBlockPos()).isIn(BlockTags.FIRE);
            else if (egg.getEggType().dragonType() == DragonType.LIGHTNING)
                isLocationValid = egg.getWorld().hasRain(egg.getBlockPos());
            if (isLocationValid) {
                this.walk(this.Egg1, 0.3F, 0.3F, true, 1, 0, animationProgress, 1);
                this.flap(this.Egg1, 0.3F, 0.3F, false, 0, 0, animationProgress, 1);
            }
        }
    }

    public void renderPodium() {
        this.Egg1.rotateAngleX = (float) Math.toRadians(-180);
    }

    public void renderFrozen(BlockEntityEggInIce tile) {
        this.resetToDefaultPose();
        this.Egg1.rotateAngleX = (float) Math.toRadians(-180);
        this.walk(this.Egg1, 0.3F, 0.1F, true, 1, 0, tile.ticksExisted, 1);
        this.flap(this.Egg1, 0.3F, 0.1F, false, 0, 0, tile.ticksExisted, 1);
    }
}
