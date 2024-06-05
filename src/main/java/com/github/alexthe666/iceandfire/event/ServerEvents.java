package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.ai.AiDebug;
import com.github.alexthe666.iceandfire.entity.ai.EntitySheepAIFollowCyclops;
import com.github.alexthe666.iceandfire.entity.ai.VillagerAIFearUntamed;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.item.*;
import com.github.alexthe666.iceandfire.message.MessagePlayerHitMultipart;
import com.github.alexthe666.iceandfire.message.MessageSwingArm;
import com.github.alexthe666.iceandfire.message.MessageSyncPath;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.AbstractPathJob;
import com.github.alexthe666.iceandfire.world.gen.WorldGenFireDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenIceDragonCave;
import com.github.alexthe666.iceandfire.world.gen.WorldGenLightningDragonCave;
import com.iafenvoy.iafextra.event.AttackEntityEvent;
import com.iafenvoy.iafextra.event.Event;
import com.iafenvoy.iafextra.event.ProjectileImpactEvent;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageRecord;
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
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");
    // FIXME :: No check for shouldFear()?
    private static final Predicate<LivingEntity> VILLAGER_FEAR = entity -> entity instanceof IVillagerFear;
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
        return type.isIn(Objects.requireNonNull(Registries.ENTITY_TYPE.tags()).createTagKey(loc));
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

    private static final String[] VILLAGE_TYPES = new String[]{"plains", "desert", "snowy", "savanna", "taiga"};

    @SubscribeEvent
    public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
        if (IafConfig.villagerHouseWeight > 0) {
            Registry<StructurePool> templatePoolRegistry = event.getServer().registryAccess().registry(RegistryKeys.TEMPLATE_POOL).orElseThrow();
            Registry<StructureProcessorList> processorListRegistry = event.getServer().registryAccess().registry(RegistryKeys.PROCESSOR_LIST).orElseThrow();
            for (String type : VILLAGE_TYPES) {
                IafVillagerRegistry.addBuildingToPool(templatePoolRegistry, processorListRegistry, new Identifier("village/" + type + "/houses"), IceAndFire.MOD_ID, "village/" + type + "_scriber_1", IafConfig.villagerHouseWeight);
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
                IceAndFire.NETWORK_WRAPPER.sendToServer(new MessagePlayerHitMultipart(parent.getId(), extraData));
            }
        }
    }

    @SubscribeEvent
    public void onEntityFall(final LivingFallEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            EntityDataProvider.getCapability(event.getEntity()).ifPresent(data -> {
                if (data.miscData.hasDismounted) {
                    event.setDamageMultiplier(0);
                    data.miscData.setDismounted(false);
                }
            });
        }
    }


    @SubscribeEvent
    public void onEntityDamage(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
            float multi = 1;
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemTrollArmor) {
                multi -= 0.3f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemTrollArmor) {
                multi -= 0.2f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemTrollArmor) {
                multi -= 0.1f;
            }
            event.setAmount(event.getAmount() * multi);
        }
        if (event.getSource().is(IafDamageRegistry.DRAGON_FIRE_TYPE) || event.getSource().is(IafDamageRegistry.DRAGON_ICE_TYPE) || event.getSource().is(IafDamageRegistry.DRAGON_LIGHTNING_TYPE)) {
            float multi = 1;
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemScaleArmor ||
                    event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemScaleArmor ||
                    event.getEntity().getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.3f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemScaleArmor ||
                    event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.2f;
            }
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemScaleArmor ||
                    event.getEntity().getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ItemDragonsteelArmor) {
                multi -= 0.1f;
            }
            event.setAmount(event.getAmount() * multi);
        }
    }

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof WitherSkeletonEntity) {
            event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                    new ItemStack(IafItemRegistry.WITHERBONE.get(), event.getEntity().getRandom().nextInt(2))));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void makeItemDropsFireImmune(final LivingDropsEvent event) {
        boolean makeFireImmune = false;

        if (event.getSource().getDirectEntity() instanceof LightningEntity bolt && bolt.getCommandTags().contains(BOLT_DONT_DESTROY_LOOT)) {
            makeFireImmune = true;
        } else if (event.getSource().getEntity() instanceof PlayerEntity player && player.getStackInHand(player.getActiveHand()).isIn(IafItemTags.MAKE_ITEM_DROPS_FIREIMMUNE)) {
            makeFireImmune = true;
        }

        if (makeFireImmune) {
            Set<ItemEntity> fireImmuneDrops = event.getDrops().stream().map(itemEntity -> new ItemEntity(itemEntity.level(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getItem()) {
                @Override
                public boolean fireImmune() {
                    return true;
                }
            }).collect(Collectors.toSet());

            event.getDrops().clear();
            event.getDrops().addAll(fireImmuneDrops);
        }
    }

    @SubscribeEvent
    public void onLivingAttacked(final LivingAttackEvent event) {
        if (event.getSource() != null && event.getSource().getEntity() != null) {
            Entity attacker = event.getSource().getEntity();

            if (attacker instanceof LivingEntity) {
                EntityDataProvider.getCapability(attacker).ifPresent(data -> {
                    if (data.miscData.loveTicks > 0) {
                        event.setCanceled(true);
                    }
                });

                if (isChicken(event.getEntity())) {
                    signalChickenAlarm(event.getEntity(), (LivingEntity) attacker);
                } else if (DragonUtils.isVillager(event.getEntity())) {
                    signalAmphithereAlarm(event.getEntity(), (LivingEntity) attacker);
                }
            }
        }

    }

    @SubscribeEvent
    public void onLivingSetTarget(LivingChangeTargetEvent event) {
        final LivingEntity target = event.getOriginalTarget();
        if (target != null) {
            final LivingEntity attacker = event.getEntity();
            if (isChicken(target)) {
                signalChickenAlarm(target, attacker);
            } else if (DragonUtils.isVillager(target)) {
                signalAmphithereAlarm(target, attacker);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(final AttackEntityEvent event) {
        if (event.getTarget() != null && isSheep(event.getTarget())) {
            float dist = IafConfig.cyclopesSheepSearchLength;
            final List<Entity> list = event.getTarget().level().getEntities(event.getEntity(), event.getEntity().getBoundingBox().expandTowards(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity entity : list) {
                    if (entity instanceof EntityCyclops cyclops) {
                        if (!cyclops.isBlinded() && !event.getEntity().isCreative()) {
                            cyclops.setTarget(event.getEntity());
                        }
                    }
                }
            }
        }

        if (event.getTarget() instanceof EntityStoneStatue statue) {
            statue.setHealth(statue.getMaxHealth());

            if (event.getEntity() != null) {
                ItemStack stack = event.getEntity().getMainHandItem();
                event.getTarget().playSound(SoundEvents.BLOCK_STONE_BREAK, 2, 0.5F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);

                if (stack.getItem().isSuitableFor(Blocks.STONE.getDefaultState()) || stack.getItem().getTranslationKey().contains("pickaxe")) {
                    event.setCanceled(true);
                    statue.setCrackAmount(statue.getCrackAmount() + 1);

                    if (statue.getCrackAmount() > 9) {
                        NbtCompound writtenTag = new NbtCompound();
                        event.getTarget().saveWithoutId(writtenTag);
                        event.getTarget().playSound(SoundEvents.BLOCK_STONE_BREAK, 2, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                        event.getTarget().remove(Entity.RemovalReason.KILLED);

                        if (stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
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
                                statue.dropItem(Blocks.COBBLESTONE.asItem(), 2 + event.getEntity().getRandom().nextInt(4));
                            }
                        }

                        statue.remove(Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityDie(LivingDeathEvent event) {
        EntityDataProvider.getCapability(event.getEntity()).ifPresent(data -> {
            if (event.getEntity().level().isClientSide()) {
                return;
            }

            if (!data.chainData.getChainedTo().isEmpty()) {
                ItemEntity entityitem = new ItemEntity(event.getEntity().level(),
                        event.getEntity().getX(),
                        event.getEntity().getY() + 1,
                        event.getEntity().getZ(),
                        new ItemStack(IafItemRegistry.CHAIN.get(), data.chainData.getChainedTo().size()));
                entityitem.setDefaultPickUpDelay();
                event.getEntity().level().addFreshEntity(entityitem);

                data.chainData.clearChains();
            }
        });

        if (event.getEntity().getUUID().equals(ServerEvents.ALEX_UUID)) {
            event.getEntity().spawnAtLocation(new ItemStack(IafItemRegistry.WEEZER_BLUE_ALBUM.get()), 1);
        }

        if (event.getEntity() instanceof PlayerEntity && IafConfig.ghostsFromPlayerDeaths) {
            Entity attacker = event.getEntity().getLastHurtByMob();
            if (attacker instanceof PlayerEntity && event.getEntity().getRandom().nextInt(3) == 0) {
                DamageTracker combat = event.getEntity().getCombatTracker();
                DamageRecord entry = combat.getBiggestFall();
                boolean flag = entry != null && (entry.damageSource().isOf(DamageTypes.FALL) || entry.damageSource().isOf(DamageTypes.DROWN) || entry.damageSource().isOf(DamageTypes.LAVA));
                if (event.getEntity().hasEffect(StatusEffects.POISON)) {
                    flag = true;
                }
                if (flag) {
                    World world = event.getEntity().level();
                    EntityGhost ghost = IafEntityRegistry.GHOST.get().create(world);
                    ghost.copyPositionAndRotation(event.getEntity());
                    if (!world.isClient) {
                        ghost.initialize((ServerWorldAccess) world, world.getLocalDifficulty(event.getEntity().blockPosition()), SpawnReason.SPAWNER, null, null);
                        world.spawnEntity(ghost);
                    }
                    ghost.setDaytimeMode(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityStopUsingItem(LivingEntityUseItemEvent.Tick event) {
        if (event.getItem().getItem() instanceof ItemDeathwormGauntlet || event.getItem().getItem() instanceof ItemCockatriceScepter) {
            event.setDuration(20);
        }
    }

    @SubscribeEvent
    public void onEntityUseItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() != null && event.getEntity().getXRot() > 87 && event.getEntity().getVehicle() != null && event.getEntity().getVehicle() instanceof EntityDragonBase) {
            ((EntityDragonBase) event.getEntity().getVehicle()).interactMob(event.getEntity(), event.getHand());
        }
/*        if (event.getEntity() instanceof EntityDragonBase && !event.getEntity().isAlive()) {
            event.setResult(Event.Result.DENY);
            ((EntityDragonBase) event.getEntityLiving()).mobInteract(event.getPlayer(), event.getHand());
        }*/
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingTickEvent event) {
        if (AiDebug.isEnabled() && event.getEntity() instanceof MobEntity && AiDebug.contains((MobEntity) event.getEntity())) {
            AiDebug.logData();
        }
    }

    @SubscribeEvent
    public void onEntityInteract(final PlayerInteractEvent.EntityInteract event) {
        // Handle chain removal
        if (event.getTarget() instanceof LivingEntity target) {
            EntityDataProvider.getCapability(target).ifPresent(data -> {
                if (data.chainData.isChainedTo(event.getEntity())) {
                    data.chainData.removeChain(event.getEntity());

                    if (!event.getLevel().isClientSide()) {
                        event.getTarget().spawnAtLocation(IafItemRegistry.CHAIN.get(), 1);
                    }

                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            });
        }

        // Handle debug path render
        if (!event.getLevel().isClientSide() && event.getTarget() instanceof MobEntity && event.getItemStack().getItem() == Items.STICK) {
            if (AiDebug.isEnabled())
                AiDebug.addEntity((MobEntity) event.getTarget());
            if (Pathfinding.isDebug()) {
                if (AbstractPathJob.trackingMap.getOrDefault(event.getEntity(), UUID.randomUUID()).equals(event.getTarget().getUUID())) {
                    AbstractPathJob.trackingMap.remove(event.getEntity());
                    IceAndFire.sendMSGToPlayer(new MessageSyncPath(new HashSet<>(), new HashSet<>(), new HashSet<>()), (ServerPlayerEntity) event.getEntity());
                } else {
                    AbstractPathJob.trackingMap.put(event.getEntity(), event.getTarget().getUUID());
                }
            }
        }
    }

    @SubscribeEvent // TODO :: Can this be moved into the item itself?
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        onLeftClick(event.getEntity(), event.getItemStack());
        if (event.getLevel().isClientSide) {
            IceAndFire.sendMSGToServer(new MessageSwingArm());
        }
    }

    public static void onLeftClick(final PlayerEntity playerEntity, final ItemStack stack) {
        if (stack.getItem() == IafItemRegistry.GHOST_SWORD.get()) {
            ItemGhostSword.spawnGhostSwordEntity(stack, playerEntity);
        }
    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() != null && (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof AbstractChestBlock) && !event.getEntity().isCreative()) {
            float dist = IafConfig.dragonGoldSearchLength;
            final List<Entity> list = event.getLevel().getEntities(event.getEntity(), event.getEntity().getBoundingBox().inflate(dist, dist, dist));
            if (!list.isEmpty()) {
                for (final Entity entity : list) {
                    if (entity instanceof EntityDragonBase dragon) {
                        if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(event.getEntity())) {
                            dragon.setInSittingPose(false);
                            dragon.setSitting(false);
                            dragon.setTarget(event.getEntity());
                        }
                    }
                }
            }
        }
        if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof WallBlock) {
            ItemChain.attachToFence(event.getEntity(), event.getLevel(), event.getPos());
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && (event.getState().getBlock() instanceof AbstractChestBlock || event.getState().getBlock() == IafBlockRegistry.GOLD_PILE.get() || event.getState().getBlock() == IafBlockRegistry.SILVER_PILE.get() || event.getState().getBlock() == IafBlockRegistry.COPPER_PILE.get())) {
            final float dist = IafConfig.dragonGoldSearchLength;
            List<Entity> list = event.getLevel().getEntities(event.getPlayer(), event.getPlayer().getBoundingBox().inflate(dist, dist, dist));
            if (list.isEmpty()) return;

            for (Entity entity : list) {
                if (entity instanceof EntityDragonBase dragon) {
                    if (!dragon.isTamed() && !dragon.isModelDead() && !dragon.isOwner(event.getPlayer()) && !event.getPlayer().isCreative()) {
                        dragon.setInSittingPose(false);
                        dragon.setSitting(false);
                        dragon.setTarget(event.getPlayer());
                    }
                }
            }
        }
    }

    //@SubscribeEvent // FIXME :: Unused
    public static void onChestGenerated(LootTableLoadEvent event) {
        final Identifier eventName = event.getName();
        final boolean condition1 = eventName.equals(LootTables.SIMPLE_DUNGEON_CHEST)
                || eventName.equals(LootTables.ABANDONED_MINESHAFT_CHEST)
                || eventName.equals(LootTables.DESERT_PYRAMID_CHEST)
                || eventName.equals(LootTables.JUNGLE_TEMPLE_CHEST)
                || eventName.equals(LootTables.STRONGHOLD_CORRIDOR_CHEST)
                || eventName.equals(LootTables.STRONGHOLD_CROSSING_CHEST);

        if (condition1 || eventName.equals(LootTables.VILLAGE_CARTOGRAPHER_CHEST)) {
            LootPoolEntry.Builder item = ItemEntry.builder(IafItemRegistry.MANUSCRIPT.get()).setQuality(20).setWeight(5);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_manuscript").add(item).when(RandomChanceLootCondition.builder(0.35f)).setRolls(UniformLootNumberProvider.create(1, 4)).setBonusRolls(UniformLootNumberProvider.create(0, 3));
            event.getTable().addPool(builder.build());
        }
        if (condition1
                || eventName.equals(LootTables.IGLOO_CHEST_CHEST)
                || eventName.equals(LootTables.WOODLAND_MANSION_CHEST)
                || eventName.equals(LootTables.VILLAGE_TOOLSMITH_CHEST)
                || eventName.equals(LootTables.VILLAGE_ARMORER_CHEST)) {


            LootPoolEntry.Builder item = ItemEntry.builder(IafItemRegistry.SILVER_INGOT.get()).setQuality(15).setWeight(12);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_silver_ingot").add(item).when(RandomChanceLootCondition.builder(0.5f)).setRolls(UniformLootNumberProvider.create(1, 3)).setBonusRolls(UniformLootNumberProvider.create(0, 3));
            event.getTable().addPool(builder.build());

        } else if ((event.getName().equals(WorldGenFireDragonCave.FIRE_DRAGON_CHEST)
                || event.getName().equals(WorldGenFireDragonCave.FIRE_DRAGON_CHEST_MALE)
                || event.getName().equals(WorldGenIceDragonCave.ICE_DRAGON_CHEST)
                || event.getName().equals(WorldGenIceDragonCave.ICE_DRAGON_CHEST_MALE)
                || event.getName().equals(WorldGenLightningDragonCave.LIGHTNING_DRAGON_CHEST)
                || event.getName().equals(WorldGenLightningDragonCave.LIGHTNING_DRAGON_CHEST_MALE))) {
            LootPoolEntry.Builder item = ItemEntry.builder(IafItemRegistry.WEEZER_BLUE_ALBUM.get()).setQuality(100).setWeight(1);
            LootPool.Builder builder = new LootPool.Builder().name("iaf_weezer").add(item).when(RandomChanceLootCondition.builder(0.01f)).setRolls(UniformLootNumberProvider.create(1, 1));
            event.getTable().addPool(builder.build());
        }
    }

    @SubscribeEvent
    public void onPlayerLeaveEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() != null && !event.getEntity().getPassengers().isEmpty()) {
            for (Entity entity : event.getEntity().getPassengers()) {
                entity.stopRiding();
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(MobSpawnEvent.FinalizeSpawn event) {
        MobEntity mob = event.getEntity();
        try {
            if (isSheep(mob) && mob instanceof AnimalEntity animal) {
                animal.goalSelector.addGoal(8, new EntitySheepAIFollowCyclops(animal, 1.2D));
            }
            if (isVillager(mob) && IafConfig.villagersFearDragons) {
                mob.goalSelector.addGoal(1, new VillagerAIFearUntamed((PathAwareEntity) mob, LivingEntity.class, 8.0F, 0.8D, 0.8D, VILLAGER_FEAR));
            }
            if (isLivestock(mob) && IafConfig.animalsFearDragons) {
                mob.goalSelector.addGoal(1, new VillagerAIFearUntamed((PathAwareEntity) mob, LivingEntity.class, 30, 1.0D, 0.5D, entity -> entity instanceof IAnimalFear && ((IAnimalFear) entity).shouldAnimalsFear(mob)));
            }
        } catch (Exception e) {
            IceAndFire.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == IafVillagerRegistry.SCRIBE.get()) {
            IafVillagerRegistry.addScribeTrades(event.getTrades());
        }
    }

    public static String BOLT_DONT_DESTROY_LOOT = "iceandfire.bolt_skip_loot";

    @SubscribeEvent
    public void onLightningHit(final EntityStruckByLightningEvent event) {
        if ((event.getEntity() instanceof ItemEntity || event.getEntity() instanceof ExperienceOrbEntity) && event.getLightning().getTags().contains(BOLT_DONT_DESTROY_LOOT)) {
            event.setCanceled(true);
        } else if (event.getLightning().getTags().contains(event.getEntity().getStringUUID())) {
            event.setCanceled(true);
        }
    }
}
