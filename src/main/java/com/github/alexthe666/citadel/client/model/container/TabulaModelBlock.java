package com.github.alexthe666.citadel.client.model.container;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelElementTexture;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class TabulaModelBlock
{
    private static final Logger LOGGER = LogManager.getLogger();
    @VisibleForTesting
    static final Gson SERIALIZER = (new GsonBuilder()).registerTypeAdapter(TabulaModelBlock.class, new Deserializer()).registerTypeAdapter(ModelElement.class, new ModelElement.Deserializer()).registerTypeAdapter(ModelElementFace.class, new ModelElementFace.Deserializer()).registerTypeAdapter(ModelElementTexture.class, new ModelElementTexture.Deserializer()).registerTypeAdapter(Transformation.class, new Transformation.Deserializer()).registerTypeAdapter(ModelTransformation.class, new ModelTransformation.Deserializer()).registerTypeAdapter(ModelOverride.class, new ModelOverride.Deserializer()).create();
    private final List<ModelElement> elements;
    private final boolean gui3d;
    public final boolean ambientOcclusion;
    private final ModelTransformation cameraTransforms;
    private final List<ModelOverride> overrides;
    public String name = "";
    @VisibleForTesting
    public final Map<String, String> textures;
    @VisibleForTesting
    public TabulaModelBlock parent;
    @VisibleForTesting
    protected Identifier parentLocation;

    public static TabulaModelBlock deserialize(Reader readerIn)
    {
        return JsonUtils.gsonDeserialize(SERIALIZER, readerIn, TabulaModelBlock.class, false);
    }

    public static TabulaModelBlock deserialize(String jsonString)
    {
        return deserialize(new StringReader(jsonString));
    }

    public TabulaModelBlock(@Nullable Identifier parentLocationIn, List<ModelElement> elementsIn, Map<String, String> texturesIn, boolean ambientOcclusionIn, boolean gui3dIn, ModelTransformation cameraTransformsIn, List<ModelOverride> overridesIn)
    {
        this.elements = elementsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.textures = texturesIn;
        this.parentLocation = parentLocationIn;
        this.cameraTransforms = cameraTransformsIn;
        this.overrides = overridesIn;
    }

    public List<ModelElement> getElements()
    {
        return this.elements.isEmpty() && this.hasParent() ? this.parent.getElements() : this.elements;
    }

    private boolean hasParent()
    {
        return this.parent != null;
    }

    public boolean isAmbientOcclusion()
    {
        return this.hasParent() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return this.gui3d;
    }

    public boolean isResolved()
    {
        return this.parentLocation == null || this.parent != null && this.parent.isResolved();
    }

    public void getParentFromMap(Map<Identifier, TabulaModelBlock> p_178299_1_)
    {
        if (this.parentLocation != null)
        {
            this.parent = p_178299_1_.get(this.parentLocation);
        }
    }

    public Collection<Identifier> getOverrideLocations()
    {
        Set<Identifier> set = Sets.newHashSet();

        for (ModelOverride itemoverride : this.overrides)
        {
            set.add(itemoverride.getModelId());
        }

        return set;
    }

    public List<ModelOverride> getOverrides()
    {
        return this.overrides;
    }

    public boolean isTexturePresent(String textureName)
    {
        return !"missingno".equals(this.resolveTextureName(textureName));
    }

    public String resolveTextureName(String textureName)
    {
        if (!this.startsWithHash(textureName))
        {
            textureName = '#' + textureName;
        }

        return this.resolveTextureName(textureName, new Bookkeep(this));
    }

    private String resolveTextureName(String textureName, Bookkeep p_178302_2_)
    {
        if (this.startsWithHash(textureName))
        {
            if (this == p_178302_2_.modelExt)
            {
                LOGGER.warn("Unable to resolve texture due to upward reference: {} in {}", textureName, this.name);
                return "missingno";
            }
            else
            {
                String s = this.textures.get(textureName.substring(1));

                if (s == null && this.hasParent())
                {
                    s = this.parent.resolveTextureName(textureName, p_178302_2_);
                }

                p_178302_2_.modelExt = this;

                if (s != null && this.startsWithHash(s))
                {
                    s = p_178302_2_.model.resolveTextureName(s, p_178302_2_);
                }

                return s != null && !this.startsWithHash(s) ? s : "missingno";
            }
        }
        else
        {
            return textureName;
        }
    }

    private boolean startsWithHash(String hash)
    {
        return hash.charAt(0) == '#';
    }

    @Nullable
    public Identifier getParentLocation()
    {
        return this.parentLocation;
    }

    public TabulaModelBlock getRootModel()
    {
        return this.hasParent() ? this.parent.getRootModel() : this;
    }

    public ModelTransformation getAllTransforms()
    {
        Transformation itemtransformvec3f = this.getTransform(ModelTransformationMode.THIRD_PERSON_LEFT_HAND);
        Transformation itemtransformvec3f1 = this.getTransform(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND);
        Transformation itemtransformvec3f2 = this.getTransform(ModelTransformationMode.FIRST_PERSON_LEFT_HAND);
        Transformation itemtransformvec3f3 = this.getTransform(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND);
        Transformation itemtransformvec3f4 = this.getTransform(ModelTransformationMode.HEAD);
        Transformation itemtransformvec3f5 = this.getTransform(ModelTransformationMode.GUI);
        Transformation itemtransformvec3f6 = this.getTransform(ModelTransformationMode.GROUND);
        Transformation itemtransformvec3f7 = this.getTransform(ModelTransformationMode.FIXED);
        return new ModelTransformation(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3, itemtransformvec3f4, itemtransformvec3f5, itemtransformvec3f6, itemtransformvec3f7);
    }

    private Transformation getTransform(ModelTransformationMode type)
    {
        return this.parent != null && !this.cameraTransforms.isTransformationDefined(type) ? this.parent.getTransform(type) : this.cameraTransforms.getTransformation(type);
    }

    public static void checkModelHierarchy(Map<Identifier, TabulaModelBlock> p_178312_0_)
    {
        for (TabulaModelBlock TabulaModelBlock : p_178312_0_.values())
        {
            try
            {
                TabulaModelBlock TabulaModelBlock1 = TabulaModelBlock.parent;

                for (TabulaModelBlock TabulaModelBlock2 = TabulaModelBlock1.parent; TabulaModelBlock1 != TabulaModelBlock2; TabulaModelBlock2 = TabulaModelBlock2.parent.parent)
                {
                    TabulaModelBlock1 = TabulaModelBlock1.parent;
                }

                throw new LoopException();
            }
            catch (NullPointerException var5)
            {
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static final class Bookkeep
    {
        public final TabulaModelBlock model;
        public TabulaModelBlock modelExt;

        private Bookkeep(TabulaModelBlock modelIn)
        {
            this.model = modelIn;
        }
    }

    public static class Deserializer implements JsonDeserializer<TabulaModelBlock>
    {
        public TabulaModelBlock deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            List<ModelElement> list = this.getModelElements(p_deserialize_3_, jsonobject);
            String s = this.getParent(jsonobject);
            Map<String, String> map = this.getTextures(jsonobject);
            boolean flag = this.getAmbientOcclusionEnabled(jsonobject);
            ModelTransformation itemcameratransforms = ModelTransformation.NONE;

            if (jsonobject.has("display"))
            {
                JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "display");
                itemcameratransforms = p_deserialize_3_.deserialize(jsonobject1, ModelTransformation.class);
            }

            List<ModelOverride> list1 = this.getItemOverrides(p_deserialize_3_, jsonobject);
            Identifier resourcelocation = s.isEmpty() ? null : new Identifier(s);
            return new TabulaModelBlock(resourcelocation, list, map, flag, true, itemcameratransforms, list1);
        }

        protected List<ModelOverride> getItemOverrides(JsonDeserializationContext deserializationContext, JsonObject object)
        {
            List<ModelOverride> list = Lists.newArrayList();

            if (object.has("overrides"))
            {
                for (JsonElement jsonelement : JsonUtils.getJsonArray(object, "overrides"))
                {
                    list.add(deserializationContext.deserialize(jsonelement, ModelOverride.class));
                }
            }

            return list;
        }

        private Map<String, String> getTextures(JsonObject object)
        {
            Map<String, String> map = Maps.newHashMap();

            if (object.has("textures"))
            {
                JsonObject jsonobject = object.getAsJsonObject("textures");

                for (Map.Entry<String, JsonElement> entry : jsonobject.entrySet())
                {
                    map.put(entry.getKey(), entry.getValue().getAsString());
                }
            }

            return map;
        }

        private String getParent(JsonObject object)
        {
            return JsonUtils.getString(object, "parent", "");
        }

        protected boolean getAmbientOcclusionEnabled(JsonObject object)
        {
            return JsonUtils.getBoolean(object, "ambientocclusion", true);
        }

        protected List<ModelElement> getModelElements(JsonDeserializationContext deserializationContext, JsonObject object)
        {
            List<ModelElement> list = Lists.newArrayList();

            if (object.has("elements"))
            {
                for (JsonElement jsonelement : JsonUtils.getJsonArray(object, "elements"))
                {
                    list.add(deserializationContext.deserialize(jsonelement, ModelElement.class));
                }
            }

            return list;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class LoopException extends RuntimeException
    {
    }
}
