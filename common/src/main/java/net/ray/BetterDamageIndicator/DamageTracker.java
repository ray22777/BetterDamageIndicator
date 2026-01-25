package net.ray.BetterDamageIndicator;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.Iterator;

public class DamageTracker {
    private static final Int2ObjectMap<EntityData> ENTITY_DATA = new Int2ObjectOpenHashMap<>();
    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static int cleanupCounter = 0;

    private static class EntityData {
        float lastHealth;
        float lastMaxHealth;
        float damageToShow;
        int showTicks;
        int lastSeenTick;
        boolean wasAlive;

        EntityData(float initialHealth, float maxHealth) {
            this.lastHealth = initialHealth;
            this.lastMaxHealth = maxHealth;
            this.lastSeenTick = getCurrentTick();
            this.wasAlive = true;
        }

        boolean update(LivingEntity entity) {
            lastSeenTick = getCurrentTick();

            boolean isAlive = entity.isAlive();
            float currentHealth = entity.getHealth();
            float maxHealth = entity.getMaxHealth();

            if (maxHealth > lastMaxHealth) {
                lastHealth = currentHealth;
                lastMaxHealth = maxHealth;
            }

            if (currentHealth < lastHealth) {
                float rawDamage = lastHealth - currentHealth;

                if (rawDamage > 0) {
                    damageToShow = rawDamage;
                    DamageRenderer.renderDamageIndicator(entity, rawDamage);
                }

                lastHealth = currentHealth;
            } else if (currentHealth > lastHealth) {
                lastHealth = currentHealth;
            }

            wasAlive = isAlive;

            // Count down display timer
            if (showTicks > 0) {
                showTicks--;
            }

            return (getCurrentTick() - lastSeenTick < 100) || showTicks > 0;
        }
    }

    public static void updateEntity(LivingEntity entity) {
        if (!isValidEntity(entity)) return;

        int entityId = entity.getId();
        EntityData data = ENTITY_DATA.get(entityId);

        if (data == null) {
            data = new EntityData(entity.getHealth(), entity.getMaxHealth());
            ENTITY_DATA.put(entityId, data);
        }

        if (!data.update(entity)) {
            ENTITY_DATA.remove(entityId);
        }
    }

    private static boolean isValidEntity(LivingEntity entity) {
        if (!entity.level().isClientSide()) return false;
        if (CLIENT.player == null) return false;
        if (entity instanceof ArmorStand) return false;

        return entity.distanceTo(CLIENT.player) <= 64;
    }

    public static void tick() {
        cleanupCounter++;

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