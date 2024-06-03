package com.github.alexthe666.citadel.client.model.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gegy1000
 * @since 1.0.0
 */
public class TabulaAnimationContainer {
    private String name;
    private String identifier;

    private boolean loops;

    private final Map<String, List<TabulaAnimationComponentContainer>> sets = new HashMap<>();

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean doesLoop() {
        return this.loops;
    }

    public Map<String, List<TabulaAnimationComponentContainer>> getComponents() {
        return this.sets;
    }
}
