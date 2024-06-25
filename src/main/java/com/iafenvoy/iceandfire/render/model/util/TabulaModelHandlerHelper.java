package com.iafenvoy.iceandfire.render.model.util;

import com.iafenvoy.citadel.client.model.TabulaModelHandler;
import com.iafenvoy.citadel.client.model.tabula.TabulaModelContainer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TabulaModelHandlerHelper {
    public static TabulaModelContainer loadTabulaModel(String path) throws IOException {
        if (!path.startsWith("/")) path = "/" + path;
        if (!path.endsWith(".tbl")) path = path + ".tbl";
        InputStream stream;
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            if (!path.startsWith(".")) path = "." + path;
            stream = new FileInputStream(path);
        } else stream = TabulaModelHandlerHelper.class.getResourceAsStream(path);
        return TabulaModelHandler.INSTANCE.loadTabulaModel(getModelJsonStream(path, stream));
    }

    private static InputStream getModelJsonStream(String name, InputStream file) throws IOException {
        ZipInputStream zip = new ZipInputStream(file);
        ZipEntry entry;
        do {
            if ((entry = zip.getNextEntry()) == null)
                throw new RuntimeException("No model.json present in " + name);
        } while (!entry.getName().equals("model.json"));
        return zip;
    }
}
