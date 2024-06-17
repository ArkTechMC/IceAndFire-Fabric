package com.iafenvoy.iceandfire.client.particle;

import com.iafenvoy.iceandfire.IceAndFire;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ParticlePixieDust extends SpriteBillboardParticle {
    private static final Identifier PIXIE_DUST = new Identifier(IceAndFire.MOD_ID, "textures/particles/pixie_dust.png");
    final float reddustParticleScale;

    public ParticlePixieDust(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn, 1F, p_i46349_8_, p_i46349_9_, p_i46349_10_);
    }

    public ParticlePixieDust(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float scale, float red, float green, float blue) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.velocityX *= 0.10000000149011612D;
        this.velocityY *= 0.10000000149011612D;
        this.velocityZ *= 0.10000000149011612D;
        float f = (float) Math.random() * 0.4F + 0.6F;
        this.red = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * red * f;
        this.green = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * green * f;
        this.blue = ((float) (Math.random() * 0.20000000298023224D) + 0.8F) * blue * f;
        this.scale *= scale;
        this.reddustParticleScale = this.scale;
        this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        this.maxAge = (int) ((float) this.maxAge * scale);
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float scaley = ((float) this.age + partialTicks) / (float) this.maxAge * 32.0F;
        scaley = MathHelper.clamp(scaley, 0.0F, 1.0F);
        this.scale = this.reddustParticleScale * scaley;

        if (this.age > this.getMaxAge()) this.markDead();

        Vec3d Vector3d = renderInfo.getPos();
        float f = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.x) - Vector3d.getX());
        float f1 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.y) - Vector3d.getY());
        float f2 = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.z) - Vector3d.getZ());
        Quaternionf quaternion;
        if (this.angle == 0.0F) quaternion = renderInfo.getRotation();
        else {
            quaternion = new Quaternionf(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.prevAngle, this.angle);
            quaternion.mul(RotationAxis.POSITIVE_Z.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        quaternion.transform(vector3f1);
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
        RenderSystem.setShaderTexture(0, PIXIE_DUST);
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
    public int getBrightness(float partialTick) {
        return 240;
    }

    public void onUpdate() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) this.markDead();

        this.move(this.velocityX, this.velocityY, this.velocityZ);

        if (this.y == this.prevPosY) {
            this.velocityX *= 1.1D;
            this.velocityZ *= 1.1D;
        }

        this.velocityX *= 0.9599999785423279D;
        this.velocityY *= 0.9599999785423279D;
        this.velocityZ *= 0.9599999785423279D;

        if (this.onGround) {
            this.velocityX *= 0.699999988079071D;
            this.velocityZ *= 0.699999988079071D;
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }
}
