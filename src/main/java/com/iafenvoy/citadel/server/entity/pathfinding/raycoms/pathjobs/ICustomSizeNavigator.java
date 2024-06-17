package com.iafenvoy.citadel.server.entity.pathfinding.raycoms.pathjobs;

public interface ICustomSizeNavigator {

    boolean isSmallerThanBlock();

    float getXZNavSize();

    int getYNavSize();
}
