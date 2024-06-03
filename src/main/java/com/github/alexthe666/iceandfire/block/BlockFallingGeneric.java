package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;

public class BlockFallingGeneric extends FallingBlock {
    public Item itemBlock;

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

    public static BlockFallingGeneric builder(float hardness, float resistance, BlockSoundGroup sound, MapColor color, Instrument instrument) {
        Settings props = Settings.create()
                .mapColor(color)
                .instrument(instrument)
                .sounds(sound)
                .strength(hardness, resistance);
        return new BlockFallingGeneric(props);
    }

    public BlockFallingGeneric(Settings props) {
        super(props);
    }


    public int getDustColor(BlockState blkst) {
        return -8356741;
    }
}
