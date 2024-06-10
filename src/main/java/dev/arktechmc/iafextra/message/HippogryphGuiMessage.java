package dev.arktechmc.iafextra.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import dev.arktechmc.iafextra.network.S2CMessage;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class HippogryphGuiMessage implements S2CMessage {
    private int hippogryphId;

    public HippogryphGuiMessage(EntityHippogryph hippogryph) {
        this.hippogryphId = hippogryph.getId();
    }

    public HippogryphGuiMessage() {
    }

    @Override
    public Identifier getId() {
        return new Identifier(IceAndFire.MOD_ID, "hippogryph_gui");
    }

    @Override
    public void encode(PacketByteBuf buf) {
        buf.writeInt(this.hippogryphId);
    }

    @Override
    public void decode(PacketByteBuf buf) {
        this.hippogryphId = buf.readInt();
    }

    @Override
    public void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender) {
        Entity entity = client.world.getEntityById(this.hippogryphId);
        //TODO: Screen
//        if(entity instanceof EntityHippogryph hippogryph)
//            client.setScreen(new ContainerHippogryph(0, hippogryph.hippogryphInventory, client.player.getInventory(), hippogryph));
//        client.setScreen(new NamedScreenHandlerFactory() {
//            @Override
//            public ScreenHandler createMenu(int p_createMenu_1_, @NotNull PlayerInventory p_createMenu_2_, @NotNull PlayerEntity p_createMenu_3_) {
//                return new ContainerHippogryph(p_createMenu_1_, hippogryph.hippogryphInventory, p_createMenu_2_, hippogryph);
//            }
//
//            @Override
//            public @NotNull Text getDisplayName() {
//                return Text.translatable("entity.iceandfire.hippogryph");
//            }
//        }.);
    }
}
