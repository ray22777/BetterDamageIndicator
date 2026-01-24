
package net.ray.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.ray.DamageTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class HealthBarMixin {
    @Unique
    private float lastHealth;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickStart(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity.level().isClientSide()) {
            lastHealth = entity.getHealth();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTickEnd(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity.level().isClientSide()) {
            float currentHealth = entity.getHealth();
            if (currentHealth < lastHealth - 0.1f) {
                float damage = lastHealth - currentHealth;
                DamageTracker.addDamage(entity, damage);
            }
        }
    }
}