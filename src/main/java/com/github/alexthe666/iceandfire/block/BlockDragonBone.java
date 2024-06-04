package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;

public class BlockDragonBone extends PillarBlock implements IDragonProof {

    public BlockDragonBone() {
        super(
            Settings
                .create()
                .mapColor(MapColor.STONE_GRAY)
                .instrument(Instrument.BASEDRUM)
                .sounds(BlockSoundGroup.WOOD)
                .strength(30F, 500F)
                .requiresTool()
		);

    }
}
