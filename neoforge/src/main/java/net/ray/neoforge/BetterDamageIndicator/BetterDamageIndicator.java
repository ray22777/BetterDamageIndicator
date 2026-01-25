package net.ray.neoforge.BetterDamageIndicator;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.ray.BetterDamageIndicator.config.DamageConfig;

@Mod(BetterDamageIndicator.MODID)
public class BetterDamageIndicator {
    public static final String MODID = "better_damage_indicator";

    public BetterDamageIndicator() {
        AutoConfig.register(DamageConfig.class, GsonConfigSerializer::new);
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (minecraft, parent) -> AutoConfig.getConfigScreen(DamageConfig.class, parent).get()
        );
    }
}