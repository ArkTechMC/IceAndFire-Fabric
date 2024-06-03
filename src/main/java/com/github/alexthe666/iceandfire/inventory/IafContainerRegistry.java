package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import java.util.function.Supplier;

public class IafContainerRegistry {

    public static final LazyRegistrar<ScreenHandlerType<?>> CONTAINERS = LazyRegistrar
            .create(Registries.SCREEN_HANDLER, IceAndFire.MOD_ID);

    public static final RegistryObject<ScreenHandlerType<ContainerLectern>> IAF_LECTERN_CONTAINER = register(
            () -> new ScreenHandlerType<>(ContainerLectern::new, FeatureFlags.VANILLA_FEATURES), "iaf_lectern");
    public static final RegistryObject<ScreenHandlerType<ContainerPodium>> PODIUM_CONTAINER = register(
            () -> new ScreenHandlerType<>(ContainerPodium::new, FeatureFlags.VANILLA_FEATURES), "podium");
    public static final RegistryObject<ScreenHandlerType<ContainerDragon>> DRAGON_CONTAINER = register(
            () -> new ScreenHandlerType<>(ContainerDragon::new, FeatureFlags.VANILLA_FEATURES), "dragon");
    public static final RegistryObject<ScreenHandlerType<ContainerHippogryph>> HIPPOGRYPH_CONTAINER = register(
            () -> new ScreenHandlerType<>(ContainerHippogryph::new, FeatureFlags.VANILLA_FEATURES), "hippogryph");
    public static final RegistryObject<ScreenHandlerType<HippocampusContainerMenu>> HIPPOCAMPUS_CONTAINER = register(
            () -> new ScreenHandlerType<>(HippocampusContainerMenu::new, FeatureFlags.VANILLA_FEATURES), "hippocampus");
    public static final RegistryObject<ScreenHandlerType<ContainerDragonForge>> DRAGON_FORGE_CONTAINER = register(
            () -> new ScreenHandlerType<>(ContainerDragonForge::new, FeatureFlags.VANILLA_FEATURES), "dragon_forge");

    public static <C extends ScreenHandler> RegistryObject<ScreenHandlerType<C>> register(Supplier<ScreenHandlerType<C>> type,
                                                                                          String name) {
        return CONTAINERS.register(name, type);
    }

}
