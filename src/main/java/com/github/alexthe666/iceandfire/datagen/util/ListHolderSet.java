package com.github.alexthe666.iceandfire.datagen.util;

import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Optional;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;

public class ListHolderSet<T> extends RegistryEntryList.ListBacked<T> {
    List<RegistryEntry<T>> contents;
    public ListHolderSet(List<RegistryEntry<T>> contents) {
        this.contents = contents;
    }
    @Override
    protected List<RegistryEntry<T>> getEntries() {
        return this.contents;
    }

    @Override
    public Either<TagKey<T>, List<RegistryEntry<T>>> getStorage() {
        return Either.right(this.contents);
    }

    @Override
    public boolean contains(RegistryEntry<T> pHolder) {
        return contents.contains(pHolder);
    }

    @Override
    public Optional<TagKey<T>> getTagKey() {
        return Optional.empty();
    }
}
