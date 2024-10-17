package com.iafenvoy.iceandfire.item.tool;

import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemAmphithereMacuahuitl extends SwordItem {
    public ItemAmphithereMacuahuitl() {
        super(IafItems.AMPHITHERE_SWORD_TOOL_MATERIAL, 3, -2.4F, new Settings());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        targetEntity.playSound(IafSounds.AMPHITHERE_GUST, 1, 1);
        targetEntity.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1, 1);
        targetEntity.velocityDirty = true;
        double xRatio = -MathHelper.sin(attacker.getYaw() * 0.017453292F);
        double zRatio = MathHelper.cos(attacker.getYaw() * 0.017453292F);
        float strength = -0.6F;
        float f = MathHelper.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
        targetEntity.setVelocity((targetEntity.getVelocity().x / 2) - xRatio / (double) f * (double) strength, 0.8D, (targetEntity.getVelocity().z / 2) - zRatio / (double) f * (double) strength);
        Random rand = new Random();
        for (int i = 0; i < 20; ++i) {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            targetEntity.getWorld().addParticle(ParticleTypes.CLOUD, targetEntity.getX() + (double) (rand.nextFloat() * targetEntity.getWidth() * 5.0F) - (double) targetEntity.getWidth() - d0 * 10.0D, targetEntity.getY() + (double) (rand.nextFloat() * targetEntity.getHeight()) - d1 * 10.0D, targetEntity.getZ() + (double) (rand.nextFloat() * targetEntity.getWidth() * 5.0F) - (double) targetEntity.getWidth() - d2 * 10.0D, d0, d1, d2);
        }
        return super.postHit(stack, targetEntity, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.amphithere_macuahuitl.desc_0").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.amphithere_macuahuitl.desc_1").formatted(Formatting.GRAY));
    }
}
