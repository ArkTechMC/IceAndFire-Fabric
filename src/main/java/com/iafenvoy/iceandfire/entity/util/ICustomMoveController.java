package com.iafenvoy.iceandfire.entity.util;

public interface ICustomMoveController {
    void up(boolean up);

    void down(boolean down);

    void attack(boolean attack);

    void strike(boolean strike);

    void dismount(boolean dismount);

    byte getControlState();

    void setControlState(byte state);
}
