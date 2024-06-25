package com.iafenvoy.iceandfire.entity.util;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class BlockLaunchExplosion extends Explosion {
    private final float size;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final DestructionType mode;

    public BlockLaunchExplosion(World world, MobEntity entity, double x, double y, double z, float size) {
        this(world, entity, x, y, z, size, DestructionType.DESTROY);
    }

    public BlockLaunchExplosion(World world, MobEntity entity, double x, double y, double z, float size, DestructionType mode) {
        this(world, entity, null, x, y, z, size, mode);
    }

    public BlockLaunchExplosion(World world, MobEntity entity, DamageSource source, double x, double y, double z, float size, DestructionType mode) {
        super(world, entity, source, null, x, y, z, size, false, mode);
        this.world = world;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.mode = mode;
    }

    private static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos) {
        int i = dropPositionArray.size();
        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.canMerge(itemstack, stack)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, stack, 16);
                dropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (stack.isEmpty()) return;
            }
        }
        dropPositionArray.add(Pair.of(stack, pos));
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    @Override
    public void affectWorld(boolean spawnParticles) {
        if (this.world.isClient)
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);

        boolean flag = this.mode != DestructionType.KEEP;
        if (spawnParticles) {
            if (!(this.size < 2.0F) && flag)
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            else
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
        }

        if (flag) {
            Collections.shuffle(this.getAffectedBlocks(), ThreadLocalRandom.current());

            for (BlockPos blockpos : this.getAffectedBlocks()) {
                BlockState blockstate = this.world.getBlockState(blockpos);
                if (!blockstate.isAir()) {
                    BlockPos blockpos1 = blockpos.toImmutable();
                    this.world.getProfiler().push("explosion_blocks");
                    Vec3d Vector3d = new Vec3d(this.x, this.y, this.z);
                    this.world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
                    blockstate.getBlock().onDestroyedByExplosion(this.world, blockpos, this);
                    FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, this.world);
                    fallingBlockEntity.setFallingBlockPos(blockpos1);
                    fallingBlockEntity.setPosition(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D);
                    double d5 = fallingBlockEntity.getX() - this.x;
                    double d7 = fallingBlockEntity.getEyeY() - this.y;
                    double d9 = fallingBlockEntity.getZ() - this.z;
                    float f3 = this.size * 2.0F;
                    double d12 = Math.sqrt(fallingBlockEntity.squaredDistanceTo(Vector3d)) / f3;
                    double d14 = getExposure(Vector3d, fallingBlockEntity);
                    double d11 = (1.0D - d12) * d14;
                    fallingBlockEntity.setVelocity(fallingBlockEntity.getVelocity().add(d5 * d11, d7 * d11, d9 * d11));
                    this.world.getProfiler().pop();
                }
            }
        }
    }
}