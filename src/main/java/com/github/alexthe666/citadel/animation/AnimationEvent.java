package com.github.alexthe666.citadel.animation;

import com.iafenvoy.iafextra.event.Cancelable;
import com.iafenvoy.iafextra.event.Event;
import net.minecraft.entity.Entity;

public class AnimationEvent<T extends Entity & IAnimatedEntity> extends Event {
    protected Animation animation;
    private final T entity;

    AnimationEvent(T entity, Animation animation) {
        this.entity = entity;
        this.animation = animation;
    }

    public T getEntity() {
        return this.entity;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    @Cancelable
    public static class Start<T extends Entity & IAnimatedEntity> extends AnimationEvent<T> {
        public Start(T entity, Animation animation) {
            super(entity, animation);
        }

        public void setAnimation(Animation animation) {
            this.animation = animation;
        }
    }

    public static class Tick<T extends Entity & IAnimatedEntity> extends AnimationEvent<T> {
        protected int tick;

        public Tick(T entity, Animation animation, int tick) {
            super(entity, animation);
            this.tick = tick;
        }

        public int getTick() {
            return this.tick;
        }
    }
}