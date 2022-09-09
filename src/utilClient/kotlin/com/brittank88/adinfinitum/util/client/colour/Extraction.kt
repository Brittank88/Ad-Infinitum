package com.brittank88.adinfinitum.util.client.colour

import com.brittank88.adinfinitum.AdInfinitum
import com.mojang.blaze3d.systems.RenderSystem
import it.unimi.dsi.fastutil.doubles.Double2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntFunction
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.Framebuffer
import net.minecraft.client.gl.SimpleFramebuffer
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.ScreenshotRecorder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.profiler.Profiler
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.Configurator
import org.slf4j.LoggerFactory
import smile.clustering.KMeans
import smile.clustering.KModes
import smile.clustering.kmodes
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.logging.Logger

class ColourExtractionException(itemStack: ItemStack?, exception: Exception? = null)
    : RuntimeException("Error extracting dominant RGB for item stack" + (itemStack?.let { ": $it" } ?: "."), exception)

object ItemStackDominantColourExtractor {

    /** Disable logger within KModes. */
    init { Configurator.setLevel(LogManager.getLogger(KModes::class.java).name, Level.OFF) }

    val CLIENT: MinecraftClient = MinecraftClient.getInstance()
    val EXTRACTED_DOMINANT_COLOURS = Object2IntOpenHashMap<ItemStack>()

    fun ItemConvertible.extractDominantColour() = this.asItem().defaultStack.extractDominantColour()
    fun ItemStack.extractDominantColour() = EXTRACTED_DOMINANT_COLOURS.computeIfAbsent(this, Object2IntFunction {

        // Render the item to the buffer.
        ResourceReloadAwareFramebuffer.renderItemStackToBuffer(this)

        val kModes = ResourceReloadAwareFramebuffer.getFramebuffer().toNativeImage().applyKModes(3)

        // AdInfinitum.LOGGER.info(this.toString())
        // kModes.centroids.forEach { c -> AdInfinitum.LOGGER.info("Centroid: ${c.map(::Colour).joinToString()}") }

        // TODO: Extract dominant colour from clusters.
        return@Object2IntFunction 0
    })

    private fun Framebuffer.toNativeImage() = ScreenshotRecorder.takeScreenshot(this)

    object ResourceReloadAwareFramebuffer : SimpleResourceReloadListener<Object2IntOpenHashMap<ItemStack>> {

        init {
            // Register this as a resource reload listener.
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this)
        }

        // TODO: Framebuffer should be created with new correct sizing on screen size change.
        // FIXME: Framebuffer causes GUI elements to be invisible.
        private val FRAMEBUFFER_SIZE_CACHE = Double2IntOpenHashMap()
        private fun getFramebufferSize() = FRAMEBUFFER_SIZE_CACHE.computeIfAbsent(CLIENT.window.scaleFactor) { (CLIENT.window.scaleFactor * 16).toInt() }

        var FRAMEBUFFER: SimpleFramebuffer? = null

        fun getFramebuffer(recreate: Boolean = false): SimpleFramebuffer {
            if (FRAMEBUFFER == null || recreate) recreateFramebuffer()
            return FRAMEBUFFER!!
        }

        /** Recreates the framebuffer with a default configuration. */
        private fun recreateFramebuffer() {
            FRAMEBUFFER?.delete()   // Delete the old framebuffer, in case of a forced recreation.
            FRAMEBUFFER = getFramebufferSize().run {
                SimpleFramebuffer(this, this, false, MinecraftClient.IS_SYSTEM_MAC).apply {
                    setClearColor(0f, 0f, 0f, 0f)   // Black clear colour as it is the least dominant colour possible.
                }
            }
        }

        fun renderItemToBuffer(item: ItemConvertible) = this.renderItemStackToBuffer(item.asItem().defaultStack)
        fun renderItemStackToBuffer(itemStack: ItemStack) {

            // Clear the framebuffer (in case any data is left over from a previous extraction).
            // Must be cleared prior to beginWrite, otherwise the rendered item is cleared too!
            getFramebuffer().clear(MinecraftClient.IS_SYSTEM_MAC)

            // Begin writing to our framebuffer.
            getFramebuffer().beginWrite(false)

            // Get a copy of the original projection matrix - we will reset to it after rendering to our framebuffer.
            RenderSystem.getProjectionMatrix().copy().let { originalProjectionMatrix ->

                // Reset the projection matrix to a default orthographic projection.
                RenderSystem.setProjectionMatrix(Matrix4f.projectionMatrix(16f, -16f, 1000f, 3000f))

                // Get the existing model view matrix to use as a base for our model view matrix.
                RenderSystem.getModelViewStack().run {
                    push()                                                                      // Push the existing model view stack.
                    loadIdentity()                                                              // Reset the model view stack to identity matrices.
                    multiplyPositionMatrix(Matrix4f.translate(8.0f, 8.0f, -2000.0f))    // Multiply the position matrix by our model view matrix.
                    scale(16f, -16f, 1.0f)                                              // Scale the model view matrix by 16x16, flipping about the Y axis.
                    RenderSystem.applyModelViewMatrix()                                         // Apply the model view matrix via the render system.

                    // Get a reference to the entity vertex consumer (in reality this is just the base vertex consumer) for rendering the item.
                    CLIENT.bufferBuilders.entityVertexConsumers.run {

                        // Instruct the game to render the item into our framebuffer, as if it was being rendered in a GUI.
                        CLIENT.itemRenderer.renderItem(
                            itemStack,
                            ModelTransformation.Mode.GUI,
                            LightmapTextureManager.MAX_LIGHT_COORDINATE,
                            OverlayTexture.DEFAULT_UV,
                            MatrixStack(),
                            this,
                            0
                        )
                        draw()
                    }

                    // Pop the model view stack.
                    pop()
                }

                // Apply the model view matrix via the render system.
                RenderSystem.applyModelViewMatrix()

                // Reset the projection matrix to the original projection matrix.
                RenderSystem.setProjectionMatrix(originalProjectionMatrix)
            }

            // End writing to our framebuffer, by switching back to the game's framebuffer.
            CLIENT.framebuffer.beginWrite(false)
        }

        override fun getFabricId(): Identifier = AdInfinitum.id("itemstack_extracted_colour_resource")

        override fun load(manager: ResourceManager?, profiler: Profiler?, executor: Executor?): CompletableFuture<Object2IntOpenHashMap<ItemStack>> = CompletableFuture.supplyAsync({
            // TODO: This should load from a file.
            EXTRACTED_DOMINANT_COLOURS
        }, executor)

        override fun apply(data: Object2IntOpenHashMap<ItemStack>?, manager: ResourceManager?, profiler: Profiler?, executor: Executor?): CompletableFuture<Void> = CompletableFuture.runAsync({
            // Clear the dominant colours cache, so that the extracted colours will be recalculated.
            EXTRACTED_DOMINANT_COLOURS.clear()

            // Refill cache with loaded data.
            // TODO: This is terrible you're dumb you're literally refilling the cache that you just cleared with exactly the same data :)))).
            data?.let(EXTRACTED_DOMINANT_COLOURS::putAll)
        }, executor)
    }
}
