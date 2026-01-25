package net.ray.BetterDamageIndicator.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "better-damage-indicator")
public class DamageConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip //Enable health indicators
    boolean enableIndicator = true;

    @ConfigEntry.Gui.Tooltip //Renders the damage indicator over everything, including blocks and entities. If enabled, indicator will not show in third person.
    boolean renderInfront = true;

    @ConfigEntry.Gui.Tooltip //Changes how far damage indicators will be shown
    int renderDistance = 30;

    @ConfigEntry.Category("Damage Text")
    @ConfigEntry.Gui.Tooltip //Enable damage indicator
    boolean enableOnDamage = true;

    @ConfigEntry.Category("Damage Text")
    @ConfigEntry.Gui.Tooltip //Formatting for damage text, supporting the use of minecraft color codes (e.g. §a). Use %dmg% as damage placeholder.
    String damageFormat = "-%dmg%";

    @ConfigEntry.Category("Damage Text")
    @ConfigEntry.Gui.Tooltip //Animation for damage text.
    Enum animation;




}