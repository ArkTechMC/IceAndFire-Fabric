package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.IafConfig;
import com.iafenvoy.iceandfire.entity.EntityPixie;
import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PixieAISteal extends Goal {
    private final EntityPixie temptedEntity;
    private PlayerEntity temptingPlayer;
    private int delayTemptCounter = 0;
    private boolean isRunning;

    public PixieAISteal(EntityPixie temptedEntityIn, double speedIn) {
        this.temptedEntity = temptedEntityIn;
    }

    @Override
    public boolean canStart() {
        if (!IafConfig.pixiesStealItems || !this.temptedEntity.getMainHandStack().isEmpty() || this.temptedEntity.stealCooldown > 0)
            return false;
        if (this.temptedEntity.getRandom().nextInt(200) == 0)
            return false;
        if (this.temptedEntity.isTamed())
            return false;
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {
            this.temptingPlayer = this.temptedEntity.getWorld().getClosestPlayer(this.temptedEntity, 10.0D);
            return this.temptingPlayer != null && (this.temptedEntity.getStackInHand(Hand.MAIN_HAND).isEmpty() && !this.temptingPlayer.getInventory().isEmpty() && !this.temptingPlayer.isCreative());
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.temptedEntity.isTamed() && this.temptedEntity.getMainHandStack().isEmpty() && this.delayTemptCounter == 0 && this.temptedEntity.stealCooldown == 0;
    }

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.temptingPlayer = null;
        if (this.delayTemptCounter < 10)
            this.delayTemptCounter += 10;
        this.isRunning = false;
    }

    @Override
    public void tick() {
        this.temptedEntity.getLookControl().lookAt(this.temptingPlayer, this.temptedEntity.getMaxHeadRotation() + 20, this.temptedEntity.getMaxLookPitchChange());
        ArrayList<Integer> slotlist = new ArrayList<>();
        if (this.temptedEntity.squaredDistanceTo(this.temptingPlayer) < 3D && !this.temptingPlayer.getInventory().isEmpty()) {

            for (int i = 0; i < this.temptingPlayer.getInventory().size(); i++) {
                ItemStack targetStack = this.temptingPlayer.getInventory().getStack(i);
                if (!PlayerInventory.isValidHotbarIndex(i) && !targetStack.isEmpty() && targetStack.isStackable())
                    slotlist.add(i);
            }
            if (!slotlist.isEmpty()) {
                final int slot = slotlist.size() == 1 ? slotlist.get(0) : slotlist.get(ThreadLocalRandom.current().nextInt(slotlist.size()));
                ItemStack randomItem = this.temptingPlayer.getInventory().getStack(slot);
                this.temptedEntity.setStackInHand(Hand.MAIN_HAND, randomItem);
                this.temptingPlayer.getInventory().removeStack(slot);
                this.temptedEntity.flipAI(true);
                this.temptedEntity.playSound(IafSounds.PIXIE_TAUNT, 1F, 1F);

                for (EntityPixie pixie : this.temptingPlayer.getWorld().getNonSpectatingEntities(EntityPixie.class, this.temptedEntity.getBoundingBox().expand(40)))
                    pixie.stealCooldown = 1000 + pixie.getRandom().nextInt(3000);
                if (this.temptingPlayer != null)
                    this.temptingPlayer.addStatusEffect(new StatusEffectInstance(this.temptedEntity.negativePotions[this.temptedEntity.getColor()], 100));
            } else {
                //If the pixie couldn't steal anything
                this.temptedEntity.flipAI(true);
                this.delayTemptCounter = 10 * 20;
            }
        } else
            this.temptedEntity.getMoveControl().moveTo(this.temptingPlayer.getX(), this.temptingPlayer.getY() + 1.5F, this.temptingPlayer.getZ(), 1D);
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}