package com.github.alexthe666.iceandfire.registry;

import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
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

public class IafRecipes {
    public static final RecipeType<DragonForgeRecipe> DRAGON_FORGE_TYPE = RecipeType.register("dragonforge");

    public static void registerDispenser() {
        DispenserBlock.registerBehavior(IafItems.STYMPHALIAN_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(IafEntities.STYMPHALIAN_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItems.AMPHITHERE_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(IafEntities.AMPHITHERE_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItems.SEA_SERPENT_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(IafEntities.SEA_SERPENT_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItems.DRAGONBONE_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(IafEntities.DRAGON_ARROW, position.getX(), position.getY(), position.getZ(), worldIn);
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItems.HYDRA_ARROW, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityHydraArrow entityarrow = new EntityHydraArrow(IafEntities.HYDRA_ARROW, worldIn, position.getX(), position.getY(), position.getZ());
                entityarrow.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItems.HIPPOGRYPH_EGG, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                return new EntityHippogryphEgg(IafEntities.HIPPOGRYPH_EGG, worldIn, position.getX(), position.getY(), position.getZ(), stackIn);
            }
        });
        DispenserBlock.registerBehavior(IafItems.ROTTEN_EGG, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                return new EntityCockatriceEgg(IafEntities.COCKATRICE_EGG, position.getX(), position.getY(), position.getZ(), worldIn);
            }
        });
        DispenserBlock.registerBehavior(IafItems.DEATHWORM_EGG, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                return new EntityDeathWormEgg(IafEntities.DEATH_WORM_EGG, position.getX(), position.getY(), position.getZ(), worldIn, false);
            }
        });
        DispenserBlock.registerBehavior(IafItems.DEATHWORM_EGG_GIGANTIC, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                return new EntityDeathWormEgg(IafEntities.DEATH_WORM_EGG, position.getX(), position.getY(), position.getZ(), worldIn, true);
            }
        });
        BrewingRecipeRegistry.registerPotionRecipe(Potions.WATER, IafItems.SHINY_SCALES, Potions.WATER_BREATHING);
    }

    public static void init() {
        registerDispenser();
    }
}
