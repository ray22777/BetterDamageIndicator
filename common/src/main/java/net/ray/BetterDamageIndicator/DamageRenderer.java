package net.ray.BetterDamageIndicator;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.ray.BetterDamageIndicator.animation.DamageAnimations;
import net.ray.HologramAPI.Hologram;
import net.ray.HologramAPI.HologramAPI;

import java.util.Random;

public class DamageRenderer {
    // Animation interface
    public interface DamageAnimation {
        void apply(Hologram hologram, Random random);
    }

    // Animation instances
    public static final DamageAnimation FLOATING_ANIMATION = DamageAnimations.FLOATING_ANIMATION;
    public static final DamageAnimation PARTICLE_ANIMATION = DamageAnimations.PARTICLE_ANIMATION;
    public static final DamageAnimation HYBRID_ANIMATION = DamageAnimations.HYBRID_ANIMATION;
    public static final DamageAnimation ARCH_ANIMATION = DamageAnimations.ARCH_ANIMATION;
    public static final DamageAnimation NEW_ANIM = DamageAnimations.NEW_ANIM;
    // Current animation
    private static DamageAnimation currentAnimation = NEW_ANIM;

    // Set animation
    public static void setAnimation(DamageAnimation animation) {
        currentAnimation = animation;
    }

    // Get current animation
    public static DamageAnimation getCurrentAnimation() {
        return currentAnimation;
    }

    // Cycle through animations
    public static void cycleAnimation() {
        DamageAnimation[] animations = {
                FLOATING_ANIMATION,
                PARTICLE_ANIMATION,
                HYBRID_ANIMATION,
                ARCH_ANIMATION
        };

        for (int i = 0; i < animations.length; i++) {
            if (currentAnimation == animations[i]) {
                currentAnimation = animations[(i + 1) % animations.length];
                return;
            }
        }
        currentAnimation = FLOATING_ANIMATION;
    }

    // Main render method
    public static void renderDamageIndicator(Entity entity, float damage) {
        renderDamageIndicator(entity, damage, currentAnimation);
    }

    // Render with specific animation
    public static void renderDamageIndicator(Entity entity, float damage, DamageAnimation animation) {
        Component styled = Component.literal(String.format("%.1f", damage))
                .withStyle(ChatFormatting.RED);

        Random random = new Random();

        // Start position
        double startX = entity.getX();
        double startY = entity.getY() + entity.getBbHeight() * 0.7;
        double startZ = entity.getZ();

        // Spawn position in radius 0.5-1.0 blocks
        float radius = 0.5f + random.nextFloat() * 0.5f;
        float randomAngle = random.nextFloat() * (float)Math.PI * 2;
        float uniformRadius = (float)Math.sqrt(random.nextFloat()) * radius;

        double spawnX = startX + Math.cos(randomAngle) * uniformRadius;
        double spawnZ = startZ + Math.sin(randomAngle) * uniformRadius;

        // Create hologram
        Hologram holo = HologramAPI.create(styled, spawnX, startY, spawnZ)
                .shadow(true)
                .lifetime(30)
                .scale(1f)
                .renderOnTop(true);

        // Apply animation
        animation.apply(holo, random);
    }

    // Get animation name
    public static String getAnimationName(DamageAnimation animation) {
        if (animation == FLOATING_ANIMATION) return "Floating";
        if (animation == PARTICLE_ANIMATION) return "Particle Style";
        if (animation == HYBRID_ANIMATION) return "Hybrid";
        if (animation == ARCH_ANIMATION) return "Arc";
        return "Unknown";
    }
}