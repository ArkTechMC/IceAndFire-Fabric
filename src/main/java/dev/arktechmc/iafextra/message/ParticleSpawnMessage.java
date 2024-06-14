package dev.arktechmc.iafextra.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import dev.arktechmc.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ParticleSpawnMessage implements S2CMessage {
    private DefaultParticleType type;
    private double x, y, z;
    private double vx, vy, vz;

    public ParticleSpawnMessage() {
    }

    public ParticleSpawnMessage(DefaultParticleType type, double x, double y, double z, double vx, double vy, double vz) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "particle");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeString(this.type.asString());
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeDouble(this.vx);
        buf.writeDouble(this.vy);
        buf.writeDouble(this.vz);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.type = (DefaultParticleType) Registries.PARTICLE_TYPE.get(new Identifier(buf.readString()));
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.vx = buf.readDouble();
        this.vy = buf.readDouble();
        this.vz = buf.readDouble();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        assert client.world != null;
        client.world.addParticle(this.type, this.x, this.y, this.z, this.vx, this.vy, this.vz);
    }
}
