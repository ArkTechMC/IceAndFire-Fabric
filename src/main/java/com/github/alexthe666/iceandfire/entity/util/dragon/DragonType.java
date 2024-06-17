package com.github.alexthe666.iceandfire.entity.util.dragon;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.registry.IafEntities;
import net.minecraft.entity.EntityType;

public class DragonType {
    public static final DragonType FIRE = new DragonType("fire");
    public static final DragonType ICE = new DragonType("ice").setPiscivore();
    public static final DragonType LIGHTNING = new DragonType("lightning");

    private String name;
    private boolean piscivore;

    public DragonType(String name) {
        this.name = name;
    }

    public static String getNameFromInt(int type) {
        return switch (type) {
            case 2 -> "lightning";
            case 1 -> "ice";
            default -> "fire";
        };
    }

    public int getIntFromType() {
        if (this == LIGHTNING) return 2;
        else if (this == ICE) return 1;
        else return 0;
    }

    public EntityType<? extends EntityDragonBase> getEntity() {
        if (this == LIGHTNING)
            return IafEntities.LIGHTNING_DRAGON;
        else if (this == ICE)
            return IafEntities.ICE_DRAGON;
        return IafEntities.FIRE_DRAGON;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPiscivore() {
        return this.piscivore;
    }

    public DragonType setPiscivore() {
        this.piscivore = true;
        return this;
    }
}
