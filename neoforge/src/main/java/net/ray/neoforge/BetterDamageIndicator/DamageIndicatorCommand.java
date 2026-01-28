package net.ray.neoforge.BetterDamageIndicator;

import com.mojang.brigadier.CommandDispatcher;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.ray.BetterDamageIndicator.config.IndicatorConfig;

import java.util.function.Supplier;

// Use NeoForge's EventBusSubscriber
@EventBusSubscriber(modid = "better_damage_indicator", bus = EventBusSubscriber.Bus.GAME)
public class DamageIndicatorCommand {

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("damageindicator")
                .executes(context -> {
                    Minecraft client = Minecraft.getInstance();
                    if (client.level == null) return 0;

                    client.execute(() -> {
                        try {
                            Supplier<Screen> screenSupplier = AutoConfig.getConfigScreen(
                                    IndicatorConfig.class,
                                    client.screen
                            );
                            Screen configScreen = screenSupplier.get();
                            client.setScreen(configScreen);

                        } catch (Exception e) {
                            System.err.println("Failed to open config: " + e);
                            if (client.player != null) {
                                client.player.displayClientMessage(
                                        Component.literal("Error while opening config"),
                                        false
                                );
                            }
                        }
                    });

                    return 1;
                })
        );
    }
}