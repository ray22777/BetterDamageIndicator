package net.ray.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.ray.DamageRenderer;
import net.ray.DamageTracker;

public final class ExampleModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Update damage trackers every tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                DamageTracker.tickAll();
            }
        });

        // Render damage numbers during world render
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            DamageRenderer.renderAll();
        });
    }
}
