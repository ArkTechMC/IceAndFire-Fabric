package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.client.gui.data.*;
import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.JsonHelper;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BookPage {
    public static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(BookPage.class, new Deserializer()).create();
    public String translatableTitle = null;
    private final String parent;
    private final String textFileToReadFrom;
    private final List<LinkData> linkedButtons;
    private final List<EntityLinkData> linkedEntites;
    private final List<ItemRenderData> itemRenders;
    private final List<RecipeData> recipes;
    private final List<TabulaRenderData> tabulaRenders;
    private final List<EntityRenderData> entityRenders;
    private final List<ImageData> images;
    private final String title;

    public BookPage(String parent, String textFileToReadFrom, List<LinkData> linkedButtons, List<EntityLinkData> linkedEntities, List<ItemRenderData> itemRenders, List<RecipeData> recipes, List<TabulaRenderData> tabulaRenders, List<EntityRenderData> entityRenders, List<ImageData> images, String title) {
        this.parent = parent;
        this.textFileToReadFrom = textFileToReadFrom;
        this.linkedButtons = linkedButtons;
        this.itemRenders = itemRenders;
        this.linkedEntites = linkedEntities;
        this.recipes = recipes;
        this.tabulaRenders = tabulaRenders;
        this.entityRenders = entityRenders;
        this.images = images;
        this.title = title;
    }

    public static BookPage deserialize(Reader readerIn) {
        return JsonHelper.deserialize(GSON, readerIn, BookPage.class);
    }

    public static BookPage deserialize(String jsonString) {
        return deserialize(new StringReader(jsonString));
    }

    public String getParent() {
        return this.parent;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTextFileToReadFrom() {
        return this.textFileToReadFrom;
    }

    public List<LinkData> getLinkedButtons() {
        return this.linkedButtons;
    }

    public List<EntityLinkData> getLinkedEntities() {
        return this.linkedEntites;
    }

    public List<ItemRenderData> getItemRenders() {
        return this.itemRenders;
    }

    public List<RecipeData> getRecipes() {
        return this.recipes;
    }

    public List<TabulaRenderData> getTabulaRenders() {
        return this.tabulaRenders;
    }

    public List<EntityRenderData> getEntityRenders() {
        return this.entityRenders;
    }

    public List<ImageData> getImages() {
        return this.images;
    }

    public String generateTitle() {
        if (this.translatableTitle != null) {
            return I18n.translate(this.translatableTitle);
        }
        return this.title;
    }

    @Environment(EnvType.CLIENT)
    public static class Deserializer implements JsonDeserializer<BookPage> {
        public BookPage deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = JsonHelper.asObject(p_deserialize_1_, "book page");
            LinkData[] linkedPageRead = JsonHelper.deserialize(jsonobject, "linked_page_buttons", new LinkData[0], p_deserialize_3_, LinkData[].class);
            EntityLinkData[] linkedEntitesRead = JsonHelper.deserialize(jsonobject, "entity_buttons", new EntityLinkData[0], p_deserialize_3_, EntityLinkData[].class);
            ItemRenderData[] itemRendersRead = JsonHelper.deserialize(jsonobject, "item_renders", new ItemRenderData[0], p_deserialize_3_, ItemRenderData[].class);
            RecipeData[] recipesRead = JsonHelper.deserialize(jsonobject, "recipes", new RecipeData[0], p_deserialize_3_, RecipeData[].class);
            TabulaRenderData[] tabulaRendersRead = JsonHelper.deserialize(jsonobject, "tabula_renders", new TabulaRenderData[0], p_deserialize_3_, TabulaRenderData[].class);
            EntityRenderData[] entityRendersRead = JsonHelper.deserialize(jsonobject, "entity_renders", new EntityRenderData[0], p_deserialize_3_, EntityRenderData[].class);
            ImageData[] imagesRead = JsonHelper.deserialize(jsonobject, "images", new ImageData[0], p_deserialize_3_, ImageData[].class);

            String readParent = "";
            if (jsonobject.has("parent")) {
                readParent = JsonHelper.getString(jsonobject, "parent");
            }

            String readTextFile = "";
            if (jsonobject.has("text")) {
                readTextFile = JsonHelper.getString(jsonobject, "text");
            }

            String title = "";
            if (jsonobject.has("title")) {
                title = JsonHelper.getString(jsonobject, "title");
            }


            BookPage page = new BookPage(readParent, readTextFile, Arrays.asList(linkedPageRead), Arrays.asList(linkedEntitesRead), Arrays.asList(itemRendersRead), Arrays.asList(recipesRead), Arrays.asList(tabulaRendersRead), Arrays.asList(entityRendersRead), Arrays.asList(imagesRead), title);
            if (jsonobject.has("title")) {
                page.translatableTitle = JsonHelper.getString(jsonobject, "title");
            }
            return page;
        }
    }
}
