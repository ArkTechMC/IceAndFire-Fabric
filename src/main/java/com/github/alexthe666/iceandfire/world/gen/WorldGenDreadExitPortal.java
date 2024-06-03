package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import java.util.Random;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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