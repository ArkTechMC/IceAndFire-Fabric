package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.EntityDreadMob;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;

public class BlockGeneric extends Block {
/*    public BlockGeneric(float hardness, float resistance, SoundType sound) {
        super(
            BlockBehaviour.Properties
                .of()
                .sound(sound)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops()
        );
    }

    public BlockGeneric(float hardness, float resistance, SoundType sound, boolean slippery) {
        super(
            BlockBehaviour.Properties
                .of()
                .sound(sound)
                .strength(hardness, resistance)
                .friction(0.98F)
        );
    }*/

    public BlockGeneric(Settings props) {
        super(props);
    }

    public static BlockGeneric builder(float hardness, float resistance, BlockSoundGroup sound, MapColor color, Instrument instrument, PistonBehavior reaction, boolean ignited) {
        Settings props = Settings.create().mapColor(color).sounds(sound).strength(hardness, resistance).requiresTool();
        if (instrument != null) props.instrument(instrument);
        if (reaction != null) props.pistonBehavior(reaction);
        if (ignited) props.burnable();
        return new BlockGeneric(props);
    }

    public static BlockGeneric builder(float hardness, float resistance, BlockSoundGroup sound, boolean slippery, MapColor color, Instrument instrument, PistonBehavior reaction, boolean ignited) {
        Settings props = Settings.create().mapColor(color).sounds(sound).strength(hardness, resistance).slipperiness(0.98F);
        if (instrument != null) props.instrument(instrument);
        if (reaction != null) props.pistonBehavior(reaction);
        if (ignited) props.burnable();
        return new BlockGeneric(props);
    }

    public boolean isOpaqueCube(BlockState state) {
        return this != IafBlockRegistry.DRAGON_ICE;
    }

    public boolean isFullCube(BlockState state) {
        return this != IafBlockRegistry.DRAGON_ICE;
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, Entity entity) {
        return entity instanceof EntityDreadMob || !DragonUtils.isDreadBlock(state);
    }
}
