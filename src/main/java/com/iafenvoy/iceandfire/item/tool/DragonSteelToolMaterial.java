package com.iafenvoy.iceandfire.item.tool;

import com.iafenvoy.uranus.object.item.ToolMaterialUtil;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ToolMaterial;

public class DragonSteelToolMaterial {
    public static ToolMaterial createMaterialWithRepairItem(ItemConvertible ingredient, String name) {
        return ToolMaterialUtil.of(4000, 4F, 10F, 21, 10, ingredient);
    }
}
