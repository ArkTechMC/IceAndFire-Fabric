package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockDragonScales extends Block implements IDragonProof {
    final EnumDragonEgg type;

    public BlockDragonScales(EnumDragonEgg type) {
        super(
                Settings
                        .create()
                        .mapColor(MapColor.STONE_GRAY)
                        .instrument(Instrument.BASEDRUM)
                        .dynamicBounds()
                        .strength(30F, 500)
                        .sounds(BlockSoundGroup.STONE)
                        .requiresTool()
        );

        this.type = type;
    }


    @Override
    public void appendTooltip(@NotNull ItemStack stack, BlockView worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("dragon." + this.type.toString().toLowerCase()).formatted(this.type.color));
    }
}
