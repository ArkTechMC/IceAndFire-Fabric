package com.iafenvoy.iceandfire.screen.gui;

import com.google.common.collect.Lists;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.StaticVariables;
import com.iafenvoy.iceandfire.entity.util.MyrmexHive;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.screen.gui.bestiary.ChangePageButton;
import com.iafenvoy.iceandfire.screen.handler.MyrmexStaffScreenHandler;
import com.iafenvoy.iceandfire.world.MyrmexWorldData;
import com.iafenvoy.iceandfire.world.structure.MyrmexHiveStructure;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class MyrmexStaffScreen extends HandledScreen<MyrmexStaffScreenHandler> {
    private static final Identifier JUNGLE_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/myrmex_staff_jungle.png");
    private static final Identifier DESERT_TEXTURE = new Identifier(IceAndFire.MOD_ID, "textures/gui/myrmex_staff_desert.png");
    private static final MyrmexHiveStructure.RoomType[] ROOMS = {MyrmexHiveStructure.RoomType.FOOD, MyrmexHiveStructure.RoomType.NURSERY, MyrmexHiveStructure.RoomType.EMPTY};
    private static final int ROOMS_PER_PAGE = 5;
    private final List<Room> allRoomPos = Lists.newArrayList();
    private final List<MyrmexDeleteButton> allRoomButtonPos = Lists.newArrayList();
    private final boolean jungle;
    public ChangePageButton previousPage;
    public ChangePageButton nextPage;
    int ticksSinceDeleted = 0;
    int currentPage = 0;
    private int hiveCount;

    public MyrmexStaffScreen(MyrmexStaffScreenHandler handler, PlayerInventory playerInventory, Text name) {
        super(handler, playerInventory, name);
        this.jungle = handler.getStaff().getItem() == IafItems.MYRMEX_JUNGLE_STAFF;
    }

    @Override
    protected void init() {
        super.init();
        this.drawables.clear();
        this.allRoomButtonPos.clear();
        int i = (this.width - 248) / 2;
        int j = (this.height - 166) / 2;
        int x_translate = 193;
        int y_translate = 37;
        MyrmexHive hive = MyrmexWorldData.get(MinecraftClient.getInstance().world).getHiveFromUUID(this.handler.getTargetId());
        if (hive == null) return;
        this.populateRoomMap();
        this.addSelectableChild(ButtonWidget.builder(hive.reproduces ? Text.translatable("myrmex.message.disablebreeding") : Text.translatable("myrmex.message.enablebreeding"), (p_214132_1_) -> hive.reproduces = !hive.reproduces).position(i + 124, j + 15).size(120, 20).build());
        this.addSelectableChild(this.previousPage = new ChangePageButton(i + 5, j + 150, false, this.jungle ? 2 : 1, (p_214132_1_) -> {
            if (this.currentPage > 0) this.currentPage--;
        }));
        this.addSelectableChild(this.nextPage = new ChangePageButton(i + 225, j + 150, true, this.jungle ? 2 : 1, (p_214132_1_) -> {
            if (this.currentPage < this.allRoomButtonPos.size() / ROOMS_PER_PAGE) this.currentPage++;
        }));
        int totalRooms = this.allRoomPos.size();
        for (int rooms = 0; rooms < this.allRoomPos.size(); rooms++) {
            int yIndex = rooms % ROOMS_PER_PAGE;
            BlockPos pos = this.allRoomPos.get(rooms).pos;
            //IndexPageButton button = new IndexPageButton(2 + i, centerX + 15 + (xIndex * 200), centerY + 10 + (yIndex * 20) - (xIndex == 1 ? 20 : 0), StatCollector.translateToLocal("bestiary." + EnumBestiaryPages.values()[allPageTypes.get(i).ordinal()].toString().toLowerCase(Locale.ROOT)));
            MyrmexDeleteButton button = new MyrmexDeleteButton(i + x_translate, j + y_translate + (yIndex) * 22, pos, Text.translatable("myrmex.message.delete"), (p_214132_1_) -> {
                if (this.ticksSinceDeleted <= 0) {
                    hive.removeRoom(pos);
                    this.ticksSinceDeleted = 5;
                }
            });
            button.visible = rooms < ROOMS_PER_PAGE * (this.currentPage + 1) && rooms >= ROOMS_PER_PAGE * this.currentPage;
            this.addSelectableChild(button);
            this.allRoomButtonPos.add(button);
        }
        if (totalRooms <= ROOMS_PER_PAGE * (this.currentPage) && this.currentPage > 0)
            this.currentPage--;
    }

    private void populateRoomMap() {
        this.allRoomPos.clear();
        MyrmexHive hive = MyrmexWorldData.get(MinecraftClient.getInstance().world).getHiveFromUUID(this.handler.getTargetId());

        for (MyrmexHiveStructure.RoomType type : ROOMS) {
            List<BlockPos> roomPos = hive.getRooms(type);
            for (BlockPos pos : roomPos) {
                String name = type == MyrmexHiveStructure.RoomType.FOOD ? "food" : type == MyrmexHiveStructure.RoomType.NURSERY ? "nursery" : "misc";
                this.allRoomPos.add(new Room(pos, name));
                //this.buttonList.add(new MyrmexDeleteButton(buttons, i + x_translate, j + y_translate + (-1 + buttons) * 22, pos, I18n.format("myrmex.message.delete")));
            }
        }
        for (BlockPos pos : hive.getEntrances().keySet())
            this.allRoomPos.add(new Room(pos, "enterance_surface"));
        //this.buttonList.add(new MyrmexDeleteButton(buttons, i + x_translate, j + y_translate + (-1 + buttons) * 22, pos, I18n.format("myrmex.message.delete")));
        for (BlockPos pos : hive.getEntranceBottoms().keySet())
            this.allRoomPos.add(new Room(pos, "enterance_bottom"));
        //this.buttonList.add(new MyrmexDeleteButton(buttons, i + x_translate, j + y_translate + (-1 + buttons) * 22, pos, I18n.format("myrmex.message.delete")));
    }

    @Override
    public void renderBackground(DrawContext ms) {
        super.renderBackground(ms);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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
        if (this.ticksSinceDeleted > 0) this.ticksSinceDeleted--;
        this.hiveCount = 0;
        for (int rooms = 0; rooms < this.allRoomButtonPos.size(); rooms++)
            if (rooms < ROOMS_PER_PAGE * (this.currentPage + 1) && rooms >= ROOMS_PER_PAGE * this.currentPage)
                this.drawRoomInfo(ms, this.allRoomPos.get(rooms).string, this.allRoomPos.get(rooms).pos, i, j, color);
        MyrmexHive hive = MyrmexWorldData.get(MinecraftClient.getInstance().world).getHiveFromUUID(this.handler.getTargetId());
        if (hive != null) {
            assert this.client != null;
            TextRenderer textRenderer = this.client.textRenderer;
            if (!hive.colonyName.isEmpty()) {
                String title = I18n.translate("myrmex.message.colony_named", hive.colonyName);
                textRenderer.draw(title, i + 40 - (float) title.length() / 2, j - 3, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            } else
                textRenderer.draw(I18n.translate("myrmex.message.colony"), i + 80, j - 3, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            assert MinecraftClient.getInstance().player != null;
            int opinion = hive.getPlayerReputation(MinecraftClient.getInstance().player.getUuid());
            textRenderer.draw(I18n.translate("myrmex.message.hive_opinion", opinion), i, j + 12, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            textRenderer.draw(I18n.translate("myrmex.message.rooms"), i, j + 25, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            /*int hiveCount = 0;
            for (WorldGenMyrmexHive.RoomType type : ROOMS) {
                List<BlockPos> roomPos = hive.getRooms(type);
                String name = type == WorldGenMyrmexHive.RoomType.FOOD ? "myrmex.message.room.food" : type == WorldGenMyrmexHive.RoomType.NURSERY ? "myrmex.message.room.nursery" : "myrmex.message.room.misc";
                for (BlockPos pos : roomPos) {
                    hiveCount++;
                    this.fontRenderer.drawString(I18n.format(name, pos.getX(), pos.getY(), pos.getZ()), i, j + 16 + hiveCount * 22, color, true);
                }
            }
            for (BlockPos pos : hive.getEntrances().keySet()) {
                hiveCount++;
                this.fontRenderer.drawString(I18n.format("myrmex.message.room.enterance_surface", pos.getX(), pos.getY(), pos.getZ()), i, j + 16 + hiveCount * 22, color, true);
            }
            for (BlockPos pos : hive.getEntranceBottoms().keySet()) {
                hiveCount++;
                this.fontRenderer.drawString(I18n.format("myrmex.message.room.enterance_bottom", pos.getX(), pos.getY(), pos.getZ()), i, j + 16 + hiveCount * 22, color, true);
            }*/

        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Override
    public void removed() {
        MyrmexHive hive = MyrmexWorldData.get(MinecraftClient.getInstance().world).getHiveFromUUID(this.handler.getTargetId());
        if (hive != null) {
            PacketByteBuf buf = PacketByteBufs.create().writeNbt(hive.toNBT());
            ClientPlayNetworking.send(StaticVariables.MYRMEX_SYNC, buf);
        }
    }

    private void drawRoomInfo(DrawContext ms, String type, BlockPos pos, int i, int j, int color) {
        String translate = "myrmex.message.room." + type;
        assert this.client != null;
        this.client.textRenderer.draw(I18n.translate(translate, pos.getX(), pos.getY(), pos.getZ()), i, j + 36 + this.hiveCount * 22, color, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        this.hiveCount++;
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        //Remove texts.
    }

    private record Room(BlockPos pos, String string) {
    }
}
