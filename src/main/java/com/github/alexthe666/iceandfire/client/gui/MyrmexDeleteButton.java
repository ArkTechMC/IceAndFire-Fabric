package com.github.alexthe666.iceandfire.client.gui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class MyrmexDeleteButton extends ButtonWidget {
    public BlockPos pos;

    public MyrmexDeleteButton(int x, int y, BlockPos pos, Text delete, PressAction onPress) {
        super(x, y, 50, 20, delete, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.pos = pos;
    }
}
