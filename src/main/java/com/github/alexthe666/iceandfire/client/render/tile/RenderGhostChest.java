package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityGhostChest;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import org.jetbrains.annotations.NotNull;

import static com.github.alexthe666.iceandfire.client.IafClientSetup.*;
import static net.minecraft.client.render.TexturedRenderLayers.CHEST_ATLAS_TEXTURE;

public class RenderGhostChest extends ChestBlockEntityRenderer<TileEntityGhostChest> {

    private static final SpriteIdentifier GHOST_CHEST = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, GHOST_CHEST_LOCATION);
    private static final SpriteIdentifier GHOST_CHEST_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, GHOST_CHEST_LEFT_LOCATION);
    private static final SpriteIdentifier GHOST_CHEST_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, GHOST_CHEST_RIGHT_LOCATION);

    public RenderGhostChest(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    private static SpriteIdentifier getChestMaterial(ChestType chestType, SpriteIdentifier doubleMaterial, SpriteIdentifier leftMaterial, SpriteIdentifier rightMaterial) {
        return switch (chestType) {
            case LEFT -> leftMaterial;
            case RIGHT -> rightMaterial;
            default -> doubleMaterial;
        };
    }

//    @Override
//    protected @NotNull SpriteIdentifier getMaterial(@NotNull TileEntityGhostChest tileEntity, @NotNull ChestType chestType) {
//        return getChestMaterial(chestType, GHOST_CHEST, GHOST_CHEST_LEFT, GHOST_CHEST_RIGHT);
//    }
}
