package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ModelDreadThrall;
import com.github.alexthe666.iceandfire.client.model.util.HideableLayer;
import com.github.alexthe666.iceandfire.client.render.entity.layer.IHasArmorVariantResource;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerBipedArmorMultiple;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class RenderDreadThrall extends MobEntityRenderer<EntityDreadThrall, ModelDreadThrall> implements IHasArmorVariantResource {
    public static final Identifier TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_thrall.png");
    public static final Identifier TEXTURE_EYES = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/dread_thrall_eyes.png");
    public static final Identifier TEXTURE_LEG_ARMOR = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_legs.png");
    public static final Identifier TEXTURE_ARMOR_0 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_chest_1.png");
    public static final Identifier TEXTURE_ARMOR_1 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_chest_2.png");
    public static final Identifier TEXTURE_ARMOR_2 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_chest_3.png");
    public static final Identifier TEXTURE_ARMOR_3 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_chest_4.png");
    public static final Identifier TEXTURE_ARMOR_4 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_chest_5.png");
    public static final Identifier TEXTURE_ARMOR_5 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_chest_6.png");
    public static final Identifier TEXTURE_ARMOR_6 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_chest_7.png");
    public static final Identifier TEXTURE_ARMOR_7 = new Identifier(IceAndFire.MOD_ID, "textures/models/dread/thrall_chest_8.png");
    public final HideableLayer<EntityDreadThrall, ModelDreadThrall, HeldItemFeatureRenderer<EntityDreadThrall, ModelDreadThrall>> itemLayer;

    public RenderDreadThrall(EntityRendererFactory.Context context) {
        super(context, new ModelDreadThrall(0.0F, false), 0.6F);

        this.addFeature(new LayerGenericGlowing<>(this, TEXTURE_EYES));
        this.itemLayer = new HideableLayer<>(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()), this);
        this.addFeature(this.itemLayer);
        this.addFeature(new LayerBipedArmorMultiple<>(this,
                new ModelDreadThrall(0.5F, true), new ModelDreadThrall(1.0F, true),
                TEXTURE_ARMOR_0, TEXTURE_LEG_ARMOR));
    }

    @Override
    public Identifier getArmorResource(int variant, EquipmentSlot equipmentSlotType) {
        if (equipmentSlotType == EquipmentSlot.LEGS)
            return TEXTURE_LEG_ARMOR;
        return switch (variant) {
            case 1 -> TEXTURE_ARMOR_1;
            case 2 -> TEXTURE_ARMOR_2;
            case 3 -> TEXTURE_ARMOR_3;
            case 4 -> TEXTURE_ARMOR_4;
            case 5 -> TEXTURE_ARMOR_5;
            case 6 -> TEXTURE_ARMOR_6;
            case 7 -> TEXTURE_ARMOR_7;
            default -> TEXTURE_ARMOR_0;
        };
    }

    @Override
    public void scale(EntityDreadThrall livingEntityIn, MatrixStack stack, float partialTickTime) {
        stack.scale(0.95F, 0.95F, 0.95F);
        if (livingEntityIn.getAnimation() == this.getModel().getSpawnAnimation()) {
            this.itemLayer.hidden = livingEntityIn.getAnimationTick() <= this.getModel().getSpawnAnimation().getDuration() - 10;
            return;
        }
        this.itemLayer.hidden = false;

    }

    @Override
    public Identifier getTexture(EntityDreadThrall entity) {
        return TEXTURE;
    }
}
