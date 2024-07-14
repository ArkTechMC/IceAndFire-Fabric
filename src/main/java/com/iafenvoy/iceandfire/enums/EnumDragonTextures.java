package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.entity.EntityDragonSkull;
import com.iafenvoy.iceandfire.entity.EntityIceDragon;
import com.iafenvoy.iceandfire.entity.EntityLightningDragon;
import com.iafenvoy.iceandfire.item.armor.ItemDragonArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public enum EnumDragonTextures {
    VARIANT1("red_", "blue_", "electric_"),
    VARIANT2("green_", "white_", "amethyst_"),
    VARIANT3("bronze_", "sapphire_", "copper_"),
    VARIANT4("gray_", "silver_", "black_");

    public final Identifier FIRESTAGE1TEXTURE;
    public final Identifier FIRESTAGE2TEXTURE;
    public final Identifier FIRESTAGE3TEXTURE;
    public final Identifier FIRESTAGE4TEXTURE;
    public final Identifier FIRESTAGE5TEXTURE;
    public final Identifier ICESTAGE1TEXTURE;
    public final Identifier ICESTAGE2TEXTURE;
    public final Identifier ICESTAGE3TEXTURE;
    public final Identifier ICESTAGE4TEXTURE;
    public final Identifier ICESTAGE5TEXTURE;
    public final Identifier FIRESTAGE1SLEEPINGTEXTURE;
    public final Identifier FIRESTAGE2SLEEPINGTEXTURE;
    public final Identifier FIRESTAGE3SLEEPINGTEXTURE;
    public final Identifier FIRESTAGE4SLEEPINGTEXTURE;
    public final Identifier FIRESTAGE5SLEEPINGTEXTURE;
    public final Identifier ICESTAGE1SLEEPINGTEXTURE;
    public final Identifier ICESTAGE2SLEEPINGTEXTURE;
    public final Identifier ICESTAGE3SLEEPINGTEXTURE;
    public final Identifier ICESTAGE4SLEEPINGTEXTURE;
    public final Identifier ICESTAGE5SLEEPINGTEXTURE;
    public final Identifier FIRESTAGE1EYESTEXTURE;
    public final Identifier FIRESTAGE2EYESTEXTURE;
    public final Identifier FIRESTAGE3EYESTEXTURE;
    public final Identifier FIRESTAGE4EYESTEXTURE;
    public final Identifier FIRESTAGE5EYESTEXTURE;
    public final Identifier ICESTAGE1EYESTEXTURE;
    public final Identifier ICESTAGE2EYESTEXTURE;
    public final Identifier ICESTAGE3EYESTEXTURE;
    public final Identifier ICESTAGE4EYESTEXTURE;
    public final Identifier ICESTAGE5EYESTEXTURE;
    public final Identifier FIRESTAGE1SKELETONTEXTURE;
    public final Identifier FIRESTAGE2SKELETONTEXTURE;
    public final Identifier FIRESTAGE3SKELETONTEXTURE;
    public final Identifier FIRESTAGE4SKELETONTEXTURE;
    public final Identifier FIRESTAGE5SKELETONTEXTURE;
    public final Identifier ICESTAGE1SKELETONTEXTURE;
    public final Identifier ICESTAGE2SKELETONTEXTURE;
    public final Identifier ICESTAGE3SKELETONTEXTURE;
    public final Identifier ICESTAGE4SKELETONTEXTURE;
    public final Identifier ICESTAGE5SKELETONTEXTURE;
    public final Identifier FIRE_MALE_OVERLAY;
    public final Identifier ICE_MALE_OVERLAY;

    public final Identifier LIGHTNINGSTAGE1TEXTURE;
    public final Identifier LIGHTNINGSTAGE2TEXTURE;
    public final Identifier LIGHTNINGSTAGE3TEXTURE;
    public final Identifier LIGHTNINGSTAGE4TEXTURE;
    public final Identifier LIGHTNINGSTAGE5TEXTURE;
    public final Identifier LIGHTNINGSTAGE1SLEEPINGTEXTURE;
    public final Identifier LIGHTNINGSTAGE2SLEEPINGTEXTURE;
    public final Identifier LIGHTNINGSTAGE3SLEEPINGTEXTURE;
    public final Identifier LIGHTNINGSTAGE4SLEEPINGTEXTURE;
    public final Identifier LIGHTNINGSTAGE5SLEEPINGTEXTURE;
    public final Identifier LIGHTNINGSTAGE1EYESTEXTURE;
    public final Identifier LIGHTNINGSTAGE2EYESTEXTURE;
    public final Identifier LIGHTNINGSTAGE3EYESTEXTURE;
    public final Identifier LIGHTNINGSTAGE4EYESTEXTURE;
    public final Identifier LIGHTNINGSTAGE5EYESTEXTURE;
    public final Identifier LIGHTNINGSTAGE1SKELETONTEXTURE;
    public final Identifier LIGHTNINGSTAGE2SKELETONTEXTURE;
    public final Identifier LIGHTNINGSTAGE3SKELETONTEXTURE;
    public final Identifier LIGHTNINGSTAGE4SKELETONTEXTURE;
    public final Identifier LIGHTNINGSTAGE5SKELETONTEXTURE;

    public final Identifier LIGHTNING_MALE_OVERLAY;

    EnumDragonTextures(String fireVariant, String iceVariant, String lightningVariant) {
        this.FIRESTAGE1TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "1.png");
        this.FIRESTAGE2TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "2.png");
        this.FIRESTAGE3TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "3.png");
        this.FIRESTAGE4TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "4.png");
        this.FIRESTAGE5TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "5.png");
        this.FIRESTAGE1SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "1_sleeping.png");
        this.FIRESTAGE2SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "2_sleeping.png");
        this.FIRESTAGE3SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "3_sleeping.png");
        this.FIRESTAGE4SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "4_sleeping.png");
        this.FIRESTAGE5SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "5_sleeping.png");
        this.FIRESTAGE1EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "1_eyes.png");
        this.FIRESTAGE2EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "2_eyes.png");
        this.FIRESTAGE3EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "3_eyes.png");
        this.FIRESTAGE4EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "4_eyes.png");
        this.FIRESTAGE5EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/" + fireVariant + "5_eyes.png");
        this.FIRESTAGE1SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/fire_skeleton_1.png");
        this.FIRESTAGE2SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/fire_skeleton_2.png");
        this.FIRESTAGE3SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/fire_skeleton_3.png");
        this.FIRESTAGE4SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/fire_skeleton_4.png");
        this.FIRESTAGE5SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/fire_skeleton_5.png");
        this.ICESTAGE1TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "1.png");
        this.ICESTAGE2TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "2.png");
        this.ICESTAGE3TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "3.png");
        this.ICESTAGE4TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "4.png");
        this.ICESTAGE5TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "5.png");
        this.ICESTAGE1SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "1_sleeping.png");
        this.ICESTAGE2SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "2_sleeping.png");
        this.ICESTAGE3SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "3_sleeping.png");
        this.ICESTAGE4SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "4_sleeping.png");
        this.ICESTAGE5SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "5_sleeping.png");
        this.ICESTAGE1EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "1_eyes.png");
        this.ICESTAGE2EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "2_eyes.png");
        this.ICESTAGE3EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "3_eyes.png");
        this.ICESTAGE4EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "4_eyes.png");
        this.ICESTAGE5EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/" + iceVariant + "5_eyes.png");
        this.ICESTAGE1SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/ice_skeleton_1.png");
        this.ICESTAGE2SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/ice_skeleton_2.png");
        this.ICESTAGE3SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/ice_skeleton_3.png");
        this.ICESTAGE4SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/ice_skeleton_4.png");
        this.ICESTAGE5SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/ice_skeleton_5.png");
        this.FIRE_MALE_OVERLAY = new Identifier(IceAndFire.MOD_ID, "textures/models/firedragon/male_" + fireVariant.substring(0, fireVariant.length() - 1) + ".png");
        this.ICE_MALE_OVERLAY = new Identifier(IceAndFire.MOD_ID, "textures/models/icedragon/male_" + iceVariant.substring(0, iceVariant.length() - 1) + ".png");

        this.LIGHTNINGSTAGE1TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "1.png");
        this.LIGHTNINGSTAGE2TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "2.png");
        this.LIGHTNINGSTAGE3TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "3.png");
        this.LIGHTNINGSTAGE4TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "4.png");
        this.LIGHTNINGSTAGE5TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "5.png");
        this.LIGHTNINGSTAGE1SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "1_sleeping.png");
        this.LIGHTNINGSTAGE2SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "2_sleeping.png");
        this.LIGHTNINGSTAGE3SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "3_sleeping.png");
        this.LIGHTNINGSTAGE4SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "4_sleeping.png");
        this.LIGHTNINGSTAGE5SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "5_sleeping.png");
        this.LIGHTNINGSTAGE1EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "1_eyes.png");
        this.LIGHTNINGSTAGE2EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "2_eyes.png");
        this.LIGHTNINGSTAGE3EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "3_eyes.png");
        this.LIGHTNINGSTAGE4EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "4_eyes.png");
        this.LIGHTNINGSTAGE5EYESTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/" + lightningVariant + "5_eyes.png");
        this.LIGHTNINGSTAGE1SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/lightning_skeleton_1.png");
        this.LIGHTNINGSTAGE2SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/lightning_skeleton_2.png");
        this.LIGHTNINGSTAGE3SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/lightning_skeleton_3.png");
        this.LIGHTNINGSTAGE4SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/lightning_skeleton_4.png");
        this.LIGHTNINGSTAGE5SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/lightning_skeleton_5.png");
        this.LIGHTNING_MALE_OVERLAY = new Identifier(IceAndFire.MOD_ID, "textures/models/lightningdragon/male_" + lightningVariant.substring(0, lightningVariant.length() - 1) + ".png");

    }


    public static Identifier getTextureFromDragon(EntityDragonBase dragon) {
        if (dragon instanceof EntityIceDragon)
            return getIceDragonTextures(dragon);
        else if (dragon instanceof EntityLightningDragon)
            return getLightningDragonTextures(dragon);
        else
            return getFireDragonTextures(dragon);
    }


    public static Identifier getEyeTextureFromDragon(EntityDragonBase dragon) {
        EnumDragonTextures textures = getDragonEnum(dragon);
        if (dragon instanceof EntityIceDragon) {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.ICESTAGE1EYESTEXTURE;
                case 2 -> textures.ICESTAGE2EYESTEXTURE;
                case 3 -> textures.ICESTAGE3EYESTEXTURE;
                case 5 -> textures.ICESTAGE5EYESTEXTURE;
                default -> textures.ICESTAGE4EYESTEXTURE;
            };
        } else if (dragon instanceof EntityLightningDragon) {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.LIGHTNINGSTAGE1EYESTEXTURE;
                case 2 -> textures.LIGHTNINGSTAGE2EYESTEXTURE;
                case 3 -> textures.LIGHTNINGSTAGE3EYESTEXTURE;
                case 5 -> textures.LIGHTNINGSTAGE5EYESTEXTURE;
                default -> textures.LIGHTNINGSTAGE4EYESTEXTURE;
            };
        } else {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.FIRESTAGE1EYESTEXTURE;
                case 2 -> textures.FIRESTAGE2EYESTEXTURE;
                case 3 -> textures.FIRESTAGE3EYESTEXTURE;
                case 5 -> textures.FIRESTAGE5EYESTEXTURE;
                default -> textures.FIRESTAGE4EYESTEXTURE;
            };
        }
    }

    private static Identifier getFireDragonTextures(EntityDragonBase dragon) {
        EnumDragonTextures textures = getDragonEnum(dragon);
        if (dragon.isModelDead()) {
            if (dragon.getDeathStage() >= (dragon.getAgeInDays() / 5) / 2) {
                return switch (dragon.getDragonStage()) {
                    case 1 -> textures.FIRESTAGE1SKELETONTEXTURE;
                    case 2 -> textures.FIRESTAGE2SKELETONTEXTURE;
                    case 3 -> textures.FIRESTAGE3SKELETONTEXTURE;
                    case 5 -> textures.FIRESTAGE5SKELETONTEXTURE;
                    default -> textures.FIRESTAGE4SKELETONTEXTURE;
                };
            } else {
                return switch (dragon.getDragonStage()) {
                    case 1 -> textures.FIRESTAGE1SLEEPINGTEXTURE;
                    case 2 -> textures.FIRESTAGE2SLEEPINGTEXTURE;
                    case 3 -> textures.FIRESTAGE3SLEEPINGTEXTURE;
                    case 5 -> textures.FIRESTAGE5SLEEPINGTEXTURE;
                    default -> textures.FIRESTAGE4SLEEPINGTEXTURE;
                };
            }
        }
        if (dragon.isSleeping() || dragon.isBlinking()) {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.FIRESTAGE1SLEEPINGTEXTURE;
                case 2 -> textures.FIRESTAGE2SLEEPINGTEXTURE;
                case 3 -> textures.FIRESTAGE3SLEEPINGTEXTURE;
                case 5 -> textures.FIRESTAGE5SLEEPINGTEXTURE;
                default -> textures.FIRESTAGE4SLEEPINGTEXTURE;
            };
        } else {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.FIRESTAGE1TEXTURE;
                case 2 -> textures.FIRESTAGE2TEXTURE;
                case 3 -> textures.FIRESTAGE3TEXTURE;
                case 5 -> textures.FIRESTAGE5TEXTURE;
                default -> textures.FIRESTAGE4TEXTURE;
            };
        }
    }

    private static Identifier getIceDragonTextures(EntityDragonBase dragon) {
        EnumDragonTextures textures = getDragonEnum(dragon);
        if (dragon.isModelDead()) {
            if (dragon.getDeathStage() >= (dragon.getAgeInDays() / 5) / 2) {
                return switch (dragon.getDragonStage()) {
                    case 1 -> textures.ICESTAGE1SKELETONTEXTURE;
                    case 2 -> textures.ICESTAGE2SKELETONTEXTURE;
                    case 3 -> textures.ICESTAGE3SKELETONTEXTURE;
                    case 5 -> textures.ICESTAGE5SKELETONTEXTURE;
                    default -> textures.ICESTAGE4SKELETONTEXTURE;
                };
            } else {
                return switch (dragon.getDragonStage()) {
                    case 1 -> textures.ICESTAGE1SLEEPINGTEXTURE;
                    case 2 -> textures.ICESTAGE2SLEEPINGTEXTURE;
                    case 3 -> textures.ICESTAGE3SLEEPINGTEXTURE;
                    case 5 -> textures.ICESTAGE5SLEEPINGTEXTURE;
                    default -> textures.ICESTAGE4SLEEPINGTEXTURE;
                };
            }
        }
        if (dragon.isSleeping() || dragon.isBlinking()) {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.ICESTAGE1SLEEPINGTEXTURE;
                case 2 -> textures.ICESTAGE2SLEEPINGTEXTURE;
                case 3 -> textures.ICESTAGE3SLEEPINGTEXTURE;
                case 5 -> textures.ICESTAGE5SLEEPINGTEXTURE;
                default -> textures.ICESTAGE4SLEEPINGTEXTURE;
            };
        } else {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.ICESTAGE1TEXTURE;
                case 2 -> textures.ICESTAGE2TEXTURE;
                case 3 -> textures.ICESTAGE3TEXTURE;
                case 5 -> textures.ICESTAGE5TEXTURE;
                default -> textures.ICESTAGE4TEXTURE;
            };
        }
    }

    private static Identifier getLightningDragonTextures(EntityDragonBase dragon) {
        EnumDragonTextures textures = getDragonEnum(dragon);
        if (dragon.isModelDead()) {
            if (dragon.getDeathStage() >= (dragon.getAgeInDays() / 5) / 2) {
                return switch (dragon.getDragonStage()) {
                    case 1 -> textures.LIGHTNINGSTAGE1SKELETONTEXTURE;
                    case 2 -> textures.LIGHTNINGSTAGE2SKELETONTEXTURE;
                    case 3 -> textures.LIGHTNINGSTAGE3SKELETONTEXTURE;
                    case 5 -> textures.LIGHTNINGSTAGE5SKELETONTEXTURE;
                    default -> textures.LIGHTNINGSTAGE4SKELETONTEXTURE;
                };
            } else {
                return switch (dragon.getDragonStage()) {
                    case 1 -> textures.LIGHTNINGSTAGE1SLEEPINGTEXTURE;
                    case 2 -> textures.LIGHTNINGSTAGE2SLEEPINGTEXTURE;
                    case 3 -> textures.LIGHTNINGSTAGE3SLEEPINGTEXTURE;
                    case 5 -> textures.LIGHTNINGSTAGE5SLEEPINGTEXTURE;
                    default -> textures.LIGHTNINGSTAGE4SLEEPINGTEXTURE;
                };
            }
        }
        if (dragon.isSleeping() || dragon.isBlinking()) {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.LIGHTNINGSTAGE1SLEEPINGTEXTURE;
                case 2 -> textures.LIGHTNINGSTAGE2SLEEPINGTEXTURE;
                case 3 -> textures.LIGHTNINGSTAGE3SLEEPINGTEXTURE;
                case 5 -> textures.LIGHTNINGSTAGE5SLEEPINGTEXTURE;
                default -> textures.LIGHTNINGSTAGE4SLEEPINGTEXTURE;
            };
        } else {
            return switch (dragon.getDragonStage()) {
                case 1 -> textures.LIGHTNINGSTAGE1TEXTURE;
                case 2 -> textures.LIGHTNINGSTAGE2TEXTURE;
                case 3 -> textures.LIGHTNINGSTAGE3TEXTURE;
                case 5 -> textures.LIGHTNINGSTAGE5TEXTURE;
                default -> textures.LIGHTNINGSTAGE4TEXTURE;
            };
        }
    }


    public static EnumDragonTextures getDragonEnum(EntityDragonBase dragon) {
        return switch (dragon.getVariant()) {
            default -> VARIANT1;
            case 1 -> VARIANT2;
            case 2 -> VARIANT3;
            case 3 -> VARIANT4;
        };
    }

    public static Identifier getFireDragonSkullTextures(EntityDragonSkull skull) {
        return switch (skull.getDragonStage()) {
            case 1 -> VARIANT1.FIRESTAGE1SKELETONTEXTURE;
            case 2 -> VARIANT1.FIRESTAGE2SKELETONTEXTURE;
            case 3 -> VARIANT1.FIRESTAGE3SKELETONTEXTURE;
            case 5 -> VARIANT1.FIRESTAGE5SKELETONTEXTURE;
            default -> VARIANT1.FIRESTAGE4SKELETONTEXTURE;
        };
    }

    public static Identifier getIceDragonSkullTextures(EntityDragonSkull skull) {
        return switch (skull.getDragonStage()) {
            case 1 -> VARIANT1.ICESTAGE1SKELETONTEXTURE;
            case 2 -> VARIANT1.ICESTAGE2SKELETONTEXTURE;
            case 3 -> VARIANT1.ICESTAGE3SKELETONTEXTURE;
            case 5 -> VARIANT1.ICESTAGE5SKELETONTEXTURE;
            default -> VARIANT1.ICESTAGE4SKELETONTEXTURE;
        };
    }

    public static Identifier getLightningDragonSkullTextures(EntityDragonSkull skull) {
        return switch (skull.getDragonStage()) {
            case 1 -> VARIANT1.LIGHTNINGSTAGE1SKELETONTEXTURE;
            case 2 -> VARIANT1.LIGHTNINGSTAGE2SKELETONTEXTURE;
            case 3 -> VARIANT1.LIGHTNINGSTAGE3SKELETONTEXTURE;
            case 5 -> VARIANT1.LIGHTNINGSTAGE5SKELETONTEXTURE;
            default -> VARIANT1.LIGHTNINGSTAGE4SKELETONTEXTURE;
        };
    }

    public static Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        String name = "";
        if (!stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor armorItem)
            name = armorItem.type.getId();
        return switch (slot) {
            case MAINHAND, OFFHAND -> null;
            case FEET -> Identifier.of(IceAndFire.MOD_ID, "textures/models/dragon_armor/armor_tail_" + name + ".png");
            case LEGS -> Identifier.of(IceAndFire.MOD_ID, "textures/models/dragon_armor/armor_body_" + name + ".png");
            case CHEST -> Identifier.of(IceAndFire.MOD_ID, "textures/models/dragon_armor/armor_neck_" + name + ".png");
            case HEAD -> Identifier.of(IceAndFire.MOD_ID, "textures/models/dragon_armor/armor_head_" + name + ".png");
        };
    }
}
