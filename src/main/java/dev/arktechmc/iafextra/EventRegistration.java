package dev.arktechmc.iafextra;

import com.github.alexthe666.iceandfire.event.ServerEvents;
import dev.arktechmc.iafextra.event.AttackEntityEvent;
import dev.arktechmc.iafextra.event.EventBus;
import dev.arktechmc.iafextra.event.ProjectileImpactEvent;

public class EventRegistration {
    public static void register() {
        EventBus.register(ProjectileImpactEvent.class, ServerEvents::onArrowCollide);
        EventBus.register(AttackEntityEvent.class, ServerEvents::onPlayerAttackMob);
    }
}
