package com.brittank88.adinfinitum.client.item;

import com.brittank88.adinfinitum.api.client.registry.singularity.SingularityItemDataRegistryKt;
import com.brittank88.adinfinitum.api.client.render.item.SingularityItemRenderData;
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem;
import com.brittank88.adinfinitum.client.AdInfinitumClient;
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

        SingularityItemRenderData sid = SingularityItemDataRegistryKt.getSingularities().get((SingularityItem) stack.getItem());

        // Get the singularity's colour.
        int colour = sid.getColourFunction().applyAsInt(stack);
        float r = ColorHelper.Argb.getRed(colour);
        float g = ColorHelper.Argb.getGreen(colour);
        float b = ColorHelper.Argb.getBlue(colour);
        float a = ColorHelper.Argb.getAlpha(colour);

        // Get current tick and delta.
        float tickCount = AdInfinitumClient.getTickCount();
        float tickDelta = MinecraftClient.getInstance().getTickDelta();

        // The position and normal matrix need to be acquired after each rotation operation.
        MatrixStack.Entry entry;
        Matrix4f positionMatrix;
        Matrix3f normalMatrix;

        //#===#// BASE SPRITE //#===#//

        // Push matrices.
        matrices.push();

        // Rotate matrices.
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((sid.getBaseRotationSpeed().apply(stack) * (tickCount + tickDelta)) % 360));

        // Get current normal and position matrix.
        entry          = matrices.peek();
        positionMatrix = entry.getPositionMatrix();
        normalMatrix   = entry.getNormalMatrix();

        // Draw base sprite.
        VertexConsumer baseConsumer = vertexConsumers.getBuffer(RenderLayer.getItemEntityTranslucentCull(sid.getBaseSpriteIdentifier().invoke(stack).getTextureId()));
        baseConsumer.vertex(positionMatrix, 1, 1, 0).color(r, g, b, a).texture(1, 0).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        baseConsumer.vertex(positionMatrix, 0, 1, 0).color(r, g, b, a).texture(0, 0).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        baseConsumer.vertex(positionMatrix, 0, 0, 0).color(r, g, b, a).texture(0, 1).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        baseConsumer.vertex(positionMatrix, 1, 0, 0).color(r, g, b, a).texture(1, 1).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();

        // Pop matrices.
        matrices.pop();

        //#===#// CORE SPRITE //#===#//

        // Push and rotate matrices.
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((sid.getCoreRotationSpeed().apply(stack) * (tickCount + tickDelta)) % 360));

        // Get current normal and position matrix.
        entry          = matrices.peek();
        positionMatrix = entry.getPositionMatrix();
        normalMatrix   = entry.getNormalMatrix();

        // Draw core sprite.
        VertexConsumer coreConsumer = vertexConsumers.getBuffer(RenderLayer.getItemEntityTranslucentCull(sid.getCoreSpriteIdentifier().invoke(stack).getTextureId()));
        coreConsumer.vertex(positionMatrix, 1, 1, 0).color(r, g, b, a).texture(1, 0).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        coreConsumer.vertex(positionMatrix, 0, 1, 0).color(r, g, b, a).texture(0, 0).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        coreConsumer.vertex(positionMatrix, 0, 0, 0).color(r, g, b, a).texture(0, 1).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();
        coreConsumer.vertex(positionMatrix, 1, 0, 0).color(r, g, b, a).texture(1, 1).overlay(overlay).light(light).normal(normalMatrix, 0, 0, 1).next();

        // Pop matrices.
        matrices.pop();
    }
}
