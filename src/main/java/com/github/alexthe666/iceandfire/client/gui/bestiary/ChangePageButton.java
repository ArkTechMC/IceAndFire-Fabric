package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ChangePageButton extends ButtonWidget {
    private final boolean right;
    private final int color;

    public ChangePageButton(int x, int y, boolean right, int color, PressAction press) {
        super(x, y, 23, 10, Text.literal(""), press, DEFAULT_NARRATION_SUPPLIER);
        this.right = right;
        this.color = color;
    }

    @Override
    public void renderButton(DrawContext matrixStack, int mouseX, int mouseY, float partial) {
        if (this.active) {
            Identifier resourceLocation = new Identifier(IceAndFire.MOD_ID, "textures/gui/bestiary/widgets.png");
            boolean flag = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            int i = 0;
            int j = 64;
            if (flag) i += 23;
            if (!this.right) j += 13;
            j += this.color * 23;
            matrixStack.drawTexture(resourceLocation, this.getX(), this.getY(), i, j, this.width, this.height);
        }
    }
}