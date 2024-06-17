package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.texture.ArrayLayeredTexture;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.dragon.DragonType;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LayerDragonArmor extends FeatureRenderer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private static final Map<String, Identifier> LAYERED_ARMOR_CACHE = Maps.newHashMap();
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public LayerDragonArmor(MobEntityRenderer renderIn, int type) {
        super(renderIn);
    }

    public static void clearCache(String str) {
        LAYERED_ARMOR_CACHE.remove(str);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        int armorHead = dragon.getArmorOrdinal(dragon.getEquippedStack(EquipmentSlot.HEAD));
        int armorNeck = dragon.getArmorOrdinal(dragon.getEquippedStack(EquipmentSlot.CHEST));
        int armorLegs = dragon.getArmorOrdinal(dragon.getEquippedStack(EquipmentSlot.LEGS));
        int armorFeet = dragon.getArmorOrdinal(dragon.getEquippedStack(EquipmentSlot.FEET));
        String armorTexture = dragon.dragonType.getName() + "_" + armorHead + "_" + armorNeck + "_" + armorLegs + "_" + armorFeet;
        if (!armorTexture.equals(dragon.dragonType.getName() + "_0_0_0_0")) {
            Identifier resourcelocation = LAYERED_ARMOR_CACHE.get(armorTexture);
            if (resourcelocation == null) {
                resourcelocation = new Identifier("iceandfire" + "dragon_armor_" + armorTexture);
                List<String> tex = new ArrayList<>();
                for (EquipmentSlot slot : ARMOR_SLOTS) {
                    if (dragon.dragonType == DragonType.FIRE)
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).FIRETEXTURE.toString());
                    else if (dragon.dragonType == DragonType.ICE)
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).ICETEXTURE.toString());
                    else
                        tex.add(EnumDragonTextures.Armor.getArmorForDragon(dragon, slot).LIGHTNINGTEXTURE.toString());
                }
                ArrayLayeredTexture layeredBase = new ArrayLayeredTexture(tex);
                MinecraftClient.getInstance().getTextureManager().registerTexture(resourcelocation, layeredBase);
                LAYERED_ARMOR_CACHE.put(armorTexture, resourcelocation);
            }
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderLayer.getEntityCutoutNoCull(resourcelocation));
            this.getContextModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}