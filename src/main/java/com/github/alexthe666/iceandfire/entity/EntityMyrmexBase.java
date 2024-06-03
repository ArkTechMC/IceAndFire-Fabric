package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockMyrmexConnectedResin;
import com.github.alexthe666.iceandfire.block.BlockMyrmexResin;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.IPassabilityNavigator;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.ICustomSizeNavigator;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public abstract class EntityMyrmexBase extends AnimalEntity implements IAnimatedEntity, Merchant, ICustomSizeNavigator, IPassabilityNavigator, IHasCustomizableAttributes {

    public static final Animation ANIMATION_PUPA_WIGGLE = Animation.create(20);
    private static final TrackedData<Byte> CLIMBING = DataTracker.registerData(EntityMyrmexBase.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Integer> GROWTH_STAGE = DataTracker.registerData(EntityMyrmexBase.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> VARIANT = DataTracker.registerData(EntityMyrmexBase.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final Identifier TEXTURE_DESERT_LARVA = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_desert_larva.png");
    private static final Identifier TEXTURE_DESERT_PUPA = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_desert_pupa.png");
    private static final Identifier TEXTURE_JUNGLE_LARVA = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_jungle_larva.png");
    private static final Identifier TEXTURE_JUNGLE_PUPA = new Identifier(IceAndFire.MOD_ID, "textures/models/myrmex/myrmex_jungle_pupa.png");
    private final SimpleInventory villagerInventory = new SimpleInventory(8);
    public boolean isEnteringHive = false;
    public boolean isBeingGuarded = false;
    protected int growthTicks = 1;
    protected TradeOfferList offers;
    private int waitTicks = 0;
    private int animationTick;
    private Animation currentAnimation;
    private MyrmexHive hive;
    private int timeUntilReset;
    private boolean leveledUp;
    private PlayerEntity customer;


    public EntityMyrmexBase(EntityType<? extends EntityMyrmexBase> t, World worldIn) {
        super(t, worldIn);
        this.navigation = createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    private static boolean isJungleBiome(World world, BlockPos position) {
        return BiomeConfig.test(BiomeConfig.jungleMyrmexBiomes, world.getBiome(position));
    }

    public static boolean haveSameHive(EntityMyrmexBase myrmex, Entity entity) {
        if (entity instanceof EntityMyrmexBase) {
            if (myrmex.getHive() != null && ((EntityMyrmexBase) entity).getHive() != null) {
                if (myrmex.isJungle() == ((EntityMyrmexBase) entity).isJungle()) {
                    return myrmex.getHive().getCenter() == ((EntityMyrmexBase) entity).getHive().getCenter();
                }
            }

        }
        if (entity instanceof EntityMyrmexEgg) {
            return myrmex.isJungle() == ((EntityMyrmexEgg) entity).isJungle();
        }
        return false;
    }

    public static boolean isEdibleBlock(BlockState blockState) {
        return blockState.isIn(TagKey.of(RegistryKeys.BLOCK, IafTagRegistry.MYRMEX_HARVESTABLES));
    }

    public static int getRandomCaste(World world, Random random, boolean royal) {
        float rand = random.nextFloat();
        if (royal) {
            if (rand > 0.9) {
                return 2;//royal
            } else if (rand > 0.75) {
                return 3;//sentinel
            } else if (rand > 0.5) {
                return 1;//soldier
            } else {
                return 0;//worker
            }
        } else {
            if (rand > 0.8) {
                return 3;//sentinel
            } else if (rand > 0.6) {
                return 1;//soldier
            } else {
                return 0;//worker
            }
        }
    }

    @Override
    public @NotNull SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    public boolean canMove() {
        return this.getGrowthStage() > 1;
    }

    @Override
    public boolean isBaby() {
        return this.getGrowthStage() < 2;
    }

    @Override
    protected void mobTick() {
        if (!this.hasCustomer() && this.timeUntilReset > 0) {
            --this.timeUntilReset;
            if (this.timeUntilReset <= 0) {
                if (this.leveledUp) {
                    this.levelUp();
                    this.leveledUp = false;
                }
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
            }
        }
        if (this.getHive() != null && this.getCustomer() != null) {
            this.getWorld().sendEntityStatus(this, (byte) 14);
            this.getHive().setWorld(this.getWorld());
        }
        super.mobTick();
    }

    @Override
    public int getXpToDrop() {
        return (this.getCasteImportance() * 7) + this.getWorld().random.nextInt(3);
    }

    @Override
    public boolean damage(@NotNull DamageSource dmg, float i) {
        if (dmg == this.getWorld().getDamageSources().inWall() && this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getGrowthStage() < 2) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }
        return super.damage(dmg, i);
    }

    @Override
    protected float getJumpVelocity() {
        return 0.52F;
    }

    @Override
    public float getPathfindingFavor(BlockPos pos) {
        return this.getWorld().getBlockState(pos.down()).getBlock() instanceof BlockMyrmexResin ? 10.0F : super.getPathfindingFavor(pos);
    }

    @Override
    protected @NotNull EntityNavigation createNavigation(@NotNull World worldIn) {
        return createNavigator(worldIn, AdvancedPathNavigate.MovementType.CLIMBING);
    }

    protected EntityNavigation createNavigator(World worldIn, AdvancedPathNavigate.MovementType type) {
        return createNavigator(worldIn, type, getWidth(), getHeight());
    }

    protected EntityNavigation createNavigator(World worldIn, AdvancedPathNavigate.MovementType type, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, getWorld(), type, width, height);
        this.navigation = newNavigator;
        newNavigator.setCanSwim(true);
        newNavigator.getNodeMaker().setCanOpenDoors(true);
        return newNavigator;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CLIMBING, (byte) 0);
        this.dataTracker.startTracking(GROWTH_STAGE, 2);
        this.dataTracker.startTracking(VARIANT, Boolean.FALSE);
    }

    @Override
    public void tick() {
        super.tick();
        this.setStepHeight(1);
        if (getWorld().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof PlayerEntity) {
            this.setTarget(null);
        }
        if (this.getGrowthStage() < 2 && this.getVehicle() != null && this.getVehicle() instanceof EntityMyrmexBase) {
            float yaw = this.getVehicle().getYaw();
            this.setYaw(yaw);
            this.headYaw = yaw;
            this.bodyYaw = 0;
            this.prevBodyYaw = 0;
        }
        if (!this.getWorld().isClient) {
            this.setBesideClimbableBlock(this.horizontalCollision && (this.isOnGround() || !this.verticalCollision));
        }
        if (this.getGrowthStage() < 2) {
            growthTicks++;
            if (growthTicks == IafConfig.myrmexLarvaTicks) {
                this.setGrowthStage(this.getGrowthStage() + 1);
                growthTicks = 0;
            }
        }
        if (!this.getWorld().isClient && this.getGrowthStage() < 2 && this.getRandom().nextInt(150) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_PUPA_WIGGLE);
        }

        if (this.getTarget() != null && !(this.getTarget() instanceof PlayerEntity) && this.getNavigation().isIdle()) {
            this.setTarget(null);
        }
        if (this.getTarget() != null && (haveSameHive(this, this.getTarget()) ||
                this.getTarget() instanceof TameableEntity && !canAttackTamable((TameableEntity) this.getTarget()) ||
                this.getTarget() instanceof PlayerEntity && this.getHive() != null && !this.getHive().isPlayerReputationLowEnoughToFight(this.getTarget().getUuid()))) {
            this.setTarget(null);
        }
        if (this.getWaitTicks() > 0) {
            this.setWaitTicks(this.getWaitTicks() - 1);
        }
        if (this.getHealth() < this.getMaxHealth() && this.age % 500 == 0 && this.isOnResin()) {
            this.heal(1);
            this.getWorld().sendEntityStatus(this, (byte) 76);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void writeCustomDataToNbt(@NotNull NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putInt("GrowthStage", this.getGrowthStage());
        tag.putInt("GrowthTicks", growthTicks);
        tag.putBoolean("Variant", this.isJungle());
        if (this.getHive() != null) {
            tag.putUuid("HiveUUID", this.getHive().hiveUUID);
        }
        TradeOfferList merchantoffers = this.getOffers();
        if (!merchantoffers.isEmpty()) {
            tag.put("Offers", merchantoffers.toNbt());
        }

        NbtList listnbt = new NbtList();

        for (int i = 0; i < this.villagerInventory.size(); ++i) {
            ItemStack itemstack = this.villagerInventory.getStack(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.writeNbt(new NbtCompound()));
            }
        }
        tag.put("Inventory", listnbt);
    }

    @Override
    public void readCustomDataFromNbt(@NotNull NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setGrowthStage(tag.getInt("GrowthStage"));
        this.growthTicks = tag.getInt("GrowthTicks");
        this.setJungleVariant(tag.getBoolean("Variant"));
        if (tag.containsUuid("HiveUUID")) {
            this.setHive(MyrmexWorldData.get(getWorld()).getHiveFromUUID(tag.getUuid("HiveUUID")));
        }
        if (tag.contains("Offers", 10)) {
            this.offers = new TradeOfferList(tag.getCompound("Offers"));
        }

        NbtList listnbt = tag.getList("Inventory", 10);

        for (int i = 0; i < listnbt.size(); ++i) {
            ItemStack itemstack = ItemStack.fromNbt(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.villagerInventory.addStack(itemstack);
            }
        }
        this.setConfigurableAttributes();

    }


    public boolean canAttackTamable(TameableEntity tameable) {
        if (tameable.getOwner() != null && this.getHive() != null) {
            return this.getHive().isPlayerReputationLowEnoughToFight(tameable.getOwnerUuid());
        }
        return true;
    }

    public Vec3d getPos() {
        return this.getBlockPos().toCenterPos();
    }

    public int getGrowthStage() {
        return this.dataTracker.get(GROWTH_STAGE).intValue();
    }

    public void setGrowthStage(int stage) {
        this.dataTracker.set(GROWTH_STAGE, stage);
    }

    public int getWaitTicks() {
        return waitTicks;
    }

    public void setWaitTicks(int waitTicks) {
        this.waitTicks = waitTicks;
    }

    public boolean isJungle() {
        return this.dataTracker.get(VARIANT).booleanValue();
    }

    public void setJungleVariant(boolean isJungle) {
        this.dataTracker.set(VARIANT, isJungle);
    }

    @Override
    public @NotNull EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    public boolean isBesideClimbableBlock() {
        return (this.dataTracker.get(CLIMBING).byteValue() & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataTracker.get(CLIMBING).byteValue();

        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataTracker.set(CLIMBING, b0);
    }

    @Override
    public boolean isClimbing() {
        if (this.getNavigation() instanceof AdvancedPathNavigate) {
            //Make sure the entity can only climb when it's on or below the path. This prevents the entity from getting stuck
            if (((AdvancedPathNavigate) this.getNavigation()).entityOnAndBelowPath(this, new Vec3d(1.1, 0, 1.1)))
                return true;
        }
        return super.isClimbing();
    }

    @Override
    public PassiveEntity createChild(@NotNull ServerWorld serverWorld, @NotNull PassiveEntity ageable) {
        return null;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE};
    }

    @Override
    public void setAttacker(LivingEntity livingBase) {
        if (this.getHive() == null || livingBase == null || livingBase instanceof PlayerEntity && this.getHive().isPlayerReputationLowEnoughToFight(livingBase.getUuid())) {
            super.setAttacker(livingBase);
        }
        if (this.getHive() != null && livingBase != null) {
            this.getHive().addOrRenewAgressor(livingBase, this.getImportance());
        }
        if (this.getHive() != null && livingBase != null) {
            if (livingBase instanceof PlayerEntity) {
                int i = -5 * this.getCasteImportance();
                this.getHive().setWorld(this.getWorld());
                this.getHive().modifyPlayerReputation(livingBase.getUuid(), i);
                if (this.isAlive()) {
                    this.getWorld().sendEntityStatus(this, (byte) 13);
                }
            }
        }
    }

    @Override
    public void onDeath(@NotNull DamageSource cause) {
        if (this.getHive() != null) {
            Entity entity = cause.getAttacker();
            if (entity != null) {
                this.getHive().setWorld(this.getWorld());
                this.getHive().modifyPlayerReputation(entity.getUuid(), -15);
            }
        }
        this.resetCustomer();
        super.onDeath(cause);
    }

    @Override
    public @NotNull ActionResult interactMob(PlayerEntity player, @NotNull Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        if (!shouldHaveNormalAI()) {
            return ActionResult.PASS;
        }
        boolean flag2 = itemstack.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get() || itemstack.getItem() == IafItemRegistry.MYRMEX_DESERT_STAFF.get();

        if (flag2) {
            this.onStaffInteract(player, itemstack);
            player.swingHand(hand);
            return ActionResult.SUCCESS;
        }
        boolean flag = itemstack.getItem() == Items.NAME_TAG || itemstack.getItem() == Items.LEAD;
        if (flag) {
            return super.interactMob(player, hand);
        } else if (this.getGrowthStage() >= 2 && this.isAlive() && !this.isBaby() && !player.isSneaking()) {
            if (this.getOffers().isEmpty()) {
                return super.interactMob(player, hand);
            } else {
                if (!this.getWorld().isClient && (this.getTarget() == null || !this.getTarget().equals(player)) && hand == Hand.MAIN_HAND) {
                    if (this.getHive() != null && !this.getHive().isPlayerReputationTooLowToTrade(player.getUuid())) {
                        this.setCustomer(player);
                        this.sendOffers(player, this.getDisplayName(), 1);
                        return ActionResult.SUCCESS;
                    }
                }

                return ActionResult.PASS;
            }
        } else {
            return super.interactMob(player, hand);
        }
    }

    public void onStaffInteract(PlayerEntity player, ItemStack itemstack) {
        if (itemstack.getNbt() == null) {
            return;
        }
        UUID staffUUID = itemstack.getNbt().containsUuid("HiveUUID") ? itemstack.getNbt().getUuid("HiveUUID") : null;
        if (getWorld().isClient) {
            return;
        }
        if (!player.isCreative()) {
            if ((this.getHive() != null && !this.getHive().canPlayerCommandHive(player.getUuid()))) {
                return;
            }
        }
        if (this.getHive() == null) {
            player.sendMessage(Text.translatable("myrmex.message.null_hive"), true);

        } else {
            if (staffUUID != null && staffUUID.equals(this.getHive().hiveUUID)) {
                player.sendMessage(Text.translatable("myrmex.message.staff_already_set"), true);
            } else {
                this.getHive().setWorld(this.getWorld());
                EntityMyrmexQueen queen = this.getHive().getQueen();
                BlockPos center = this.getHive().getCenterGround();
                if (queen != null && queen.hasCustomName()) {
                    player.sendMessage(Text.translatable("myrmex.message.staff_set_named", queen.getName(), center.getX(), center.getY(), center.getZ()), true);
                } else {
                    player.sendMessage(Text.translatable("myrmex.message.staff_set_unnamed", center.getX(), center.getY(), center.getZ()), true);
                }
                itemstack.getNbt().putUuid("HiveUUID", this.getHive().hiveUUID);
            }

        }

    }

    @Override
    public EntityData initialize(@NotNull ServerWorldAccess worldIn, @NotNull LocalDifficulty difficultyIn, @NotNull SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        spawnDataIn = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setHive(MyrmexWorldData.get(getWorld()).getNearestHive(this.getBlockPos(), 400));
        if (this.getHive() != null) {
            this.setJungleVariant(isJungleBiome(getWorld(), this.getHive().getCenter()));
        } else {
            this.setJungleVariant(random.nextBoolean());
        }
        return spawnDataIn;
    }

    public abstract boolean shouldLeaveHive();

    public abstract boolean shouldEnterHive();

    @Override
    public float getScaleFactor() {
        return this.getGrowthStage() == 0 ? 0.5F : this.getGrowthStage() == 1 ? 0.75F : 1F;
    }

    public abstract Identifier getAdultTexture();

    public abstract float getModelScale();

    public Identifier getTexture() {
        if (this.getGrowthStage() == 0) {
            return isJungle() ? TEXTURE_JUNGLE_LARVA : TEXTURE_DESERT_LARVA;
        } else if (this.getGrowthStage() == 1) {
            return isJungle() ? TEXTURE_JUNGLE_PUPA : TEXTURE_DESERT_PUPA;
        } else {
            return getAdultTexture();
        }
    }

    public MyrmexHive getHive() {
        return hive;
    }

    public void setHive(MyrmexHive newHive) {
        hive = newHive;
        if (hive != null) {
            hive.addMyrmex(this);
        }
    }

    @Override
    protected void pushAway(@NotNull Entity entityIn) {
        if (!haveSameHive(this, entityIn)) {
            entityIn.pushAwayFrom(this);
        }
    }

    public boolean canSeeSky() {
        return getWorld().isSkyVisibleAllowingSea(this.getBlockPos());
    }

    public boolean isOnResin() {
        int d0 = this.getBlockY() - 1;
        BlockPos blockpos = new BlockPos(this.getBlockX(), d0, this.getBlockZ());
        while (getWorld().isAir(blockpos) && blockpos.getY() > 1) {
            blockpos = blockpos.down();
        }
        BlockState BlockState = this.getWorld().getBlockState(blockpos);
        return BlockState.getBlock() instanceof BlockMyrmexResin || BlockState.getBlock() instanceof BlockMyrmexConnectedResin;
    }

    public boolean isInNursery() {
        if (getHive() != null && getHive().getRooms(WorldGenMyrmexHive.RoomType.NURSERY).isEmpty() && getHive().getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRandom(), this.getBlockPos()) != null) {
            return false;
        }
        if (getHive() != null) {
            BlockPos nursery = getHive().getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.getRandom(), this.getBlockPos());
            return Math.sqrt(this.squaredDistanceTo(nursery.getX(), nursery.getY(), nursery.getZ())) < 45;
        }
        return false;
    }

    public boolean isInHive() {
        if (getHive() != null) {
            for (BlockPos pos : getHive().getAllRooms()) {
                if (isCloseEnoughToTarget(MyrmexHive.getGroundedPos(getWorld(), pos), 50))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void travel(@NotNull Vec3d motion) {
        if (!this.canMove()) {
            super.travel(Vec3d.ZERO);
            return;
        }
        super.travel(motion);
    }

    public int getImportance() {
        if (this.getGrowthStage() < 2) {
            return 1;
        }
        return getCasteImportance();
    }

    public abstract int getCasteImportance();

    public boolean needsGaurding() {
        return true;
    }

    public boolean shouldMoveThroughHive() {
        return true;
    }

    public boolean shouldWander() {
        return this.getHive() == null;
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 76) {
            this.playVillagerEffect();
        } else {
            super.handleStatus(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.MYRMEX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.MYRMEX_DIE;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(IafSoundRegistry.MYRMEX_WALK, 0.16F * this.getMyrmexPitch() * (this.getRandom().nextFloat() * 0.6F + 0.4F), 1.0F);
    }

    protected void playBiteSound() {
        this.playSound(IafSoundRegistry.MYRMEX_BITE, this.getMyrmexPitch(), 1.0F);
    }

    protected void playStingSound() {
        this.playSound(IafSoundRegistry.MYRMEX_STING, this.getMyrmexPitch(), 0.6F);
    }

    protected void playVillagerEffect() {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.getWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getY() + 0.5D + (double) (this.random.nextFloat() * this.getHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
        }
    }

    public float getMyrmexPitch() {
        return getWidth();
    }

    public boolean shouldHaveNormalAI() {
        return true;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public Box getAttackBounds() {
        float size = this.getScaleFactor() * 0.65F;
        return this.getBoundingBox().expand(1.0F + size, 1.0F + size, 1.0F + size);
    }

    @Override
    public PlayerEntity getCustomer() {
        return this.customer;
    }

    @Override
    public void setCustomer(PlayerEntity player) {
        this.customer = player;
    }

    public boolean hasCustomer() {
        return this.customer != null;
    }

    @Override
    public @NotNull TradeOfferList getOffers() {
        if (this.offers == null) {
            this.offers = new TradeOfferList();
            this.populateTradeData();
        }

        return this.offers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {
    }

    @Override
    public void setExperienceFromServer(int xpIn) {
    }

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
        this.ambientSoundChance = -this.getMinAmbientSoundDelay();
        this.onVillagerTrade(offer);
    }

    protected void onVillagerTrade(TradeOffer offer) {
        if (offer.shouldRewardPlayerExperience()) {
            int i = 3 + this.random.nextInt(4);
            this.getWorld().spawnEntity(new ExperienceOrbEntity(this.getWorld(), this.getX(), this.getY() + 0.5D, this.getZ(), i));
        }
        if (this.getHive() != null && this.getCustomer() != null) {
            this.getHive().setWorld(this.getWorld());
            this.getHive().modifyPlayerReputation(this.getCustomer().getUuid(), 1);
        }
    }

    @Override
    public void onSellingItem(@NotNull ItemStack stack) {
        if (!this.getWorld().isClient && this.ambientSoundChance > -this.getMinAmbientSoundDelay() + 20) {
            this.ambientSoundChance = -this.getMinAmbientSoundDelay();
            this.playSound(this.getVillagerYesNoSound(!stack.isEmpty()), this.getSoundVolume(), this.getSoundPitch());
        }

    }

    @Override
    public @NotNull SoundEvent getYesSound() {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    protected SoundEvent getVillagerYesNoSound(boolean getYesSound) {
        return IafSoundRegistry.MYRMEX_IDLE;
    }

    public void playCelebrateSound() {
    }

    protected void resetCustomer() {
        this.setCustomer(null);
    }

    @Override
    public Entity changeDimension(@NotNull ServerWorld server, net.minecraftforge.common.util.@NotNull ITeleporter teleporter) {
        this.resetCustomer();
        return super.moveToWorld(server, teleporter);
    }

    public SimpleInventory getVillagerInventory() {
        return this.villagerInventory;
    }


    @Override
    public @NotNull ItemStack tryEquip(@NotNull ItemStack stack) {
        ItemStack superStack = super.tryEquip(stack);
        if (ItemStack.areItemsEqual(superStack, stack) && ItemStack.areEqual(superStack, stack)) {
            return stack;
        } else {
            EquipmentSlot inventorySlot = stack.getEquipmentSlot();
            int i = inventorySlot.getEntitySlotId() - 300;
            if (i >= 0 && i < this.villagerInventory.size()) {
                this.villagerInventory.setStack(i, stack);
                return stack;
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    protected void addTrades(TradeOfferList givenMerchantOffers, TradeOffers.Factory[] newTrades, int maxNumbers) {
        Set<Integer> set = Sets.newHashSet();
        if (newTrades.length > maxNumbers) {
            while (set.size() < maxNumbers) {
                set.add(this.random.nextInt(newTrades.length));
            }
        } else {
            for (int i = 0; i < newTrades.length; ++i) {
                set.add(i);
            }
        }

        for (Integer integer : set) {
            TradeOffers.Factory villagertrades$itrade = newTrades[integer];
            TradeOffer merchantoffer = villagertrades$itrade.create(this, this.random);
            if (merchantoffer != null) {
                givenMerchantOffers.add(merchantoffer);
            }
        }

    }

    private void levelUp() {
        this.populateTradeData();
    }

    protected abstract TradeOffers.Factory[] getLevel1Trades();

    protected abstract TradeOffers.Factory[] getLevel2Trades();

    protected void populateTradeData() {
        TradeOffers.Factory[] level1 = getLevel1Trades();
        TradeOffers.Factory[] level2 = getLevel2Trades();
        if (level1 != null && level2 != null) {
            TradeOfferList merchantoffers = this.getOffers();
            this.addTrades(merchantoffers, level1, 5);
            int i = this.random.nextInt(level2.length);
            int j = this.random.nextInt(level2.length);
            int k = this.random.nextInt(level2.length);
            int rolls = 0;
            while ((j == i) && rolls < 100) {
                j = this.random.nextInt(level2.length);
                rolls++;
            }
            rolls = 0;
            while ((k == i || k == j) && rolls < 100) {
                k = this.random.nextInt(level2.length);
                rolls++;
            }
            TradeOffers.Factory rareTrade1 = level2[i];
            TradeOffers.Factory rareTrade2 = level2[j];
            TradeOffers.Factory rareTrade3 = level2[k];
            TradeOffer merchantoffer1 = rareTrade1.create(this, this.random);
            if (merchantoffer1 != null) {
                merchantoffers.add(merchantoffer1);
            }
            TradeOffer merchantoffer2 = rareTrade2.create(this, this.random);
            if (merchantoffer2 != null) {
                merchantoffers.add(merchantoffer2);
            }
            TradeOffer merchantoffer3 = rareTrade3.create(this, this.random);
            if (merchantoffer3 != null) {
                merchantoffers.add(merchantoffer3);
            }
        }
    }

    public boolean isCloseEnoughToTarget(BlockPos target, double distanceSquared) {
        if (target != null) {
            return this.squaredDistanceTo(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D) <= distanceSquared;
        }
        return false;
    }

    //if the path created couldn't reach the destination or if the entity isn't close enough to the targetBlock
    public boolean pathReachesTarget(PathResult path, BlockPos target, double distanceSquared) {
        return !path.failedToReachDestination()
                && (this.isCloseEnoughToTarget(target, distanceSquared) || this.getNavigation().getCurrentPath() == null || !this.getNavigation().getCurrentPath().isFinished());
    }

    @Override
    public boolean isSmallerThanBlock() {
        return false;
    }

    @Override
    public float getXZNavSize() {
        return getWidth() / 2;
    }

    @Override
    public int getYNavSize() {
        return (int) getHeight() / 2;
    }

    @Override
    public int maxSearchNodes() {
        return IafConfig.maxDragonPathingNodes;
    }

    @Override
    public boolean isBlockExplicitlyPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return false;
    }

    @Override
    public boolean isBlockExplicitlyNotPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return state.getBlock() instanceof LeavesBlock;
    }
}
