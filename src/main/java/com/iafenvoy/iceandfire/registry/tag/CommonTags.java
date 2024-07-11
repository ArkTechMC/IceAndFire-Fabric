package com.iafenvoy.iceandfire.registry.tag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

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

    public static class Items {
        public static final TagKey<Item> COBBLESTONE = tag("cobblestone");
        public static final TagKey<Item> GRAVEL = tag("gravel");
        public static final TagKey<Item> INGOTS = tag("ingots");
        public static final TagKey<Item> SAND = tag("sand");
        public static final TagKey<Item> STONE = tag("stone");
        public static final TagKey<Item> STRING = tag("string");

        private static TagKey<Item> tag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
        }
    }

}
