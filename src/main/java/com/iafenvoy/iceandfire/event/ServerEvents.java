package com.iafenvoy.iceandfire.event;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.data.EntityDataComponent;
import com.iafenvoy.iceandfire.entity.*;
import com.iafenvoy.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.iafenvoy.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.iafenvoy.iceandfire.entity.util.IAnimalFear;
import com.iafenvoy.iceandfire.entity.util.IVillagerFear;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import com.iafenvoy.iceandfire.item.ItemChain;
import com.iafenvoy.iceandfire.item.armor.ItemDragonSteelArmor;
import com.iafenvoy.iceandfire.item.armor.ItemScaleArmor;
import com.iafenvoy.iceandfire.item.armor.ItemTrollArmor;
import com.iafenvoy.iceandfire.network.IafClientNetworkHandler;
import com.iafenvoy.iceandfire.network.message.MessagePlayerHitMultipart;
import com.iafenvoy.iceandfire.registry.*;
import com.iafenvoy.iceandfire.util.IdUtil;
import com.iafenvoy.iceandfire.util.RandomHelper;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageRecord;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ServerEvents {
    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    public static final String BOLT_DONT_DESTROY_LOOT = "iceandfire.bolt_skip_loot";
    // FIXME :: No check for shouldFear()?
    private static final Predicate<LivingEntity> VILLAGER_FEAR = entity -> entity instanceof IVillagerFear fear && fear.shouldFear();
    private static final String[] VILLAGE_TYPES = new String[]{"plains", "desert", "snowy", "savanna", "taiga"};

    private static void signalChickenAlarm(LivingEntity chicken, LivingEntity attacker) {
        final float d0 = IafConfig.getInstance().cockatrice.chickenSearchLength;
        final List<EntityCockatrice> list = chicken.getWorld().getNonSpectatingEntities(EntityCockatrice.class, (new Box(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.getX() + 1.0D, chicken.getY() + 1.0D, chicken.getZ() + 1.0D)).expand(d0, 10.0D, d0));
        if (list.isEmpty()) return;

        for (final EntityCockatrice cockatrice : list) {
            if (!(attacker instanceof EntityCockatrice)) {
                if (!DragonUtils.hasSameOwner(cockatrice, attacker)) {
                    if (attacker instanceof PlayerEntity player) {
                        if (!player.isCreative() && !cockatrice.isOwner(player)) {
                            cockatrice.setTarget(player);
                        }
                    } else {
                        cockatrice.setTarget(attacker);
                    }
                }
            }
        }
    }

    private static void signalAmphithereAlarm(LivingEntity villager, LivingEntity attacker) {
        final float d0 = IafConfig.getInstance().amphithere.villagerSearchLength;
        final List<EntityAmphithere> list = villager.getWorld().getNonSpectatingEntities(EntityAmphithere.class, (new Box(villager.getX() - 1.0D, villager.getY() - 1.0D, villager.getZ() - 1.0D, villager.getX() + 1.0D, villager.getY() + 1.0D, villager.getZ() + 1.0D)).expand(d0, d0, d0));
        if (list.isEmpty()) return;

        for (final Entity entity : list) {
            if (entity instanceof EntityAmphithere amphithere && !(attacker instanceof EntityAmphithere)) {
                if (!DragonUtils.hasSameOwner(amphithere, attacker)) {
                    if (attacker instanceof PlayerEntity player) {
                        if (!player.isCreative() && !amphithere.isOwner(player)) {
                            amphithere.setTarget(player);
                        }
                    } else {
                        amphithere.setTarget(attacker);
                    }
                }
            }
        }
    }

    public static boolean isRidingOrBeingRiddenBy(final Entity first, final Entity entityIn) {
        if (first == null || entityIn == null) return false;
        for (final Entity entity : first.getPassengerList())
            if (entity.equals(entityIn) || isRidingOrBeingRiddenBy(entity, entityIn))
                return true;
        return false;
    }

//    public static void onArrowCollide(final ProjectileImpactEvent event) {
//        if (event.getRayTraceResult() instanceof EntityHitResult result) {
//            Entity shotEntity = result.getEntity();
//
//            if (shotEntity instanceof EntityGhost) event.cancel();
//            else if (event.getProjectile() instanceof PersistentProjectileEntity arrow && arrow.getOwner() != null) {
//                Entity shootingEntity = arrow.getOwner();
//                if (shootingEntity instanceof LivingEntity && isRidingOrBeingRiddenBy(shootingEntity, shotEntity))
//                    if (shotEntity instanceof TameableEntity tamable && tamable.isTamed() && shotEntity.isTeammate(shootingEntity))
//                        event.cancel();
//            }
//        }
//    }

    public static void addNewVillageBuilding(MinecraftServer server) {
        if (IafConfig.getInstance().worldGen.villagerHouseWeight > 0) {
            Registry<StructurePool> templatePoolRegistry = server.getRegistryManager().get(RegistryKeys.TEMPLATE_POOL);
            Registry<StructureProcessorList> processorListRegistry = server.getRegistryManager().get(RegistryKeys.PROCESSOR_LIST);
            for (String type : VILLAGE_TYPES)
                IafTrades.addBuildingToPool(templatePoolRegistry, processorListRegistry, new Identifier("village/" + type + "/houses"), IdUtil.build(IceAndFire.MOD_ID, "village/" + type + "_scriber_1"), IafConfig.getInstance().worldGen.villagerHouseWeight);
        }
    }

    public static void onEntityFall(LivingEntity entity, float fallDistance, float multiplier, DamageSource source) {
        if (entity instanceof PlayerEntity) {
            EntityDataComponent data = EntityDataComponent.get(entity);
            if (data.miscData.hasDismounted)
                data.miscData.setDismounted(false);
        }
    }

    public static float onEntityDamage(LivingEntity entity, DamageSource source, float amount) {
        if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            float multi = 1;
            if (entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ItemTrollArmor)
                multi -= 0.1f;
            if (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ItemTrollArmor)
                multi -= 0.3f;
            if (entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof ItemTrollArmor)
                multi -= 0.2f;
            if (entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof ItemTrollArmor)
                multi -= 0.1f;
            amount *= multi;
        }
        if (source.isOf(IafDamageTypes.DRAGON_FIRE_TYPE) || source.isOf(IafDamageTypes.DRAGON_ICE_TYPE) || source.isOf(IafDamageTypes.DRAGON_LIGHTNING_TYPE)) {
            float multi = 1;
            if (entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ItemScaleArmor ||
                    entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ItemDragonSteelArmor)
                multi -= 0.1f;
            if (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ItemScaleArmor ||
                    entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ItemDragonSteelArmor)
                multi -= 0.3f;
            if (entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof ItemScaleArmor ||
                    entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof ItemDragonSteelArmor)
                multi -= 0.2f;
            if (entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof ItemScaleArmor ||
                    entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof ItemDragonSteelArmor)
                multi -= 0.1f;
            amount *= multi;
        }
        return amount;
    }

    public static void onLivingSetTarget(Entity tracking, ServerPlayerEntity player) {
        if (tracking instanceof LivingEntity target) {
            if (target.getType().isIn(IafTags.CHICKENS)) {
                signalChickenAlarm(target, player);
            } else if (DragonUtils.isVillager(target)) {
                signalAmphithereAlarm(target, player);
            }
        }
    }

    public static ActionResult onPlayerAttack(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity != null && entity.getType().isIn(IafTags.SHEEP)) {
            float dist = IafConfig.getInstance().cyclops.sheepSearchLength;
            final List<Entity> list = entity.getWorld().getOtherEntities(entity, entity.getBoundingBox().expand(dist, dist, dist));
            if (!list.isEmpty())
                for (final Entity e : list)
                    if (e instanceof EntityCyclops cyclops)
                        if (!cyclops.isBlinded() && !player.isCreative())
                            cyclops.setTarget(player);
        }
        if (entity instanceof EntityStoneStatue statue) {
            statue.setHealth(statue.getMaxHealth());
            if (player != null) {
                ItemStack stack = player.getMainHandStack();
                entity.playSound(SoundEvents.BLOCK_STONE_BREAK, 2, 0.5F + (float) (RandomHelper.nextDouble(-1, 1) * 0.2 + 0.5));

                if (stack.getItem().isSuitableFor(Blocks.STONE.getDefaultState()) || stack.getItem().getTranslationKey().contains("pickaxe")) {
                    statue.setCrackAmount(statue.getCrackAmount() + 1);

                    if (statue.getCrackAmount() > 9) {
                        NbtCompound writtenTag = new NbtCompound();
                        entity.writeNbt(writtenTag);
                        entity.playSound(SoundEvents.BLOCK_STONE_BREAK, 2F, (float) (RandomHelper.nextDouble(-1, 1) * 0.2 + 0.5));
                        entity.remove(Entity.RemovalReason.KILLED);

                        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) > 0) {
                            ItemStack statuette = new ItemStack(IafItems.STONE_STATUE);
                            NbtCompound tag = statuette.getOrCreateNbt();
                            tag.putBoolean("IAFStoneStatuePlayerEntity", statue.getTrappedEntityTypeString().equalsIgnoreCase("minecraft:player"));
                            tag.putString("IAFStoneStatueEntityID", statue.getTrappedEntityTypeString());
                            tag.put("IAFStoneStatueNBT", writtenTag);
                            statue.writeCustomDataToNbt(tag);

                            if (!statue.getWorld().isClient())
                                statue.dropStack(statuette, 1);
                        } else if (!statue.getWorld().isClient)
                            statue.dropItem(Blocks.COBBLESTONE, 2 + player.getRandom().nextInt(4));

                        statue.remove(Entity.RemovalReason.KILLED);
                    }
                    return ActionResult.PASS;
                }
            }
        }
        if (entity instanceof EntityMutlipartPart mutlipartPart) {
            Entity parent = mutlipartPart.getParent();
            try {
                //If the attacked entity is the parent itself parent will be null and also doesn't have to be attacked
                if (parent != null)
                    player.attack(parent);
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Exception thrown while interacting with entity.", e);
            }
            int extraData = 0;
            if (mutlipartPart instanceof EntityHydraHead hydraHead && parent instanceof EntityHydra hydra) {
                extraData = hydraHead.headIndex;
                hydra.triggerHeadFlags(extraData);
            }
            if (mutlipartPart.getWorld().isClient && parent != null)
                IafClientNetworkHandler.send(new MessagePlayerHitMultipart(parent.getId(), extraData));
        }
        if (entity instanceof LivingEntity livingEntity) {
            if (entity.getType().isIn(IafTags.CHICKENS)) signalChickenAlarm(livingEntity, player);
            else if (DragonUtils.isVillager(entity)) signalAmphithereAlarm(livingEntity, player);
        }
        return ActionResult.PASS;
    }

    public static void onEntityDie(LivingEntity entity, DamageSource damageSource) {
        EntityDataComponent data = EntityDataComponent.get(entity);
        if (entity.getWorld().isClient) return;

        if (!data.chainData.getChainedTo().isEmpty()) {
            ItemEntity entityitem = new ItemEntity(entity.getWorld(),
                    entity.getX(),
                    entity.getY() + 1,
                    entity.getZ(),
                    new ItemStack(IafItems.CHAIN, data.chainData.getChainedTo().size()));
            entityitem.setToDefaultPickupDelay();
            entity.getWorld().spawnEntity(entityitem);

            data.chainData.clearChains();
        }

        if (entity.getUuid().equals(ServerEvents.ALEX_UUID))
            entity.dropStack(new ItemStack(IafItems.WEEZER_BLUE_ALBUM), 1);

        if (entity instanceof PlayerEntity && IafConfig.getInstance().ghost.fromPlayerDeaths) {
            Entity attacker = entity.getAttacker();
            if (attacker instanceof PlayerEntity && entity.getRandom().nextInt(3) == 0) {
                DamageTracker combat = entity.getDamageTracker();
                DamageRecord entry = combat.getBiggestFall();
                boolean flag = entry != null && (entry.damageSource().isOf(DamageTypes.FALL) || entry.damageSource().isOf(DamageTypes.DROWN) || entry.damageSource().isOf(DamageTypes.LAVA));
                if (entity.hasStatusEffect(StatusEffects.POISON))
                    flag = true;
                if (flag) {
                    World world = entity.getWorld();
                    EntityGhost ghost = IafEntities.GHOST.create(world);
                    assert ghost != null;
                    ghost.copyPositionAndRotation(entity);
                    if (!world.isClient) {
                        ghost.initialize((ServerWorldAccess) world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, null, null);
                        world.spawnEntity(ghost);
                    }
                    ghost.setDaytimeMode(true);
                }
            }
        }
    }

    public static TypedActionResult<ItemStack> onEntityUseItem(PlayerEntity player, World world, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.getX() > 87 && player.getVehicle() != null && player.getVehicle() instanceof EntityDragonBase) {
            ((EntityDragonBase) player.getVehicle()).interactMob(player, hand);
            return TypedActionResult.success(stack, true);
        }
        return TypedActionResult.pass(stack);
