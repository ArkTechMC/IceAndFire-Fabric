package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenDreadExitPortal {
    private static final Identifier STRUCTURE = new Identifier(IceAndFire.MOD_ID, "dread_exit_portal");

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        /*
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(Rotation.NONE);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Biome biome = worldIn.getBiome(position);
        template.addBlocksToWorld(worldIn, position, new DreadPortalProcessor(position, settings, biome), settings, 2);

         */
        return true;
    }
}