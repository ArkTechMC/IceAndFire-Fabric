package com.github.alexthe666.citadel.client.rewards;

import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.*;

public class CitadelCapes {

    private static final List<Cape> CAPES = new ArrayList<>();
    private static final Map<UUID, Boolean> HAS_CAPES_ENABLED = new LinkedHashMap<>();

    public static void addCapeFor(List<UUID> uuids, String translationKey, Identifier texture) {
        CAPES.add(new Cape(uuids, translationKey, texture));
    }

    public static List<Cape> getCapesFor(UUID uuid) {
        return CAPES.isEmpty() ? CAPES : CAPES.stream().filter(cape -> cape.isFor(uuid)).toList();
    }


    public static Cape getNextCape(String currentID, UUID playerUUID) {
        if (CAPES.isEmpty()) {
            return null;
        }
        int currentIndex = -1;
        for (int i = 0; i < CAPES.size(); i++) {
            if (CAPES.get(i).getIdentifier().equals(currentID)) {
                currentIndex = i;
                break;
            }
        }
        boolean flag = false;
        for (int i = currentIndex + 1; i < CAPES.size(); i++) {
            if (CAPES.get(i).isFor(playerUUID)) {
                return CAPES.get(i);
            }
        }
        return null;
    }

    public static Cape getById(String identifier) {
        for (int i = 0; i < CAPES.size(); i++) {
            if (CAPES.get(i).getIdentifier().equals(identifier)) {
                return CAPES.get(i);
            }
        }
        return null;
    }

    private static Cape getFirstApplicable(PlayerEntity player) {
        for (int i = 0; i < CAPES.size(); i++) {
            if (CAPES.get(i).isFor(player.getUuid())) {
                return CAPES.get(i);
            }
        }
        return null;
    }

    public static Cape getCurrentCape(PlayerEntity player) {
        NbtCompound tag = CitadelEntityData.getOrCreateCitadelTag(player);
        if (tag.getBoolean("CitadelCapeDisabled")) {
            return null;
        }
        if (tag.contains("CitadelCapeType")) {
            if (tag.getString("CitadelCapeType").isEmpty()) {
                return getFirstApplicable(player);
            } else {
                return CitadelCapes.getById(tag.getString("CitadelCapeType"));
            }
        } else {
            return null;
        }
    }

    public static class Cape {
        private final List<UUID> isFor;
        private final String identifier;
        private final Identifier texture;

        public Cape(List<UUID> isFor, String identifier, Identifier texture) {
            this.isFor = isFor;
            this.identifier = identifier;
            this.texture = texture;
        }

        public List<UUID> getIsFor() {
            return isFor;
        }

        public String getIdentifier() {
            return identifier;
        }

        public Identifier getTexture() {
            return texture;
        }

        public boolean isFor(UUID uuid) {
            return isFor.contains(uuid);
        }
    }
}
