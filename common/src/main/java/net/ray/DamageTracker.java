// DamageTracker.java
package net.ray;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

import java.util.Iterator;

public class DamageTracker {
    private static final Int2ObjectMap<EntityData> ENTITY_DATA = new Int2ObjectOpenHashMap<>();
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static int cleanupCounter = 0;

    private static class EntityData {
        float lastHealth;
        float damageToShow;
        int showTicks;
        int lastSeenTick;

        EntityData(float initialHealth) {
            this.lastHealth = initialHealth;
            this.lastSeenTick = getCurrentTick();
        }

        boolean update(LivingEntity entity) {
            lastSeenTick = getCurrentTick();

            float currentHealth = Math.min(entity.getHealth(), entity.getMaxHealth());

            // Check for significant health change (>0.5 to avoid floating point noise)
            if (Math.abs(currentHealth - lastHealth) > 0.5f) {
                float damage = lastHealth - currentHealth;

                if (damage > 0) {
                    damageToShow = damage;
                    showTicks = 40; // Show for 2 seconds (40 ticks)

                    // Debug output
                    System.out.printf("[DAMAGE] %s: -%.1f HP (%.1f -> %.1f)%n",
                            entity.getDisplayName().getString(),
                            damage,
                            lastHealth,
                            currentHealth
                    );
                }

                lastHealth = currentHealth;
            }

            // Count down display timer
            if (showTicks > 0) {
                showTicks--;
            }

            // Keep alive if recently seen or showing damage
            return (getCurrentTick() - lastSeenTick < 100) || showTicks > 0;
        }
    }

    public static void updateEntity(LivingEntity entity) {
        if (!isValidEntity(entity)) return;

        int entityId = entity.getId();
        EntityData data = ENTITY_DATA.get(entityId);

        if (data == null) {
            data = new EntityData(entity.getHealth());
            ENTITY_DATA.put(entityId, data);
        }

        if (!data.update(entity)) {
            ENTITY_DATA.remove(entityId);
        }
    }

    private static boolean isValidEntity(LivingEntity entity) {
        if (!entity.isAlive() || !entity.level().isClientSide()) return false;
        if (CLIENT.player == null) return false;

        // Only track entities within 64 blocks
        return entity.distanceTo(CLIENT.player) <= 64;
    }

    public static void tick() {
        cleanupCounter++;

        // Cleanup every 5 seconds (100 ticks)
        if (cleanupCounter >= 100) {
            cleanupCounter = 0;
            cleanupStaleEntries();
        }
    }

    private static void cleanupStaleEntries() {
        int currentTick = getCurrentTick();
        Iterator<Int2ObjectMap.Entry<EntityData>> iterator = ENTITY_DATA.int2ObjectEntrySet().iterator();

        while (iterator.hasNext()) {
            Int2ObjectMap.Entry<EntityData> entry = iterator.next();
            EntityData data = entry.getValue();

            // Remove if not seen for 5 seconds and not showing damage
            if ((currentTick - data.lastSeenTick > 100) && data.showTicks <= 0) {
                iterator.remove();
            }
        }
    }

    public static float getDamageToShow(LivingEntity entity) {
        EntityData data = ENTITY_DATA.get(entity.getId());
        return data != null && data.showTicks > 0 ? data.damageToShow : 0;
    }

    public static int getShowTicks(LivingEntity entity) {
        EntityData data = ENTITY_DATA.get(entity.getId());
        return data != null ? data.showTicks : 0;
    }

    private static int getCurrentTick() {
        return CLIENT.player != null ? CLIENT.player.tickCount : 0;
    }
}