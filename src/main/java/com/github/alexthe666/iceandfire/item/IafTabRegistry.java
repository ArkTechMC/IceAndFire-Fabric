package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class IafTabRegistry {
    public static final List<Block> TAB_BLOCKS_LIST = new ArrayList<>();
    public static final List<Item> TAB_ITEMS_LIST = new ArrayList<>();
    public static final ItemGroup TAB_BLOCKS = register("blocks", FabricItemGroup.builder()
            // Set name of tab to display
            .displayName(Text.translatable("itemGroup." + IceAndFire.MOD_ID + ".blocks"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(IafBlockRegistry.DRAGON_SCALE_RED))
            // Add default items to tab
            .entries((params, output) -> {
                for (Block block : TAB_BLOCKS_LIST) {
                    ItemStack stack = new ItemStack(block);
                    if (!stack.isEmpty()) output.add(stack);
                    else System.err.println("Failed to put item: " + block);
                }
            })
            .build()
    );

    public static final ItemGroup TAB_ITEMS = register("items", FabricItemGroup.builder()
            // Set name of tab to display
            .displayName(Text.translatable("itemGroup." + IceAndFire.MOD_ID + ".items"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE))
            // Add default items to tab
            .entries((params, output) -> {
                for (Item item : TAB_ITEMS_LIST)
                    output.add(item);
            })
            .build()
    );

    public static ItemGroup register(String name, ItemGroup group) {
        return Registry.register(Registries.ITEM_GROUP, new Identifier(IceAndFire.MOD_ID, name), group);
    }

    public static void init() {
    }
}
