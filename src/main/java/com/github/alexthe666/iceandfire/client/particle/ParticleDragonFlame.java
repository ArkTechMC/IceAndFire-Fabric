package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
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

public class ParticleDragonFlame extends SpriteBillboardParticle {
    private static final Identifier DRAGONFLAME = new Identifier(IceAndFire.MOD_ID, "textures/particles/dragon_flame.png");
    private final float dragonSize;
    private final double initialX;
    private final double initialY;
    private final double initialZ;
    private final float speedBonus;
    private double targetX;
    private double targetY;
    private double targetZ;
    private EntityDragonBase dragon;

    public ParticleDragonFlame(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, float dragonSize) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.initialX = xCoordIn;
        this.initialY = yCoordIn;
        this.initialZ = zCoordIn;
        this.targetX = xCoordIn + (double) ((this.random.nextFloat() - this.random.nextFloat()) * 1.75F * dragonSize);
        this.targetY = yCoordIn + (double) ((this.random.nextFloat() - this.random.nextFloat()) * 1.75F * dragonSize);
        this.targetZ = zCoordIn + (double) ((this.random.nextFloat() - this.random.nextFloat()) * 1.75F * dragonSize);
        this.setPos(this.x, this.y, this.z);
        this.dragonSize = dragonSize;
        this.speedBonus = this.random.nextFloat() * 0.015F;
    }

    public ParticleDragonFlame(ClientWorld world, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase, int startingAge) {
        this(world, x, y, z, motX, motY, motZ, MathHelper.clamp(entityDragonBase.getRenderSize() * 0.08F, 0.55F, 3F));
        this.dragon = entityDragonBase;
        this.targetX = this.dragon.burnParticleX + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.targetY = this.dragon.burnParticleY + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.targetZ = this.dragon.burnParticleZ + (double) ((this.random.nextFloat() - this.random.nextFloat())) * 3.5F;
        this.x = x;
        this.y = y;
        this.z = z;
        this.age = startingAge;
    }

    @Override
    public int getMaxAge() {
        return this.dragon == null ? 10 : 30;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        //TODO: use buffer stuff
        Vec3d inerp = renderInfo.getPos();
        if (this.age > this.getMaxAge()) {
            this.markDead();
        }

        Vec3d Vector3d = renderInfo.getPos();
        float f = (float) (MathHelper.lerp(partialTicks, this.prevPosX, this.x) - Vector3d.getX());
        float f1 = (float) (MathHelper.lerp(partialTicks, this.prevPosY, this.y) - Vector3d.getY());
        float f2 = (float) (MathHelper.lerp(partialTicks, this.prevPosZ, this.z) - Vector3d.getZ());
        Quaternionf quaternion;
        if (this.angle == 0.0F)
            quaternion = renderInfo.getRotation();
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
        RenderSystem.setShaderTexture(0, DRAGONFLAME);
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

    @Override
    public void tick() {
        super.tick();

        if (this.dragon == null) {
            float distX = (float) (this.initialX - this.x);
            float distZ = (float) (this.initialZ - this.z);
            this.velocityX += distX * -0.01F * this.dragonSize * this.random.nextFloat();
            this.velocityZ += distZ * -0.01F * this.dragonSize * this.random.nextFloat();
            this.velocityY += 0.015F * this.random.nextFloat();
        } else {
            double d2 = this.targetX - this.initialX;
            double d3 = this.targetY - this.initialY;
            double d4 = this.targetZ - this.initialZ;
            float speed = 0.015F + this.speedBonus;
            this.velocityX += d2 * speed;
            this.velocityY += d3 * speed;
            this.velocityZ += d4 * speed;
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }
}
