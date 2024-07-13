package com.iafenvoy.iceandfire.particle;

import com.iafenvoy.uranus.util.RandomHelper;
import com.iafenvoy.uranus.util.VecUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.NotNull;

public class ParticleDragonFrost extends SpriteBillboardParticle {
    protected ParticleDragonFrost(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, SpriteProvider provider) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.scale *= this.random.nextBoolean() ? 2 : 1;
        this.maxAge = 30;
        this.gravityStrength = 0.0F;
        this.collidesWithWorld = false;
        this.setSprite(provider);
        this.setVelocity(RandomHelper.randomize(xSpeedIn, 0.5), RandomHelper.randomize(ySpeedIn, 0.5), RandomHelper.randomize(zSpeedIn, 0.5));
    }

    public static Provider provider(SpriteProvider spriteSet) {
        return new Provider(spriteSet);
    }

    @Override
    public int getBrightness(float partialTick) {
        int i = super.getBrightness(partialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        if (j > 240) j = 240;
        return j | k << 16;
    }

    @Override
    public void tick() {
        super.tick();
        BlockState state = this.world.getBlockState(VecUtil.createBlockPos(this.x, this.y, this.z));
        if (state != null && state.isSolid())
            this.markDead();
    }

    @Override
    public @NotNull ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }


    public record Provider(SpriteProvider spriteSet) implements ParticleFactory<DefaultParticleType> {
        @Override
        public Particle createParticle(DefaultParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleDragonFrost(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
