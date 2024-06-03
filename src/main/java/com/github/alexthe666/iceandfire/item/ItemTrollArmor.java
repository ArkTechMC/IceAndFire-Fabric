package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelTrollArmor;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.interfaces.IArmorTextureProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.item.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ItemTrollArmor extends ArmorItem implements IArmorTextureProvider {

    public EnumTroll troll;

    public ItemTrollArmor(EnumTroll troll, CustomArmorMaterial material, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.troll = troll;
    }

    public static String getName(EnumTroll troll, EquipmentSlot slot) {
        return "%s_troll_leather_%s".formatted(troll.name().toLowerCase(Locale.ROOT), getArmorPart(slot));
    }

    @Override
    public @NotNull ArmorMaterial getMaterial() {
        return troll.material;
    }


    private static String getArmorPart(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> "helmet";
            case CHEST -> "chestplate";
            case LEGS -> "leggings";
            case FEET -> "boots";
            default -> "";
        };
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BipedEntityModel<?> getHumanoidArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<?> _default) {
                return new ModelTrollArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
            }
        });
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_troll_" + troll.name().toLowerCase(Locale.ROOT) + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.troll_leather_armor_" + getArmorPart(type.getEquipmentSlot()) + ".desc").formatted(Formatting.GREEN));
    }
}
