package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class IafRecipeRegistry {
    public static final RecipeType<DragonForgeRecipe> DRAGON_FORGE_TYPE = RecipeType.register("dragonforge");

    public static void registerDispenser() {
        DispenserBlock.registerBehavior(IafItemRegistry.STYMPHALIAN_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(IafEntityRegistry.STYMPHALIAN_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.AMPHITHERE_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(IafEntityRegistry.AMPHITHERE_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.SEA_SERPENT_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(IafEntityRegistry.SEA_SERPENT_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.DRAGONBONE_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(IafEntityRegistry.DRAGON_ARROW, position.getX(), position.getY(), position.getZ(), worldIn);
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.HYDRA_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityHydraArrow entityarrow = new EntityHydraArrow(IafEntityRegistry.HYDRA_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.HIPPOGRYPH_EGG, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                return new EntityHippogryphEgg(IafEntityRegistry.HIPPOGRYPH_EGG, worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.ROTTEN_EGG, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                return new EntityCockatriceEgg(IafEntityRegistry.COCKATRICE_EGG, position.getX(), position.getY(), position.getZ(), worldIn);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.DEATHWORM_EGG, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                return new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG, position.getX(), position.getY(), position.getZ(), worldIn, false);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.DEATHWORM_EGG_GIGANTIC, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                return new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG, position.getX(), position.getY(), position.getZ(), worldIn, true);
            }
        });
        BrewingRecipeRegistry.registerPotionRecipe(Potions.WATER, IafItemRegistry.SHINY_SCALES, Potions.WATER_BREATHING);
    }

    public static void init() {
        registerDispenser();
    }
}
