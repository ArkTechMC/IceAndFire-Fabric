package com.iafenvoy.iceandfire.block;

import net.minecraft.block.FallingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;

public class BlockFallingGeneric extends FallingBlock {
/*    public BlockFallingGeneric(float hardness, float resistance, SoundType sound) {
        super(
            BlockBehaviour.Properties
                .of()
                .sound(sound)
                .strength(hardness, resistance)
        );
    }

    @SuppressWarnings("deprecation")
    public BlockFallingGeneric(float hardness, float resistance, SoundType sound, boolean slippery) {
        super(
            BlockBehaviour.Properties
                .of()
                .sound(sound)
                .strength(hardness, resistance)
                .friction(0.98F)
        );
    }*/

    public BlockFallingGeneric(Settings props) {
        super(props);
    }

    public static BlockFallingGeneric builder(float hardness, float resistance, BlockSoundGroup sound, MapColor color, Instrument instrument) {
        Settings props = Settings.create().mapColor(color).instrument(instrument).sounds(sound).strength(hardness, resistance);
        return new BlockFallingGeneric(props);
    }
}
