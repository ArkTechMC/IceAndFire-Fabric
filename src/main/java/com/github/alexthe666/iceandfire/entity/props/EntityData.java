package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.PacketDistributor;

public class EntityData {
    public FrozenData frozenData = new FrozenData();
    public ChainData chainData = new ChainData();
    public SirenData sirenData = new SirenData();
    public ChickenData chickenData = new ChickenData();
    public MiscData miscData = new MiscData();

    public void tick(final LivingEntity entity) {
        frozenData.tickFrozen(entity);
        chainData.tickChain(entity);
        sirenData.tickCharmed(entity);
        chickenData.tickChicken(entity);
        miscData.tickMisc(entity);

        boolean triggerClientUpdate = frozenData.doesClientNeedUpdate();
        triggerClientUpdate = chainData.doesClientNeedUpdate() || triggerClientUpdate;
        triggerClientUpdate = sirenData.doesClientNeedUpdate() || triggerClientUpdate;
        triggerClientUpdate = miscData.doesClientNeedUpdate() || triggerClientUpdate;

        if (triggerClientUpdate && !entity.getWorld().isClient()) {
            if (entity instanceof ServerPlayerEntity serverPlayer) {
                IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new SyncEntityData(entity.getId(), serialize()));
            } else {
                IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SyncEntityData(entity.getId(), serialize()));
            }
        }
    }

    public NbtCompound serialize() {
        NbtCompound tag = new NbtCompound();
        frozenData.serialize(tag);
        chainData.serialize(tag);
        sirenData.serialize(tag);
        chickenData.serialize(tag);
        miscData.serialize(tag);
        return tag;
    }

    public void deserialize(final NbtCompound tag) {
        frozenData.deserialize(tag);
        chainData.deserialize(tag);
        sirenData.deserialize(tag);
        chickenData.deserialize(tag);
        miscData.deserialize(tag);
    }
}
