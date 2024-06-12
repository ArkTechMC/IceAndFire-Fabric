package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.arktechmc.iafextra.network.IafClientNetworkHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

public class GuiMyrmexAddRoom extends Screen {
    private static final Identifier JUNGLE_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/myrmex_staff_jungle.png");
    private static final Identifier DESERT_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/myrmex_staff_desert.png");
    private final boolean jungle;
    private final BlockPos interactPos;
    private final Direction facing;

    public GuiMyrmexAddRoom(ItemStack staff, BlockPos interactPos, Direction facing) {
        super(Text.translatable("myrmex_add_room"));
        this.jungle = staff.getItem() == IafItemRegistry.MYRMEX_JUNGLE_STAFF.get();
        this.interactPos = interactPos;
        this.facing = facing;
        this.init();
    }

    public static void onGuiClosed() {
        IafClientNetworkHandler.send(new MessageGetMyrmexHive(ClientProxy.getReferedClientHive().toNBT()));
    }

    @Override
    protected void init() {
        super.init();
        this.drawables.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        if (ClientProxy.getReferedClientHive() != null) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            this.addSelectableChild(
                    ButtonWidget.builder(
                                    Text.translatable("myrmex.message.establishroom_food"), (p_214132_1_) -> {
                                        ClientProxy.getReferedClientHive().addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.FOOD);
                                        onGuiClosed();
                                        MinecraftClient.getInstance().setScreen(null);
                                    })
                            .position(i + 50, j + 35)
                            .size(150, 20)
                            .build());
            this.addSelectableChild(
                    ButtonWidget.builder(
                                    Text.translatable("myrmex.message.establishroom_nursery"), (p_214132_1_) -> {
                                        ClientProxy.getReferedClientHive().addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.NURSERY);
                                        onGuiClosed();
                                        MinecraftClient.getInstance().setScreen(null);
                                    })
                            .position(i + 50, j + 60)
                            .size(150, 20)
                            .build());

            this.addSelectableChild(
                    ButtonWidget.builder(
                                    Text.translatable("myrmex.message.establishroom_enterance_surface"), (p_214132_1_) -> {
                                        ClientProxy.getReferedClientHive().addEnteranceWithMessage(player, false, this.interactPos, this.facing);
                                        onGuiClosed();
                                        MinecraftClient.getInstance().setScreen(null);
                                    })
                            .position(i + 50, j + 85)
                            .size(150, 20)
                            .build());
            this.addSelectableChild(
                    ButtonWidget.builder(
                                    Text.translatable("myrmex.message.establishroom_enterance_bottom"), (p_214132_1_) -> {
                                        ClientProxy.getReferedClientHive().addEnteranceWithMessage(player, true, this.interactPos, this.facing);
                                        onGuiClosed();
                                        MinecraftClient.getInstance().setScreen(null);
                                    })
                            .position(i + 50, j + 110)
                            .size(150, 20)
                            .build());
            this.addSelectableChild(
                    ButtonWidget.builder(Text.translatable("myrmex.message.establishroom_misc"), (p_214132_1_) -> {
                                ClientProxy.getReferedClientHive().addRoomWithMessage(player, this.interactPos, WorldGenMyrmexHive.RoomType.EMPTY);
                                onGuiClosed();
                                MinecraftClient.getInstance().setScreen(null);
                            })
                            .position(i + 50, j + 135)
                            .size(150, 20)
                            .build());
        }
    }

    @Override
    public void renderBackground(@NotNull DrawContext ms) {
        super.renderBackground(ms);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(this.jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE);
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        ms.drawTexture(this.jungle ? JUNGLE_TEXTURE : DESERT_TEXTURE, i, j, 0, 0, 248, 166);
    }

    @Override
    public void render(@NotNull DrawContext ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        this.init();
        int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        super.render(ms, mouseX, mouseY, partialTicks);
        int color = this.jungle ? 0X35EA15 : 0XFFBF00;
        TextRenderer textRenderer = this.client.textRenderer;
        if (ClientProxy.getReferedClientHive() != null) {
            if (!ClientProxy.getReferedClientHive().colonyName.isEmpty()) {
                String title = I18n.translate("myrmex.message.colony_named", ClientProxy.getReferedClientHive().colonyName);
                textRenderer.draw(title, i + 40 - (float) title.length() / 2, j - 3, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            } else {
                textRenderer.draw(I18n.translate("myrmex.message.colony"), i + 80, j - 3, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            }
            textRenderer.draw(I18n.translate("myrmex.message.create_new_room", this.interactPos.getX(), this.interactPos.getY(), this.interactPos.getZ()), i + 30, j + 6, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}
