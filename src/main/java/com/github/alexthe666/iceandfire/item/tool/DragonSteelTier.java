package com.github.alexthe666.iceandfire.item.tool;

import dev.arktechmc.iafextra.util.ToolMaterialUtil;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ToolMaterial;

public class DragonSteelTier {
    public static ToolMaterial createMaterialWithRepairItem(ItemConvertible ingredient, String name) {
        return ToolMaterialUtil.of(4000, 4F, 10F, 21, 10, ingredient);
    }
}
