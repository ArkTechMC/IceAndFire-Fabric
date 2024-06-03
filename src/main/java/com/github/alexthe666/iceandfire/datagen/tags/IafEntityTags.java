package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.vanilla.VanillaEntityTypeTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class IafEntityTags extends VanillaEntityTypeTagProvider {
    public static TagKey<EntityType<?>> IMMUNE_TO_GORGON_STONE = createKey("immune_to_gorgon_stone");

    public IafEntityTags(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> provider) {
        super(output, provider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup provider) {
        getOrCreateTagBuilder(IMMUNE_TO_GORGON_STONE)
                .addTag(Tags.EntityTypes.BOSSES)
                .add(EntityType.WARDEN);
    }

    private static TagKey<EntityType<?>> createKey(final String name) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, name));
    }
}
