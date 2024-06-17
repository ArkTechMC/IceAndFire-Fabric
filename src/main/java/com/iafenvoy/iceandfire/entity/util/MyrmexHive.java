package com.iafenvoy.iceandfire.entity.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iafenvoy.iceandfire.IafConfig;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;
import com.iafenvoy.iceandfire.entity.EntityMyrmexQueen;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.world.gen.WorldGenMyrmexHive;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;

public class MyrmexHive {
    private final List<BlockPos> foodRooms = Lists.newArrayList();
    private final List<BlockPos> babyRooms = Lists.newArrayList();
    private final List<BlockPos> miscRooms = Lists.newArrayList();
    private final List<BlockPos> allRooms = Lists.newArrayList();
    private final Map<BlockPos, Direction> entrances = Maps.newHashMap();
    private final Map<BlockPos, Direction> entranceBottoms = Maps.newHashMap();
    private final Map<UUID, Integer> playerReputation = Maps.newHashMap();
    private final List<HiveAggressor> villageAgressors = Lists.newArrayList();
    private final List<UUID> myrmexList = Lists.newArrayList();
    public UUID hiveUUID;
    public String colonyName = "";
    public boolean reproduces = true;
    public boolean hasOwner = false;
    public UUID ownerUUID = null;
    private World world;
    private BlockPos centerHelper = BlockPos.ORIGIN;
    private BlockPos center = BlockPos.ORIGIN;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numMyrmex;
    private int noBreedTicks;
    private int wanderRadius = 16;

    public MyrmexHive() {
        this.hiveUUID = UUID.randomUUID();
    }

    public MyrmexHive(World worldIn) {
        this.world = worldIn;
        this.hiveUUID = UUID.randomUUID();
    }

    public MyrmexHive(World worldIn, BlockPos center, int radius) {
        this.world = worldIn;
        this.center = center;
        this.villageRadius = radius;
        this.hiveUUID = UUID.randomUUID();
    }

    public static BlockPos getGroundedPos(WorldAccess world, BlockPos pos) {
        BlockPos current = pos;
        while (world.isAir(current.down()) && current.getY() > 0)
            current = current.down();
        return current;
    }

    public static MyrmexHive fromNBT(NbtCompound hive) {
        MyrmexHive hive1 = new MyrmexHive();
        hive1.readVillageDataFromNBT(hive);
        return hive1;
    }

    public NbtCompound toNBT() {
        NbtCompound tag = new NbtCompound();
        this.writeVillageDataToNBT(tag);
        return tag;
    }

    public void setWorld(World worldIn) {
        this.world = worldIn;
    }

    public void tick(int tickCounterIn, World world) {
        this.tickCounter++;
        this.removeDeadAndOldAgressors();
        if (this.tickCounter % 20 == 0)
            this.updateNumMyrmex(world);
    }

    private void updateNumMyrmex(World world) {
        this.numMyrmex = this.myrmexList.size();
        if (this.numMyrmex == 0)
            this.playerReputation.clear();
    }

    public EntityMyrmexQueen getQueen() {
        List<EntityMyrmexQueen> ourQueens = new ArrayList<>();
        if (!this.world.isClient) {
            assert this.world.getServer() != null;
            ServerWorld serverWorld = this.world.getServer().getWorld(this.world.getRegistryKey());
            assert serverWorld != null;
            List<? extends EntityMyrmexQueen> allQueens = serverWorld.getEntitiesByType(IafEntities.MYRMEX_QUEEN, EntityPredicates.EXCEPT_SPECTATOR);
            for (EntityMyrmexQueen queen : allQueens)
                if (queen instanceof EntityMyrmexQueen && queen.getHive().equals(this))
                    ourQueens.add(queen);
        }

        return ourQueens.isEmpty() ? null : ourQueens.get(0);
    }

    public BlockPos getCenter() {
        return this.center;
    }

    public BlockPos getCenterGround() {
        return getGroundedPos(this.world, this.center);
    }

    public int getVillageRadius() {
        return this.villageRadius;
    }

    public int getNumMyrmex() {
        return this.numMyrmex;
    }

    public int getWanderRadius() {
        return this.wanderRadius;
    }

    public void setWanderRadius(int wanderRadius) {
        this.wanderRadius = Math.min(wanderRadius, IafConfig.myrmexMaximumWanderRadius);
    }

