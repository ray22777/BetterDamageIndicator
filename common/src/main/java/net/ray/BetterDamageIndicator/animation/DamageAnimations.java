package net.ray.BetterDamageIndicator.animation;

import net.ray.BetterDamageIndicator.DamageRenderer;

public class DamageAnimations {
    // Floating Animation (original)
    public static final DamageRenderer.DamageAnimation FLOATING_ANIMATION = (hologram, random) -> {
        float angle = random.nextFloat() * (float)Math.PI * 2;
        final float[] vel = {
                (float) Math.cos(angle) * (0.004f + random.nextFloat() * 0.001f),
                0.08f + random.nextFloat() * 0.02f,
                (float) Math.sin(angle) * (0.004f + random.nextFloat() * 0.001f)
        };

        final float gravity = -0.004f;
        final float dragXZ = 0.97f;
        final float dragY = 0.98f;

        hologram.onUpdate(h -> {
            // Apply physics
            vel[0] *= dragXZ;
            vel[1] *= dragY;
            vel[2] *= dragXZ;

            // Apply gravity
            vel[1] += gravity;

            // Update position
            h.x += vel[0];
            h.y += vel[1];
            h.z += vel[2];

            // Gentle fade out
            float progress = h.age / (float)h.lifetime;

            if (progress > 0.6f) {
                float fadeRate = 0.95f - (progress - 0.6f) * 0.5f;
                h.alpha *= fadeRate;
            } else {
                h.alpha = 0.8f + 0.2f * (float)Math.sin(h.age * 0.2f);
            }

            h.scale = 1f - progress * 0.3f;

            if (h.alpha < 0.05f) {
                h.remove();
            }
        });
    };

    // Particle Animation (from TextParticle)
    public static final DamageRenderer.DamageAnimation PARTICLE_ANIMATION = (hologram, random) -> {
        final float[] vel = {
                (random.nextFloat() - 0.5f) * 0.05f,
                0.15f + random.nextFloat() * 0.1f,
                (random.nextFloat() - 0.5f) * 0.05f
        };

        final float velocityMultiplier = 0.99F;
        final float gravityStrength = 0.75F;
        final int maxAge = 32;

        hologram.lifetime = maxAge;

        hologram.onUpdate(h -> {
            // Apply drag
            vel[0] *= velocityMultiplier;
            vel[1] *= velocityMultiplier;
            vel[2] *= velocityMultiplier;

            // Apply gravity
            vel[1] -= 0.04F * gravityStrength;

            // Update position
            h.x += vel[0];
            h.y += vel[1];
            h.z += vel[2];

            // Simple fade out
            float progress = h.age / (float)maxAge;
            h.alpha = 1f - progress;

            // Optional: Add slight shrinking
            h.scale = 1f - progress * 0.5f;

            if (h.alpha < 0.05f) {
                h.remove();
            }
        });
    };

    // Hybrid Animation
    public static final DamageRenderer.DamageAnimation HYBRID_ANIMATION = (hologram, random) -> {
        float angle = random.nextFloat() * (float)Math.PI * 2;
        final float[] vel = {
                (float) Math.cos(angle) * (0.008f + random.nextFloat() * 0.002f),
                0.12f + random.nextFloat() * 0.03f,
                (float) Math.sin(angle) * (0.008f + random.nextFloat() * 0.002f)
        };

        final float velocityMultiplier = 0.96f;
        final float gravityStrength = 0.25f;
        final int maxAge = 40;

        hologram.lifetime = maxAge;

        hologram.onUpdate(h -> {
            // Apply drag
            vel[0] *= velocityMultiplier;
            vel[1] *= velocityMultiplier;
            vel[2] *= velocityMultiplier;

            // Apply gravity
            vel[1] -= 0.04F * gravityStrength;

            // Update position
            h.x += vel[0];
            h.y += vel[1];
            h.z += vel[2];

            // Fade with slight pulsing
            float progress = h.age / (float)maxAge;

            if (progress > 0.7f) {
                h.alpha *= 0.85f;
            } else {
                h.alpha = 0.9f + 0.1f * (float)Math.sin(h.age * 0.15f);
            }

            h.scale = 1f - progress * 0.4f;

            if (h.alpha < 0.05f) {
                h.remove();
            }
        });
    };

