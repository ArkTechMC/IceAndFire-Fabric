package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.data.component.EntityDataComponent;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EntityChainTie extends AbstractDecorationEntity {
    public EntityChainTie(EntityType<? extends AbstractDecorationEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityChainTie(EntityType<? extends AbstractDecorationEntity> type, World worldIn, BlockPos hangingPositionIn) {
        super(type, worldIn, hangingPositionIn);
        this.setPosition(hangingPositionIn.getX() + 0.5D, hangingPositionIn.getY(), hangingPositionIn.getZ() + 0.5D);
    }

    public static EntityChainTie createTie(World worldIn, BlockPos fence) {
        EntityChainTie entityChainTie = new EntityChainTie(IafEntities.CHAIN_TIE, worldIn, fence);
        worldIn.spawnEntity(entityChainTie);
        entityChainTie.onPlace();
        return entityChainTie;
    }

    public static EntityChainTie getKnotForPosition(World worldIn, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (EntityChainTie entityleashknot : worldIn.getNonSpectatingEntities(EntityChainTie.class, new Box(i - 1.0D, j - 1.0D, k - 1.0D, i + 1.0D, j + 1.0D, k + 1.0D)))
            if (entityleashknot != null && entityleashknot.getDecorationBlockPos() != null && entityleashknot.getDecorationBlockPos().equals(pos))
                return entityleashknot;
        return null;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(MathHelper.floor(x) + 0.5D, MathHelper.floor(y) + 0.5D, MathHelper.floor(z) + 0.5D);
    }

    @Override
    protected void updateAttachmentPosition() {
        this.setPos(this.attachmentPos.getX() + 0.5D, this.attachmentPos.getY() + 0.5D, this.attachmentPos.getZ() + 0.5D);
        double xSize = 0.3D;
        double ySize = 0.875D;
        this.setBoundingBox(new Box(this.getX() - xSize, this.getY() - 0.5, this.getZ() - xSize, this.getX() + xSize, this.getY() + ySize - 0.5, this.getZ() + xSize));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() != null && source.getAttacker() instanceof PlayerEntity)
            return super.damage(source, amount);
        return false;
    }

    @Override
    public int getWidthPixels() {
        return 9;
    }

    @Override
    public int getHeightPixels() {
        return 9;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        BlockPos blockpos = this.getDecorationBlockPos();
        compound.putInt("TileX", blockpos.getX());
        compound.putInt("TileY", blockpos.getY());
        compound.putInt("TileZ", blockpos.getZ());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        this.attachmentPos = new BlockPos(compound.getInt("TileX"), compound.getInt("TileY"), compound.getInt("TileZ"));
    }

    @Override
    protected float getEyeHeight(EntityPose poseIn, EntityDimensions sizeIn) {
        return -0.0625F;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 1024.0D;
    }

    @Override
    public void onBreak(Entity brokenEntity) {
        this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }

    @Override
    public void remove(RemovalReason removalReason) {
        super.remove(removalReason);
        double d0 = 30D;

        List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, new Box(this.getX() - d0, this.getY() - d0, this.getZ() - d0, this.getX() + d0, this.getY() + d0, this.getZ() + d0));

        for (LivingEntity livingEntity : list) {
            EntityDataComponent data = EntityDataComponent.get(livingEntity);
            if (data.chainData.isChainedTo(this)) {
                data.chainData.removeChain(this);
                ItemEntity entityitem = new ItemEntity(this.getWorld(), this.getX(), this.getY() + 1, this.getZ(), new ItemStack(IafItems.CHAIN));
                entityitem.resetPickupDelay();
                this.getWorld().spawnEntity(entityitem);
            }
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient)
            return ActionResult.SUCCESS;
        else {
            AtomicBoolean flag = new AtomicBoolean(false);
            double radius = 30D;
            List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, new Box(this.getX() - radius, this.getY() - radius, this.getZ() - radius, this.getX() + radius, this.getY() + radius, this.getZ() + radius));

            for (LivingEntity livingEntity : list) {
                EntityDataComponent data = EntityDataComponent.get(livingEntity);
                if (data.chainData.isChainedTo(player)) {
                    data.chainData.removeChain(player);
                    data.chainData.attachChain(this);
                    flag.set(true);
                }
            }

            if (!flag.get()) {
                this.remove(RemovalReason.DISCARDED);
                return ActionResult.SUCCESS;
            }

            return ActionResult.CONSUME;
        }
    }

    @Override
    public boolean canStayAttached() {
        return this.getWorld().getBlockState(this.attachmentPos).getBlock() instanceof WallBlock;
    }

    @Override
    public void onPlace() {
        this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }
}
