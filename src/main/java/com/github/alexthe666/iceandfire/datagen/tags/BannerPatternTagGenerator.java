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

    private static TagKey<BannerPattern> create(String name) {
        return TagKey.of(RegistryKeys.BANNER_PATTERN, new Identifier(IceAndFire.MOD_ID, name));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup pProvider) {
        assert IafBannerPatterns.PATTERN_FIRE.getKey() != null;
        this.getOrCreateTagBuilder(FIRE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FIRE.getKey());
        assert IafBannerPatterns.PATTERN_ICE.getKey() != null;
        this.getOrCreateTagBuilder(ICE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_ICE.getKey());
        assert IafBannerPatterns.PATTERN_LIGHTNING.getKey() != null;
        this.getOrCreateTagBuilder(LIGHTNING_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_LIGHTNING.getKey());
        assert IafBannerPatterns.PATTERN_FIRE_HEAD.getKey() != null;
        this.getOrCreateTagBuilder(FIRE_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FIRE_HEAD.getKey());
        assert IafBannerPatterns.PATTERN_ICE_HEAD.getKey() != null;
        this.getOrCreateTagBuilder(ICE_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_ICE_HEAD.getKey());
        assert IafBannerPatterns.PATTERN_LIGHTNING_HEAD.getKey() != null;
        this.getOrCreateTagBuilder(LIGHTNING_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_LIGHTNING_HEAD.getKey());
        assert IafBannerPatterns.PATTERN_AMPHITHERE.getKey() != null;
        this.getOrCreateTagBuilder(AMPHITHERE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_AMPHITHERE.getKey());
        assert IafBannerPatterns.PATTERN_BIRD.getKey() != null;
        this.getOrCreateTagBuilder(BIRD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_BIRD.getKey());
        assert IafBannerPatterns.PATTERN_EYE.getKey() != null;
        this.getOrCreateTagBuilder(EYE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_EYE.getKey());
        assert IafBannerPatterns.PATTERN_FAE.getKey() != null;
        this.getOrCreateTagBuilder(FAE_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FAE.getKey());
        assert IafBannerPatterns.PATTERN_FEATHER.getKey() != null;
        this.getOrCreateTagBuilder(FEATHER_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_FEATHER.getKey());
        assert IafBannerPatterns.PATTERN_GORGON.getKey() != null;
        this.getOrCreateTagBuilder(GORGON_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_GORGON.getKey());
        assert IafBannerPatterns.PATTERN_HIPPOCAMPUS.getKey() != null;
        this.getOrCreateTagBuilder(HIPPOCAMPUS_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_HIPPOCAMPUS.getKey());
        assert IafBannerPatterns.PATTERN_HIPPOGRYPH_HEAD.getKey() != null;
        this.getOrCreateTagBuilder(HIPPOGRYPH_HEAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_HIPPOGRYPH_HEAD.getKey());
        assert IafBannerPatterns.PATTERN_MERMAID.getKey() != null;
        this.getOrCreateTagBuilder(MERMAID_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_MERMAID.getKey());
        assert IafBannerPatterns.PATTERN_SEA_SERPENT.getKey() != null;
        this.getOrCreateTagBuilder(SEA_SERPENT_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_SEA_SERPENT.getKey());
        assert IafBannerPatterns.PATTERN_TROLL.getKey() != null;
        this.getOrCreateTagBuilder(TROLL_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_TROLL.getKey());
        assert IafBannerPatterns.PATTERN_WEEZER.getKey() != null;
        this.getOrCreateTagBuilder(WEEZER_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_WEEZER.getKey());
        assert IafBannerPatterns.PATTERN_DREAD.getKey() != null;
        this.getOrCreateTagBuilder(DREAD_BANNER_PATTERN).add(IafBannerPatterns.PATTERN_DREAD.getKey());
    }

    @Override
    public String getName() {
        return "Ice and Fire Banner Pattern Tags";
    }
}

