package com.iafenvoy.iceandfire.render.entity.layer;

import com.iafenvoy.uranus.animation.IAnimatedEntity;
import com.iafenvoy.iceandfire.entity.util.IHasArmorVariant;
import com.iafenvoy.iceandfire.render.model.ModelBipedBase;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class LayerBipedArmorMultiple<R extends MobEntityRenderer<T, M> & IHasArmorVariantResource, T extends MobEntity & IHasArmorVariant & IAnimatedEntity, M extends ModelBipedBase<T>, A extends ModelBipedBase<T>> extends LayerBipedArmor<T, M, A> {
    final R mobRenderer;

    public LayerBipedArmorMultiple(R mobRenderer, A modelLeggings, A modelArmor, Identifier defaultArmor, Identifier defaultLegArmor) {
        super(mobRenderer, modelLeggings, modelArmor, defaultArmor, defaultLegArmor);
        this.mobRenderer = mobRenderer;
    }

    @Override
    public Identifier getArmorResource(T entity, ItemStack stack, EquipmentSlot slot, String type) {
        return this.mobRenderer.getArmorResource(entity.getBodyArmorVariant(), slot);
    }
}
