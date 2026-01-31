// mixin/LivingEntityMixin.java
package net.ray.BetterDamageIndicator.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.ray.BetterDamageIndicator.DamageTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class HealthBarMixin {

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void onBaseTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        DamageTracker.updateEntity(entity);
    }
}