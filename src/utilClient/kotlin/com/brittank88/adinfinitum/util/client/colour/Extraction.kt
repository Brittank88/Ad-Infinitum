package com.brittank88.adinfinitum.util.client.colour

import com.brittank88.adinfinitum.AdInfinitum
import com.brittank88.adinfinitum.util.client.lifecycled.LifeCycledBufferManager
import com.brittank88.adinfinitum.util.client.lifecycled.LifeCycledFramebufferManager
import com.mojang.blaze3d.systems.RenderSystem
import it.unimi.dsi.fastutil.Pair
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.objects.Object2IntFunction
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.SimpleFramebuffer
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.resource.SynchronousResourceReloader
import net.minecraft.util.Identifier
import net.minecraft.util.math.Matrix4f
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.config.Configurator
import org.lwjgl.system.MemoryUtil
import smile.clustering.KModes
import smile.clustering.kmodes

/**
 * Container object for all methods and data related to extracting dominant colours from [ItemStack]s.
 * @author Brittank88
 */
object ItemStackDominantColourExtractor : SynchronousResourceReloader, IdentifiableResourceReloadListener {

    /** Disable logger within KModes. */
    init {
        // Disable KModes logger.
        Configurator.setLevel(LogManager.getLogger(KModes::class.java).name, Level.OFF)

        // Register this as a resource reload listener.
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this)
    }

    /** Cache for previously calculated dominant colours. */
    private val dominantColourCache = Object2IntOpenHashMap<ItemStack>()

    /** Cache for previously calculated colour palettes. */
    private val paletteCache = Object2ObjectOpenHashMap<ItemStack, IntList>()

    /** Reference to the [MinecraftClient] singleton instance. */
    private val clientInstance by lazy { MinecraftClient.getInstance() }

    /**
     * Framebuffer that the target [ItemStack] is rendered to.
     * Respects resource reloading and the game quitting.
     */
    private val framebufferManager = LifeCycledFramebufferManager(
        Identifier("ad-infinitum", "item_stack_dominant_colour_extractor/framebuffer_manager")
    ) { (clientInstance.window.scaleFactor * 16).toInt().let {
        SimpleFramebuffer(it, it, false, MinecraftClient.IS_SYSTEM_MAC)
    } }

    /**
     * Pixel buffer for storing pixel data from the [framebufferManager].
     * Respects resource reloading and the game quitting.
     */
    private val pixelBufferManager = LifeCycledBufferManager(
        Identifier("ad-infinitum", "item_stack_dominant_colour_extractor/pixel_buffer_manager")
    ) { MemoryUtil.memAllocInt(framebufferManager.framebuffer.textureWidth * framebufferManager.framebuffer.textureHeight) }

    /**
     * Extracts the dominant colour of the [ItemConvertible]'s default [ItemStack].
     * @return The dominant colour as a BGRA integer.
     *         Note that the alpha channel is always 255 (fully opaque) as a result of compositing.
     */
    fun ItemConvertible.extractDominantColour() = this.asItem().defaultStack.extractDominantColour()

    /**
     * Extracts the dominant colour from the given [ItemStack].
     * @return The dominant colour as a BGRA integer.
     *         Note that the alpha channel is always 255 (fully opaque) as a result of compositing.
     */
    fun ItemStack.extractDominantColour() = dominantColourCache.computeIfAbsent(this, Object2IntFunction {
        return@Object2IntFunction this.extractColourPalette().apply {
            sortedWith { a, b -> DominanceComparator().compare(Colour(a).hsb(), Colour(b).hsb()) }
        }.first()
    })

    /**
     * Extracts a palette of BGRA colours from the [ItemConvertible]'s default [ItemStack].
     * @param count The target number of palette entries.
     * @return An [IntList] of BGRA colours in the palette.
     */
    fun ItemConvertible.extractColourPalette(count: Int = 8) = this.asItem().defaultStack.extractColourPalette(count)

    /**
     * Extracts a palette of BGRA colours from the given [ItemStack].
     * @param count The target number of palette entries.
     * @return An [IntList] of BGRA colours in the palette.
     */
    fun ItemStack.extractColourPalette(count: Int = 8) = paletteCache.compute(this) { _, v ->

        // If the palette has never been calculated before, or has only been calculated for a size less than the target size, calculate it.
        if (v == null || count > v.size) {

            // Use KModes to cluster the colours into centroids.
            kmodes(this.toPixelMatrix(), count).centroids.map { array -> IntList.of().apply {
                array.forEach { if (it and 0xFF != 0) add(it) }    // Cull transparent pixels.

            // The most dominant colour of each centroid is chosen as a palette entry.
            } }.filter(IntList::isNotEmpty).onEach {
                // Order of importance: Coloured -> Near White -> Near Black
                it.sort { a, b -> DominanceComparator().compare(Colour(a).hsb(), Colour(b).hsb()) }
            }.map { it.first() }.toCollection(IntList.of())

        // If the count is less than the target size, return a subset of the palette we already calculated
        } else if (count < v.size) v.subList(0, count)
        // Otherwise, return the existing palette as-is.
        else v
    }!!

    /**
     * Converts the [ItemStack] to a [List]<[IntList]> of BGRA pixels. The remaining pixels are filled with black.
     * @return A [List]<[IntList]> of BGRA pixels.
     */
    fun ItemStack.toPixelMatrix(): Array<IntArray> {
        // Render the item to the buffer.
        renderItemStackToBuffer(this)

        // Load the pixels from the framebuffer into the pixel buffer.
        pixelBufferManager.loadFramebufferPixels(framebufferManager.framebuffer)

        // Convert the pixel buffer contents into a 2D int array for KModes.
        return Array(framebufferManager.framebuffer.textureWidth) {
            IntArray(framebufferManager.framebuffer.textureHeight) { pixelBufferManager.buffer.get() }
        }
    }

    private fun renderItemStackToBuffer(itemStack: ItemStack) {

        // Clear the framebuffer (in case any data is left over from a previous extraction).
        // Must be cleared prior to beginWrite, otherwise the rendered item is cleared too!
        framebufferManager.framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC)

        // Begin writing to our framebuffer.
        framebufferManager.framebuffer.beginWrite(false)

        // Get a copy of the original projection matrix - we will reset to it after rendering to our framebuffer.
        RenderSystem.getProjectionMatrix().copy().let { originalProjectionMatrix ->

            // Reset the projection matrix to a default orthographic projection.
            RenderSystem.setProjectionMatrix(Matrix4f.projectionMatrix(16f, -16f, 1000f, 3000f))

            // Get the existing model view matrix to use as a base for our model view matrix.
            RenderSystem.getModelViewStack().run {

                // Push the existing model view stack.
                push()

                // Reset the model view stack to identity matrices.
                loadIdentity()

                // Multiply the position matrix by our model view matrix.
                multiplyPositionMatrix(Matrix4f.translate(8.0f, 8.0f, -2000.0f))

                // Scale the model view matrix by 16x16, flipping about the Y axis.
                scale(16f, -16f, 1.0f)

                // Apply the model view matrix via the render system.
                RenderSystem.applyModelViewMatrix()

                // Get a reference to the entity vertex consumer (in reality this is just the base vertex consumer) for rendering the item.
                clientInstance.bufferBuilders.entityVertexConsumers.run {

                    // Instruct the game to render the item into our framebuffer, as if it was being rendered in a GUI.
                    clientInstance.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GUI, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, MatrixStack(), this, 0)
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
        clientInstance.framebuffer.beginWrite(true)    // TODO: Check if changing to true fixed rendering.
    }

    override fun reload(resourceManager: ResourceManager) = dominantColourCache.clear()

    override fun getFabricId(): Identifier = AdInfinitum.id("item_stack_dominant_colour_extractor")

    override fun getFabricDependencies() = mutableListOf(framebufferManager.fabricId, pixelBufferManager.fabricId)
}
