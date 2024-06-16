package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.block.util.IDragonProof;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;

public class BlockDragonBone extends PillarBlock implements IDragonProof {

    public BlockDragonBone() {
        super(Settings.create().mapColor(MapColor.STONE_GRAY).instrument(Instrument.BASEDRUM).sounds(BlockSoundGroup.WOOD).strength(30F, 500F).requiresTool());
    }
}
