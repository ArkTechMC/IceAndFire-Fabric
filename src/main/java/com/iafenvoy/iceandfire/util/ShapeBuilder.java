package com.iafenvoy.iceandfire.util;

import com.google.common.collect.AbstractIterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class ShapeBuilder {
    Iterable<BlockPos> blocks;

    ShapeBuilder() {
    }

    public static ShapeBuilder start() {
        return new ShapeBuilder();
    }

    public ShapeBuilder getAllInSphereMutable(int radius, BlockPos center) {
        return this.getAllInSphereMutable(radius, center.getX(), center.getY(), center.getZ());
    }

    public ShapeBuilder getAllInSphereMutable(int radius, int c1, int c2, int c3) {
        return this.getAllInCutOffSphereMutable(radius, radius, c1, c2, c3);
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOff, BlockPos center) {
        return this.getAllInCutOffSphereMutable(radiusX, yCutOff, yCutOff, center.getX(), center.getY(), center.getZ());
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOff, int c1, int c2, int c3) {
        return this.getAllInCutOffSphereMutable(radiusX, yCutOff, yCutOff, c1, c2, c3);
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOffMax, int yCutOffMin, BlockPos center) {
        return this.getAllInCutOffSphereMutable(radiusX, yCutOffMax, yCutOffMin, center.getX(), center.getY(), center.getZ());
    }

    public ShapeBuilder getAllInCutOffSphereMutable(int radiusX, int yCutOffMax, int yCutOffMin, int c1, int c2, int c3) {
        int r2 = radiusX * radiusX;
        this.blocks = () -> new AbstractIterator<>() {
            private final BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            private int currRX = radiusX;
            private int currRY = yCutOffMax;
            private int offset = 0;
            private int phase = 1;

            @Override
            protected BlockPos computeNext() {
                if (-this.currRY > yCutOffMin) {
                    return this.endOfData();
                } else {
                    if (this.isWithinRange(this.currRX, this.currRY, this.phase, this.offset, r2)) {
                        BlockPos pos = this.mutablePos.set(c1 + this.currRX, c2 + this.currRY, c3 + this.phase * this.offset);
                        this.offset++;
                        return pos;
                    } else {
                        if (this.phase == 1) {
                            this.phase = -1;
                            this.offset = 1;
                        } else if (this.phase == -1) {
                            this.phase = 1;
                            this.offset = 0;
                            this.currRX--;
                        }
                        if (-this.currRX > radiusX) {
                            this.currRY--;
                            this.currRX = radiusX;
                        }
                        return this.computeNext();
                    }
                }
            }

            private boolean isWithinRange(int currentRadiusX, int currentRadiusY, int phase, int offset, int radius2) {
                return Math.round((double) currentRadiusX * currentRadiusX + currentRadiusY * currentRadiusY + (phase * offset) * (phase * offset)) <= radius2;
            }
        };
        return this;
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int yCutOff, Random rand, BlockPos center) {
        return this.getAllInRandomlyDistributedRangeYCutOffSphereMutable(maxRadiusX, minRadiusX, yCutOff, rand, center.getX(), center.getY(), center.getZ());
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int ycutoffmin, Random rand, int c1, int c2, int c3) {
        return this.getAllInRandomlyDistributedRangeYCutOffSphereMutable(maxRadiusX, minRadiusX, ycutoffmin, ycutoffmin, rand, c1, c2, c3);
    }

    public ShapeBuilder getAllInRandomlyDistributedRangeYCutOffSphereMutable(int maxRadiusX, int minRadiusX, int yCutOffMax, int yCutOffMin, Random rand, int c1, int c2, int c3) {
        int maxr2 = maxRadiusX * maxRadiusX;
        int minr2 = minRadiusX * minRadiusX;
        float rDifference = (float) minRadiusX / maxRadiusX;
        this.blocks = () -> new AbstractIterator<>() {
            private final BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            private int currRX = maxRadiusX;
            private int currRY = yCutOffMax;
            private int offset = 0;
            private int phase = 1;

            @Override
            protected BlockPos computeNext() {
                if (-this.currRY > yCutOffMin) {
                    return this.endOfData();
                } else {
                    int distance = this.distance(this.currRX, this.currRY, this.phase, this.offset);
                    if (distance <= minr2 || distance <= maxr2 * MathHelper.clamp(rand.nextFloat(), rDifference, 1.0F)) {
                        BlockPos pos = this.mutablePos.set(c1 + this.currRX, c2 + this.currRY, c3 + this.phase * this.offset);
                        this.offset++;
                        return pos;
                    } else if (distance <= maxr2) {
                        this.offset++;
                        return this.computeNext();
                    } else {
                        if (this.phase == 1) {
                            this.phase = -1;
                            this.offset = 1;
                        } else if (this.phase == -1) {
                            this.phase = 1;
                            this.offset = 0;
                            this.currRX--;
                        }
                        if (-this.currRX > maxRadiusX) {
                            this.currRY--;
                            this.currRX = maxRadiusX;
                        }
                        return this.computeNext();
                    }
                }
            }

            private int distance(int currentRadiusX, int currentRadiusY, int phase, int offset) {
                return (int) Math.round((double) currentRadiusX * currentRadiusX + currentRadiusY * currentRadiusY + (phase * offset) * (phase * offset));
            }
        };
        return this;
    }

    public ShapeBuilder getAllInCircleMutable(int radius, int c1, int c2, int c3) {
        int r2 = radius * radius;
        this.blocks = () -> new AbstractIterator<>() {
            private final BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            private int totalAmount;
            private int currR = radius;
            private int offset = 0;
            private int phase = 1;

            @Override
            protected BlockPos computeNext() {
                if (-this.currR > radius) {
                    return this.endOfData();
                } else {
                    if (this.isWithinRange(this.currR, this.phase, this.offset, r2)) {
                        BlockPos pos = this.mutablePos.set(c1 + this.currR, c2, c3 + this.phase * this.offset);
                        this.offset++;
                        return pos;
                    } else {
                        if (this.phase == 1) {
                            this.phase = -1;
                            this.offset = 1;
                        } else if (this.phase == -1) {
                            this.phase = 1;
                            this.offset = 0;
                            this.currR--;
                        }
                        return this.computeNext();
                    }
                }
            }

            private boolean isWithinRange(int currentRadius, int phase, int offset, int radius2) {
                return Math.floor((double) currentRadius * currentRadius + (phase * offset) * (phase * offset)) <= radius2;
            }
        };
        return this;
    }

    public Stream<BlockPos> toStream(boolean parallel) {
        return StreamSupport.stream(this.blocks.spliterator(), parallel);
    }

    public Iterable<BlockPos> toIterable() {
        return this.blocks;
    }

}


