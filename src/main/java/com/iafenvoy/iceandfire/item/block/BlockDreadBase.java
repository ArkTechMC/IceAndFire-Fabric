package com.iafenvoy.iceandfire.item.block;

import com.iafenvoy.iceandfire.item.block.util.IDragonProof;
import com.iafenvoy.iceandfire.item.block.util.IDreadBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;

public class BlockDreadBase extends BlockGeneric implements IDragonProof, IDreadBlock {
    public BlockDreadBase(Settings settings) {
        super(settings);
    }

    public static BlockDreadBase builder(float hardness, float resistance, BlockSoundGroup sound, MapColor color, Instrument instrument, boolean burnable) {
        Settings props = Settings.create().mapColor(color).sounds(sound).strength(hardness, resistance);
        if (instrument != null) props.instrument(instrument);
        if (burnable) props.burnable();
        return new BlockDreadBase(props);
    }
}