    public boolean isBlockPosWithinSqVillageRadius(BlockPos pos) {
        return this.center.getSquaredDistance(pos) < this.villageRadius * this.villageRadius;
    }

    public boolean isAnnihilated() {
        return false;
    }

    public void addOrRenewAgressor(LivingEntity LivingEntityIn, int agressiveLevel) {
        for (HiveAggressor hiveAggressor : this.villageAgressors)
            if (hiveAggressor.agressor == LivingEntityIn) {
                hiveAggressor.agressionTime = this.tickCounter;
                return;
            }

        this.villageAgressors.add(new HiveAggressor(LivingEntityIn, this.tickCounter, agressiveLevel));
    }

    public LivingEntity findNearestVillageAggressor(LivingEntity LivingEntityIn) {
        double d0 = Double.MAX_VALUE;
        int previousAgressionLevel = 0;
        HiveAggressor hiveAggressor = null;
        for (HiveAggressor hive$villageaggressor1 : this.villageAgressors) {
            double d1 = hive$villageaggressor1.agressor.squaredDistanceTo(LivingEntityIn);
            int agressionLevel = hive$villageaggressor1.agressionLevel;

            if (d1 <= d0 || agressionLevel > previousAgressionLevel) {
                hiveAggressor = hive$villageaggressor1;
                d0 = d1;
            }
            previousAgressionLevel = agressionLevel;
        }

        return hiveAggressor == null ? null : hiveAggressor.agressor;
    }

    public PlayerEntity getNearestTargetPlayer(LivingEntity villageDefender, World world) {
        double d0 = Double.MAX_VALUE;
        PlayerEntity PlayerEntity = null;
        for (UUID s : this.playerReputation.keySet()) {
            if (this.isPlayerReputationLowEnoughToFight(s)) {
                PlayerEntity PlayerEntity1 = world.getPlayerByUuid(s);
                if (PlayerEntity1 != null) {
                    double d1 = PlayerEntity1.squaredDistanceTo(villageDefender);
                    if (d1 <= d0) {
                        PlayerEntity = PlayerEntity1;
                        d0 = d1;
                    }
                }
            }
        }
        return PlayerEntity;
    }

    private void removeDeadAndOldAgressors() {
        this.villageAgressors.removeIf(hive$villageaggressor -> !hive$villageaggressor.agressor.isAlive() || Math.abs(this.tickCounter - hive$villageaggressor.agressionTime) > 300);
    }

    public int getPlayerReputation(UUID playerName) {
        Integer integer = this.playerReputation.get(playerName);
        return integer == null ? 0 : integer;
    }

    private UUID findUUID(String name) {
        if (this.world == null || this.world.getServer() == null)
            return Uuids.getOfflinePlayerUuid(name);
        Optional<GameProfile> profile = this.world.getServer().getUserCache().findByName(name);
        return profile.isPresent() ? Uuids.getUuidFromProfile(profile.get()) : Uuids.getOfflinePlayerUuid(name);
    }

    public void modifyPlayerReputation(UUID playerName, int reputation) {
        int i = this.getPlayerReputation(playerName);
        int j = MathHelper.clamp(i + reputation, 0, 100);
        if (this.hasOwner && playerName.equals(this.ownerUUID))
            j = 100;
        PlayerEntity player = null;
        try {
            player = this.world.getPlayerByUuid(playerName);
        } catch (Exception e) {
            IceAndFire.LOGGER.warn("Myrmex Hive could not find player with associated UUID");
        }
        if (player != null) {
            if (j - i != 0)
                player.sendMessage(Text.translatable(j - i >= 0 ? "myrmex.message.raised_reputation" : "myrmex.message.lowered_reputation", Math.abs(j - i), j), true);
            if (i < 25 && j >= 25)
                player.sendMessage(Text.translatable("myrmex.message.peaceful"), false);
            if (i >= 25 && j < 25)
                player.sendMessage(Text.translatable("myrmex.message.hostile"), false);
            if (i < 50 && j >= 50)
                player.sendMessage(Text.translatable("myrmex.message.trade"), false);
            if (i >= 50 && j < 50)
                player.sendMessage(Text.translatable("myrmex.message.no_trade"), false);
            if (i < 75 && j >= 75)
                player.sendMessage(Text.translatable("myrmex.message.can_use_staff"), false);
            if (i >= 75 && j < 75)
                player.sendMessage(Text.translatable("myrmex.message.cant_use_staff"), false);
        }

        this.playerReputation.put(playerName, j);
    }

