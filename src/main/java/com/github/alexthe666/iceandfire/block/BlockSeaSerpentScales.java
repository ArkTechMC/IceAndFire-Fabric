package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class BlockSeaSerpentScales extends Block {
    final Formatting color;
    final String name;

    public BlockSeaSerpentScales(String name, Formatting color) {
        super(
                Settings
                        .create()
                        .mapColor(MapColor.STONE_GRAY)
                        .strength(30F, 500F)
                        .sounds(BlockSoundGroup.STONE)
                        .requiresTool()
        );

        this.color = color;
        this.name = name;
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, BlockView worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("sea_serpent." + this.name).formatted(this.color));
    }
}
