package com.github.alexthe666.citadel.server.generation;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import java.util.ArrayList;
import java.util.List;

public class SurfaceRulesManager {
    private static final List<MaterialRules.MaterialRule> OVERWORLD_REGISTRY = new ArrayList<>();
    private static final List<MaterialRules.MaterialRule> NETHER_REGISTRY = new ArrayList<>();
    private static final List<MaterialRules.MaterialRule> END_REGISTRY = new ArrayList<>();
    private static final List<MaterialRules.MaterialRule> CAVE_REGISTRY = new ArrayList<>();

    public SurfaceRulesManager() {
    }

    public static void registerOverworldSurfaceRule(MaterialRules.MaterialCondition condition, MaterialRules.MaterialRule rule) {
        registerOverworldSurfaceRule(MaterialRules.condition(condition, rule));
    }

    public static void registerOverworldSurfaceRule(MaterialRules.MaterialRule rule) {
        OVERWORLD_REGISTRY.add(rule);
    }

    public static void registerNetherSurfaceRule(MaterialRules.MaterialCondition condition, MaterialRules.MaterialRule rule) {
        registerNetherSurfaceRule(MaterialRules.condition(condition, rule));
    }

    public static void registerNetherSurfaceRule(MaterialRules.MaterialRule rule) {
        NETHER_REGISTRY.add(rule);
    }

    public static void registerEndSurfaceRule(MaterialRules.MaterialCondition condition, MaterialRules.MaterialRule rule) {
        registerEndSurfaceRule(MaterialRules.condition(condition, rule));
    }

    public static void registerEndSurfaceRule(MaterialRules.MaterialRule rule) {
        END_REGISTRY.add(rule);
    }

    public static void registerCaveSurfaceRule(MaterialRules.MaterialCondition condition, MaterialRules.MaterialRule rule) {
        registerCaveSurfaceRule(MaterialRules.condition(condition, rule));
    }

    public static void registerCaveSurfaceRule(MaterialRules.MaterialRule rule) {
        CAVE_REGISTRY.add(rule);
    }

    public static MaterialRules.MaterialRule mergeRules(MaterialRules.MaterialRule prev, List<MaterialRules.MaterialRule> toMerge) {
        ImmutableList.Builder<MaterialRules.MaterialRule> builder = ImmutableList.builder();
        builder.addAll(toMerge);
        builder.add(prev);
        return MaterialRules.sequence(builder.build().toArray(MaterialRules.MaterialRule[]::new));
    }

    public static MaterialRules.MaterialRule mergeOverworldRules(MaterialRules.MaterialRule rulesIn) {
        return mergeRules(rulesIn, OVERWORLD_REGISTRY);
    }
}