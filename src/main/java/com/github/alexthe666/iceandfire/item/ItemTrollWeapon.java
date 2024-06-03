package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.client.render.tile.RenderTrollWeapon;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ItemTrollWeapon extends SwordItem {

    public EnumTroll.Weapon weapon = EnumTroll.Weapon.AXE;

    public ItemTrollWeapon(EnumTroll.Weapon weapon) {
        super(IafItemRegistry.TROLL_WEAPON_TOOL_MATERIAL, 15, -3.5F, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.weapon = weapon;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {

        consumer.accept(new IClientItemExtensions() {
            static final NonNullLazy<BuiltinModelItemRenderer> renderer = NonNullLazy.of(() -> new RenderTrollWeapon(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        return player.getAttackCooldownProgress(0) < 0.95 || player.handSwingProgress != 0;
    }

    public boolean onEntitySwing(LivingEntity LivingEntity, ItemStack stack) {
        if (LivingEntity instanceof PlayerEntity player) {
            if (player.getAttackCooldownProgress(0) < 1 && player.handSwingProgress > 0) {
                return true;
            } else {
                player.handSwingTicks = -1;
            }
        }
        return false;
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof PlayerEntity player && isSelected) {
            if (player.getAttackCooldownProgress(0) < 0.95 && player.handSwingProgress > 0) {
                player.handSwingTicks--;
            }
        }
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
    }

}
