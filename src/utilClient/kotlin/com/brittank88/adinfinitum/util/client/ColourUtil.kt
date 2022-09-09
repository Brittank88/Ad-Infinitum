package com.brittank88.adinfinitum.util.client

import com.mojang.blaze3d.systems.RenderSystem
import de.androidpit.colorthief.ColorThief
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.SimpleFramebuffer
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.math.ColorHelper
import java.io.ByteArrayInputStream
import java.io.IOException
import java.lang.Exception
import java.util.function.ToIntFunction
import javax.imageio.ImageIO

const val DEFAULT_COLOUR = -0x1
private val DOMINANT_RGB_CACHE = Object2IntOpenHashMap<Sprite>()

fun SpriteIdentifier.extractProminentRGB() = sprite.extractProminentRGB()

fun Sprite.extractProminentRGB() = DOMINANT_RGB_CACHE.computeIfAbsent(this, ToIntFunction {
    // TODO: Find most dominant of all frames rather than just the first frame.
    try {
        ByteArrayInputStream(it.images.first().bytes).use { input ->
            ColorThief.getColorMap(ImageIO.read(input), 8).vboxes.first().avg(false).let { dominant ->
                ColorHelper.Argb.getArgb(255, dominant[0], dominant[1], dominant[2])
            }
        }
    } catch (e: IOException) { throw ColourExtractionException(it, e) }
})

class ColourExtractionException(sprite: Sprite?, exception: Exception? = null)
    : RuntimeException("Error extracting dominant RGB for sprite" + (sprite?.id?.let { ": $it" } ?: "."), exception)

fun ItemStack.retrieveColourData() {

    val client = MinecraftClient.getInstance()

    val scaleFactor = client.window.scaleFactor

    val framebufferSize = (16 * scaleFactor).toInt()
    val framebuffer = SimpleFramebuffer(client.window.framebufferWidth, client.window.framebufferHeight, false, MinecraftClient.IS_SYSTEM_MAC)

    framebuffer.beginWrite(false)

    // framebuffer.setClearColor(1f, 1f, 1f, 1f)
    // framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC)

    /*
    val tessellator = Tessellator.getInstance()
    val buffer = tessellator.buffer

    buffer.begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR)
    RenderSystem.disableBlend()
    RenderSystem.disableDepthTest()
    RenderSystem.disableCull()
    RenderSystem.setShader(GameRenderer::getPositionColorShader)
    buffer.vertex(0.0, 0.0, 0.0).color(1f, 1f, 0f, 1f).next()
    buffer.vertex(framebufferSize.toDouble(), 0.0, 0.0).color(1f, 1f, 0f, 1f).next()
    buffer.vertex(framebufferSize.toDouble(), framebufferSize.toDouble(), 0.0).color(1f, 1f, 0f, 1f).next()
    tessellator.draw()
    */

    val modelViewStack = RenderSystem.getModelViewStack()

    val vertexConsumer = client.bufferBuilders.entityVertexConsumers

    modelViewStack.push()

    //modelViewStack.translate(8.0, 8.0, 0.0)
    modelViewStack.scale(16f, -16f, 0f)
    RenderSystem.applyModelViewMatrix()

    /*
        public void setScaleFactor(double d) {
            this.scaleFactor = d;
            int i = (int)((double)this.framebufferWidth / d);
            this.scaledWidth = (double)this.framebufferWidth / d > (double)i ? i + 1 : i;
            int j = (int)((double)this.framebufferHeight / d);
            this.scaledHeight = (double)this.framebufferHeight / d > (double)j ? j + 1 : j;
        }
     */

    val matrixStack = MatrixStack()
    matrixStack.push()
    matrixStack.translate(client.window.scaledWidth / 32.0, client.window.scaledHeight / -32.0, 0.0)
    matrixStack.scale(client.window.scaledWidth / 16f, client.window.scaledHeight / 16f, 0f)

    client.itemRenderer.renderItem(this, ModelTransformation.Mode.GUI, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumer, 0)

    vertexConsumer.draw()

    matrixStack.pop()

    modelViewStack.pop()

    RenderSystem.applyModelViewMatrix()

    client.framebuffer.beginWrite(false)

    framebuffer.draw(framebufferSize, framebufferSize)

    framebuffer.delete()
}
