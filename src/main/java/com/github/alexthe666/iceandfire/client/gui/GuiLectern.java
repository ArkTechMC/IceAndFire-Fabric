package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Random;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class GuiLectern extends HandledScreen<ContainerLectern> {
    private static final Identifier ENCHANTMENT_TABLE_GUI_TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/gui/lectern.png");
    private static final Identifier ENCHANTMENT_TABLE_BOOK_TEXTURE = new Identifier(IceAndFire.MOD_ID,"textures/models/lectern_book.png");
    private static BookModel bookModel;
    private final Random random = new Random();
    private final Text nameable;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private int flapTimer = 0;

    public GuiLectern(ContainerLectern container, PlayerInventory inv, Text name) {
        super(container, inv, name);
        this.nameable = name;
    }

    @Override
    protected void init() {
        super.init();
        bookModel = new BookModel(this.client.getEntityModelLoader().getModelPart(EntityModelLayers.BOOK));
    }

    @Override
    protected void drawForeground(@NotNull DrawContext ms, int mouseX, int mouseY) {
        TextRenderer font = this.client.textRenderer;
        font.draw(this.nameable.getString(), 12, 4, 4210752, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        font.draw(this.playerInventoryTitle, 8, this.backgroundHeight - 96 + 2, 4210752, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        this.handler.onUpdate();
        this.tickBook();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;

        for (int k = 0; k < 3; ++k) {
            double l = mouseX - (i + 60);
            double i1 = mouseY - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.handler.onButtonClick(getMinecraft().player, k)) {
                flapTimer = 5;
                this.getMinecraft().gameMode.handleInventoryButtonClick(this.handler.syncId, k);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawBackground(@NotNull DrawContext ms, float partialTicks, int mouseX, int mouseY) {
        DiffuseLighting.disableGuiDepthLighting();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        ms.drawTexture(ENCHANTMENT_TABLE_GUI_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int k = (int) this.client.getWindow().getScaleFactor();
        RenderSystem.viewport((this.width - 320) / 2 * k, (this.height - 240) / 2 * k, 320 * k, 240 * k);
        Matrix4f matrix4f = new Matrix4f().m03(-0.34F).m13(0.23F);
        matrix4f.mul(new Matrix4f().perspective(90.0F, 1.3333334F, 9.0F, 80.0F));
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(matrix4f, null);
        ms.getMatrices().push();
        ms.getMatrices().loadIdentity();
        ms.getMatrices().translate(0.0D, 3.3F, 1984.0D);
        float f = 5.0F;
        ms.getMatrices().scale(5.0F, 5.0F, 5.0F);
        ms.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        ms.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(20.0F));
        float f1 = MathHelper.lerp(partialTicks, this.oOpen, this.open);
        ms.getMatrices().translate(((1.0F - f1) * 0.2F), ((1.0F - f1) * 0.1F), ((1.0F - f1) * 0.25F));
        float f2 = -(1.0F - f1) * 90.0F - 90.0F;
        ms.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f2));
        ms.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        float f3 = MathHelper.lerp(partialTicks, this.oFlip, this.flip) + 0.25F;
        float f4 = MathHelper.lerp(partialTicks, this.oFlip, this.flip) + 0.75F;
        f3 = (f3 - (float) MathHelper.floor(f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float) MathHelper.floor(f4)) * 1.6F - 0.3F;
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        if (f4 < 0.0F) {
            f4 = 0.0F;
        }

        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        bookModel.setPageAngles(0, f3, f4, f1);
        VertexConsumerProvider.Immediate multibuffersource$buffersource = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(bookModel.getLayer(ENCHANTMENT_TABLE_BOOK_TEXTURE));
        bookModel.render(ms.getMatrices(), vertexconsumer, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        multibuffersource$buffersource.draw();
        ms.getMatrices().pop();
        RenderSystem.viewport(0, 0, this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
        RenderSystem.restoreProjectionMatrix();
        DiffuseLighting.enableGuiDepthLighting();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.handler.getManuscriptAmount();

        for (int i1 = 0; i1 < 3; ++i1) {
            int j1 = i + 60;
            int k1 = j1 + 20;
            int l1 = this.handler.getPossiblePages()[i1] == null ? -1 : this.handler.getPossiblePages()[i1].ordinal();//enchantment level
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (l1 == -1) {
                ms.drawTexture(ENCHANTMENT_TABLE_GUI_TEXTURE, j1, j + 14 + 19 * i1, 0, 185, 108, 19);
            } else {
                String s = "" + 3;
                TextRenderer fontrenderer = this.getMinecraft().font;
                String s1 = "";
                float textScale = 1.0F;
                EnumBestiaryPages enchantment = this.handler.getPossiblePages()[i1];
                if (enchantment != null) {
                    s1 = I18n.translate("bestiary." + enchantment.toString().toLowerCase());//EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, l1);
                    if (fontrenderer.getWidth(s1) > 80) {
                        textScale = 1.0F - (fontrenderer.getWidth(s1) - 80) * 0.01F;
                    }
                }
                int j2 = 6839882;
                if (IceAndFire.PROXY.getRefrencedTE() instanceof TileEntityLectern) {
                    IceAndFire.PROXY.getRefrencedTE();
                    if (handler.getSlot(0).getStack().getItem() == IafItemRegistry.BESTIARY.get()) { // Forge: render buttons as disabled when enchantable but enchantability not met on lower levels
                        int k2 = mouseX - (i + 60);
                        int l2 = mouseY - (j + 14 + 19 * i1);
                        int j3 = 0X9F988C;
                        if (k2 >= 0 && l2 >= 0 && k2 < 108 && l2 < 19) {
                            ms.drawTexture(ENCHANTMENT_TABLE_GUI_TEXTURE, j1, j + 14 + 19 * i1, 0, 204, 108, 19);
                            j2 = 16777088;
                            j3 = 16777088;
                        } else {
                            ms.drawTexture(ENCHANTMENT_TABLE_GUI_TEXTURE, j1, j + 14 + 19 * i1, 0, 166, 108, 19);
                        }

                        ms.drawTexture(ENCHANTMENT_TABLE_GUI_TEXTURE, j1 + 1, j + 15 + 19 * i1, 16 * i1, 223, 16, 16);
                        ms.getMatrices().push();
                        ms.getMatrices().translate(width / 2F - 10, height / 2F - 83 + (1.0F - textScale) * 55, 2);
                        ms.getMatrices().scale(textScale, textScale, 1);
                        fontrenderer.draw(s1, 0, 20 + 19 * i1, j2, false, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
                        ms.getMatrices().pop();
                        fontrenderer = this.getMinecraft().font;
                        fontrenderer.draw(s, k1 + 84 - fontrenderer.getWidth(s),
                            j + 13 + 19 * i1 + 7, j3, true, ms.getMatrices().peek().getPositionMatrix(), ms.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
                    } else {
                        ms.drawTexture(ENCHANTMENT_TABLE_GUI_TEXTURE, j1, j + 14 + 19 * i1, 0, 185, 108, 19);
                        ms.drawTexture(ENCHANTMENT_TABLE_GUI_TEXTURE, j1 + 1, j + 15 + 19 * i1, 16 * i1, 239, 16, 16);
                    }
                }
            }
        }
    }

    @Override
    public void render(@NotNull DrawContext ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.drawMouseoverTooltip(ms, mouseX, mouseY);
        boolean flag = this.getMinecraft().player.isCreative();
        int i = this.handler.getManuscriptAmount();

        for (int j = 0; j < 3; ++j) {
            int k = 1;
            EnumBestiaryPages enchantment = this.handler.getPossiblePages()[j];
            int i1 = 3;

            if (this.isPointWithinBounds(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0) {
                List<OrderedText> list = Lists.newArrayList();

                if (enchantment == null) {
                    list.add(Text.literal(Formatting.RED + I18n.translate("container.lectern.no_bestiary")).asOrderedText());
                } else if (!flag) {
                    list.add(Text.literal(String.valueOf(Formatting.WHITE) + Formatting.ITALIC + I18n.translate(enchantment == null ? "" : "bestiary." + enchantment.name().toLowerCase())).asOrderedText());
                    Formatting textformatting = i >= i1 ? Formatting.GRAY : Formatting.RED;
                    list.add(Text.literal(textformatting + I18n.translate("container.lectern.costs")).asOrderedText());
                    String s = I18n.translate("container.lectern.manuscript.many", i1);
                    list.add(Text.literal(textformatting + s).asOrderedText());
                }

                this.drawMouseoverTooltip(ms, mouseX, mouseY);
                break;
            }
        }
    }

    public void tickBook() {
        ItemStack itemstack = this.handler.getSlot(0).getStack();

        if (!ItemStack.areEqual(itemstack, this.last)) {
            this.last = itemstack;

            while (true) {
                this.flipT += this.random.nextInt(4) - this.random.nextInt(4);

                if (this.flip > this.flipT + 1.0F || this.flip < this.flipT - 1.0F) {
                    break;
                }
            }
        }

        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;

        for (int i = 0; i < 3; ++i) {
            if (this.handler.getPossiblePages()[i] != null) {
                flag = true;
            }
        }

        if (flag) {
            this.open += 0.2F;
        } else {
            this.open -= 0.2F;
        }

        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        if (flapTimer > 0) {
            f1 = (ticks + this.getMinecraft().getFrameTime()) * 0.5F;
            flapTimer--;
        }
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }

}