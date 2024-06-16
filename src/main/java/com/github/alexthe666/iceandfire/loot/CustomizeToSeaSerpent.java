package com.github.alexthe666.iceandfire.loot;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentScales;
import com.github.alexthe666.iceandfire.registry.IafItems;
import com.github.alexthe666.iceandfire.registry.IafLoots;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class CustomizeToSeaSerpent extends ConditionalLootFunction {

    public CustomizeToSeaSerpent(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        if (!stack.isEmpty() && context.get(LootContextParameters.THIS_ENTITY) instanceof EntitySeaSerpent seaSerpent) {
            final int ancientModifier = seaSerpent.isAncient() ? 2 : 1;
            if (stack.getItem() instanceof ItemSeaSerpentScales) {
                stack.setCount(1 + seaSerpent.getRandom().nextInt(1 + (int) Math.ceil(seaSerpent.getSeaSerpentScale() * 3 * ancientModifier)));
                return new ItemStack(seaSerpent.getEnum().scale, stack.getCount());
            }
            if (stack.getItem() == IafItems.SERPENT_FANG) {
                stack.setCount(1 + seaSerpent.getRandom().nextInt(1 + (int) Math.ceil(seaSerpent.getSeaSerpentScale() * 2 * ancientModifier)));
                return stack;
            }
        }
        return stack;
    }

    @Override
    public LootFunctionType getType() {
        return IafLoots.CUSTOMIZE_TO_SERPENT;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<CustomizeToSeaSerpent> {
        public Serializer() {
            super();
        }

        @Override
        public CustomizeToSeaSerpent fromJson(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            return new CustomizeToSeaSerpent(conditionsIn);
        }
    }
}