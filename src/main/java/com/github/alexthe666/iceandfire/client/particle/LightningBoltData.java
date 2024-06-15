package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector4f;

import java.util.*;

/*
    Lightning bolt effect code used with permission from aidancbrady
 */
public class LightningBoltData {
    private final Random random = new Random();

    private final BoltRenderInfo renderInfo;

    private final Vec3d start;
    private final Vec3d end;

    private final int segments;

    private int count = 1;
    private float size = 0.1F;
    private int lifespan = 30;

    private SpawnFunction spawnFunction = SpawnFunction.delay(60);
    private FadeFunction fadeFunction = FadeFunction.fade(0.5F);

    public LightningBoltData(Vec3d start, Vec3d end) {
        this(BoltRenderInfo.DEFAULT, start, end, (int) (Math.sqrt(start.distanceTo(end) * 100)));
    }

    public LightningBoltData(BoltRenderInfo info, Vec3d start, Vec3d end, int segments) {
        this.renderInfo = info;
        this.start = start;
        this.end = end;
        this.segments = segments;
    }

    private static Vec3d findRandomOrthogonalVector(Vec3d vec, Random rand) {
        Vec3d newVec = new Vec3d(-0.5 + rand.nextDouble(), -0.5 + rand.nextDouble(), -0.5 + rand.nextDouble());
        return vec.crossProduct(newVec).normalize();
    }

    public LightningBoltData count(int count) {
        this.count = count;
        return this;
    }

    public LightningBoltData size(float size) {
        this.size = size;
        return this;
    }

    public LightningBoltData spawn(SpawnFunction spawnFunction) {
        this.spawnFunction = spawnFunction;
        return this;
    }

    public LightningBoltData fade(FadeFunction fadeFunction) {
        this.fadeFunction = fadeFunction;
        return this;
    }

    public LightningBoltData lifespan(int lifespan) {
        this.lifespan = lifespan;
        return this;
    }

    public int getLifespan() {
        return this.lifespan;
    }

    public SpawnFunction getSpawnFunction() {
        return this.spawnFunction;
    }

    public FadeFunction getFadeFunction() {
        return this.fadeFunction;
    }

    public Vector4f getColor() {
        return this.renderInfo.color;
    }

