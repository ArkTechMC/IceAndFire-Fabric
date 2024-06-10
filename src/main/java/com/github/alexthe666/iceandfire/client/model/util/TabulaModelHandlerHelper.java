package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.citadel.client.model.TabulaModelHandler;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class TabulaModelHandlerHelper {
    private static ResourceManager resourceManager = null;

    public static void setManager(ResourceManager manager) {
        resourceManager = manager;
    }

    public static TabulaModelContainer loadTabulaModel(String path) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (!path.endsWith(".tbl")) {
            path = path + ".tbl";
        }

        List<Resource> resources = resourceManager.getAllResources(new Identifier(IceAndFire.MOD_ID, path));
        if (resources.size() == 0) throw new IOException("Cannot find resource for tabula: " + path);
        return TabulaModelHandler.INSTANCE.loadTabulaModel(getModelJsonStream(path, resources.get(0).getInputStream()));
    }

    private static InputStream getModelJsonStream(String name, InputStream file) throws IOException {
        ZipInputStream zip = new ZipInputStream(file);

        ZipEntry entry;
        do {
            if ((entry = zip.getNextEntry()) == null) {
                throw new RuntimeException("No model.json present in " + name);
            }
        } while (!entry.getName().equals("model.json"));

        return zip;
    }
}
