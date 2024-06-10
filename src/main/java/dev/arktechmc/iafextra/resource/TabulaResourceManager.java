package dev.arktechmc.iafextra.resource;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.IafClientSetup;
import com.github.alexthe666.iceandfire.client.model.animator.FireDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.IceDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.LightningTabulaDragonAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.SeaSerpentTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.TabulaModelHandlerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class TabulaResourceManager implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return new Identifier(IceAndFire.MOD_ID, "tabula");
    }

    @Override
    public void reload(ResourceManager manager) {
        TabulaModelHandlerHelper.setManager(manager);
        try {
            IafClientSetup.SEA_SERPENT_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("models/tabula/seaserpent/seaserpent_base"), new SeaSerpentTabulaModelAnimator());
            IafClientSetup.FIRE_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("models/tabula/firedragon/firedragon_ground"), new FireDragonTabulaModelAnimator());
            IafClientSetup.ICE_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("models/tabula/icedragon/icedragon_ground"), new IceDragonTabulaModelAnimator());
            IafClientSetup.LIGHTNING_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("models/tabula/lightningdragon/lightningdragon_ground"), new LightningTabulaDragonAnimator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
