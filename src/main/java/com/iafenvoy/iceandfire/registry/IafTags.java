package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class IafTags {
    public static final TagKey<Block> MYRMEX_HARVESTABLES = TagKey.of(RegistryKeys.BLOCK, new Identifier(IceAndFire.MOD_ID, "myrmex_harvestables"));
    public static final TagKey<EntityType<?>> CHICKENS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "chickens"));
    public static final TagKey<EntityType<?>> FEAR_DRAGONS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "fear_dragons"));
    public static final TagKey<EntityType<?>> SCARES_COCKATRICES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "scares_cockatrices"));
    public static final TagKey<EntityType<?>> SHEEP = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "sheep"));
    public static final TagKey<EntityType<?>> VILLAGERS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "villagers"));
    public static final TagKey<EntityType<?>> ICE_DRAGON_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "ice_dragon_targets"));
    public static final TagKey<EntityType<?>> FIRE_DRAGON_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "fire_dragon_targets"));
    public static final TagKey<EntityType<?>> LIGHTNING_DRAGON_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "lightning_dragon_targets"));
    public static final TagKey<EntityType<?>> COCKATRICE_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "cockatrice_targets"));
    public static final TagKey<EntityType<?>> CYCLOPS_UNLIFTABLES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "cyclops_unliftables"));
    public static final TagKey<EntityType<?>> BLINDED = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, "blinded"));
}