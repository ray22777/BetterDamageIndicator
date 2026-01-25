package net.ray.BetterDamageIndicator.animation;

import net.ray.HologramAPI.Hologram;
import java.util.Random;

public enum DamageAnimations {
    STATIONARY {
        @Override
        public void apply(Hologram hologram, Random random) {
            final float baseScale = hologram.scale;

            hologram.onUpdate(h -> {
                float progress = h.age / (float)h.lifetime;
                h.alpha = 1f - (float) Math.pow(progress, 6);

                h.scale = baseScale;

                if (h.alpha <= 0.01f) h.remove();
            });
        }
    },
    RISE {
        @Override
        public void apply(Hologram hologram, Random random) {

            hologram.y = hologram.y - 0.3f;

            final float[] riseSpeed = {(0.08f + random.nextFloat() * 0.02f)*0.5f};
            final float dragY = 0.96f;
            final float baseScale = hologram.scale;

            hologram.onUpdate(h -> {
                h.y += riseSpeed[0];
                riseSpeed[0] *= dragY;

                float progress = h.age / (float) h.lifetime;
                h.alpha = 1f - (float) Math.pow(progress, 6);
                h.scale = baseScale;

                if (h.alpha <= 0.01f) h.remove();
            });
        }
    },

    ARC {
        @Override
        public void apply(Hologram hologram, Random random) {
            final float[] vel = {
                    (random.nextFloat() - 0.5f) * 0.05f,
                    0.15f + random.nextFloat() * 0.1f,
                    (random.nextFloat() - 0.5f) * 0.05f
            };
            final float velocityMultiplier = 0.99F;
            final float gravityStrength = 0.60F;
            final float baseScale = hologram.scale;
            hologram.lifetime = (int) (hologram.lifetime * 1.67);
            hologram.onUpdate(h -> {
                vel[0] *= velocityMultiplier;
                vel[1] *= velocityMultiplier;
                vel[2] *= velocityMultiplier;
                vel[1] -= 0.04F * gravityStrength;

                h.x += vel[0];
                h.y += vel[1];
                h.z += vel[2];

                float progress = h.age / (float) h.lifetime;
                h.alpha = 1f - (float) Math.pow(progress+0.42, 6);
                h.scale = baseScale * (1f - progress * 0.5f);
                if (progress > 0.67f) h.remove();
                if (h.alpha < 0.05f) h.remove();
            });
        }
    },
    ZOOM_OUT {
        @Override
        public void apply(Hologram hologram, Random random) {
            final double originalX = hologram.x;
            final double originalY = hologram.y;
            final double originalZ = hologram.z;

            final float startScale = 3.0f;
            final float endScale = 1.0f;
            final float totalFloat = 0.05f;

            final float offsetX = (random.nextFloat() - 0.5f) * 0.08f;
            final float offsetY = (random.nextFloat() - 0.5f) * 0.05f;
            final float offsetZ = (random.nextFloat() - 0.5f) * 0.08f;

            final float textHeightAtScale1 = 0.2f;
            final float startHeight = textHeightAtScale1 * startScale;
            final float endHeight = textHeightAtScale1 * endScale;
            final float totalHeightAdjust = (startHeight - endHeight) * 0.5f;

            final float baseScale = hologram.scale;

            hologram.onUpdate(h -> {
                float progress = h.age / (float) h.lifetime;

                if (progress < 0.2f) { // Phase 1
                    float phaseProgress = progress / 0.2f;
                    float ease = (float) Math.pow(phaseProgress, 0.5f);

                    h.scale = baseScale * (startScale - (startScale - endScale) * ease);

                    float currentHeight = textHeightAtScale1 * h.scale;
                    float heightDiff = startHeight - currentHeight;
                    float verticalAdjust = heightDiff * 0.5f;

                    h.alpha = ease;

                    h.x = originalX + offsetX;
                    h.y = originalY + offsetY - verticalAdjust + (totalFloat * 0.2f * phaseProgress);
                    h.z = originalZ + offsetZ;
                } else if (progress < 0.7f) {
                    h.scale = baseScale * endScale;
                    h.alpha = 1.0f;

                    float phaseProgress = (progress - 0.2f) / 0.5f;
                    float floatEase = 1f - (float) Math.pow(1f - phaseProgress, 1.8f);

                    float verticalAdjust = totalHeightAdjust;

                    h.y = originalY + offsetY - verticalAdjust + totalFloat * (0.2f + 0.6f * floatEase);
                    h.x = originalX + offsetX;
                    h.z = originalZ + offsetZ;

                    h.scale = baseScale * endScale * (1f + 0.02f * (float) Math.sin(h.age * 0.1f));
                } else {
                    float phaseProgress = (progress - 0.7f) / 0.3f;
                    float fadeEase = (float) Math.pow(phaseProgress, 1.5f);

                    h.alpha = 1f - fadeEase;

                    float verticalAdjust = totalHeightAdjust;

                    h.y = originalY + offsetY - verticalAdjust + totalFloat * (0.8f + phaseProgress * 0.2f);
                    h.x = originalX + offsetX;
                    h.z = originalZ + offsetZ;

                    if (h.alpha < 0.05f) h.remove();
                }
            });
        }
    };

    public abstract void apply(Hologram hologram, Random random);
}
