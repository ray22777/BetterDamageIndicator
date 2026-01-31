package net.ray.fabric.BetterDamageIndicator;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.ray.BetterDamageIndicator.config.ConfigGetter;
import net.ray.BetterDamageIndicator.config.IndicatorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterDamageIndicator implements ModInitializer {
    public static final String MOD_ID = "better_damage_indicator";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AutoConfig.register(IndicatorConfig.class, GsonConfigSerializer::new);
        ConfigGetter.iconfig = AutoConfig.getConfigHolder(IndicatorConfig.class).getConfig();
    }

}