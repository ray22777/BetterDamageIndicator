//// SimpleDamageRenderer.java
//package net.ray;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Font;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.LivingEntity;
//import java.util.*;
//
//public class DamageRenderer {
//    private static final Minecraft MC = Minecraft.getInstance();
//    private static final Map<Integer, DamageInfo> damageMap = new HashMap<>();
//
//    static class DamageInfo {
//        String text;
//        int ticksLeft = 40;
//        double x, y, z;
//
//        DamageInfo(float damage, LivingEntity entity) {
//            this.text = String.format("%.1f", damage);
//            this.x = entity.getX();
//            this.y = entity.getY() + entity.getBbHeight() + 0.5;
//            this.z = entity.getZ();
//        }
//
//        void tick() { ticksLeft--; }
//        boolean shouldRemove() { return ticksLeft <= 0; }
//    }
//
//    public static void addDamage(LivingEntity entity, float damage) {
//        damageMap.put(entity.getId(), new DamageInfo(damage, entity));
//        System.out.println("Damage: " + damage + " to " + entity.getName().getString());
//    }
//
//    public static void tick() {
//        Iterator<Map.Entry<Integer, DamageInfo>> it = damageMap.entrySet().iterator();
//        while (it.hasNext()) {
//            DamageInfo info = it.next().getValue();
//            info.tick();
//            if (info.shouldRemove()) it.remove();
//        }
//    }
//
//    public static void renderAll(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
//                                 float partialTick) {
//        if (MC.player == null || MC.level == null) return;
//
//        for (DamageInfo info : damageMap.values()) {
//            renderDamage(poseStack, bufferSource, info, partialTick);
//        }
//    }
//
//    private static void renderDamage(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
//                                     DamageInfo info, float partialTick) {
//        if (MC.getEntityRenderDispatcher() == null || MC.getEntityRenderDispatcher().camera == null) return;
//
//        poseStack.pushPose();
//
//        // Move to position
//        double camX = MC.getEntityRenderDispatcher().camera.getPosition().x;
//        double camY = MC.getEntityRenderDispatcher().camera.getPosition().y;
//        double camZ = MC.getEntityRenderDispatcher().camera.getPosition().z;
//
//        poseStack.translate(info.x - camX, info.y - camY, info.z - camZ);
//
//        // Face camera
//        poseStack.mulPose(MC.getEntityRenderDispatcher().camera.rotation());
//
//        // Scale
//        float scale = 0.025f;
//        poseStack.scale(-scale, -scale, scale);
//
//        // Draw text
//        Font font = MC.font;
//        int textWidth = font.width(info.text);
//        float x = -textWidth / 2f;
//
//        // Color (white)
//        int color = 0xFFFFFF;
//
//        font.drawInBatch(
//                Component.literal(info.text),
//                x, 0,
//                color,
//                false,
//                poseStack.last().pose(),
//                bufferSource,
//                Font.DisplayMode.NORMAL,
//                0,
//                15728880
//        );
//
//        poseStack.popPose();
//    }
//}