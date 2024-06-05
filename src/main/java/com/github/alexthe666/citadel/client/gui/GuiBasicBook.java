package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.gui.data.*;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.TabulaModelHandler;
import com.github.alexthe666.citadel.recipe.SpecialRecipeInGuideBook;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class GuiBasicBook extends Screen {
    private static final Identifier BOOK_PAGE_TEXTURE = new Identifier("citadel:textures/gui/book/book_pages.png");
    private static final Identifier BOOK_BINDING_TEXTURE = new Identifier("citadel:textures/gui/book/book_binding.png");
    private static final Identifier BOOK_WIDGET_TEXTURE = new Identifier("citadel:textures/gui/book/widgets.png");
    private static final Identifier BOOK_BUTTONS_TEXTURE = new Identifier("citadel:textures/gui/book/link_buttons.png");
    protected final List<LineData> lines = new ArrayList<>();
    protected final List<LinkData> links = new ArrayList<>();
    protected final List<ItemRenderData> itemRenders = new ArrayList<>();
    protected final List<RecipeData> recipes = new ArrayList<>();
    protected final List<TabulaRenderData> tabulaRenders = new ArrayList<>();
    protected final List<EntityRenderData> entityRenders = new ArrayList<>();
    protected final List<EntityLinkData> entityLinks = new ArrayList<>();
    protected final List<ImageData> images = new ArrayList<>();
    protected final List<Whitespace> yIndexesToSkip = new ArrayList<>();
    private final Map<String, TabulaModel> renderedTabulaModels = new HashMap<>();
    private final Map<String, Entity> renderedEntites = new HashMap<>();
    private final Map<String, Identifier> textureMap = new HashMap<>();
    protected ItemStack bookStack;
    protected int xSize = 390;
    protected int ySize = 320;
    protected int currentPageCounter = 0;
    protected int maxPagesFromPrinting = 0;
    protected int linesFromJSON = 0;
    protected int linesFromPrinting = 0;
    protected Identifier prevPageJSON;
    protected Identifier currentPageJSON;
    protected Identifier currentPageText = null;
    protected BookPageButton buttonNextPage;
    protected BookPageButton buttonPreviousPage;
    protected BookPage internalPage = null;
    protected String writtenTitle = "";
    protected int preservedPageIndex = 0;
    protected String entityTooltip;
    private int mouseX;
    private int mouseY;

    public GuiBasicBook(ItemStack bookStack, Text title) {
        super(title);
        this.bookStack = bookStack;
        this.currentPageJSON = this.getRootPage();
    }

    public static void drawTabulaModelOnScreen(DrawContext guiGraphics, TabulaModel model, Identifier tex, int posX, int posY, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY) {
        float f = (float) Math.atan(mouseX / 40.0F);
        float f1 = (float) Math.atan(mouseY / 40.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate((float) posX, (float) posY, 120.0D);
        matrixstack.scale(scale, scale, scale);
        Quaternionf quaternion = RotationAxis.POSITIVE_Z.rotationDegrees(0.0F);
        Quaternionf quaternion1 = RotationAxis.POSITIVE_X.rotationDegrees(f1 * 20.0F);
        if (follow) {
            quaternion.mul(quaternion1);
        }
        matrixstack.multiply(quaternion);
        if (follow) {
            matrixstack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F + f * 40.0F));
        }
        matrixstack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) -xRot));
        matrixstack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) yRot));
        matrixstack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) zRot));
        EntityRenderDispatcher entityrenderermanager = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderermanager.setRotation(quaternion1);
        entityrenderermanager.setRenderShadows(false);
        VertexConsumerProvider.Immediate irendertypebuffer$impl = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            VertexConsumer ivertexbuilder = irendertypebuffer$impl.getBuffer(RenderLayer.getEntityCutoutNoCull(tex));
            model.resetToDefaultPose();
            model.render(matrixstack, ivertexbuilder, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        });
        DiffuseLighting.enableGuiDepthLighting();
    }

    public void drawEntityOnScreen(DrawContext guiGraphics, VertexConsumerProvider bufferSource, int posX, int posY, float zOff, float scale, boolean follow, double xRot, double yRot, double zRot, float mouseX, float mouseY, Entity entity) {
        float customYaw = posX - mouseX;
        float customPitch = posY - mouseY;
        float f = (float) Math.atan(customYaw / 40.0F);
        float f1 = (float) Math.atan(customPitch / 40.0F);

        if (follow) {
            float setX = f1 * 20.0F;
            float setY = f * 20.0F;
            entity.setPitch(setX);
            entity.setYaw(setY);
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).bodyYaw = setY;
                ((LivingEntity) entity).prevBodyYaw = setY;
                ((LivingEntity) entity).headYaw = setY;
                ((LivingEntity) entity).prevHeadYaw = setY;
            }
        } else {
            f = 0;
            f1 = 0;
        }

        guiGraphics.getMatrices().push();
        guiGraphics.getMatrices().translate(posX, posY, zOff);
        guiGraphics.getMatrices().multiplyPositionMatrix((new Matrix4f()).scaling(scale, scale, -scale));
        Quaternionf quaternion = RotationAxis.POSITIVE_Z.rotationDegrees(180F);
        Quaternionf quaternion1 = RotationAxis.POSITIVE_X.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        quaternion.mul(RotationAxis.NEGATIVE_X.rotationDegrees((float) xRot));
        quaternion.mul(RotationAxis.POSITIVE_Y.rotationDegrees((float) yRot));
        quaternion.mul(RotationAxis.POSITIVE_Z.rotationDegrees((float) zRot));
        guiGraphics.getMatrices().multiply(quaternion);

        Vector3f light0 = new Vector3f(1, -1.0F, -1.0F).normalize();
        Vector3f light1 = new Vector3f(-1, 1.0F, 1.0F).normalize();
        RenderSystem.setShaderLights(light0, light1);
        EntityRenderDispatcher entityrenderdispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderdispatcher.setRotation(quaternion1);
        entityrenderdispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, guiGraphics.getMatrices(), bufferSource, 240);
        });
        entityrenderdispatcher.setRenderShadows(true);
        entity.setYaw(0);
        entity.setPitch(0);
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).bodyYaw = 0;
            ((LivingEntity) entity).prevHeadYaw = 0;
            ((LivingEntity) entity).headYaw = 0;
        }

        guiGraphics.draw();
        entityrenderdispatcher.setRenderShadows(true);
        guiGraphics.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

    protected void init() {
        super.init();
        this.playBookOpeningSound();
        this.addNextPreviousButtons();
        this.addLinkButtons();
    }

    private void addNextPreviousButtons() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;
        this.buttonPreviousPage = this.addDrawableChild(new BookPageButton(this, k + 10, l + 180, false, (p_214208_1_) -> {
            this.onSwitchPage(false);
        }, true));
        this.buttonNextPage = this.addDrawableChild(new BookPageButton(this, k + 365, l + 180, true, (p_214205_1_) -> {
            this.onSwitchPage(true);
        }, true));
    }

    private void addLinkButtons() {
        this.drawables.clear();
        this.clearChildren();
        this.addNextPreviousButtons();
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;

        for (LinkData linkData : this.links) {
            if (linkData.getPage() == this.currentPageCounter) {
                int maxLength = Math.max(100, MinecraftClient.getInstance().textRenderer.getWidth(linkData.getTitleText()) + 20);
                this.yIndexesToSkip.add(new Whitespace(linkData.getPage(), linkData.getX() - maxLength / 2, linkData.getY(), 100, 20));
                this.addDrawableChild(new LinkButton(this, k + linkData.getX() - maxLength / 2, l + linkData.getY(), maxLength, 20, Text.translatable(linkData.getTitleText()), linkData.getDisplayItem(), (p_213021_1_) -> {
                    this.prevPageJSON = this.currentPageJSON;
                    this.currentPageJSON = new Identifier(this.getTextFileDirectory() + linkData.getLinkedPage());
                    this.preservedPageIndex = this.currentPageCounter;
                    this.currentPageCounter = 0;
                    this.addNextPreviousButtons();
                }));
            }
            if (linkData.getPage() > this.maxPagesFromPrinting) {
                this.maxPagesFromPrinting = linkData.getPage();
            }
        }

        for (EntityLinkData linkData : this.entityLinks) {
            if (linkData.getPage() == this.currentPageCounter) {
                this.yIndexesToSkip.add(new Whitespace(linkData.getPage(), linkData.getX() - 12, linkData.getY(), 100, 20));
                this.addDrawableChild(new EntityLinkButton(this, linkData, k, l, (p_213021_1_) -> {
                    this.prevPageJSON = this.currentPageJSON;
                    this.currentPageJSON = new Identifier(this.getTextFileDirectory() + linkData.getLinkedPage());
                    this.preservedPageIndex = this.currentPageCounter;
                    this.currentPageCounter = 0;
                    this.addNextPreviousButtons();
                }));
            }
            if (linkData.getPage() > this.maxPagesFromPrinting) {
                this.maxPagesFromPrinting = linkData.getPage();
            }
        }
    }

    private void onSwitchPage(boolean next) {
        if (next) {
            if (this.currentPageCounter < this.maxPagesFromPrinting) {
                this.currentPageCounter++;
            }
        } else {
            if (this.currentPageCounter > 0) {
                this.currentPageCounter--;
            } else {
                if (this.internalPage != null && !this.internalPage.getParent().isEmpty()) {
                    this.prevPageJSON = this.currentPageJSON;
                    this.currentPageJSON = new Identifier(this.getTextFileDirectory() + this.internalPage.getParent());
                    this.currentPageCounter = this.preservedPageIndex;
                    this.preservedPageIndex = 0;
                }
            }
        }
        this.refreshSpacing();
    }

    @Override
    public void render(DrawContext guiGraphics, int x, int y, float partialTicks) {
        this.mouseX = x;
        this.mouseY = y;
        int bindingColor = this.getBindingColor();
        int bindingR = bindingColor >> 16 & 255;
        int bindingG = bindingColor >> 8 & 255;
        int bindingB = bindingColor & 255;
        this.renderBackground(guiGraphics);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;
        BookBlit.blitWithColor(guiGraphics, this.getBookBindingTexture(), k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize, bindingR, bindingG, bindingB, 255);
        BookBlit.blitWithColor(guiGraphics, this.getBookPageTexture(), k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize, 255, 255, 255, 255);
        if (this.internalPage == null || this.currentPageJSON != this.prevPageJSON || this.prevPageJSON == null) {
            this.internalPage = this.generatePage(this.currentPageJSON);
            if (this.internalPage != null) {
                this.refreshSpacing();
            }
        }
        if (this.internalPage != null) {
            this.writePageText(guiGraphics, x, y);
        }
        super.render(guiGraphics, x, y, partialTicks);
        this.prevPageJSON = this.currentPageJSON;
        if (this.internalPage != null) {
            guiGraphics.getMatrices().push();
            this.renderOtherWidgets(guiGraphics, x, y, this.internalPage);
            guiGraphics.getMatrices().pop();
        }
        if (this.entityTooltip != null) {
            guiGraphics.getMatrices().push();
            guiGraphics.getMatrices().translate(0, 0, 550);
            guiGraphics.drawOrderedTooltip(this.textRenderer, MinecraftClient.getInstance().textRenderer.wrapLines(Text.translatable(this.entityTooltip), Math.max(this.width / 2 - 43, 170)), x, y);
            this.entityTooltip = null;
            guiGraphics.getMatrices().pop();
        }
    }

    private void refreshSpacing() {
        if (this.internalPage != null) {
            String lang = MinecraftClient.getInstance().getLanguageManager().getLanguage().toLowerCase();
            this.currentPageText = new Identifier(this.getTextFileDirectory() + lang + "/" + this.internalPage.getTextFileToReadFrom());
            boolean invalid = false;
            try {
                //test if it exists. if no exception, then the language is supported
                InputStream is = MinecraftClient.getInstance().getResourceManager().open(this.currentPageText);
                is.close();
            } catch (Exception e) {
                invalid = true;
                Citadel.LOGGER.warn("Could not find language file for translation, defaulting to english");
                this.currentPageText = new Identifier(this.getTextFileDirectory() + "en_us/" + this.internalPage.getTextFileToReadFrom());
            }

            this.readInPageWidgets(this.internalPage);
            this.addWidgetSpacing();
            this.addLinkButtons();
            this.readInPageText(this.currentPageText);
        }
    }

    private Item getItemByRegistryName(String registryName) {
        return Registries.ITEM.get(new Identifier(registryName));
    }

    private Recipe getRecipeByName(String registryName) {
        try {
            assert MinecraftClient.getInstance().world != null;
            RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
            if (manager.get(new Identifier(registryName)).isPresent()) {
                return manager.get(new Identifier(registryName)).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addWidgetSpacing() {
        this.yIndexesToSkip.clear();
        for (ItemRenderData itemRenderData : this.itemRenders) {
            this.yIndexesToSkip.add(new Whitespace(itemRenderData.getPage(), itemRenderData.getX(), itemRenderData.getY(), (int) (itemRenderData.getScale() * 17), (int) (itemRenderData.getScale() * 15)));
        }
        for (RecipeData recipeData : this.recipes) {
            Recipe recipe = this.getRecipeByName(recipeData.getRecipe());
            if (recipe != null) {
                this.yIndexesToSkip.add(new Whitespace(recipeData.getPage(), recipeData.getX(), recipeData.getY() - (int) (recipeData.getScale() * 15), (int) (recipeData.getScale() * 35), (int) (recipeData.getScale() * 60), true));
            }
        }
        for (ImageData imageData : this.images) {
            if (imageData != null) {
                this.yIndexesToSkip.add(new Whitespace(imageData.getPage(), imageData.getX(), imageData.getY(), (int) (imageData.getScale() * imageData.getWidth()), (int) (imageData.getScale() * imageData.getHeight() * 0.8F)));
            }
        }
        if (!this.writtenTitle.isEmpty()) {
            this.yIndexesToSkip.add(new Whitespace(0, 20, 5, 70, 15));
        }
    }

    private void renderOtherWidgets(DrawContext guiGraphics, int x, int y, BookPage page) {
        int color = this.getBindingColor();
        int r = (color & 0xFF0000) >> 16;
        int g = (color & 0xFF00) >> 8;
        int b = (color & 0xFF);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;

        for (ImageData imageData : this.images) {
            if (imageData.getPage() == this.currentPageCounter) {
                if (imageData != null) {
                    Identifier tex = this.textureMap.get(imageData.getTexture());
                    if (tex == null) {
                        tex = new Identifier(imageData.getTexture());
                        this.textureMap.put(imageData.getTexture(), tex);
                    }
                    // yIndexesToSkip.put(imageData.getPage(), new Whitespace(imageData.getX(), imageData.getY(),(int) (imageData.getScale() * imageData.getWidth()), (int) (imageData.getScale() * imageData.getHeight() * 0.8F)));
                    float scale = (float) imageData.getScale();
                    guiGraphics.getMatrices().push();
                    guiGraphics.getMatrices().translate(k + imageData.getX(), l + imageData.getY(), 0);
                    guiGraphics.getMatrices().scale(scale, scale, scale);
                    guiGraphics.drawTexture(tex, 0, 0, imageData.getU(), imageData.getV(), imageData.getWidth(), imageData.getHeight());
                    guiGraphics.getMatrices().pop();
                }
            }
        }
        for (RecipeData recipeData : this.recipes) {
            if (recipeData.getPage() == this.currentPageCounter) {
                guiGraphics.getMatrices().push();
                guiGraphics.getMatrices().translate(k + recipeData.getX(), l + recipeData.getY(), 0);
                float scale = (float) recipeData.getScale();
                guiGraphics.getMatrices().scale(scale, scale, scale);
                guiGraphics.drawTexture(this.getBookWidgetTexture(), 0, 0, 0, 88, 116, 53);
                guiGraphics.getMatrices().pop();
            }
        }

        for (TabulaRenderData tabulaRenderData : this.tabulaRenders) {
            if (tabulaRenderData.getPage() == this.currentPageCounter) {
                TabulaModel model = null;
                Identifier texture;
                if (this.textureMap.get(tabulaRenderData.getTexture()) != null) {
                    texture = this.textureMap.get(tabulaRenderData.getTexture());
                } else {
                    texture = this.textureMap.put(tabulaRenderData.getTexture(), new Identifier(tabulaRenderData.getTexture()));
                }
                if (this.renderedTabulaModels.get(tabulaRenderData.getModel()) != null) {
                    model = this.renderedTabulaModels.get(tabulaRenderData.getModel());
                } else {
                    try {
                        model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/" + tabulaRenderData.getModel().split(":")[0] + "/" + tabulaRenderData.getModel().split(":")[1]));
                    } catch (Exception e) {
                        Citadel.LOGGER.warn("Could not load in tabula model for book at " + tabulaRenderData.getModel());
                    }
                    this.renderedTabulaModels.put(tabulaRenderData.getModel(), model);
                }

                if (model != null && texture != null) {
                    float scale = (float) tabulaRenderData.getScale();
                    drawTabulaModelOnScreen(guiGraphics, model, texture, k + tabulaRenderData.getX(), l + tabulaRenderData.getY(), 30 * scale, tabulaRenderData.isFollow_cursor(), tabulaRenderData.getRot_x(), tabulaRenderData.getRot_y(), tabulaRenderData.getRot_z(), this.mouseX, this.mouseY);
                }
            }
        }
        for (EntityRenderData data : this.entityRenders) {
            if (data.getPage() == this.currentPageCounter) {
                EntityType<?> type = Registries.ENTITY_TYPE.get(new Identifier(data.getEntity()));
                Entity model = this.renderedEntites.putIfAbsent(data.getEntity(), type.create(MinecraftClient.getInstance().world));
                if (model != null) {
                    float scale = (float) data.getScale();
                    model.age = MinecraftClient.getInstance().player.age;
                    if (data.getEntityData() != null) {
                        try {
                            NbtCompound tag = StringNbtReader.parse(data.getEntityData());
                            model.readNbt(tag);
                        } catch (CommandSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    this.drawEntityOnScreen(guiGraphics, guiGraphics.getVertexConsumers(), k + data.getX(), l + data.getY(), 1050F, 30 * scale, data.isFollow_cursor(), data.getRot_x(), data.getRot_y(), data.getRot_z(), this.mouseX, this.mouseY, model);
                }
            }
        }
        for (RecipeData recipeData : this.recipes) {
            if (recipeData.getPage() == this.currentPageCounter) {
                Recipe recipe = this.getRecipeByName(recipeData.getRecipe());
                if (recipe != null) {
                    this.renderRecipe(guiGraphics, recipe, recipeData, k, l);
                }
            }
        }
        for (ItemRenderData itemRenderData : this.itemRenders) {
            if (itemRenderData.getPage() == this.currentPageCounter) {
                Item item = this.getItemByRegistryName(itemRenderData.getItem());
                if (item != null) {
                    float scale = (float) itemRenderData.getScale();
                    ItemStack stack = new ItemStack(item);
                    if (itemRenderData.getItemTag() != null && !itemRenderData.getItemTag().isEmpty()) {
                        NbtCompound tag = null;
                        try {
                            tag = StringNbtReader.parse(itemRenderData.getItemTag());
                        } catch (CommandSyntaxException e) {
                            e.printStackTrace();
                        }
                        stack.setNbt(tag);
                    }
                    guiGraphics.getMatrices().push();
                    guiGraphics.getMatrices().translate(k, l, 0);
                    guiGraphics.getMatrices().scale(scale, scale, scale);
                    guiGraphics.drawItem(stack, itemRenderData.getX(), itemRenderData.getY());
                    guiGraphics.getMatrices().pop();
                }
            }
        }
    }

    protected void renderRecipe(DrawContext guiGraphics, Recipe recipe, RecipeData recipeData, int k, int l) {
        int playerTicks = MinecraftClient.getInstance().player.age;
        float scale = (float) recipeData.getScale();
        DefaultedList<Ingredient> ingredients = recipe instanceof SpecialRecipeInGuideBook ? ((SpecialRecipeInGuideBook) recipe).getDisplayIngredients() : recipe.getIngredients();
        DefaultedList<ItemStack> displayedStacks = DefaultedList.of();

        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ing = ingredients.get(i);
            ItemStack stack = ItemStack.EMPTY;
            if (!ing.isEmpty()) {
                if (ing.getMatchingStacks().length > 1) {
                    int currentIndex = (int) ((playerTicks / 20F) % ing.getMatchingStacks().length);
                    stack = ing.getMatchingStacks()[currentIndex];
                } else {
                    stack = ing.getMatchingStacks()[0];
                }
            }
            if (!stack.isEmpty()) {
                guiGraphics.getMatrices().push();
                guiGraphics.getMatrices().translate(k, l, 32.0F);
                guiGraphics.getMatrices().translate((int) (recipeData.getX() + (i % 3) * 20 * scale), (int) (recipeData.getY() + (i / 3) * 20 * scale), 0);
                guiGraphics.getMatrices().scale(scale, scale, scale);
                guiGraphics.drawItem(stack, 0, 0);
                guiGraphics.getMatrices().pop();
            }
            displayedStacks.add(i, stack);
        }
        guiGraphics.getMatrices().push();
        guiGraphics.getMatrices().translate(k, l, 32.0F);
        float finScale = scale * 1.5F;
        guiGraphics.getMatrices().translate(recipeData.getX() + 70 * finScale, recipeData.getY() + 10 * finScale, 0);
        guiGraphics.getMatrices().scale(finScale, finScale, finScale);
        ItemStack result = recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager());
        if (recipe instanceof SpecialRecipeInGuideBook) {
            result = ((SpecialRecipeInGuideBook) recipe).getDisplayResultFor(displayedStacks);
        }
        guiGraphics.getMatrices().translate(0.0F, 0.0F, 100.0F);
        guiGraphics.drawItem(result, 0, 0);
        guiGraphics.getMatrices().pop();
    }

    protected void writePageText(DrawContext guiGraphics, int x, int y) {
        TextRenderer font = this.textRenderer;
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize + 128) / 2;
        for (LineData line : this.lines) {
            if (line.getPage() == this.currentPageCounter) {
                guiGraphics.drawText(font, line.getText(), k + 10 + line.getxIndex(), l + 10 + line.getyIndex() * 12, this.getTextColor(), false);
            }
        }
        if (this.currentPageCounter == 0 && !this.writtenTitle.isEmpty()) {
            String actualTitle = I18n.translate(this.writtenTitle);
            guiGraphics.getMatrices().push();
            float scale = 2F;
            if (font.getWidth(actualTitle) > 80) {
                scale = 2.0F - MathHelper.clamp((font.getWidth(actualTitle) - 80) * 0.011F, 0, 1.95F);
            }
            guiGraphics.getMatrices().translate(k + 10, l + 10, 0);
            guiGraphics.getMatrices().scale(scale, scale, scale);
            guiGraphics.drawText(font, actualTitle, 0, 0, this.getTitleColor(), false);
            guiGraphics.getMatrices().pop();
        }
        this.buttonNextPage.visible = this.currentPageCounter < this.maxPagesFromPrinting;
        this.buttonPreviousPage.visible = this.currentPageCounter > 0 || !this.currentPageJSON.equals(this.getRootPage());
    }

    public boolean shouldPause() {
        return false;
    }

    protected void playBookOpeningSound() {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
    }

    protected void playBookClosingSound() {
    }

    protected abstract int getBindingColor();

    protected int getWidgetColor() {
        return this.getBindingColor();
    }

    protected int getTextColor() {
        return 0X303030;
    }

    protected int getTitleColor() {
        return 0XBAAC98;
    }

    public abstract Identifier getRootPage();

    public abstract String getTextFileDirectory();

    protected Identifier getBookPageTexture() {
        return BOOK_PAGE_TEXTURE;
    }

    protected Identifier getBookBindingTexture() {
        return BOOK_BINDING_TEXTURE;
    }

    protected Identifier getBookWidgetTexture() {
        return BOOK_WIDGET_TEXTURE;
    }

    protected void playPageFlipSound() {
    }

    protected BookPage generatePage(Identifier res) {
        Optional<Resource> resource = null;
        BookPage page = null;
        try {
            resource = MinecraftClient.getInstance().getResourceManager().getResource(res);
            try {
                resource = MinecraftClient.getInstance().getResourceManager().getResource(res);
                if (resource.isPresent()) {
                    BufferedReader inputstream = resource.get().getReader();
                    page = BookPage.deserialize(inputstream);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            return null;
        }
        return page;
    }

    protected void readInPageWidgets(BookPage page) {
        this.links.clear();
        this.itemRenders.clear();
        this.recipes.clear();
        this.tabulaRenders.clear();
        this.entityRenders.clear();
        this.images.clear();
        this.entityLinks.clear();
        this.links.addAll(page.getLinkedButtons());
        this.entityLinks.addAll(page.getLinkedEntities());
        this.itemRenders.addAll(page.getItemRenders());
        this.recipes.addAll(page.getRecipes());
        this.tabulaRenders.addAll(page.getTabulaRenders());
        this.entityRenders.addAll(page.getEntityRenders());
        this.images.addAll(page.getImages());
        this.writtenTitle = page.generateTitle();
    }

    protected void readInPageText(Identifier res) {
        Resource resource = null;
        int xIndex = 0;
        int actualTextX = 0;
        int yIndex = 0;
        try {
            BufferedReader bufferedreader = MinecraftClient.getInstance().getResourceManager().openAsReader(res);
            try {
                List<String> readStrings = IOUtils.readLines(bufferedreader);
                this.linesFromJSON = readStrings.size();
                this.lines.clear();
                List<String> splitBySpaces = new ArrayList<>();
                for (String line : readStrings) {
                    splitBySpaces.addAll(Arrays.asList(line.split(" ")));
                }
                String lineToPrint = "";
                this.linesFromPrinting = 0;
                int page = 0;
                for (int i = 0; i < splitBySpaces.size(); i++) {
                    String word = splitBySpaces.get(i);
                    int cutoffPoint = xIndex > 100 ? 30 : 35;
                    boolean newline = word.equals("<NEWLINE>");
                    for (Whitespace indexes : this.yIndexesToSkip) {
                        int indexPage = indexes.getPage();
                        if (indexPage == page) {
                            int buttonX = indexes.getX();
                            int buttonY = indexes.getY();
                            int width = indexes.getWidth();
                            int height = indexes.getHeight();
                            if (indexes.isDown()) {
                                if (yIndex >= (buttonY) / 12F && yIndex <= (buttonY + height) / 12F) {
                                    if (buttonX < 90 && xIndex < 90 || buttonX >= 90 && xIndex >= 90) {
                                        yIndex += 2;
                                    }
                                }
                            } else {
                                if (yIndex >= (buttonY - height) / 12F && yIndex <= (buttonY + height) / 12F) {
                                    if (buttonX < 90 && xIndex < 90 || buttonX >= 90 && xIndex >= 90) {
                                        yIndex++;
                                    }
                                }
                            }
                        }
                    }
                    boolean last = i == splitBySpaces.size() - 1;
                    actualTextX += word.length() + 1;
                    if (lineToPrint.length() + word.length() + 1 >= cutoffPoint || newline) {
                        this.linesFromPrinting++;
                        if (yIndex > 13) {
                            if (xIndex > 0) {
                                page++;
                                xIndex = 0;
                                yIndex = 0;
                            } else {
                                xIndex = 200;
                                yIndex = 0;
                            }
                        }
                        if (last) {
                            lineToPrint = lineToPrint + " " + word;
                        }
                        this.lines.add(new LineData(xIndex, yIndex, lineToPrint, page));
                        yIndex++;
                        actualTextX = 0;
                        if (newline) {
                            yIndex++;
                        }
                        lineToPrint = word.equals("<NEWLINE>") ? "" : word;
                    } else {
                        lineToPrint = lineToPrint + " " + word;
                        if (last) {
                            this.linesFromPrinting++;
                            this.lines.add(new LineData(xIndex, yIndex, lineToPrint, page));
                            yIndex++;
                            actualTextX = 0;
                            if (newline) {
                                yIndex++;
                            }
                        }
                    }
                }
                this.maxPagesFromPrinting = page;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            Citadel.LOGGER.warn("Could not load in page .txt from json from page, page: " + res);
        }
    }

    public void setEntityTooltip(String hoverText) {
        this.entityTooltip = hoverText;
    }

    public Identifier getBookButtonsTexture() {
        return BOOK_BUTTONS_TEXTURE;
    }
}
