package net.ray.fabric.BetterDamageIndicator.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public final class BetterDamageIndicatorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
//        // Update damage timers
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (client.player != null) {
//                DamageTracker.tick();
//            }
//        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            DamageIndicatorCommand.register(dispatcher);
        });

    }
}