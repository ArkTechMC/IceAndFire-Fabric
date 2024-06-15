package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.github.alexthe666.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.item.*;
import com.github.alexthe666.iceandfire.message.MessagePlayerHitMultipart;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import dev.arktechmc.iafextra.data.EntityDataComponent;
import dev.arktechmc.iafextra.event.AttackEntityEvent;
import dev.arktechmc.iafextra.event.ProjectileImpactEvent;
import dev.arktechmc.iafextra.network.IafClientNetworkHandler;
import dev.arktechmc.iafextra.util.IdUtil;
import dev.arktechmc.iafextra.util.RandomHelper;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingDamageEvent;
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
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
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

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    public static final String BOLT_DONT_DESTROY_LOOT = "iceandfire.bolt_skip_loot";
    // FIXME :: No check for shouldFear()?
    private static final Predicate<LivingEntity> VILLAGER_FEAR = entity -> entity instanceof IVillagerFear;
    private static final String[] VILLAGE_TYPES = new String[]{"plains", "desert", "snowy", "savanna", "taiga"};
    private final Random rand = new Random();

    private static void signalChickenAlarm(LivingEntity chicken, LivingEntity attacker) {
        final float d0 = IafConfig.cockatriceChickenSearchLength;
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
        final float d0 = IafConfig.amphithereVillagerSearchLength;
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

    private static boolean isInEntityTag(Identifier loc, EntityType<?> type) {
        return type.isIn(TagKey.of(RegistryKeys.ENTITY_TYPE, loc));
    }

    public static boolean isLivestock(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.FEAR_DRAGONS, entity.getType());
    }

    public static boolean isVillager(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.VILLAGERS, entity.getType());
    }

    public static boolean isSheep(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.SHEEP, entity.getType());
    }

    public static boolean isChicken(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.CHICKENS, entity.getType());
    }

    public static boolean isCockatriceTarget(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.COCKATRICE_TARGETS, entity.getType());
    }

    public static boolean doesScareCockatrice(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.SCARES_COCKATRICES, entity.getType());
    }

    public static boolean isBlindMob(Entity entity) {
        return entity != null && isInEntityTag(IafTagRegistry.BLINDED, entity.getType());
    }

    public static boolean isRidingOrBeingRiddenBy(final Entity first, final Entity entityIn) {
        if (first == null || entityIn == null) {
            return false;
        }

        for (final Entity entity : first.getPassengerList()) {
            if (entity.equals(entityIn) || isRidingOrBeingRiddenBy(entity, entityIn)) {
                return true;
            }
        }

        return false;
    }

    public static void onArrowCollide(final ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult result) {
            Entity shotEntity = result.getEntity();

            if (shotEntity instanceof EntityGhost) {
                event.cancel();
            } else if (event.getProjectile() instanceof PersistentProjectileEntity arrow && arrow.getOwner() != null) {
                Entity shootingEntity = arrow.getOwner();

                if (shootingEntity instanceof LivingEntity && isRidingOrBeingRiddenBy(shootingEntity, shotEntity)) {
                    if (shotEntity instanceof TameableEntity tamable && tamable.isTamed() && shotEntity.isTeammate(shootingEntity)) {
                        event.cancel();
                    }
                }
            }
        }
    }

    public static void addNewVillageBuilding(MinecraftServer server) {
        if (IafConfig.villagerHouseWeight > 0) {
            Registry<StructurePool> templatePoolRegistry = server.getRegistryManager().get(RegistryKeys.TEMPLATE_POOL);
            Registry<StructureProcessorList> processorListRegistry = server.getRegistryManager().get(RegistryKeys.PROCESSOR_LIST);
            for (String type : VILLAGE_TYPES) {
                IafVillagerRegistry.addBuildingToPool(templatePoolRegistry, processorListRegistry, new Identifier("village/" + type + "/houses"), IdUtil.build(IceAndFire.MOD_ID, "village/" + type + "_scriber_1"), IafConfig.villagerHouseWeight);
            }
        }

    }

    public static void onPlayerAttackMob(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityMutlipartPart && event.getEntity() instanceof PlayerEntity) {
            event.cancel();
            Entity parent = ((EntityMutlipartPart) event.getTarget()).getParent();
            try {
                //If the attacked entity is the parent itself parent will be null and also doesn't have to be attacked
                if (parent != null)
                    ((PlayerEntity) event.getEntity()).attack(parent);
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Exception thrown while interacting with entity.", e);
            }
            int extraData = 0;
            if (event.getTarget() instanceof EntityHydraHead && parent instanceof EntityHydra) {
                extraData = ((EntityHydraHead) event.getTarget()).headIndex;
                ((EntityHydra) parent).triggerHeadFlags(extraData);
            }
            if (event.getTarget().getWorld().isClient && parent != null) {
                IafClientNetworkHandler.send(new MessagePlayerHitMultipart(parent.getId(), extraData));
            }
        }
    }

    public static void onEntityFall(LivingEntityEvents.Fall.FallEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            EntityDataComponent data = EntityDataComponent.ENTITY_DATA_COMPONENT.get(event.getEntity());
            if (data.miscData.hasDismounted) {
                event.setDamageMultiplier(0);
                data.miscData.setDismounted(false);
            }
        }
    }

    public static void onEntityDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();
        if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            float multi = 1;
            if (entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1f;
            }
            if (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ItemTrollArmor) {
                multi -= 0.3f;
            }
            if (entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof ItemTrollArmor) {
                multi -= 0.2f;
            }
            if (entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1f;
            }
            event.setAmount(event.getAmount() * multi);
        }
        if (source.isOf(IafDamageRegistry.DRAGON_FIRE_TYPE) || source.isOf(IafDamageRegistry.DRAGON_ICE_TYPE) || source.isOf(IafDamageRegistry.DRAGON_LIGHTNING_TYPE)) {
            float multi = 1;
            if (entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ItemScaleArmor ||
                    entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1f;
            }
            if (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ItemScaleArmor ||
                    entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.3f;
            }
            if (entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof ItemScaleArmor ||
                    entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.2f;
            }
            if (entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof ItemScaleArmor ||
                    entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1f;
            }
            event.setAmount(event.getAmount() * multi);
        }
    }

