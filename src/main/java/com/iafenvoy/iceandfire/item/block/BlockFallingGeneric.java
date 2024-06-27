package com.iafenvoy.iceandfire.item.block;

import net.minecraft.block.FallingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;

public class BlockFallingGeneric extends FallingBlock {
    public BlockFallingGeneric(Settings props) {
        super(props);
    }

    public static BlockFallingGeneric builder(float hardness, float resistance, BlockSoundGroup sound, MapColor color, Instrument instrument) {
        Settings props = Settings.create().mapColor(color).instrument(instrument).sounds(sound).strength(hardness, resistance);
        return new BlockFallingGeneric(props);
    }
}
