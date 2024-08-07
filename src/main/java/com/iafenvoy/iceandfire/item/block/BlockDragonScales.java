package com.iafenvoy.iceandfire.item.block;

import com.iafenvoy.iceandfire.enums.DragonColor;
import com.iafenvoy.iceandfire.item.block.util.IDragonProof;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.world.BlockView;

import java.util.List;
import java.util.Locale;

public class BlockDragonScales extends Block implements IDragonProof {
    final DragonColor type;

    public BlockDragonScales(DragonColor type) {
        super(Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).dynamicBounds().strength(30F, 500).sounds(BlockSoundGroup.STONE).requiresTool());
        this.type = type;
    }

    @Override
    public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("dragon." + this.type.toString().toLowerCase(Locale.ROOT)).formatted(this.type.color()));
    }
}
