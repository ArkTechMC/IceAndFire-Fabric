package com.github.alexthe666.iceandfire.compat.jade;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum DragonComponentProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public Identifier getUid() {
        return new Identifier(IceAndFire.MOD_ID, "dragon");
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getEntity() instanceof EntityDragonBase dragon) {
            iTooltip.add(Text.translatable("dragon.stage").formatted(Formatting.GRAY).append(dragon.getDragonStage() + " ").append(dragon.getAgeInDays() + "d"));
            iTooltip.add(Text.literal(dragon.isMale() ? "Male" : "Female"));
        }
    }
}
