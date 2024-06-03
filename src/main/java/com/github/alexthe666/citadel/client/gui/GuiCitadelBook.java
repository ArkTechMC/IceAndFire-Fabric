package com.github.alexthe666.citadel.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class GuiCitadelBook extends GuiBasicBook {

    public GuiCitadelBook(ItemStack bookStack) {
        super(bookStack, Text.translatable("citadel_guide_book.title"));
    }

    @Override
    protected int getBindingColor() {
        return 0X64A27B;
    }

    @Override
    public Identifier getRootPage() {
        return new Identifier("citadel:book/citadel_book/root.json");
    }

    @Override
    public String getTextFileDirectory() {
        return "citadel:book/citadel_book/";
    }
}
