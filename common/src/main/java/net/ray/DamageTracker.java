// DamageTracker.java - MODIFIED
package net.ray;

import net.minecraft.world.entity.LivingEntity;
import java.util.*;

public class DamageTracker {
    private static final Map<Integer, DamageInfo> damageMap = new HashMap<>();

    static class DamageInfo {
        float damage;
        int ticksLeft;
        double x, y, z;

        DamageInfo(float damage, LivingEntity entity) {
            this.damage = damage;
            this.ticksLeft = 40; // 2 seconds
            this.x = entity.getX();
            this.y = entity.getY() + entity.getBbHeight() + 0.5;
            this.z = entity.getZ();
        }

        void tick() {
            ticksLeft--;
        }

        boolean shouldRemove() {
            return ticksLeft <= 0;
        }
    }

    public static void addDamage(LivingEntity entity, float damage) {
        damageMap.put(entity.getId(), new DamageInfo(damage, entity));
        System.out.println("[DAMAGE] " + entity.getName().getString() +
                " took " + String.format("%.1f", damage) + " damage");
    }

    public static void tickAll() {
        Iterator<Map.Entry<Integer, DamageInfo>> it = damageMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, DamageInfo> entry = it.next();
            entry.getValue().tick();
            if (entry.getValue().shouldRemove()) {
                it.remove();
            }
        }
    }

    public static Collection<DamageInfo> getAllDamage() {
        return damageMap.values();
    }
}