package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.block.util.IDragonProof;
import com.github.alexthe666.iceandfire.block.util.IDreadBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.sound.BlockSoundGroup;

public class BlockDreadWoodLog extends PillarBlock implements IDragonProof, IDreadBlock {
    public BlockDreadWoodLog() {
        super(Settings.create().mapColor(MapColor.OAK_TAN).instrument(Instrument.BASS).burnable().strength(2F, 10000F).sounds(BlockSoundGroup.WOOD));
    }
}
