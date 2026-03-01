package net.ray.BetterDamageIndicator;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.ray.BetterDamageIndicator.config.ConfigGetter;
import net.ray.HologramAPI.Hologram;
import net.ray.HologramAPI.HologramAPI;
import java.util.Random;

public class DamageRenderer {
//    private static DamageAnimations currentAnimation = DamageAnimations.NEW_ANIM;
//
//    public static void setAnimation(DamageAnimations animation) {
//        currentAnimation = animation;
//    }
//
//    public static DamageAnimations getCurrentAnimation() {
//        return currentAnimation;
//    }
//
//    public static void cycleAnimation() {
//        DamageAnimations[] animations = {
//                DamageAnimations.FLOATING,
//                DamageAnimations.PARTICLE,
//                DamageAnimations.HYBRID,
//                DamageAnimations.ARCH
//        };
//
//        for (int i = 0; i < animations.length; i++) {
//            if (currentAnimation == animations[i]) {
//                currentAnimation = animations[(i + 1) % animations.length];
//                return;
//            }
//        }
//        currentAnimation = DamageAnimations.FLOATING;
//    }


    public static void renderDamageIndicator(LivingEntity entity, float damage, boolean isCritical) {
        if(!ConfigGetter.iconfig.damageEnable) return;
        if(!ConfigGetter.iconfig.enableIndicator) return;
        String result;
        if (isCritical) {
            result = ConfigGetter.iconfig.critDamageFormat.replace("{dmg}",
                    String.format("%." + ConfigGetter.iconfig.decimal + "f", damage));
        } else {
            result = ConfigGetter.iconfig.damageFormat.replace("{dmg}",
                    String.format("%." + ConfigGetter.iconfig.decimal + "f", damage));
        }
        Component component = ComponentUtilsParser.parseColorCodes(result);

        Random random = new Random();
        double startX = entity.getX();
        double startY = entity.getY() + entity.getBbHeight() * 0.85;
        double startZ = entity.getZ();

        float radius = 0.2f + random.nextFloat() * 0.3f;

        float u = random.nextFloat();
        float v = random.nextFloat();
        float theta = 2 * (float)Math.PI * u;
        float phi = (float)Math.acos(2 * v - 1);

        double spawnX = startX + ConfigGetter.iconfig.damageoffset * (radius * Math.sin(phi) * Math.cos(theta) * 1.5);
        double spawnY = startY + ConfigGetter.iconfig.damageoffset * (radius * Math.sin(phi) * Math.sin(theta));
        double spawnZ = startZ + ConfigGetter.iconfig.damageoffset * (radius * Math.cos(phi));

        Hologram holo = HologramAPI.create(component, spawnX, spawnY, spawnZ)
                .shadow(ConfigGetter.iconfig.shadow)
                .lifetime(ConfigGetter.iconfig.damagelifetime)
                .scale(ConfigGetter.iconfig.damagescale)
                .renderOnTop(ConfigGetter.iconfig.renderInfront)
                .renderDistance(ConfigGetter.iconfig.renderDistance);
        ConfigGetter.iconfig.damageanimations.apply(holo, random);
    }

    public static void renderHealingIndicator(Entity entity, float heal) {
        if(!ConfigGetter.iconfig.healEnable) return;
        if(!ConfigGetter.iconfig.enableIndicator) return;

        String result = ConfigGetter.iconfig.healFormat.replace("{heal}", String.format("%." + ConfigGetter.iconfig.decimal + "f", heal));
        Component component = ComponentUtilsParser.parseColorCodes(result);

        Random random = new Random();
        double startX = entity.getX();
        double startY = entity.getY() + entity.getBbHeight() * 0.85;
        double startZ = entity.getZ();

        float radius = 0.2f + random.nextFloat() * 0.3f;

        float u = random.nextFloat();
        float v = random.nextFloat();
        float theta = 2 * (float)Math.PI * u;
        float phi = (float)Math.acos(2 * v - 1);

        double spawnX = startX + ConfigGetter.iconfig.healoffset * (radius * Math.sin(phi) * Math.cos(theta) * 1.5);
        double spawnY = startY + ConfigGetter.iconfig.healoffset * (radius * Math.sin(phi) * Math.sin(theta));
        double spawnZ = startZ + ConfigGetter.iconfig.healoffset * (radius * Math.cos(phi));

        Hologram holo = HologramAPI.create(component, spawnX, spawnY, spawnZ)
                .shadow(ConfigGetter.iconfig.shadow)
                .lifetime(ConfigGetter.iconfig.heallifetime)
                .scale(ConfigGetter.iconfig.healscale)
                .renderOnTop(ConfigGetter.iconfig.renderInfront)
                .renderDistance(ConfigGetter.iconfig.renderDistance);
        ConfigGetter.iconfig.healanimations.apply(holo, random);
    }
}