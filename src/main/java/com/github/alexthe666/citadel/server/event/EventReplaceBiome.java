package com.github.alexthe666.citadel.server.event;

import com.github.alexthe666.citadel.server.world.ExpandedBiomeSource;
import com.iafenvoy.iafextra.event.Event;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

@Event.HasResult
public class EventReplaceBiome extends Event {

    public RegistryEntry<Biome> biomeToGenerate;
    public ExpandedBiomeSource biomeSource;
    public float continentalness;
    public float erosion;
    public float temperature;
    public float humidity;
    public float weirdness;
    public float depth;

    private final int x;
    private final int y;
    private final int z;

    private final long worldSeed;
    private final RegistryKey<World> worldDimension;
    private final MultiNoiseUtil.MultiNoiseSampler climateSampler;

    public EventReplaceBiome(ExpandedBiomeSource biomeSource, RegistryEntry<Biome> biomeIn, int x, int y, int z, float continentalness, float erosion, float temperature, float humidity, float weirdness, float depth, long worldSeed, RegistryKey<World> worldDimension, MultiNoiseUtil.MultiNoiseSampler climateSampler) {
        this.biomeSource = biomeSource;
        this.biomeToGenerate = biomeIn;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.temperature = temperature;
        this.humidity = humidity;
        this.weirdness = weirdness;
        this.depth = depth;
        this.worldSeed = worldSeed;
        this.worldDimension = worldDimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.climateSampler = climateSampler;
    }

    public RegistryEntry<Biome> getBiomeToGenerate() {
        return biomeToGenerate;
    }

    public float getContinentalness() {
        return continentalness;
    }

    public float getErosion() {
        return erosion;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getWeirdness() {
        return weirdness;
    }

    public float getDepth() {
        return depth;
    }


    public boolean testContinentalness(float min, float max) {
        return continentalness >= min && continentalness <= max;
    }

    public boolean testErosion(float min, float max) {
        return erosion >= min && erosion <= max;
    }

    public boolean testTemperature(float min, float max) {
        return temperature >= min && temperature <= max;
    }

    public boolean testHumidity(float min, float max) {
        return humidity >= min && humidity <= max;
    }

    public boolean testWeirdness(float min, float max) {
        return weirdness >= min && weirdness <= max;
    }

    public boolean testDepth(float min, float max) {
        return depth >= min && depth <= max;
    }

    public ExpandedBiomeSource getBiomeSource() {
        return biomeSource;
    }

    public void setBiomeToGenerate(RegistryEntry<Biome> biome) {
        biomeToGenerate = biome;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getWorldSeed() {
        return worldSeed;
    }

    public RegistryKey<World> getWorldDimension() {
        return worldDimension;
    }

    public MultiNoiseUtil.MultiNoiseSampler getClimateSampler() {
        return climateSampler;
    }

}
