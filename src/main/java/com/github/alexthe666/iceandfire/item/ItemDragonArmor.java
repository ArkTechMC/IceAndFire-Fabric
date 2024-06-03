package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemDragonArmor extends Item {

    public DragonArmorType type;
    public int dragonSlot;
    public String name;
    private Pattern baseName = Pattern.compile("[a-z]+_[a-z]+");

    public ItemDragonArmor(DragonArmorType type, int dragonSlot) {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
        this.type = type;
        this.dragonSlot = dragonSlot;
        if (type == DragonArmorType.FIRE || type == DragonArmorType.ICE || type == DragonArmorType.LIGHTNING)
            baseName = Pattern.compile("[a-z]+_[a-z]+_[a-z]+");
    }

    @Override
    public @NotNull String getTranslationKey() {
        String fullName = Registries.ITEM.getId(this).getPath();
        Matcher matcher = baseName.matcher(fullName);
        name = matcher.find() ? matcher.group() : fullName;
        return "item." + IceAndFire.MOD_ID + "." + name;
    }

    static String getNameForSlot(int slot) {
        return switch (slot) {
            case 0 -> "head";
            case 1 -> "neck";
            case 2 -> "body";
            case 3 -> "tail";
            default -> "";
        };
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        String words = switch (dragonSlot) {
            case 1 -> "dragon.armor_neck";
            case 2 -> "dragon.armor_body";
            case 3 -> "dragon.armor_tail";
            default -> "dragon.armor_head";
        };
        tooltip.add(Text.translatable(words).formatted(Formatting.GRAY));
    }

    public enum DragonArmorType {
        IRON,
        GOLD,
        DIAMOND,
        SILVER,
        FIRE,
        ICE,
        COPPER,
        LIGHTNING
    }

}
