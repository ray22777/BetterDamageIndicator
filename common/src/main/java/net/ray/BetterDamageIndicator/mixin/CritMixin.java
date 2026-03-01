package net.ray.BetterDamageIndicator.mixin;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.ray.BetterDamageIndicator.DamageRenderer;
import net.ray.BetterDamageIndicator.DamageTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ParticleEngine.class)
public class CritMixin {

    @Inject(
            method = "createTrackingEmitter(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/particles/ParticleOptions;)V",
            at = @At("HEAD")
    )
    private void onCreateTrackingEmitter(Entity entity, ParticleOptions particleOptions, CallbackInfo ci) {
        if (particleOptions == ParticleTypes.CRIT || particleOptions == ParticleTypes.ENCHANTED_HIT) {
            DamageTracker.markCriticalHit(entity.getId());

        }
    }
}
