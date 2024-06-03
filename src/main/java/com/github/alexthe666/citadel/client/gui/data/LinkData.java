package com.github.alexthe666.citadel.client.gui.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class LinkData {
    private String linked_page;
    private String text;
    private int x;
    private int y;
    private final int page;
    private final String item;
    private final String item_tag;

    public LinkData(String linkedPage, String titleText, int x, int y, int page) {
        this(linkedPage, titleText, x, y, page, null, null);
    }

    public LinkData(String linkedPage, String titleText, int x, int y, int page, String item, String itemTag) {
        this.linked_page = linkedPage;
        this.text = titleText;
        this.x = x;
        this.y = y;
        this.page = page;
        this.item = item;
        this.item_tag = itemTag;
    }

    public String getLinkedPage() {
        return linked_page;
    }

    public void setLinkedPage(String linkedPage) {
        this.linked_page = linkedPage;
    }

    public String getTitleText() {
        return text;
    }

    public void setTitleText(String titleText) {
        this.text = titleText;
    }

    public int getPage() {
        return page;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ItemStack getDisplayItem() {
        if (item == null || item.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = new ItemStack(Registries.ITEM.get(new Identifier(item)));
            if (item_tag != null && !item_tag.isEmpty()) {
                NbtCompound tag = null;
                try {
                    tag = StringNbtReader.parse(item_tag);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
                stack.setNbt(tag);
            }
            return stack;
        }
    }
}