    public List<BoltQuads> generate() {
        List<BoltQuads> quads = new ArrayList<>();
        Vec3d diff = this.end.subtract(this.start);
        float totalDistance = (float) diff.length();
        for (int i = 0; i < this.count; i++) {
            LinkedList<BoltInstructions> drawQueue = new LinkedList<>();
            drawQueue.add(new BoltInstructions(this.start, 0, new Vec3d(0, 0, 0), null, false));
            while (!drawQueue.isEmpty()) {
                BoltInstructions data = drawQueue.poll();
                Vec3d perpendicularDist = data.perpendicularDist;
                float progress = data.progress + (1F / this.segments) * (1 - this.renderInfo.parallelNoise + this.random.nextFloat() * this.renderInfo.parallelNoise * 2);
                Vec3d segmentEnd;
                if (progress >= 1) {
                    segmentEnd = this.end;
                } else {
                    float segmentDiffScale = this.renderInfo.spreadFunction.getMaxSpread(progress);
                    float maxDiff = this.renderInfo.spreadFactor * segmentDiffScale * totalDistance * this.renderInfo.randomFunction.getRandom(this.random);
                    Vec3d randVec = findRandomOrthogonalVector(diff, this.random);
                    perpendicularDist = this.renderInfo.segmentSpreader.getSegmentAdd(perpendicularDist, randVec, maxDiff, segmentDiffScale, progress);
                    // new vector is original + current progress through segments + perpendicular change
                    segmentEnd = this.start.add(diff.multiply(progress)).add(perpendicularDist);
                }
                float boltSize = this.size * (0.5F + (1 - progress) * 0.5F);
                Pair<BoltQuads, QuadCache> quadData = this.createQuads(data.cache, data.start, segmentEnd, boltSize);
                quads.add(quadData.getLeft());

                if (segmentEnd == this.end) {
                    break; // break if we've reached the defined end point
                } else if (!data.isBranch) {
                    // continue the bolt if this is the primary (non-branch) segment
                    drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getRight(), false));
                } else if (this.random.nextFloat() < this.renderInfo.branchContinuationFactor) {
                    // branch continuation
                    drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getRight(), true));
                }

                while (this.random.nextFloat() < this.renderInfo.branchInitiationFactor * (1 - progress)) {
                    // branch initiation (probability decreases as progress increases)
                    drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getRight(), true));
                }
            }
        }
        return quads;
    }

    private Pair<BoltQuads, QuadCache> createQuads(QuadCache cache, Vec3d startPos, Vec3d end, float size) {
        Vec3d diff = end.subtract(startPos);
        Vec3d rightAdd = diff.crossProduct(new Vec3d(0.5, 0.5, 0.5)).normalize().multiply(size);
        Vec3d backAdd = diff.crossProduct(rightAdd).normalize().multiply(size), rightAddSplit = rightAdd.multiply(0.5F);

        Vec3d start = cache != null ? cache.prevEnd : startPos;
        Vec3d startRight = cache != null ? cache.prevEndRight : start.add(rightAdd);
        Vec3d startBack = cache != null ? cache.prevEndBack : start.add(rightAddSplit).add(backAdd);
        Vec3d endRight = end.add(rightAdd), endBack = end.add(rightAddSplit).add(backAdd);

        BoltQuads quads = new BoltQuads();
        quads.addQuad(start, end, endRight, startRight);
        quads.addQuad(startRight, endRight, end, start);

        quads.addQuad(startRight, endRight, endBack, startBack);
        quads.addQuad(startBack, endBack, endRight, startRight);

        return Pair.of(quads, new QuadCache(end, endRight, endBack));
    }

    public interface SpreadFunction {

        /**
         * A steady linear increase in perpendicular noise.
         */
        SpreadFunction LINEAR_ASCENT = (progress) -> progress;
        /**
         * A steady linear increase in perpendicular noise, followed by a steady decrease after the halfway point.
         */
        SpreadFunction LINEAR_ASCENT_DESCENT = (progress) -> (progress - Math.max(0, 2 * progress - 1)) / 0.5F;
        /**
         * Represents a unit sine wave from 0 to PI, scaled by progress.
         */
        SpreadFunction SINE = (progress) -> MathHelper.sin((float) (Math.PI * progress));

        float getMaxSpread(float progress);
    }

    public interface RandomFunction {

        RandomFunction UNIFORM = Random::nextFloat;
        RandomFunction GAUSSIAN = rand -> (float) rand.nextGaussian();

        float getRandom(Random rand);
    }

    public interface SegmentSpreader {

        /**
         * Don't remember where the last segment left off, just randomly move from the straight-line vector.
         */
        SegmentSpreader NO_MEMORY = (perpendicularDist, randVec, maxDiff, scale, progress) -> randVec.multiply(maxDiff);

        /**
         * Move from where the previous segment ended by a certain memory factor. Higher memory will restrict perpendicular movement.
         */
        static SegmentSpreader memory(float memoryFactor) {
            return (perpendicularDist, randVec, maxDiff, spreadScale, progress) -> {
                float nextDiff = maxDiff * (1 - memoryFactor);
                Vec3d cur = randVec.multiply(nextDiff);
                if (progress > 0.5F) {
                    // begin to come back to the center after we pass halfway mark
                    cur = cur.add(perpendicularDist.multiply(-1 * (1 - spreadScale)));
                }
                return perpendicularDist.add(cur);
            };
        }

        Vec3d getSegmentAdd(Vec3d perpendicularDist, Vec3d randVec, float maxDiff, float scale, float progress);
    }

    public interface SpawnFunction {

        /**
         * Allow for bolts to be spawned each update call without any delay.
         */
        SpawnFunction NO_DELAY = (rand) -> Pair.of(0F, 0F);
        /**
         * Will re-spawn a bolt each time one expires.
         */
        SpawnFunction CONSECUTIVE = new SpawnFunction() {
            @Override
            public Pair<Float, Float> getSpawnDelayBounds(Random rand) {
                return Pair.of(0F, 0F);
            }

            @Override
            public boolean isConsecutive() {
                return true;
            }
        };

        /**
         * Spawn bolts with a specified constant delay.
         */
        static SpawnFunction delay(float delay) {
            return (rand) -> Pair.of(delay, delay);
        }

        /**
         * Spawns bolts with a specified delay and specified noise value, which will be randomly applied at either end of the delay bounds.
         */
        static SpawnFunction noise(float delay, float noise) {
            return (rand) -> Pair.of(delay - noise, delay + noise);
        }

        Pair<Float, Float> getSpawnDelayBounds(Random rand);

        default float getSpawnDelay(Random rand) {
            Pair<Float, Float> bounds = this.getSpawnDelayBounds(rand);
            return bounds.getLeft() + (bounds.getRight() - bounds.getLeft()) * rand.nextFloat();
        }

        default boolean isConsecutive() {
            return false;
        }
    }

    public interface FadeFunction {

        /**
         * No fade; render the bolts entirely throughout their lifespan.
         */
        FadeFunction NONE = (totalBolts, lifeScale) -> Pair.of(0, totalBolts);

        /**
         * Remder bolts with a segment-by-segment 'fade' in and out, with a specified fade duration (applied to start and finish).
         */
        static FadeFunction fade(float fade) {
            return (totalBolts, lifeScale) -> {
                int start = lifeScale > (1 - fade) ? (int) (totalBolts * (lifeScale - (1 - fade)) / fade) : 0;
                int end = lifeScale < fade ? (int) (totalBolts * (lifeScale / fade)) : totalBolts;
                return Pair.of(start, end);
            };
        }

        Pair<Integer, Integer> getRenderBounds(int totalBolts, float lifeScale);
    }

    private record QuadCache(Vec3d prevEnd, Vec3d prevEndRight, Vec3d prevEndBack) {

    }

    protected static class BoltInstructions {

        private final Vec3d start;
        private final Vec3d perpendicularDist;
        private final QuadCache cache;
        private final float progress;
        private final boolean isBranch;

        private BoltInstructions(Vec3d start, float progress, Vec3d perpendicularDist, QuadCache cache, boolean isBranch) {
            this.start = start;
            this.perpendicularDist = perpendicularDist;
            this.progress = progress;
            this.cache = cache;
            this.isBranch = isBranch;
        }
    }

    public static class BoltRenderInfo {

        public static final BoltRenderInfo DEFAULT = new BoltRenderInfo();
        public static final BoltRenderInfo ELECTRICITY = electricity();
        private final RandomFunction randomFunction = RandomFunction.GAUSSIAN;
        private final SpreadFunction spreadFunction = SpreadFunction.SINE;
        /**
         * How much variance is allowed in segment lengths (parallel to straight line).
         */
        private float parallelNoise = 0.1F;
        /**
         * How much variance is allowed perpendicular to the straight line vector. Scaled by distance and spread function.
         */
        private float spreadFactor = 0.1F;
        /**
         * The chance of creating an additional branch after a certain segment.
         */
        private float branchInitiationFactor = 0.0F;
        /**
         * The chance of a branch continuing (post-initiation).
         */
        private float branchContinuationFactor = 0.0F;
        private Vector4f color = new Vector4f(0.45F, 0.45F, 0.5F, 0.8F);
        private SegmentSpreader segmentSpreader = SegmentSpreader.NO_MEMORY;

        public BoltRenderInfo() {
        }

        public BoltRenderInfo(float parallelNoise, float spreadFactor, float branchInitiationFactor, float branchContinuationFactor, Vector4f color, float closeness) {
            this.parallelNoise = parallelNoise;
            this.spreadFactor = spreadFactor;
            this.branchInitiationFactor = branchInitiationFactor;
            this.branchContinuationFactor = branchContinuationFactor;
            this.color = color;
            this.segmentSpreader = SegmentSpreader.memory(closeness);
        }

        public static BoltRenderInfo electricity() {
            return new BoltRenderInfo(0.5F, 0.25F, 0.25F, 0.15F, new Vector4f(0.70F, 0.45F, 0.89F, 0.8F), 0.8F);
        }
    }

    public static class BoltQuads {

        private final List<Vec3d> vecs = new ArrayList<>();

        protected void addQuad(Vec3d... quadVecs) {
            this.vecs.addAll(Arrays.asList(quadVecs));
        }

        public List<Vec3d> getVecs() {
            return this.vecs;
        }
    }
}