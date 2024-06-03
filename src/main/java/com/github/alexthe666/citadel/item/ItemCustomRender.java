package com.github.alexthe666.citadel.item;

import com.github.alexthe666.citadel.Citadel;
import net.minecraft.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemCustomRender extends Item {

    public ItemCustomRender(Settings props) {
        super(props);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<IClientItemExtensions> consumer) {
        consumer.accept(((IClientItemExtensions) Citadel.PROXY.getISTERProperties()));
    }
}
