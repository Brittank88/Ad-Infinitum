package com.brittank88.adinfinitum.client.render.item;

import com.brittank88.adinfinitum.client.AdInfinitumClient;
import com.brittank88.adinfinitum.item.SingularityItem;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class SingularityItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    @Override public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if (stack.getItem() instanceof SingularityItem si) {

            // Get the singularity's colour.
            int colour = si.getColor();
            float r = ColorHelper.Argb.getRed(colour);
            float g = ColorHelper.Argb.getGreen(colour);
            float b = ColorHelper.Argb.getBlue(colour);
            float a = ColorHelper.Argb.getAlpha(colour);

            float tickDelta = MinecraftClient.getInstance().getTickDelta();
            this.renderBase(si, mode, matrices, vertexConsumers, light, overlay, r, g, b, a, AdInfinitumClient.TICK_COUNT, tickDelta);
            this.renderCore(si, mode, matrices, vertexConsumers, light, overlay, r, g, b, a, AdInfinitumClient.TICK_COUNT, tickDelta);
        }
    }

    private void renderBase(
            SingularityItem si,
            ModelTransformation.Mode mode,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay,
            float r, float g, float b, float a,
            float ticks, float tickDelta
    ) {

        // Push matrices.
        matrices.push();

        // Rotate matrices.
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((si.getBaseSpriteRotationSpeed() * (ticks + tickDelta)) % 360));

        // Get current normal and position matrix.
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f positionMatrix = entry.getPositionMatrix();
        Matrix3f normalMatrix = entry.getNormalMatrix();

        // Draw base sprite.
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getItemEntityTranslucentCull(si.getBaseSprite().getTextureId()));
        baseConsumer.vertex(positionMatrix, 1, 1, 0).color(r, g, b, a).texture(1, 0).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        baseConsumer.vertex(positionMatrix, 0, 1, 0).color(r, g, b, a).texture(0, 0).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        baseConsumer.vertex(positionMatrix, 0, 0, 0).color(r, g, b, a).texture(0, 1).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        baseConsumer.vertex(positionMatrix, 1, 0, 0).color(r, g, b, a).texture(1, 1).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();

        // Pop matrices.
        matrices.pop();
    }

    private void renderCore(
            SingularityItem si,
            ModelTransformation.Mode mode,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay,
            float r, float g, float b, float a,
            float ticks, float tickDelta
    ) {

        // Push and rotate matrices.
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((si.getCoreSpriteRotationSpeed() * (ticks + tickDelta)) % 360));

        // Get current normal and position matrix.
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f positionMatrix = entry.getPositionMatrix();
        Matrix3f normalMatrix = entry.getNormalMatrix();

        // Draw core sprite.
        VertexConsumer coreConsumer = vertexConsumers.getBuffer(RenderLayer.getItemEntityTranslucentCull(si.getCoreSprite().getTextureId()));
        coreConsumer.vertex(positionMatrix, 1, 1, 0).color(r, g, b, a).texture(1, 0).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        coreConsumer.vertex(positionMatrix, 0, 1, 0).color(r, g, b, a).texture(0, 0).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        coreConsumer.vertex(positionMatrix, 0, 0, 0).color(r, g, b, a).texture(0, 1).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        coreConsumer.vertex(positionMatrix, 1, 0, 0).color(r, g, b, a).texture(1, 1).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();

        // Pop matrices.
        matrices.pop();
    }
}