//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public void makeItemDropsFireImmune(final LivingDropsEvent event) {
//        boolean makeFireImmune = false;
//
//        if (event.getSource().getDirectEntity() instanceof LightningEntity bolt && bolt.getCommandTags().contains(BOLT_DONT_DESTROY_LOOT)) {
//            makeFireImmune = true;
//        } else if (event.getSource().getEntity() instanceof PlayerEntity player && player.getStackInHand(player.getActiveHand()).isIn(IafItemTags.MAKE_ITEM_DROPS_FIREIMMUNE)) {
//            makeFireImmune = true;
//        }
//
//        if (makeFireImmune) {
//            Set<ItemEntity> fireImmuneDrops = event.getDrops().stream().map(itemEntity -> new ItemEntity(itemEntity.level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getItem()) {
//                @Override
//                public boolean fireImmune() {
//                    return true;
//                }
//            }).collect(Collectors.toSet());
//
//            event.getDrops().clear();
//            event.getDrops().addAll(fireImmuneDrops);
//        }
//    }

    public static boolean onEntityDrop(LivingEntity target, DamageSource source, Collection<ItemEntity> drops, int lootingLevel, boolean recentlyHit) {
        if (target instanceof WitherSkeletonEntity) {
            drops.add(new ItemEntity(target.getWorld(), target.getX(), target.getY(), target.getZ(), new ItemStack(IafItemRegistry.WITHERBONE.get(), target.getRandom().nextInt(2))));
        }
        return true;
    }

    public static void onLivingAttacked(final AttackEntityEvent event) {
        if (event.getEntity() != null) {
            LivingEntity attacker = event.getEntity();

            if (attacker instanceof LivingEntity) {
                EntityDataComponent data = EntityDataComponent.ENTITY_DATA_COMPONENT.get(attacker);
                if (data.miscData.loveTicks > 0) {
                    event.cancel();
                }

                if (isChicken(event.getEntity())) {
                    signalChickenAlarm(event.getEntity(), attacker);
                } else if (DragonUtils.isVillager(event.getEntity())) {
                    signalAmphithereAlarm(event.getEntity(), attacker);
                }
            }
        }

    }

    public static void onLivingSetTarget(Entity tracking, ServerPlayerEntity player) {
        if (tracking instanceof LivingEntity target) {
            if (isChicken(target)) {
                signalChickenAlarm(target, player);
            } else if (DragonUtils.isVillager(target)) {
                signalAmphithereAlarm(target, player);
            }
        }
    }

    public static ActionResult onPlayerAttack(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (isSheep(entity)) {
            float dist = IafConfig.cyclopesSheepSearchLength;
            final List<Entity> list = entity.getWorld().getOtherEntities(entity, entity.getBoundingBox().expand(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity e : list) {
                    if (e instanceof EntityCyclops cyclops) {
                        if (!cyclops.isBlinded() && !player.isCreative()) {
                            cyclops.setTarget(player);
                        }
                    }
                }
            }
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
                            ItemStack statuette = new ItemStack(IafItemRegistry.STONE_STATUE.get());
                            NbtCompound tag = statuette.getOrCreateNbt();
                            tag.putBoolean("IAFStoneStatuePlayerEntity", statue.getTrappedEntityTypeString().equalsIgnoreCase("minecraft:player"));
                            tag.putString("IAFStoneStatueEntityID", statue.getTrappedEntityTypeString());
                            tag.put("IAFStoneStatueNBT", writtenTag);
                            statue.writeCustomDataToNbt(tag);

                            if (!statue.getWorld().isClient()) {
                                statue.dropStack(statuette, 1);
                            }
                        } else {
                            if (!statue.getWorld().isClient) {
                                statue.dropItem(Blocks.COBBLESTONE.asItem(), 2 + player.getRandom().nextInt(4));
                            }
                        }

                        statue.remove(Entity.RemovalReason.KILLED);
                    }
                    return ActionResult.PASS;
                }
            }
        }
        return ActionResult.PASS;
    }

    public static void onEntityDie(LivingEntity entity, DamageSource damageSource) {
        EntityDataComponent data = EntityDataComponent.ENTITY_DATA_COMPONENT.get(entity);
        if (entity.getWorld().isClient) {
            return;
        }

        if (!data.chainData.getChainedTo().isEmpty()) {
            ItemEntity entityitem = new ItemEntity(entity.getWorld(),
                    entity.getX(),
                    entity.getY() + 1,
                    entity.getZ(),
                    new ItemStack(IafItemRegistry.CHAIN.get(), data.chainData.getChainedTo().size()));
            entityitem.setToDefaultPickupDelay();
            entity.getWorld().spawnEntity(entityitem);

            data.chainData.clearChains();
        }

        if (entity.getUuid().equals(ServerEvents.ALEX_UUID)) {
            entity.dropStack(new ItemStack(IafItemRegistry.WEEZER_BLUE_ALBUM.get()), 1);
        }

        if (entity instanceof PlayerEntity && IafConfig.ghostsFromPlayerDeaths) {
            Entity attacker = entity.getAttacker();
            if (attacker instanceof PlayerEntity && entity.getRandom().nextInt(3) == 0) {
                DamageTracker combat = entity.getDamageTracker();
                DamageRecord entry = combat.getBiggestFall();
                boolean flag = entry != null && (entry.damageSource().isOf(DamageTypes.FALL) || entry.damageSource().isOf(DamageTypes.DROWN) || entry.damageSource().isOf(DamageTypes.LAVA));
                if (entity.hasStatusEffect(StatusEffects.POISON)) {
                    flag = true;
                }
                if (flag) {
                    World world = entity.getWorld();
                    EntityGhost ghost = IafEntityRegistry.GHOST.get().create(world);
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
            EntityDataComponent data = EntityDataComponent.ENTITY_DATA_COMPONENT.get(target);
            if (data.chainData.isChainedTo(entity)) {
                data.chainData.removeChain(entity);
                if (!world.isClient)
                    entity.dropItem(IafItemRegistry.CHAIN.get(), 1);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    public static ActionResult onPlayerRightClick(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (player != null && (world.getBlockState(pos).getBlock() instanceof AbstractChestBlock) && !player.isCreative()) {
            float dist = IafConfig.dragonGoldSearchLength;
            final List<Entity> list = world.getOtherEntities(player, player.getBoundingBox().expand(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity entity : list) {
                    if (entity instanceof EntityDragonBase dragon) {
                        if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(player)) {
                            dragon.setInSittingPose(false);
                            dragon.setSitting(false);
                            dragon.setTarget(player);
                        }
                    }
                }
            }
        }
        if (world.getBlockState(pos).getBlock() instanceof WallBlock) {
            ItemChain.attachToFence(player, world, pos);
        }

        return ActionResult.PASS;
    }

    public static void onBreakBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (player != null && (state.getBlock() instanceof AbstractChestBlock || state.isOf(IafBlockRegistry.GOLD_PILE.get()) || state.isOf(IafBlockRegistry.SILVER_PILE.get()) || state.isOf(IafBlockRegistry.COPPER_PILE.get()))) {
            final float dist = IafConfig.dragonGoldSearchLength;
            List<Entity> list = world.getOtherEntities(player, player.getBoundingBox().expand(dist, dist, dist));
            if (list.isEmpty()) return;

            for (Entity entity : list) {
                if (entity instanceof EntityDragonBase dragon) {
                    if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(player) && !player.isCreative()) {
                        dragon.setInSittingPose(false);
                        dragon.setSitting(false);
                        dragon.setTarget(player);
                    }
                }
            }
        }
    }

    public static void onPlayerLeaveEvent(PlayerEntity player) {
        if (player != null && !player.getPassengerList().isEmpty()) {
            for (Entity entity : player.getPassengerList()) {
                entity.stopRiding();
            }
        }
    }

    public static boolean onEntityJoinWorld(Entity entity, World world, boolean loadedFromDisk) {
        if (entity instanceof MobEntity mob)
            try {
                if (isSheep(mob) && mob instanceof AnimalEntity animal) {
                    animal.goalSelector.add(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
                }
                if (isVillager(mob) && IafConfig.villagersFearDragons) {
                    mob.goalSelector.add(1, new VillagerAIFearUntamed((PathAwareEntity) mob, LivingEntity.class, 8.0F, 0.8D, 0.8D, VILLAGER_FEAR));
                }
                if (isLivestock(mob) && IafConfig.animalsFearDragons) {
                    mob.goalSelector.add(1, new VillagerAIFearUntamed((PathAwareEntity) mob, LivingEntity.class, 30, 1.0D, 0.5D, e -> e instanceof IAnimalFear fear && fear.shouldAnimalsFear(mob)));
                }
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
            }
        return true;
    }
}
