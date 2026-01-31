package net.ray.BetterDamageIndicator.forge;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.ray.BetterDamageIndicator.config.ConfigGetter;
import net.ray.BetterDamageIndicator.config.IndicatorConfig;

@Mod(BetterDamageIndicator.MODID)
public class BetterDamageIndicator {
    public static final String MODID = "better_damage_indicator";

    public BetterDamageIndicator() {
        AutoConfig.register(IndicatorConfig.class, GsonConfigSerializer::new);
        ConfigGetter.iconfig = AutoConfig.getConfigHolder(IndicatorConfig.class).getConfig();
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) ->
                                AutoConfig.getConfigScreen(IndicatorConfig.class, parent).get()
                )
        );
    }
}