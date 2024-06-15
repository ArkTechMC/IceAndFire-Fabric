package com.github.alexthe666.iceandfire.data.delegate;

import net.minecraft.screen.PropertyDelegate;

public class EntityPropertyDelegate implements PropertyDelegate {
    public int entityId = -1;

    public EntityPropertyDelegate() {
    }

    public EntityPropertyDelegate(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public int get(int index) {
        return this.entityId;
    }

    @Override
    public void set(int index, int value) {
        this.entityId = value;
    }

    @Override
    public int size() {
        return 1;
    }
}
