package com.iafenvoy.iceandfire.util.trade.profession;

import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public record ProfessionHolder(PointOfInterestType poi, VillagerProfession profession) {
}
