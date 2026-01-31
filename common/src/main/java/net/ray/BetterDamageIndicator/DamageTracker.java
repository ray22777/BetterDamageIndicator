package net.ray.BetterDamageIndicator;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.ray.BetterDamageIndicator.config.ConfigGetter;

import java.util.Iterator;
import java.util.Objects;

public class DamageTracker {
    public enum DAMAGE_SOURCE {
        SELF,
        PLAYER,
        ALL
    }

    private static final Int2ObjectMap<EntityData> ENTITY_DATA = new Int2ObjectOpenHashMap<>();
    private static final Minecraft CLIENT = Minecraft.getInstance();

    private static class EntityData {
        float lastHealth;
        float lastMaxHealth;
        float damageToShow;
        float healingToShow;
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
                    switch(ConfigGetter.iconfig.damageSource){
                        case ALL:
                            DamageRenderer.renderDamageIndicator(entity, rawDamage);
                            break;
                        case PLAYER:
                            if(Objects.requireNonNull(entity.getLastDamageSource()).getEntity() instanceof Player){
                                DamageRenderer.renderDamageIndicator(entity, rawDamage);
                            }
                            break;
                        case SELF:
                            if(Objects.requireNonNull(entity.getLastDamageSource()).getEntity() instanceof LocalPlayer){
                                DamageRenderer.renderDamageIndicator(entity, rawDamage);
                            }
                            break;
                    }
                }
                showTicks = 40;
            }
            else if (currentHealth > lastHealth) {
                float rawHealing = currentHealth - lastHealth;

                if (rawHealing > 0) {
                    healingToShow = rawHealing;
                    DamageRenderer.renderHealingIndicator(entity, rawHealing);
                }
                showTicks = 40;
            }
            lastHealth = currentHealth;

            wasAlive = isAlive;

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


    private static int getCurrentTick() {
        return CLIENT.player != null ? CLIENT.player.tickCount : 0;
    }
}