/*        if (event.getEntity() instanceof EntityDragonBase && !event.getEntity().isAlive()) {
            event.setResult(Event.Result.DENY);
            ((EntityDragonBase) event.getEntityLiving()).mobInteract(event.getPlayer(), event.getHand());
        }*/
    }

    public static ActionResult onEntityInteract(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        // Handle chain removal
        if (entity instanceof LivingEntity target && !player.isSpectator()) {
            EntityDataComponent data = EntityDataComponent.get(target);
            if (data.chainData.isChainedTo(entity)) {
                data.chainData.removeChain(entity);
                if (!world.isClient)
                    entity.dropItem(IafItems.CHAIN, 1);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    public static ActionResult onPlayerRightClick(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (player != null && (world.getBlockState(pos).getBlock() instanceof AbstractChestBlock) && !player.isCreative()) {
            float dist = IafConfig.getInstance().dragon.behaviour.goldSearchLength;
            final List<Entity> list = world.getOtherEntities(player, player.getBoundingBox().expand(dist, dist, dist));
            if (!list.isEmpty())
                for (final Entity entity : list)
                    if (entity instanceof EntityDragonBase dragon)
                        if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(player)) {
                            dragon.setInSittingPose(false);
                            dragon.setSitting(false);
                            dragon.setTarget(player);
                        }
        }
        if (world.getBlockState(pos).getBlock() instanceof WallBlock)
            ItemChain.attachToFence(player, world, pos);
        return ActionResult.PASS;
    }

    public static void onBreakBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (player != null && (state.getBlock() instanceof AbstractChestBlock || state.isOf(IafBlocks.GOLD_PILE) || state.isOf(IafBlocks.SILVER_PILE) || state.isOf(IafBlocks.COPPER_PILE))) {
            final float dist = IafConfig.getInstance().dragon.behaviour.goldSearchLength;
            List<Entity> list = world.getOtherEntities(player, player.getBoundingBox().expand(dist, dist, dist));
            if (list.isEmpty()) return;

            for (Entity entity : list)
                if (entity instanceof EntityDragonBase dragon)
                    if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(player) && !player.isCreative()) {
                        dragon.setInSittingPose(false);
                        dragon.setSitting(false);
                        dragon.setTarget(player);
                    }
        }
    }

    public static void onPlayerLeaveEvent(PlayerEntity player) {
        if (player != null && !player.getPassengerList().isEmpty())
            for (Entity entity : player.getPassengerList())
                entity.stopRiding();
    }

    public static boolean onEntityJoinWorld(Entity entity, World world) {
        if (entity instanceof MobEntity mob)
            try {
                if (mob.getType().isIn(IafTags.SHEEP) && mob instanceof AnimalEntity animal)
                    animal.goalSelector.add(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
                if (mob.getType().isIn(IafTags.VILLAGERS) && IafConfig.getInstance().dragon.villagersFear)
                    mob.goalSelector.add(1, new VillagerAIFearUntamed((PathAwareEntity) mob, LivingEntity.class, 8.0F, 0.8D, 0.8D, VILLAGER_FEAR));
                if (mob.getType().isIn(IafTags.FEAR_DRAGONS) && IafConfig.getInstance().dragon.animalsFear)
                    mob.goalSelector.add(1, new VillagerAIFearUntamed((PathAwareEntity) mob, LivingEntity.class, 30, 1.0D, 0.5D, e -> e instanceof IAnimalFear fear && fear.shouldAnimalsFear(mob)));
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
            }
        return true;
    }
}
