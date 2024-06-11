package com.github.alexthe666.citadel.client.gui.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class LinkData {
    private final int page;
    private final String item;
    private final String itemTag;
    private String linkedPage;
    private String text;
    private int x;
    private int y;

    public LinkData(String linkedPage, String titleText, int x, int y, int page) {
        this(linkedPage, titleText, x, y, page, null, null);
    }

    public LinkData(String linkedPage, String titleText, int x, int y, int page, String item, String itemTag) {
        this.linkedPage = linkedPage;
        this.text = titleText;
        this.x = x;
        this.y = y;
        this.page = page;
        this.item = item;
        this.itemTag = itemTag;
    }

    public String getLinkedPage() {
        return this.linkedPage;
    }

    public void setLinkedPage(String linkedPage) {
        this.linkedPage = linkedPage;
    }

    public String getTitleText() {
        return this.text;
    }

    public void setTitleText(String titleText) {
        this.text = titleText;
    }

    public int getPage() {
        return this.page;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ItemStack getDisplayItem() {
        if (this.item == null || this.item.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = new ItemStack(Registries.ITEM.get(new Identifier(this.item)));
            if (this.itemTag != null && !this.itemTag.isEmpty()) {
                NbtCompound tag = null;
                try {
                    tag = StringNbtReader.parse(this.itemTag);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
                stack.setNbt(tag);
            }
            return stack;
        }
    }
}
