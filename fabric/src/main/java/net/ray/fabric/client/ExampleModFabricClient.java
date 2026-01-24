package net.ray.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.ray.DamageTracker;

public final class ExampleModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.level != null) {
                DamageTracker.tick();
            }
        });

        // Render damage numbers
//        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
//            DamageRenderer.render(context.matrixStack(), context.camera(), context.tickDelta());
//        });
    }
}
