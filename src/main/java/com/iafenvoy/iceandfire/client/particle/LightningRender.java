package com.iafenvoy.iceandfire.client.particle;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix4f;

import java.util.*;

/*
    Lightning bolt effect code used with permission from aidancbrady
 */
public class LightningRender {
    private static final float REFRESH_TIME = 3F;
    private static final double MAX_OWNER_TRACK_TIME = 100;
    private final Random random = new Random();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final Map<Object, BoltOwnerData> boltOwners = new Object2ObjectOpenHashMap<>();
    private Timestamp refreshTimestamp = new Timestamp();

    public void render(float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn) {
        VertexConsumer buffer = bufferIn.getBuffer(RenderLayer.getLightning());
        Matrix4f matrix = matrixStackIn.peek().getPositionMatrix();
        assert this.client.world != null;
        Timestamp timestamp = new Timestamp(this.client.world.getTime(), partialTicks);
        boolean refresh = timestamp.isPassed(this.refreshTimestamp, (1 / REFRESH_TIME));
        if (refresh) this.refreshTimestamp = timestamp;
        for (Iterator<Map.Entry<Object, BoltOwnerData>> iter = this.boltOwners.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Object, BoltOwnerData> entry = iter.next();
            BoltOwnerData data = entry.getValue();
            // tick our bolts based on the refresh rate, removing if they're now finished
            if (refresh)
                data.bolts.removeIf(bolt -> bolt.tick(timestamp));
            if (data.bolts.isEmpty() && data.lastBolt != null && data.lastBolt.getSpawnFunction().isConsecutive())
                data.addBolt(new BoltInstance(data.lastBolt, timestamp), timestamp);
            data.bolts.forEach(bolt -> bolt.render(matrix, buffer, timestamp));

            if (data.bolts.isEmpty() && timestamp.isPassed(data.lastUpdateTimestamp, MAX_OWNER_TRACK_TIME))
                iter.remove();
        }
    }

    public void update(Object owner, LightningBoltData newBoltData, float partialTicks) {
        if (this.client.world == null) return;
        BoltOwnerData data = this.boltOwners.computeIfAbsent(owner, o -> new BoltOwnerData());
        data.lastBolt = newBoltData;
        Timestamp timestamp = new Timestamp(this.client.world.getTime(), partialTicks);
        if ((!data.lastBolt.getSpawnFunction().isConsecutive() || data.bolts.isEmpty()) && timestamp.isPassed(data.lastBoltTimestamp, data.lastBoltDelay))
            data.addBolt(new BoltInstance(newBoltData, timestamp), timestamp);
        data.lastUpdateTimestamp = timestamp;
    }

    public static class BoltInstance {
        private final LightningBoltData bolt;
        private final List<LightningBoltData.BoltQuads> renderQuads;
        private final Timestamp createdTimestamp;

        public BoltInstance(LightningBoltData bolt, Timestamp timestamp) {
            this.bolt = bolt;
            this.renderQuads = bolt.generate();
            this.createdTimestamp = timestamp;
        }

        public void render(Matrix4f matrix, VertexConsumer buffer, Timestamp timestamp) {
            float lifeScale = timestamp.subtract(this.createdTimestamp).value() / this.bolt.getLifespan();
            Pair<Integer, Integer> bounds = this.bolt.getFadeFunction().getRenderBounds(this.renderQuads.size(), lifeScale);
            for (int i = bounds.getLeft(); i < bounds.getRight(); i++)
                this.renderQuads.get(i).getVecs().forEach(v -> buffer.vertex(matrix, (float) v.x, (float) v.y, (float) v.z).color(this.bolt.getColor().x(), this.bolt.getColor().y(), this.bolt.getColor().z(), this.bolt.getColor().w()).next());
        }

        public boolean tick(Timestamp timestamp) {
            return timestamp.isPassed(this.createdTimestamp, this.bolt.getLifespan());
        }
    }

    public static class Timestamp {

        private final long ticks;
        private final float partial;

        public Timestamp() {
            this(0, 0);
        }

        public Timestamp(long ticks, float partial) {
            this.ticks = ticks;
            this.partial = partial;
        }

        public Timestamp subtract(Timestamp other) {
            long newTicks = this.ticks - other.ticks;
            float newPartial = this.partial - other.partial;
            if (newPartial < 0) {
                newPartial += 1;
                newTicks -= 1;
            }
            return new Timestamp(newTicks, newPartial);
        }

        public float value() {
            return this.ticks + this.partial;
        }

        public boolean isPassed(Timestamp prev, double duration) {
            long ticksPassed = this.ticks - prev.ticks;
            if (ticksPassed > duration) return true;
            duration -= ticksPassed;
            if (duration >= 1) return false;
            return (this.partial - prev.partial) >= duration;
        }
    }

    public class BoltOwnerData {
        private final Set<BoltInstance> bolts = new ObjectOpenHashSet<>();
        private LightningBoltData lastBolt;
        private Timestamp lastBoltTimestamp = new Timestamp();
        private Timestamp lastUpdateTimestamp = new Timestamp();
        private double lastBoltDelay;

        private void addBolt(BoltInstance instance, Timestamp timestamp) {
            this.bolts.add(instance);
            this.lastBoltDelay = instance.bolt.getSpawnFunction().getSpawnDelay(LightningRender.this.random);
            this.lastBoltTimestamp = timestamp;
        }
    }
}