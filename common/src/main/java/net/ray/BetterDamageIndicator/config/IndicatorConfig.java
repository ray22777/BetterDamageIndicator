package net.ray.BetterDamageIndicator.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.ray.BetterDamageIndicator.DamageTracker;
import net.ray.BetterDamageIndicator.animation.DamageAnimations;

@Config(name = "better-damage-indicator")
public class IndicatorConfig implements ConfigData {
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 1) //Enable health indicators
    public boolean enableIndicator = true;
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2) //Renders the damage indicator over everything, including blocks and entities. If enabled, indicator will not show in third person.
    public boolean renderInfront = true;
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 1) //Enables shadow
    public boolean shadow = true;
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 2) //Changes how far damage indicators will be shown
    public int renderDistance = 30;
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip(count = 1) //How many trailing decimals
    public int decimal = 1;
    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.Tooltip(count = 1) //Disable Self
    public boolean disableSelf = true;

    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.Tooltip(count = 2) //Enable damage indicator
    public boolean damageEnable = true;
    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @ConfigEntry.Gui.Tooltip(count = 2) //Damage source filter
    public DamageTracker.DAMAGE_SOURCE damageSource = DamageTracker.DAMAGE_SOURCE.ALL;
    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.Tooltip(count = 3) //Formatting for damage text, supporting the use of minecraft color codes (e.g. §a). Use %dmg% as damage placeholder.
    public String damageFormat = "&c-{dmg}";
    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.Tooltip(count = 3) //Formatting for critical hits, supporting the use of minecraft color codes (e.g. §a). Use %dmg% as damage placeholder.
    public String critDamageFormat = "&c&l-{dmg}";
    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @ConfigEntry.Gui.Tooltip(count = 2) //Damage animation
    public DamageAnimations damageanimations = DamageAnimations.ZOOM_OUT;
    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.Tooltip(count = 2) //How long the indicator will last (In ticks).
    public int damagelifetime = 30;
    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.Tooltip(count = 2) //Adjust offset around the entity.
    public float damageoffset = 1;
    @ConfigEntry.Category("damage")
    @ConfigEntry.Gui.Tooltip(count = 1) //How big the damage indicator will be.
    public float damagescale = 1;
    @ConfigEntry.Category("healing")
    @ConfigEntry.Gui.Tooltip(count = 2) //Enable healing indicator
    public boolean healEnable = true;
    @ConfigEntry.Category("healing")
    @ConfigEntry.Gui.Tooltip(count = 3) //Formatting for healing text, supporting the use of minecraft color codes (e.g. §a). Use %heal% as damage placeholder.
    public String healFormat = "§a+{heal}";
    @ConfigEntry.Category("healing")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @ConfigEntry.Gui.Tooltip(count = 2) //Healing animation
    public DamageAnimations healanimations = DamageAnimations.RISE;
    @ConfigEntry.Category("healing")
    @ConfigEntry.Gui.Tooltip(count = 2) //How long the indicator will last (In ticks).
    public int heallifetime = 30;
    @ConfigEntry.Category("healing")
    @ConfigEntry.Gui.Tooltip(count = 2) //Adjust offset around the entity.
    public float healoffset = 1;
    @ConfigEntry.Category("healing")
    @ConfigEntry.Gui.Tooltip(count = 1) //How big the healing indicator will be.
    public float healscale = 1;
}