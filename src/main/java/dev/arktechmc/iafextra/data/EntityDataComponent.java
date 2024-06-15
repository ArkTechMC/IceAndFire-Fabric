package dev.arktechmc.iafextra.data;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.props.*;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class EntityDataComponent implements ComponentV3, AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<EntityDataComponent> ENTITY_DATA_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(IceAndFire.MOD_ID, "entity_data"), EntityDataComponent.class);

    public final FrozenData frozenData = new FrozenData();
    public final ChainData chainData = new ChainData();
    public final SirenData sirenData = new SirenData();
    public final ChickenData chickenData = new ChickenData();
    public final MiscData miscData = new MiscData();
    private final LivingEntity entity;

    public EntityDataComponent(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.frozenData.deserialize(tag);
        this.chainData.deserialize(tag);
        this.sirenData.deserialize(tag);
        this.chickenData.deserialize(tag);
        this.miscData.deserialize(tag);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.frozenData.serialize(tag);
        this.chainData.serialize(tag);
        this.sirenData.serialize(tag);
        this.chickenData.serialize(tag);
        this.miscData.serialize(tag);
    }

    @Override
    public void tick() {
        this.frozenData.tickFrozen(this.entity);
        this.chainData.tickChain(this.entity);
        this.sirenData.tickCharmed(this.entity);
        this.chickenData.tickChicken(this.entity);
        this.miscData.tickMisc(this.entity);
    }
}
