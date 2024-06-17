package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class IafBannerPatterns {
    public static final BannerPattern PATTERN_FIRE = register("fire", new BannerPattern("iaf_fire"));
    public static final BannerPattern PATTERN_ICE = register("ice", new BannerPattern("iaf_ice"));
    public static final BannerPattern PATTERN_LIGHTNING = register("lightning", new BannerPattern("iaf_lightning"));
    public static final BannerPattern PATTERN_FIRE_HEAD = register("fire_head", new BannerPattern("iaf_fire_head"));
    public static final BannerPattern PATTERN_ICE_HEAD = register("ice_head", new BannerPattern("iaf_ice_head"));
    public static final BannerPattern PATTERN_LIGHTNING_HEAD = register("lightning_head", new BannerPattern("iaf_lightning_head"));
    public static final BannerPattern PATTERN_AMPHITHERE = register("amphithere", new BannerPattern("iaf_amphithere"));
    public static final BannerPattern PATTERN_BIRD = register("bird", new BannerPattern("iaf_bird"));
    public static final BannerPattern PATTERN_EYE = register("eye", new BannerPattern("iaf_eye"));
    public static final BannerPattern PATTERN_FAE = register("fae", new BannerPattern("iaf_fae"));
    public static final BannerPattern PATTERN_FEATHER = register("feather", new BannerPattern("iaf_feather"));
    public static final BannerPattern PATTERN_GORGON = register("gorgon", new BannerPattern("iaf_gorgon"));
    public static final BannerPattern PATTERN_HIPPOCAMPUS = register("hippocampus", new BannerPattern("iaf_hippocampus"));
    public static final BannerPattern PATTERN_HIPPOGRYPH_HEAD = register("hippogryph_head", new BannerPattern("iaf_hippogryph_head"));
    public static final BannerPattern PATTERN_MERMAID = register("mermaid", new BannerPattern("iaf_mermaid"));
    public static final BannerPattern PATTERN_SEA_SERPENT = register("sea_serpent", new BannerPattern("iaf_sea_serpent"));
    public static final BannerPattern PATTERN_TROLL = register("troll", new BannerPattern("iaf_troll"));
    public static final BannerPattern PATTERN_WEEZER = register("weezer", new BannerPattern("iaf_weezer"));
    public static final BannerPattern PATTERN_DREAD = register("dread", new BannerPattern("iaf_dread"));

    public static BannerPattern register(String name, BannerPattern pattern) {
        return Registry.register(Registries.BANNER_PATTERN, new Identifier(IceAndFire.MOD_ID, name), pattern);
    }

    public static void init() {
    }
}
