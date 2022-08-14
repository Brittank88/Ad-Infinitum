    package com.brittank88.adinfinitum.mixin;

    import com.brittank88.adinfinitum.api.item.IHaloRenderItem;
    import net.minecraft.client.render.RenderLayer;
    import net.minecraft.client.render.VertexConsumer;
    import net.minecraft.client.render.VertexConsumerProvider;
    import net.minecraft.client.render.item.ItemRenderer;
    import net.minecraft.client.render.model.BakedModel;
    import net.minecraft.client.render.model.json.ModelTransformation;
    import net.minecraft.client.util.SpriteIdentifier;
    import net.minecraft.client.util.math.MatrixStack;
    import net.minecraft.item.ItemStack;
    import net.minecraft.util.math.ColorHelper;
    import net.minecraft.util.math.MathHelper;
    import org.apache.commons.lang3.Range;
    import org.spongepowered.asm.mixin.Mixin;
    import org.spongepowered.asm.mixin.injection.At;
    import org.spongepowered.asm.mixin.injection.Inject;
    import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

    @Mixin(ItemRenderer.class)
    public class ItemRendererMixin {

        @Inject(
                method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/client/util/math/MatrixStack;push()V",
                        shift = At.Shift.BEFORE
                )
        ) private void onRender_Halo_AdInfinitum(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
            if (
                    stack.getItem() instanceof IHaloRenderItem hri
                            && hri.shouldRenderHalo(stack)
                            && renderMode == ModelTransformation.Mode.GUI
            ) {
                drawHalo(hri.getHaloSprite(stack), hri.getHaloSize(stack), hri.getHaloColour(stack), vertexConsumers, light, overlay, -1.1F);
                drawHalo(hri.getHaloNoiseSprite(stack), hri.getHaloSize(stack), hri.getHaloColour(stack), vertexConsumers, light, overlay, -1);
            }
        }

        private void drawHalo(SpriteIdentifier sID, float size, int colour, VertexConsumerProvider vertexConsumers, int light, int overlay, float zOffset) {

            float spread = Range.between(0F, 16F).fit(size) / 16;
            float min    = -spread - 0.5F;
            float max    =  spread + 0.5F;

            float r = ColorHelper.Argb.getRed(colour);
            float g = ColorHelper.Argb.getGreen(colour);
            float b = ColorHelper.Argb.getBlue(colour);
            float a = ColorHelper.Argb.getAlpha(colour);

            VertexConsumer vertexConsumer = sID.getVertexConsumer(vertexConsumers, RenderLayer::getItemEntityTranslucentCull);
            vertexConsumer.vertex(max, max, zOffset, r, g, b, a, 1, 0, overlay, light, 0, 0, 1);
            vertexConsumer.vertex(min, max, zOffset, r, g, b, a, 0, 0, overlay, light, 0, 0, 1);
            vertexConsumer.vertex(min, min, zOffset, r, g, b, a, 0, 1, overlay, light, 0, 0, 1);
            vertexConsumer.vertex(max, min, zOffset, r, g, b, a, 1, 1, overlay, light, 0, 0, 1);
        }

        @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V",
                    shift = At.Shift.AFTER
            )
        ) private void onRender_Pulse_AdInfinitum(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
            if (
                    stack.getItem() instanceof IHaloRenderItem hri
                            && hri.shouldPulse(stack)
                            && (
                                    renderMode == ModelTransformation.Mode.GUI
                                            || renderMode == ModelTransformation.Mode.GROUND
                                            || renderMode == ModelTransformation.Mode.FIXED
                            )
            ) {
                float scale = Range.between(0.8F, 1.2F).fit(hri.getPulseScale(stack));
                double trans = (1 - scale) / 2;

                matrices.translate(trans, trans, trans + MathHelper.EPSILON);
                matrices.scale(scale, scale, scale);
            }
        }
    }
