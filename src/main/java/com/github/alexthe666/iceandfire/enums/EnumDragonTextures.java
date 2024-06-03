package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public enum EnumDragonTextures {
    VARIANT1("red_", "blue_", "electric_"),
    VARIANT2("green_", "white_", "amythest_"),
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
        FIRESTAGE1TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "1.png");
        FIRESTAGE2TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "2.png");
        FIRESTAGE3TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "3.png");
        FIRESTAGE4TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "4.png");
        FIRESTAGE5TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "5.png");
        FIRESTAGE1SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "1_sleeping.png");
        FIRESTAGE2SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "2_sleeping.png");
        FIRESTAGE3SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "3_sleeping.png");
        FIRESTAGE4SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "4_sleeping.png");
        FIRESTAGE5SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "5_sleeping.png");
        FIRESTAGE1EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "1_eyes.png");
        FIRESTAGE2EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "2_eyes.png");
        FIRESTAGE3EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "3_eyes.png");
        FIRESTAGE4EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "4_eyes.png");
        FIRESTAGE5EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + fireVariant + "5_eyes.png");
        FIRESTAGE1SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/fire_skeleton_1.png");
        FIRESTAGE2SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/fire_skeleton_2.png");
        FIRESTAGE3SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/fire_skeleton_3.png");
        FIRESTAGE4SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/fire_skeleton_4.png");
        FIRESTAGE5SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/fire_skeleton_5.png");
        ICESTAGE1TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "1.png");
        ICESTAGE2TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "2.png");
        ICESTAGE3TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "3.png");
        ICESTAGE4TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "4.png");
        ICESTAGE5TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "5.png");
        ICESTAGE1SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "1_sleeping.png");
        ICESTAGE2SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "2_sleeping.png");
        ICESTAGE3SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "3_sleeping.png");
        ICESTAGE4SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "4_sleeping.png");
        ICESTAGE5SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "5_sleeping.png");
        ICESTAGE1EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "1_eyes.png");
        ICESTAGE2EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "2_eyes.png");
        ICESTAGE3EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "3_eyes.png");
        ICESTAGE4EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "4_eyes.png");
        ICESTAGE5EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + iceVariant + "5_eyes.png");
        ICESTAGE1SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/ice_skeleton_1.png");
        ICESTAGE2SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/ice_skeleton_2.png");
        ICESTAGE3SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/ice_skeleton_3.png");
        ICESTAGE4SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/ice_skeleton_4.png");
        ICESTAGE5SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/ice_skeleton_5.png");
        FIRE_MALE_OVERLAY = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/male_" + fireVariant.substring(0, fireVariant.length() - 1) + ".png");
        ICE_MALE_OVERLAY = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/male_" + iceVariant.substring(0, iceVariant.length() - 1) + ".png");

        LIGHTNINGSTAGE1TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "1.png");
        LIGHTNINGSTAGE2TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "2.png");
        LIGHTNINGSTAGE3TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "3.png");
        LIGHTNINGSTAGE4TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "4.png");
        LIGHTNINGSTAGE5TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "5.png");
        LIGHTNINGSTAGE1SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "1_sleeping.png");
        LIGHTNINGSTAGE2SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "2_sleeping.png");
        LIGHTNINGSTAGE3SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "3_sleeping.png");
        LIGHTNINGSTAGE4SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "4_sleeping.png");
        LIGHTNINGSTAGE5SLEEPINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "5_sleeping.png");
        LIGHTNINGSTAGE1EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "1_eyes.png");
        LIGHTNINGSTAGE2EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "2_eyes.png");
        LIGHTNINGSTAGE3EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "3_eyes.png");
        LIGHTNINGSTAGE4EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "4_eyes.png");
        LIGHTNINGSTAGE5EYESTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + lightningVariant + "5_eyes.png");
        LIGHTNINGSTAGE1SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/lightning_skeleton_1.png");
        LIGHTNINGSTAGE2SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/lightning_skeleton_2.png");
        LIGHTNINGSTAGE3SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/lightning_skeleton_3.png");
        LIGHTNINGSTAGE4SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/lightning_skeleton_4.png");
        LIGHTNINGSTAGE5SKELETONTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/lightning_skeleton_5.png");
        LIGHTNING_MALE_OVERLAY = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/male_" + lightningVariant.substring(0, lightningVariant.length() - 1) + ".png");

    }


    public static Identifier getTextureFromDragon(EntityDragonBase dragon) {
        if (dragon instanceof EntityIceDragon) {
            return getIceDragonTextures(dragon);
        } else if (dragon instanceof EntityLightningDragon) {
            return getLightningDragonTextures(dragon);
        } else {
            return getFireDragonTextures(dragon);
        }
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

    public enum Armor {
        EMPTY(""),
        ARMORBODY1("armor_body_1"),
        ARMORBODY2("armor_body_2"),
        ARMORBODY3("armor_body_3"),
        ARMORBODY4("armor_body_4"),
        ARMORBODY5("armor_body_5"),
        ARMORBODY6("armor_body_6"),
        ARMORBODY7("armor_body_7"),
        ARMORBODY8("armor_body_8"),
        ARMORHEAD1("armor_head_1"),
        ARMORHEAD2("armor_head_2"),
        ARMORHEAD3("armor_head_3"),
        ARMORHEAD4("armor_head_4"),
        ARMORHEAD5("armor_head_5"),
        ARMORHEAD6("armor_head_6"),
        ARMORHEAD7("armor_head_7"),
        ARMORHEAD8("armor_head_8"),
        ARMORNECK1("armor_neck_1"),
        ARMORNECK2("armor_neck_2"),
        ARMORNECK3("armor_neck_3"),
        ARMORNECK4("armor_neck_4"),
        ARMORNECK5("armor_neck_5"),
        ARMORNECK6("armor_neck_6"),
        ARMORNECK7("armor_neck_7"),
        ARMORNECK8("armor_neck_8"),
        ARMORTAIL1("armor_tail_1"),
        ARMORTAIL2("armor_tail_2"),
        ARMORTAIL3("armor_tail_3"),
        ARMORTAIL4("armor_tail_4"),
        ARMORTAIL5("armor_tail_5"),
        ARMORTAIL6("armor_tail_6"),
        ARMORTAIL7("armor_tail_7"),
        ARMORTAIL8("armor_tail_8");

        public final Identifier FIRETEXTURE;
        public final Identifier ICETEXTURE;
        public final Identifier LIGHTNINGTEXTURE;

        Armor(String resource) {
            if (!resource.isEmpty()) {
                FIRETEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/" + resource + ".png");
                ICETEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/icedragon/" + resource + ".png");
                LIGHTNINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lightningdragon/" + resource + ".png");
            } else {
                FIRETEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/empty.png");
                ICETEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/empty.png");
                LIGHTNINGTEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/firedragon/empty.png");
            }
        }

        public static Armor getArmorForDragon(EntityDragonBase dragon, EquipmentSlot slot) {
            int armor = dragon.getArmorOrdinal(dragon.getEquippedStack(slot));
            switch (slot) {
                case CHEST -> {
                    //neck
                    return switch (armor) {
                        default -> EMPTY;
                        case 1 -> ARMORNECK1;
                        case 2 -> ARMORNECK2;
                        case 3 -> ARMORNECK3;
                        case 4 -> ARMORNECK4;
                        case 5 -> ARMORNECK5;
                        case 6 -> ARMORNECK6;
                        case 7 -> ARMORNECK7;
                        case 8 -> ARMORNECK8;
                    };
                }
                case LEGS -> {
                    //body
                    return switch (armor) {
                        default -> EMPTY;
                        case 1 -> ARMORBODY1;
                        case 2 -> ARMORBODY2;
                        case 3 -> ARMORBODY3;
                        case 4 -> ARMORBODY4;
                        case 5 -> ARMORBODY5;
                        case 6 -> ARMORBODY6;
                        case 7 -> ARMORBODY7;
                        case 8 -> ARMORBODY8;
                    };
                }
                case FEET -> {
                    //tail
                    return switch (armor) {
                        default -> EMPTY;
                        case 1 -> ARMORTAIL1;
                        case 2 -> ARMORTAIL2;
                        case 3 -> ARMORTAIL3;
                        case 4 -> ARMORTAIL4;
                        case 5 -> ARMORTAIL5;
                        case 6 -> ARMORTAIL6;
                        case 7 -> ARMORTAIL7;
                        case 8 -> ARMORTAIL8;
                    };
                }
                default -> {
                    //head
                    return switch (armor) {
                        default -> EMPTY;
                        case 1 -> ARMORHEAD1;
                        case 2 -> ARMORHEAD2;
                        case 3 -> ARMORHEAD3;
                        case 4 -> ARMORHEAD4;
                        case 5 -> ARMORHEAD5;
                        case 6 -> ARMORHEAD6;
                        case 7 -> ARMORHEAD7;
                        case 8 -> ARMORHEAD8;
                    };
                }
            }
        }
    }
}
