package com.github.alexthe666.citadel.client.model;

import com.github.alexthe666.citadel.client.model.tabula.TabulaModelContainer;
import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * @author pau101
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public enum TabulaModelHandler implements JsonDeserializationContext {
    INSTANCE;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Transformation.class, new Transformation.Deserializer()).registerTypeAdapter(ModelTransformation.class, new ModelTransformation.Deserializer()).create();

    public TabulaModelContainer loadTabulaModel(InputStream stream) {
        return this.gson.fromJson(new InputStreamReader(stream), TabulaModelContainer.class);
    }

    @Override
    public <T> T deserialize(JsonElement json, Type type) throws JsonParseException {
        return this.gson.fromJson(json, type);
    }
}