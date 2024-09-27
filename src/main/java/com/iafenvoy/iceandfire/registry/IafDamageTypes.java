package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IafDamageTypes {
    public static final RegistryKey<DamageType> GORGON_DMG_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(IceAndFire.MOD_ID, "gorgon"));
    public static final RegistryKey<DamageType> DRAGON_FIRE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(IceAndFire.MOD_ID, "dragon_fire"));
    public static final RegistryKey<DamageType> DRAGON_ICE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(IceAndFire.MOD_ID, "dragon_ice"));
    public static final RegistryKey<DamageType> DRAGON_LIGHTNING_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(IceAndFire.MOD_ID, "dragon_lightning"));

    public static CustomEntityDamageSource causeGorgonDamage(Entity entity) {
        RegistryEntry<DamageType> holder = entity.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(GORGON_DMG_TYPE).get();
        return new CustomEntityDamageSource(holder, entity);
    }

    public static CustomEntityDamageSource causeDragonFireDamage(Entity entity) {
        RegistryEntry<DamageType> holder = entity.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(DRAGON_FIRE_TYPE).get();
        return new CustomEntityDamageSource(holder, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonFireDamage(Entity source, Entity indirectEntityIn) {
        RegistryEntry<DamageType> holder = indirectEntityIn.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(DRAGON_FIRE_TYPE).get();
        return new CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    public static CustomEntityDamageSource causeDragonIceDamage(Entity entity) {
        RegistryEntry<DamageType> holder = entity.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(DRAGON_ICE_TYPE).get();
        return new CustomEntityDamageSource(holder, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonIceDamage(Entity source, Entity indirectEntityIn) {
        RegistryEntry<DamageType> holder = indirectEntityIn.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(DRAGON_ICE_TYPE).get();
        return new CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    public static CustomEntityDamageSource causeDragonLightningDamage(Entity entity) {
        RegistryEntry<DamageType> holder = entity.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(DRAGON_LIGHTNING_TYPE).get();
        return new CustomEntityDamageSource(holder, entity);
    }

    public static CustomIndirectEntityDamageSource causeIndirectDragonLightningDamage(Entity source, Entity indirectEntityIn) {
        RegistryEntry<DamageType> holder = indirectEntityIn.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getEntry(DRAGON_LIGHTNING_TYPE).get();
        return new CustomIndirectEntityDamageSource(holder, source, indirectEntityIn);
    }

    public static class CustomEntityDamageSource extends DamageSource {
        public CustomEntityDamageSource(RegistryEntry<DamageType> damageTypeIn, Entity damageSourceEntityIn) {
            super(damageTypeIn, damageSourceEntityIn);
        }

        @Override
        public Text getDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getPrimeAdversary();
            String s = "death.attack." + this.getName();
            int index = entityLivingBaseIn.getRandom().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? Text.translatable(s2, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : Text.translatable(s1, entityLivingBaseIn.getDisplayName());
        }
    }

    public static class CustomIndirectEntityDamageSource extends DamageSource {
        public CustomIndirectEntityDamageSource(RegistryEntry<DamageType> damageTypeIn, Entity source, Entity indirectEntityIn) {
            super(damageTypeIn, source, indirectEntityIn);
        }

        @Override
        public Text getDeathMessage(LivingEntity entityLivingBaseIn) {
            LivingEntity livingentity = entityLivingBaseIn.getPrimeAdversary();
            String s = "death.attack." + this.getName();
            int index = entityLivingBaseIn.getRandom().nextInt(2);
            String s1 = s + "." + index;
            String s2 = s + ".attacker_" + index;
            return livingentity != null ? Text.translatable(s2, entityLivingBaseIn.getDisplayName(), livingentity.getDisplayName()) : Text.translatable(s1, entityLivingBaseIn.getDisplayName());
        }
    }
}
