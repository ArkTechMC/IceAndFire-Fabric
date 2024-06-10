package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class IafTabRegistry {
    public static final LazyRegistrar<ItemGroup> TAB_REGISTER = LazyRegistrar.create(RegistryKeys.ITEM_GROUP, IceAndFire.MOD_ID);

    public static final List<Supplier<? extends Block>> TAB_BLOCKS_LIST = new ArrayList<>();
    public static final List<Supplier<? extends Item>> TAB_ITEMS_LIST = new ArrayList<>();
    public static final RegistryObject<ItemGroup> TAB_BLOCKS = TAB_REGISTER.register("blocks", () -> FabricItemGroup.builder()
            // Set name of tab to display
            .displayName(Text.translatable("itemGroup." + IceAndFire.MOD_ID + ".blocks"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(IafBlockRegistry.DRAGON_SCALE_RED.get()))
            // Add default items to tab
            .entries((params, output) -> {
                for (Supplier<? extends Block> block : TAB_BLOCKS_LIST) {
                    ItemStack stack = new ItemStack(block.get());
                    if (!stack.isEmpty())
                        output.add(stack);
                    else
                        System.err.println("Failed to put item: " + block.get());
                }
            })
            .build()
    );

    public static final RegistryObject<ItemGroup> TAB_ITEMS = TAB_REGISTER.register("items", () -> FabricItemGroup.builder()
            // Set name of tab to display
            .displayName(Text.translatable("itemGroup." + IceAndFire.MOD_ID + ".items"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE.get()))
            // Add default items to tab
            .entries((params, output) -> {
                for (Supplier<? extends Item> item : TAB_ITEMS_LIST) {
                    output.add(item.get());
                }
            })
            .build()
    );
}
