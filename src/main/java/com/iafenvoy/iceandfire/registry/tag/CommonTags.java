package com.iafenvoy.iceandfire.registry.tag;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class CommonTags {
    public static class Blocks {
        public static final TagKey<Block> COBBLESTONE = tag("cobblestone");
        public static final TagKey<Block> GRAVEL = tag("gravel");
        public static final TagKey<Block> SAND = tag("sand");
        public static final TagKey<Block> STONE = tag("stone");

        private static TagKey<Block> tag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier("c", name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BOSSES = tag("bosses");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> BONES = tag("bones");
        public static final TagKey<Item> COBBLESTONE = tag("cobblestone");
        public static final TagKey<Item> EGGS = tag("eggs");
        public static final TagKey<Item> GEMS = tag("gems");
        public static final TagKey<Item> GRAVEL = tag("gravel");
        public static final TagKey<Item> INGOTS = tag("ingots");
        public static final TagKey<Item> NUGGETS = tag("nuggets");
        public static final TagKey<Item> ORES = ConventionalItemTags.ORES;
        public static final TagKey<Item> SAND = tag("sand");
        public static final TagKey<Item> SEEDS = tag("seeds");
        public static final TagKey<Item> STONE = tag("stone");
        public static final TagKey<Item> STORAGE_BLOCKS = tag("storage_blocks");
        public static final TagKey<Item> STRING = tag("string");

        private static TagKey<Item> tag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> IS_HOT = tag("is_hot");
        public static final TagKey<Biome> IS_HOT_OVERWORLD = tag("is_hot/overworld");
        public static final TagKey<Biome> IS_COLD = tag("is_cold");
        public static final TagKey<Biome> IS_COLD_OVERWORLD = tag("is_cold/overworld");
        public static final TagKey<Biome> IS_SPARSE = tag("is_sparse");
        public static final TagKey<Biome> IS_DENSE = tag("is_dense");
        public static final TagKey<Biome> IS_WET_OVERWORLD = tag("is_wet/overworld");
        public static final TagKey<Biome> IS_DRY = tag("is_dry");
        public static final TagKey<Biome> IS_DRY_OVERWORLD = tag("is_dry/overworld");
        public static final TagKey<Biome> IS_MAGICAL = tag("is_magical");
        public static final TagKey<Biome> IS_RARE = tag("is_rare");
        public static final TagKey<Biome> IS_WATER = tag("is_water");
        public static final TagKey<Biome> IS_PLAINS = tag("is_plains");
        public static final TagKey<Biome> IS_SWAMP = tag("is_swamp");
        public static final TagKey<Biome> IS_SANDY = tag("is_sandy");
        public static final TagKey<Biome> IS_SNOWY = tag("is_snowy");
        public static final TagKey<Biome> IS_UNDERGROUND = tag("is_underground");
        public static final TagKey<Biome> IS_PEAK = tag("is_peak");
        public static final TagKey<Biome> IS_SLOPE = tag("is_slope");
        public static final TagKey<Biome> IS_MOUNTAIN = tag("is_mountain");

        private static TagKey<Biome> tag(String name) {
            return TagKey.of(RegistryKeys.BIOME, new Identifier("c", name));
        }
    }
}
