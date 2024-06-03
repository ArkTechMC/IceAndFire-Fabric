package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.recipe.IafBannerPatterns;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class BannerPatternTagGenerator extends TagProvider<BannerPattern> {
    public static final TagKey<BannerPattern> FIRE_BANNER_PATTERN = create("pattern_item/fire");
    public static final TagKey<BannerPattern> ICE_BANNER_PATTERN = create("pattern_item/ice");
    public static final TagKey<BannerPattern> LIGHTNING_BANNER_PATTERN = create("pattern_item/lightning");
    public static final TagKey<BannerPattern> FIRE_HEAD_BANNER_PATTERN = create("pattern_item/fire_head");
    public static final TagKey<BannerPattern> ICE_HEAD_BANNER_PATTERN = create("pattern_item/ice_head");
    public static final TagKey<BannerPattern> LIGHTNING_HEAD_BANNER_PATTERN = create("pattern_item/lightning_head");
    public static final TagKey<BannerPattern> AMPHITHERE_BANNER_PATTERN = create("pattern_item/amphithere");
    public static final TagKey<BannerPattern> BIRD_BANNER_PATTERN = create("pattern_item/bird");
    public static final TagKey<BannerPattern> EYE_BANNER_PATTERN = create("pattern_item/eye");
    public static final TagKey<BannerPattern> FAE_BANNER_PATTERN = create("pattern_item/fae");
    public static final TagKey<BannerPattern> FEATHER_BANNER_PATTERN = create("pattern_item/feather");
    public static final TagKey<BannerPattern> GORGON_BANNER_PATTERN = create("pattern_item/gorgon");
    public static final TagKey<BannerPattern> HIPPOCAMPUS_BANNER_PATTERN = create("pattern_item/hippocampus");
    public static final TagKey<BannerPattern> HIPPOGRYPH_HEAD_BANNER_PATTERN = create("pattern_item/hippogryph_head");
    public static final TagKey<BannerPattern> MERMAID_BANNER_PATTERN = create("pattern_item/mermaid");
    public static final TagKey<BannerPattern> SEA_SERPENT_BANNER_PATTERN = create("pattern_item/sea_serpent");
    public static final TagKey<BannerPattern> TROLL_BANNER_PATTERN = create("pattern_item/troll");
    public static final TagKey<BannerPattern> WEEZER_BANNER_PATTERN = create("pattern_item/weezer");
    public static final TagKey<BannerPattern> DREAD_BANNER_PATTERN = create("pattern_item/dread");

    public BannerPatternTagGenerator(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> provider) {
        super(output, RegistryKeys.BANNER_PATTERN, provider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup pProvider) {
        this.getOrCreateTagBuilder(FIRE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FIRE.getKey());
        this.getOrCreateTagBuilder(ICE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_ICE.getKey());
        this.getOrCreateTagBuilder(LIGHTNING_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_LIGHTNING.getKey());
        this.getOrCreateTagBuilder(FIRE_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FIRE_HEAD.getKey());
        this.getOrCreateTagBuilder(ICE_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_ICE_HEAD.getKey());
        this.getOrCreateTagBuilder(LIGHTNING_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_LIGHTNING_HEAD.getKey());
        this.getOrCreateTagBuilder(AMPHITHERE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_AMPHITHERE.getKey());
        this.getOrCreateTagBuilder(BIRD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_BIRD.getKey());
        this.getOrCreateTagBuilder(EYE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_EYE.getKey());
        this.getOrCreateTagBuilder(FAE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FAE.getKey());
        this.getOrCreateTagBuilder(FEATHER_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FEATHER.getKey());
        this.getOrCreateTagBuilder(GORGON_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_GORGON.getKey());
        this.getOrCreateTagBuilder(HIPPOCAMPUS_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_HIPPOCAMPUS.getKey());
        this.getOrCreateTagBuilder(HIPPOGRYPH_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_HIPPOGRYPH_HEAD.getKey());
        this.getOrCreateTagBuilder(MERMAID_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_MERMAID.getKey());
        this.getOrCreateTagBuilder(SEA_SERPENT_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_SEA_SERPENT.getKey());
        this.getOrCreateTagBuilder(TROLL_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_TROLL.getKey());
        this.getOrCreateTagBuilder(WEEZER_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_WEEZER.getKey());
        this.getOrCreateTagBuilder(DREAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_DREAD.getKey());
    }

    private static TagKey<BannerPattern> create(String name) {
        return TagKey.of(RegistryKeys.BANNER_PATTERN, new Identifier(IceAndFire.MOD_ID, name));
    }

    @Override
    public String getName() {
        return "Ice and Fire Banner Pattern Tags";
    }
}

