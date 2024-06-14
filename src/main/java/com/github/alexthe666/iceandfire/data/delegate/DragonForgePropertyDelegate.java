package com.github.alexthe666.iceandfire.data.delegate;

import net.minecraft.screen.PropertyDelegate;

public class DragonForgePropertyDelegate implements PropertyDelegate {
    public int fireType = 0, cookTime = 0;

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> this.fireType;
            case 1 -> this.cookTime;
            default -> 0;
        };
    }

    @Override
    public void set(int index, int value) {
        switch (index) {
            case 0 -> this.fireType = value;
            case 1 -> this.cookTime = value;
        }
    }

    @Override
    public int size() {
        return 2;
    }
}
