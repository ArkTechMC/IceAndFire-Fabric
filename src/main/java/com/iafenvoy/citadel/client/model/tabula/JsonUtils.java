package com.iafenvoy.citadel.client.model.tabula;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.Reader;

public class JsonUtils {
    /**
     * Gets the string value of the given JsonElement.  Expects the second parameter to be the name of the element's
     * field if an error message needs to be thrown.
     */
    public static String getString(JsonElement json, String memberName) {
        if (json.isJsonPrimitive()) return json.getAsString();
        else throw new JsonSyntaxException("Expected " + memberName + " to be a string, was " + toString(json));
    }

    /**
     * Gets the string value of the field on the JsonObject with the given name.
     */
    public static String getString(JsonObject json, String memberName) {
        if (json.has(memberName)) return getString(json.get(memberName), memberName);
        else throw new JsonSyntaxException("Missing " + memberName + ", expected to find a string");
    }

    /**
     * Gets the string value of the field on the JsonObject with the given name, or the given default value if the field
     * is missing.
     */
    public static String getString(JsonObject json, String memberName, String fallback) {
        return json.has(memberName) ? getString(json.get(memberName), memberName) : fallback;
    }

    public static Item getByNameOrId(String id) {
        return Registries.ITEM.get(new Identifier(id));
    }

    public static Item getItem(JsonElement json, String memberName) {
        if (json.isJsonPrimitive()) {
            String s = json.getAsString();
            return getByNameOrId(s);
        } else throw new JsonSyntaxException("Expected " + memberName + " to be an item, was " + toString(json));
    }

    public static Item getItem(JsonObject json, String memberName) {
        if (json.has(memberName)) return getItem(json.get(memberName), memberName);
        else throw new JsonSyntaxException("Missing " + memberName + ", expected to find an item");
    }

    /**
     * Gets the boolean value of the given JsonElement.  Expects the second parameter to be the name of the element's
     * field if an error message needs to be thrown.
     */
    public static boolean getBoolean(JsonElement json, String memberName) {
        if (json.isJsonPrimitive()) return json.getAsBoolean();
        else throw new JsonSyntaxException("Expected " + memberName + " to be a Boolean, was " + toString(json));
    }

    /**
     * Gets the boolean value of the field on the JsonObject with the given name.
     */
    public static boolean getBoolean(JsonObject json, String memberName) {
        if (json.has(memberName)) return getBoolean(json.get(memberName), memberName);
        else throw new JsonSyntaxException("Missing " + memberName + ", expected to find a Boolean");
    }

    /**
     * Gets the boolean value of the field on the JsonObject with the given name, or the given default value if the
     * field is missing.
     */
    public static boolean getBoolean(JsonObject json, String memberName, boolean fallback) {
        return json.has(memberName) ? getBoolean(json.get(memberName), memberName) : fallback;
    }

    /**
     * Gets the float value of the given JsonElement.  Expects the second parameter to be the name of the element's
     * field if an error message needs to be thrown.
     */
    public static float getFloat(JsonElement json, String memberName) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) return json.getAsFloat();
        else throw new JsonSyntaxException("Expected " + memberName + " to be a Float, was " + toString(json));
    }

    /**
     * Gets the float value of the field on the JsonObject with the given name.
     */
    public static float getFloat(JsonObject json, String memberName) {
        if (json.has(memberName)) return getFloat(json.get(memberName), memberName);
        else throw new JsonSyntaxException("Missing " + memberName + ", expected to find a Float");
    }

    /**
     * Gets the float value of the field on the JsonObject with the given name, or the given default value if the field
     * is missing.
     */
    public static float getFloat(JsonObject json, String memberName, float fallback) {
        return json.has(memberName) ? getFloat(json.get(memberName), memberName) : fallback;
    }

    /**
     * Gets the integer value of the given JsonElement.  Expects the second parameter to be the name of the element's
     * field if an error message needs to be thrown.
     */
    public static int getInt(JsonElement json, String memberName) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) return json.getAsInt();
        else throw new JsonSyntaxException("Expected " + memberName + " to be a Int, was " + toString(json));
    }

    /**
     * Gets the integer value of the field on the JsonObject with the given name.
     */
    public static int getInt(JsonObject json, String memberName) {
        if (json.has(memberName)) return getInt(json.get(memberName), memberName);
        else throw new JsonSyntaxException("Missing " + memberName + ", expected to find a Int");
    }

    /**
     * Gets the integer value of the field on the JsonObject with the given name, or the given default value if the
     * field is missing.
     */
    public static int getInt(JsonObject json, String memberName, int fallback) {
        return json.has(memberName) ? getInt(json.get(memberName), memberName) : fallback;
    }

    /**
     * Gets the given JsonElement as a JsonObject.  Expects the second parameter to be the name of the element's field
     * if an error message needs to be thrown.
     */
    public static JsonObject getJsonObject(JsonElement json, String memberName) {
        if (json.isJsonObject()) return json.getAsJsonObject();
        else throw new JsonSyntaxException("Expected " + memberName + " to be a JsonObject, was " + toString(json));
    }

    public static JsonObject getJsonObject(JsonObject json, String memberName) {
        if (json.has(memberName)) return getJsonObject(json.get(memberName), memberName);
        else throw new JsonSyntaxException("Missing " + memberName + ", expected to find a JsonObject");
    }

    /**
     * Gets the given JsonElement as a JsonArray.  Expects the second parameter to be the name of the element's field if
     * an error message needs to be thrown.
     */
    public static JsonArray getJsonArray(JsonElement json, String memberName) {
        if (json.isJsonArray()) return json.getAsJsonArray();
        else throw new JsonSyntaxException("Expected " + memberName + " to be a JsonArray, was " + toString(json));
    }

    /**
     * Gets the JsonArray field on the JsonObject with the given name.
     */
    public static JsonArray getJsonArray(JsonObject json, String memberName) {
        if (json.has(memberName)) return getJsonArray(json.get(memberName), memberName);
        else throw new JsonSyntaxException("Missing " + memberName + ", expected to find a JsonArray");
    }

    /**
     * Gets a human-readable description of the given JsonElement's type.  For example: "a number (4)"
     */
    public static String toString(JsonElement json) {
        String s = org.apache.commons.lang3.StringUtils.abbreviateMiddle(String.valueOf(json), "...", 10);
        if (json == null) return "null (missing)";
        else if (json.isJsonNull()) return "null (json)";
        else if (json.isJsonArray()) return "an array (" + s + ")";
        else if (json.isJsonObject()) return "an object (" + s + ")";
        else {
            if (json.isJsonPrimitive()) {
                JsonPrimitive jsonprimitive = json.getAsJsonPrimitive();
                if (jsonprimitive.isNumber()) return "a number (" + s + ")";
                if (jsonprimitive.isBoolean()) return "a boolean (" + s + ")";
            }
            return s;
        }
    }

    public static <T> T gsonDeserialize(Gson gsonIn, Reader readerIn, Class<T> adapter, boolean lenient) {
        try {
            JsonReader jsonreader = new JsonReader(readerIn);
            jsonreader.setLenient(lenient);
            return gsonIn.getAdapter(adapter).read(jsonreader);
        } catch (IOException ioexception) {
            throw new JsonParseException(ioexception);
        }
    }
}