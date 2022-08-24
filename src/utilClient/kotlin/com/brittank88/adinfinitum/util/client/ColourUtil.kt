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
import net.minecraft.util.math.Matrix4f
import java.io.ByteArrayInputStream
import java.io.IOException
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

    val framebufferSize = (scaleFactor * 16).toInt()
    val framebuffer = SimpleFramebuffer(framebufferSize, framebufferSize, false, MinecraftClient.IS_SYSTEM_MAC)

    framebuffer.beginWrite(false)

    // framebuffer.setClearColor(1f, 1f, 1f, 1f)
    // framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC)

    run {
        val originalProjectionMatrix = RenderSystem.getProjectionMatrix().copy()
        val projectionMatrix         = Matrix4f.projectionMatrix(16f, -16f, 1000f, 3000f)

        RenderSystem.setProjectionMatrix(projectionMatrix)
        run {

            val modelViewMatrix = Matrix4f.translate(8.0f, 8.0f, -2000.0f)

            val modelViewStack = RenderSystem.getModelViewStack().apply {
                push()
                loadIdentity()
                multiplyPositionMatrix(modelViewMatrix)
                scale(16f, -16f, 1.0f)
            }

            RenderSystem.applyModelViewMatrix()

            client.bufferBuilders.entityVertexConsumers.run {
                client.itemRenderer.renderItem(this@retrieveColourData, ModelTransformation.Mode.GUI, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, MatrixStack(), this, 0)
                draw()
            }

            modelViewStack.pop()

            RenderSystem.applyModelViewMatrix()

        }
        RenderSystem.setProjectionMatrix(originalProjectionMatrix)
    }

    client.framebuffer.beginWrite(false)

    framebuffer.run {
        draw(framebufferSize, framebufferSize)
        delete()
    }
}
