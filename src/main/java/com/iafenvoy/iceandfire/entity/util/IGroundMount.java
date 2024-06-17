package com.iafenvoy.iceandfire.entity.util;

import net.minecraft.entity.player.PlayerEntity;

public interface IGroundMount {
    PlayerEntity getRidingPlayer();

    double getRideSpeedModifier();
}
