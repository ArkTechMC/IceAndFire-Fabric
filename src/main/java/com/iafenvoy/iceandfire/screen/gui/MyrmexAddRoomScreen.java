package com.iafenvoy.iceandfire.screen.gui;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.screen.handler.MyrmexAddRoomScreenHandler;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import com.iafenvoy.iceandfire.world.structure.MyrmexHiveStructure;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MyrmexAddRoomScreen extends HandledScreen<MyrmexAddRoomScreenHandler> {
    private static final Identifier JUNGLE_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/myrmex_staff_jungle.png");
    private static final Identifier DESERT_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/myrmex_staff_desert.png");
    private final boolean jungle;

    public MyrmexAddRoomScreen(MyrmexAddRoomScreenHandler container, PlayerInventory inv, Text name) {
        super(container, inv, name);
        this.jungle = container.getStaff().isOf(IafItems.MYRMEX_JUNGLE_STAFF);
        this.init();
    }

    @Override
    protected void init() {
        super.init();
        this.drawables.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        MyrmexHive hive = MyrmexWorldData.get(MinecraftClient.getInstance().world).getHiveFromUUID(this.handler.getTargetId());
        if (hive != null) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            this.addSelectableChild(ButtonWidget.builder(Text.translatable("myrmex.message.establishroom_food"), (widget) -> {
                hive.addRoomWithMessage(player, this.handler.getInteractPos(), MyrmexHiveStructure.RoomType.FOOD);
                MinecraftClient.getInstance().setScreen(null);
            }).position(i + 50, j + 35).size(150, 20).build());
            this.addSelectableChild(ButtonWidget.builder(Text.translatable("myrmex.message.establishroom_nursery"), (widget) -> {
                hive.addRoomWithMessage(player, this.handler.getInteractPos(), MyrmexHiveStructure.RoomType.NURSERY);
                MinecraftClient.getInstance().setScreen(null);
            }).position(i + 50, j + 60).size(150, 20).build());
            this.addSelectableChild(ButtonWidget.builder(Text.translatable("myrmex.message.establishroom_enterance_surface"), (widget) -> {
                hive.addEnteranceWithMessage(player, false, this.handler.getInteractPos(), this.handler.getFacing());
                MinecraftClient.getInstance().setScreen(null);
            }).position(i + 50, j + 85).size(150, 20).build());
            this.addSelectableChild(ButtonWidget.builder(Text.translatable("myrmex.message.establishroom_enterance_bottom"), (widget) -> {
                hive.addEnteranceWithMessage(player, true, this.handler.getInteractPos(), this.handler.getFacing());
                MinecraftClient.getInstance().setScreen(null);
            }).position(i + 50, j + 110).size(150, 20).build());
            this.addSelectableChild(ButtonWidget.builder(Text.translatable("myrmex.message.establishroom_misc"), (widget) -> {
                hive.addRoomWithMessage(player, this.handler.getInteractPos(), MyrmexHiveStructure.RoomType.EMPTY);
                MinecraftClient.getInstance().setScreen(null);
            }).position(i + 50, j + 135).size(150, 20).build());
        }
    }

    @Override
    public void renderBackground(DrawContext ms) {
        super.renderBackground(ms);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.client != null;
        this.client.getTextureManager().bindTexture(this.jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        ms.drawTexture(this.jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE, i, j, 0, 0, 248, 166);
    }

    @Override
    public void render(DrawContext ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        this.init();
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        super.render(ms, mouseX, mouseY, partialTicks);
        int color = this.jungle ? 0X35EA15 : 0XFFBF00;
        assert this.client != null;
        TextRenderer textRenderer = this.client.textRenderer;
        MyrmexHive hive = MyrmexWorldData.get(MinecraftClient.getInstance().world).getHiveFromUUID(this.handler.getTargetId());
        if (hive != null) {
            if (!hive.colonyName.isEmpty()) {
                String title = I18n.translate("myrmex.message.colony_named", hive.colonyName);
                textRenderer.draw(title, i + 40 - (float) title.length() / 2, j - 3, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            } else
                textRenderer.draw(I18n.translate("myrmex.message.colony"), i + 80, j - 3, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            textRenderer.draw(I18n.translate("myrmex.message.create_new_room", this.handler.getInteractPos().getX(), this.handler.getInteractPos().getY(), this.handler.getInteractPos().getZ()), i + 30, j + 6, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        //Remove texts.
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