    // Arc Animation
    public static final DamageRenderer.DamageAnimation ARCH_ANIMATION = (hologram, random) -> {
        float angle = random.nextFloat() * (float)Math.PI * 2;
        final float[] vel = {
                (float) Math.cos(angle) * (0.02f + random.nextFloat() * 0.01f),
                0.2f + random.nextFloat() * 0.1f,
                (float) Math.sin(angle) * (0.02f + random.nextFloat() * 0.01f)
        };

        final float gravity = 0.04f;
        final float drag = 0.98f;
        final int maxAge = 25;

        hologram.lifetime = maxAge;

        final double startY = hologram.y;

        hologram.onUpdate(h -> {
            // Apply drag
            vel[0] *= drag;
            vel[1] *= drag;
            vel[2] *= drag;

            // Apply gravity
            vel[1] -= gravity;

            // Update position
            h.x += vel[0];
            h.y += vel[1];
            h.z += vel[2];

            // Fade based on height
            float progress = h.age / (float)maxAge;
            float heightFactor = Math.max(0, 1f - (float)(h.y - startY) * 2f);
            h.alpha = progress < 0.5f ? 1f : heightFactor;


            if (h.alpha < 0.05f) {
                h.remove();
            }
        });
    };
    public static final DamageRenderer.DamageAnimation NEW_ANIM = (hologram, random) -> {
        // Store original position from entity
        final double originalX = hologram.x;
        final double originalY = hologram.y;
        final double originalZ = hologram.z;

        // Animation values
        final float startScale = 4f;    // Start very big
        final float midScale = 1.4f;      // Shrink to this
        final float endScale = 1f;      // Final size

        // Float upward
        final float totalFloatHeight = 0.4f;

        // Random positioning around the mob (X and Y offset)
        // Position relative to player view (spread around mob)
        final float angle = random.nextFloat() * (float)Math.PI * 2f;
        final float distance = 0.2f + random.nextFloat() * 0.6f; // 0.2 to 0.5 blocks radius

        // Calculate offset based on angle (circular around mob)
        final float offsetX = (float)Math.cos(angle) * distance;
        final float offsetY = (random.nextFloat() - 0.3f) * 0.15f; // Slight vertical variation
        final float offsetZ = (float)Math.sin(angle) * distance;

        // Critical hit chance
        final boolean isCritical = true; // 15% chance for crit
        final float critMultiplier = 1.25f;

        hologram.onUpdate(h -> {
            float progress = h.age / (float)h.lifetime;
            if (progress < 0.12f) {
                float t = progress / 0.1f;   // 0 → 1
                h.alpha = t * t;             // ease-in
            }
            // Phase 1: Quick shrink (first 15% of lifetime)
            if (progress < 0.15f) {
                float phaseProgress = progress / 0.15f;

                // FAST shrink: Very big → Medium (ease-out back for bounce effect)
                float ease = 1f - (float)Math.pow(1f - phaseProgress, 1.8);

                if (phaseProgress < 0.6f) {
                    // First 60% of shrink: startScale → midScale
                    float subProgress = phaseProgress / 0.6f;
                    h.scale = startScale - (startScale - midScale) * subProgress;
                } else {
                    // Last 40%: midScale → endScale (with slight overshoot back)
                    float subProgress = (phaseProgress - 0.6f) / 0.4f;
                    float bounce = (float)Math.sin(subProgress * Math.PI * 0.5f);
                    h.scale = midScale - (midScale - endScale) * bounce;
                }

                // Apply critical multiplier
                if (isCritical) h.scale *= critMultiplier;

                // Calculate vertical offset to keep text centered as it shrinks
                float fontHeight = 0.2f; // Approximate height of text in blocks at scale 1
                float currentHeight = fontHeight * h.scale;
                float targetHeight = fontHeight * endScale;
                float heightDifference = currentHeight - targetHeight;
                float verticalAdjust = heightDifference * 0.5f; // Move up half the height difference

                h.y = originalY + offsetY - verticalAdjust + (totalFloatHeight * 0.2f * phaseProgress);

                // Apply circular offset around mob
                h.x = originalX + offsetX;
                h.z = originalZ + offsetZ;

                // Fade in quickly
                h.alpha = Math.min(phaseProgress * 3f, 1f);
            }
            // Phase 2: Float upward (15% to 70% of lifetime)
            else if (progress < 0.7f) {
                float phaseProgress = (progress - 0.15f) / 0.55f;

                // Maintain scale
                h.scale = isCritical ? endScale * critMultiplier : endScale;

                // Float upward with smooth easing
                float floatEase = 1f - (float)Math.pow(1f - phaseProgress, 1.5f);
                h.y = originalY + offsetY + (totalFloatHeight * (0.2f + 0.8f * floatEase));

                // Maintain position around mob
                h.x = originalX + offsetX;
                h.z = originalZ + offsetZ;

                // Alpha stays at 1
                h.alpha = 1f;

                // Very subtle drift to make it feel alive
                float driftTime = h.age * 0.05f;
                h.x += Math.sin(driftTime) * 0.002f;
                h.z += Math.cos(driftTime) * 0.002f;
            }
            // Phase 3: Fade out (last 30% of lifetime)
            else {
                float phaseProgress = (progress - 0.7f) / 0.3f;

                // Maintain scale
                h.scale = isCritical ? endScale * critMultiplier : endScale;

                // Continue floating up slightly during fade
                h.y = originalY + offsetY + totalFloatHeight * (1f + phaseProgress * 0.1f);

                // Maintain position
                h.x = originalX + offsetX;
                h.z = originalZ + offsetZ;

                // Fade out
                h.alpha = 1f - phaseProgress;

                // Even subtler drift during fade
                float driftTime = h.age * 0.03f;
                h.x += Math.sin(driftTime) * 0.001f;
                h.z += Math.cos(driftTime) * 0.001f;
            }

            // Critical hit pulse effect (only during active phase)
            if (isCritical && progress < 0.8f) {
                float pulse = 1f + 0.04f * (float)Math.sin(h.age * 0.2f);
                h.scale *= pulse;
            }

            // Remove when invisible
            if (h.alpha < 0.05f) {
                h.remove();
            }
        });
    };
}