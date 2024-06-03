package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelFireArmor;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelIceArmor;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelLightningArmor;
import com.github.alexthe666.iceandfire.interfaces.IArmorTextureProvider;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
import java.util.UUID;
import java.util.function.Consumer;

import static com.github.alexthe666.iceandfire.item.IafItemRegistry.*;

public class ItemDragonsteelArmor extends ArmorItem implements IProtectAgainstDragonItem, IArmorTextureProvider {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    private final ArmorMaterial material;
    private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifierMultimap;

    public ItemDragonsteelArmor(ArmorMaterial material, int renderIndex, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.material = material;
        this.attributeModifierMultimap = createAttributeMap();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BipedEntityModel<?> getHumanoidArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<?> _default) {
                boolean inner = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD;
                if (itemStack.getItem() instanceof ArmorItem) {
                    ArmorMaterial armorMaterial = ((ArmorItem) itemStack.getItem()).getMaterial();
                    if (DRAGONSTEEL_FIRE_ARMOR_MATERIAL.equals(armorMaterial))
                        return new ModelDragonsteelFireArmor(inner);
                    if (DRAGONSTEEL_ICE_ARMOR_MATERIAL.equals(armorMaterial))
                        return new ModelDragonsteelIceArmor(inner);
                    if (DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.equals(armorMaterial))
                        return new ModelDragonsteelLightningArmor(inner);
                }
                return _default;

            }
        });
    }


    //Workaround for armor attributes being registered before the config gets loaded
    private Multimap<EntityAttribute, EntityAttributeModifier> createAttributeMap() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIERS[type.getEquipmentSlot().getEntitySlotId()];
        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uuid, "Armor modifier", material.getProtection(type), EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(uuid, "Armor toughness", material.getToughness(), EntityAttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(uuid, "Armor knockback resistance", this.knockbackResistance, EntityAttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }

    private Multimap<EntityAttribute, EntityAttributeModifier> getOrUpdateAttributeMap() {
        //If the armor values have changed recreate the map
        //There might be a prettier way of accomplishing this but it works
        if (this.attributeModifierMultimap.containsKey(EntityAttributes.GENERIC_ARMOR)
                && !this.attributeModifierMultimap.get(EntityAttributes.GENERIC_ARMOR).isEmpty()
                && this.attributeModifierMultimap.get(EntityAttributes.GENERIC_ARMOR).toArray()[0] instanceof EntityAttributeModifier
                && ((EntityAttributeModifier) this.attributeModifierMultimap.get(EntityAttributes.GENERIC_ARMOR).toArray()[0]).getValue() != getProtection()
        ) {
            this.attributeModifierMultimap = createAttributeMap();
        }
        return attributeModifierMultimap;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        if (this.type != null)
            return this.getMaterial().getDurability(this.type);
        return super.getMaxUseTime(stack);
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.dragonscales_armor.desc").formatted(Formatting.GRAY));
    }

    @Override
    public @NotNull Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        return equipmentSlot == this.type.getEquipmentSlot() ? getOrUpdateAttributeMap() : super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    public int getProtection() {
        if (this.material != null)
            return this.material.getProtection(this.getType());
        return super.getProtection();
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (material == DRAGONSTEEL_FIRE_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_fire" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        } else if (material == IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL) {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_ice" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        } else {
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_lightning" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        }
    }
}
