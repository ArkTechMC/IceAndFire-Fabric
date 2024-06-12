package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticleSirenMusic extends SpriteBillboardParticle {
    private static final Identifier SIREN_MUSIC = new Identifier(IceAndFire.MOD_ID, "textures/particles/siren_music.png");

    float noteParticleScale;
    float colorScale;

    public ParticleSirenMusic(ClientWorld world, double x, double y, double z, double motX, double motY, double motZ, float size) {
        super(world, x, y, z, motX, motY, motZ);
        this.setPos(x, y, z);
        this.colorScale = (float) 1;
        this.red = Math.max(0.0F, MathHelper.sin((this.colorScale + 0.0F) * 6.2831855F) * 0.65F + 0.35F);
        this.green = Math.max(0.0F, MathHelper.sin((this.colorScale + 0.33333334F) * 6.2831855F) * 0.65F + 0.35F);
        this.blue = Math.max(0.0F, MathHelper.sin((this.colorScale + 0.6666667F) * 6.2831855F) * 0.65F + 0.35F);
    }

    @Override
    public void buildGeometry(@NotNull VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3d inerp = renderInfo.getPos();
        if (this.age > this.getMaxAge()) {
            this.markDead();
        }

        Vec3d Vector3d = renderInfo.getPos();
        float f = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.x) - Vector3d.getX());
        float f1 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.y) - Vector3d.getY());
        float f2 = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.z) - Vector3d.getZ());
        Quaternionf quaternion;
        if (this.angle == 0.0F) {
            quaternion = renderInfo.getRotation();
        } else {
            quaternion = new Quaternionf(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.prevAngle, this.angle);
            quaternion.mul(RotationAxis.POSITIVE_Z.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1 = quaternion.transform(vector3f1);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = this.getSize(partialTicks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f = quaternion.transform(vector3f);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0;
        float f8 = 1;
        float f5 = 0;
        float f6 = 1;
        RenderSystem.setShaderTexture(0, SIREN_MUSIC);
        int j = this.getBrightness(partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
        vertexbuffer.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).texture(f8, f6).color(this.red, this.green, this.blue, this.alpha).light(j).next();
        vertexbuffer.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).texture(f8, f5).color(this.red, this.green, this.blue, this.alpha).light(j).next();
        vertexbuffer.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).texture(f7, f5).color(this.red, this.green, this.blue, this.alpha).light(j).next();
        vertexbuffer.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).texture(f7, f6).color(this.red, this.green, this.blue, this.alpha).light(j).next();
        Tessellator.getInstance().draw();
    }

    @Override
    public void tick() {
        super.tick();
        this.colorScale += 0.015F;
        if (this.colorScale > 25) {
            this.colorScale = 0;
        }
        this.red = Math.max(0.0F, MathHelper.sin((this.colorScale + 0.0F) * 6.2831855F) * 0.65F + 0.35F);
        this.green = Math.max(0.0F, MathHelper.sin((this.colorScale + 0.33333334F) * 6.2831855F) * 0.65F + 0.35F);
        this.blue = Math.max(0.0F, MathHelper.sin((this.colorScale + 0.6666667F) * 6.2831855F) * 0.65F + 0.35F);

    }

    @Override
    public int getBrightness(float partialTick) {
        return super.getBrightness(partialTick);
    }

    public int getFXLayer() {
        return 3;
    }

    @Override
    public @NotNull ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

}