    public boolean isPlayerReputationTooLowToTrade(UUID uuid) {
        return this.getPlayerReputation(uuid) < 50;
    }

    public boolean canPlayerCommandHive(UUID uuid) {
        return this.getPlayerReputation(uuid) >= 75;
    }

    public boolean isPlayerReputationLowEnoughToFight(UUID uuid) {
        return this.getPlayerReputation(uuid) < 25;
    }

    /**
     * Read this village's data from NBT.
     */
    public void readVillageDataFromNBT(NbtCompound compound) {
        this.numMyrmex = compound.getInt("PopSize");
        this.reproduces = compound.getBoolean("Reproduces");
        this.hasOwner = compound.getBoolean("HasOwner");
        if (compound.containsUuid("OwnerUUID"))
            this.ownerUUID = compound.getUuid("OwnerUUID");
        this.colonyName = compound.getString("ColonyName");
        this.villageRadius = compound.getInt("Radius");
        if (compound.containsUuid("WanderRadius"))
            this.wanderRadius = compound.getInt("WanderRadius");
        this.lastAddDoorTimestamp = compound.getInt("Stable");
        this.tickCounter = compound.getInt("Tick");
        this.noBreedTicks = compound.getInt("MTick");
        this.center = new BlockPos(compound.getInt("CX"), compound.getInt("CY"), compound.getInt("CZ"));
        this.centerHelper = new BlockPos(compound.getInt("ACX"), compound.getInt("ACY"), compound.getInt("ACZ"));
        NbtList hiveMembers = compound.getList("HiveMembers", 10);
        this.myrmexList.clear();
        for (int i = 0; i < hiveMembers.size(); ++i) {
            NbtCompound CompoundNBT = hiveMembers.getCompound(i);
            this.myrmexList.add(CompoundNBT.getUuid("MyrmexUUID"));
        }
        NbtList foodRoomList = compound.getList("FoodRooms", 10);
        this.foodRooms.clear();
        for (int i = 0; i < foodRoomList.size(); ++i) {
            NbtCompound CompoundNBT = foodRoomList.getCompound(i);
            this.foodRooms.add(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")));
        }
        NbtList babyRoomList = compound.getList("BabyRooms", 10);
        this.babyRooms.clear();
        for (int i = 0; i < babyRoomList.size(); ++i) {
            NbtCompound CompoundNBT = babyRoomList.getCompound(i);
            this.babyRooms.add(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")));
        }
        NbtList miscRoomList = compound.getList("MiscRooms", 10);
        this.miscRooms.clear();
        for (int i = 0; i < miscRoomList.size(); ++i) {
            NbtCompound CompoundNBT = miscRoomList.getCompound(i);
            this.miscRooms.add(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")));
        }
        NbtList entrancesList = compound.getList("Entrances", 10);
        this.entrances.clear();
        for (int i = 0; i < entrancesList.size(); ++i) {
            NbtCompound CompoundNBT = entrancesList.getCompound(i);
            this.entrances.put(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")), Direction.fromHorizontal(CompoundNBT.getInt("Facing")));
        }

        NbtList entranceBottomsList = compound.getList("EntranceBottoms", 10);
        this.entranceBottoms.clear();
        for (int i = 0; i < entranceBottomsList.size(); ++i) {
            NbtCompound CompoundNBT = entranceBottomsList.getCompound(i);
            this.entranceBottoms.put(new BlockPos(CompoundNBT.getInt("X"), CompoundNBT.getInt("Y"), CompoundNBT.getInt("Z")), Direction.fromHorizontal(CompoundNBT.getInt("Facing")));
        }
        this.hiveUUID = compound.getUuid("HiveUUID");
        NbtList nbttaglist1 = compound.getList("Players", 10);
        this.playerReputation.clear();
        for (int j = 0; j < nbttaglist1.size(); ++j) {
            NbtCompound CompoundNBT1 = nbttaglist1.getCompound(j);
            if (CompoundNBT1.containsUuid("UUID"))
                this.playerReputation.put(CompoundNBT1.getUuid("UUID"), CompoundNBT1.getInt("S"));
            else
                //World is never set here, so this will always be offline UUIDs, sadly there is no way to convert this.
                this.playerReputation.put(this.findUUID(CompoundNBT1.getString("Name")), CompoundNBT1.getInt("S"));
        }
    }

    /**
     * Write this village's data to NBT.
     */
    public void writeVillageDataToNBT(NbtCompound compound) {
        compound.putInt("PopSize", this.numMyrmex);
        compound.putBoolean("Reproduces", this.reproduces);
        compound.putBoolean("HasOwner", this.hasOwner);
        if (this.ownerUUID != null)
            compound.putUuid("OwnerUUID", this.ownerUUID);
        compound.putString("ColonyName", this.colonyName);
        compound.putInt("Radius", this.villageRadius);
        compound.putInt("WanderRadius", this.wanderRadius);
        compound.putInt("Stable", this.lastAddDoorTimestamp);
        compound.putInt("Tick", this.tickCounter);
        compound.putInt("MTick", this.noBreedTicks);
        compound.putInt("CX", this.center.getX());
        compound.putInt("CY", this.center.getY());
        compound.putInt("CZ", this.center.getZ());
        compound.putInt("ACX", this.centerHelper.getX());
        compound.putInt("ACY", this.centerHelper.getY());
        compound.putInt("ACZ", this.centerHelper.getZ());
        NbtList hiveMembers = new NbtList();
        for (UUID memberUUID : this.myrmexList) {
            NbtCompound CompoundNBT = new NbtCompound();
            CompoundNBT.putUuid("MyrmexUUID", memberUUID);
            hiveMembers.add(CompoundNBT);
        }
        compound.put("HiveMembers", hiveMembers);
        NbtList foodRoomList = new NbtList();
        for (BlockPos pos : this.foodRooms) {
            NbtCompound CompoundNBT = new NbtCompound();
            CompoundNBT.putInt("X", pos.getX());
            CompoundNBT.putInt("Y", pos.getY());
            CompoundNBT.putInt("Z", pos.getZ());
            foodRoomList.add(CompoundNBT);
        }
        compound.put("FoodRooms", foodRoomList);
        NbtList babyRoomList = new NbtList();
        for (BlockPos pos : this.babyRooms) {
            NbtCompound CompoundNBT = new NbtCompound();
            CompoundNBT.putInt("X", pos.getX());
            CompoundNBT.putInt("Y", pos.getY());
            CompoundNBT.putInt("Z", pos.getZ());
            babyRoomList.add(CompoundNBT);
        }
        compound.put("BabyRooms", babyRoomList);
        NbtList miscRoomList = new NbtList();
        for (BlockPos pos : this.miscRooms) {
            NbtCompound CompoundNBT = new NbtCompound();
            CompoundNBT.putInt("X", pos.getX());
            CompoundNBT.putInt("Y", pos.getY());
            CompoundNBT.putInt("Z", pos.getZ());
            miscRoomList.add(CompoundNBT);
        }
        compound.put("MiscRooms", miscRoomList);
        NbtList entrancesList = new NbtList();
        for (Map.Entry<BlockPos, Direction> entry : this.entrances.entrySet()) {
            NbtCompound CompoundNBT = new NbtCompound();
            CompoundNBT.putInt("X", entry.getKey().getX());
            CompoundNBT.putInt("Y", entry.getKey().getY());
            CompoundNBT.putInt("Z", entry.getKey().getZ());
            CompoundNBT.putInt("Facing", entry.getValue().getHorizontal());
            entrancesList.add(CompoundNBT);
        }
        compound.put("Entrances", entrancesList);

        NbtList entranceBottomsList = new NbtList();
        for (Map.Entry<BlockPos, Direction> entry : this.entranceBottoms.entrySet()) {
            NbtCompound CompoundNBT = new NbtCompound();
            CompoundNBT.putInt("X", entry.getKey().getX());
            CompoundNBT.putInt("Y", entry.getKey().getY());
            CompoundNBT.putInt("Z", entry.getKey().getZ());
            CompoundNBT.putInt("Facing", entry.getValue().getHorizontal());
            entranceBottomsList.add(CompoundNBT);
        }
        compound.put("EntranceBottoms", entranceBottomsList);
        compound.putUuid("HiveUUID", this.hiveUUID);
        NbtList nbttaglist1 = new NbtList();

        for (UUID s : this.playerReputation.keySet()) {
            NbtCompound CompoundNBT1 = new NbtCompound();
            try {
                CompoundNBT1.putUuid("UUID", s);
                CompoundNBT1.putInt("S", this.playerReputation.get(s));
                nbttaglist1.add(CompoundNBT1);
            } catch (RuntimeException ignored) {
            }
        }

        compound.put("Players", nbttaglist1);
    }

    public void addRoom(BlockPos center, WorldGenMyrmexHive.RoomType roomType) {
        if (roomType == WorldGenMyrmexHive.RoomType.FOOD && !this.foodRooms.contains(center))
            this.foodRooms.add(center);
        else if (roomType == WorldGenMyrmexHive.RoomType.NURSERY && !this.babyRooms.contains(center))
            this.babyRooms.add(center);
        else if (!this.miscRooms.contains(center) && !this.miscRooms.contains(center))
            this.miscRooms.add(center);
        this.allRooms.add(center);
    }

    public void addRoomWithMessage(PlayerEntity player, BlockPos center, WorldGenMyrmexHive.RoomType roomType) {
        List<BlockPos> allCurrentRooms = new ArrayList<>(this.getAllRooms());
        allCurrentRooms.addAll(this.getEntrances().keySet());
        allCurrentRooms.addAll(this.getEntranceBottoms().keySet());
        if (roomType == WorldGenMyrmexHive.RoomType.FOOD) {
            if (!this.foodRooms.contains(center) && !allCurrentRooms.contains(center)) {
                this.foodRooms.add(center);
                player.sendMessage(Text.translatable("myrmex.message.added_food_room", center.getX(), center.getY(), center.getZ()), false);
            } else
                player.sendMessage(Text.translatable("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
        } else if (roomType == WorldGenMyrmexHive.RoomType.NURSERY) {
            if (!this.babyRooms.contains(center) && !allCurrentRooms.contains(center)) {
                this.babyRooms.add(center);
                player.sendMessage(Text.translatable("myrmex.message.added_nursery_room", center.getX(), center.getY(), center.getZ()), false);
            } else
                player.sendMessage(Text.translatable("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
        } else if (!this.miscRooms.contains(center) && !allCurrentRooms.contains(center)) {
            this.miscRooms.add(center);
            player.sendMessage(Text.translatable("myrmex.message.added_misc_room", center.getX(), center.getY(), center.getZ()), false);
        } else
            player.sendMessage(Text.translatable("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
    }

    public void addEnteranceWithMessage(PlayerEntity player, boolean bottom, BlockPos center, Direction facing) {
        List<BlockPos> allCurrentRooms = new ArrayList<>(this.getAllRooms());
        allCurrentRooms.addAll(this.getEntrances().keySet());
        allCurrentRooms.addAll(this.getEntranceBottoms().keySet());
        if (bottom) {
            if (allCurrentRooms.contains(center))
                player.sendMessage(Text.translatable("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
            else {
                this.getEntranceBottoms().put(center, facing);
                player.sendMessage(Text.translatable("myrmex.message.added_enterance_bottom", center.getX(), center.getY(), center.getZ()), false);
            }
        } else {
            if (allCurrentRooms.contains(center))
                player.sendMessage(Text.translatable("myrmex.message.dupe_room", center.getX(), center.getY(), center.getZ()), false);
            else {
                this.getEntrances().put(center, facing);
                player.sendMessage(Text.translatable("myrmex.message.added_enterance_surface", center.getX(), center.getY(), center.getZ()), false);
            }
        }
    }

    public List<BlockPos> getRooms(WorldGenMyrmexHive.RoomType roomType) {
        if (roomType == WorldGenMyrmexHive.RoomType.FOOD)
            return this.foodRooms;
        else if (roomType == WorldGenMyrmexHive.RoomType.NURSERY)
            return this.babyRooms;
        else
            return this.miscRooms;
    }

    public List<BlockPos> getAllRooms() {
        this.allRooms.clear();
        this.allRooms.add(this.center);
        this.allRooms.addAll(this.foodRooms);
        this.allRooms.addAll(this.babyRooms);
        this.allRooms.addAll(this.miscRooms);
        return this.allRooms;
    }

    public BlockPos getRandomRoom(Random random, BlockPos returnPos) {
        List<BlockPos> rooms = this.getAllRooms();
        return rooms.isEmpty() ? returnPos : rooms.get(random.nextInt(Math.max(rooms.size() - 1, 1)));
    }

    public BlockPos getRandomRoom(WorldGenMyrmexHive.RoomType roomType, Random random, BlockPos returnPos) {
        List<BlockPos> rooms = this.getRooms(roomType);
        return rooms.isEmpty() ? returnPos : rooms.get(random.nextInt(Math.max(rooms.size() - 1, 1)));
    }

    public BlockPos getClosestEntranceToEntity(Entity entity, Random random, boolean randomize) {
        Map.Entry<BlockPos, Direction> closest = this.getClosestEntrance(entity);
        if (closest != null) {
            if (randomize) {
                BlockPos pos = closest.getKey().offset(closest.getValue(), random.nextInt(7) + 7).up(4);
                return pos.add(10 - random.nextInt(20), 0, 10 - random.nextInt(20));
            } else
                return closest.getKey().offset(closest.getValue(), 3);
        }
        return entity.getBlockPos();
    }

    public BlockPos getClosestEntranceBottomToEntity(Entity entity, Random random) {
        Map.Entry<BlockPos, Direction> closest = null;
        for (Map.Entry<BlockPos, Direction> entry : this.entranceBottoms.entrySet()) {
            Vec3i vec = new Vec3i(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
            if (closest == null || closest.getKey().getSquaredDistance(vec) > entry.getKey().getSquaredDistance(vec))
                closest = entry;
        }
        return closest != null ? closest.getKey() : entity.getBlockPos();
    }

    public PlayerEntity getOwner(World world) {
        if (this.hasOwner)
            return world.getPlayerByUuid(this.ownerUUID);
        return null;
    }

    public Map<BlockPos, Direction> getEntrances() {
        return this.entrances;
    }

    public Map<BlockPos, Direction> getEntranceBottoms() {
        return this.entranceBottoms;
    }

    private Map.Entry<BlockPos, Direction> getClosestEntrance(Entity entity) {
        Map.Entry<BlockPos, Direction> closest = null;
        for (Map.Entry<BlockPos, Direction> entry : this.entrances.entrySet()) {
            Vec3i vec = new Vec3i(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
            if (closest == null || closest.getKey().getSquaredDistance(vec) > entry.getKey().getSquaredDistance(vec))
                closest = entry;
        }
        return closest;
    }

    public void setDefaultPlayerReputation(int defaultReputation) {
        for (UUID s : this.playerReputation.keySet())
            this.modifyPlayerReputation(s, defaultReputation);
    }

    public boolean repopulate() {
        int roomCount = this.getAllRooms().size();
        return this.numMyrmex < Math.min(IafConfig.myrmexColonySize, roomCount * 9) && this.reproduces;
    }

    public void addMyrmex(EntityMyrmexBase myrmex) {
        if (!this.myrmexList.contains(myrmex.getUuid()))
            this.myrmexList.add(myrmex.getUuid());
    }

    public void removeRoom(BlockPos pos) {
        this.foodRooms.remove(pos);
        this.miscRooms.remove(pos);
        this.babyRooms.remove(pos);
        this.allRooms.remove(pos);
        this.getEntrances().remove(pos);
        this.getEntranceBottoms().remove(pos);
    }

    @Override
    public String toString() {
        return "MyrmexHive(x=" + this.center.getX() + ",y=" + this.center.getY() + ",z=" + this.center.getZ() + "), population=" + this.getNumMyrmex() + "\nUUID: " + this.hiveUUID;
    }

    public boolean equals(MyrmexHive hive) {
        return this.hiveUUID.equals(hive.hiveUUID);
    }

    static class HiveAggressor {
        public final LivingEntity agressor;
        public final int agressionLevel;
        public int agressionTime;

        HiveAggressor(LivingEntity agressorIn, int agressionTimeIn, int agressionLevel) {
            this.agressor = agressorIn;
            this.agressionTime = agressionTimeIn;
            this.agressionLevel = agressionLevel;
        }
    }
}
