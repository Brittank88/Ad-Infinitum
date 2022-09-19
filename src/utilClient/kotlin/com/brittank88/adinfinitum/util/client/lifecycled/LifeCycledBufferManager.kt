package com.brittank88.adinfinitum.util.client.lifecycled

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.gl.Framebuffer
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.resource.SynchronousResourceReloader
import net.minecraft.util.Identifier
import org.jetbrains.annotations.Contract
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil
import java.nio.*
import kotlin.reflect.KClass

/**
 * A buffer that is aware of resource reloads.
 *
 * @param resourceIdentifier The resource identifier used for the resource reload listener.
 * @param bufferSupplier     A supplier for new buffer instances.
 *
 * @author Brittank88
 */
class LifeCycledBufferManager <T: Buffer>(
    private val resourceIdentifier : Identifier,
    private val bufferSupplier     : () -> T
) : SynchronousResourceReloader, IdentifiableResourceReloadListener {

    init {
        // Free the buffer when the client exits to prevent a memory leak.
        ClientLifecycleEvents.CLIENT_STOPPING.register { if (::buffer.isInitialized) MemoryUtil.memFree(buffer) }

        // Register this as a resource reload listener.
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this)
    }

    lateinit var buffer: T
        private set

    override fun reload(resourceManager: ResourceManager) {

        // Free the old intBuffer.
        if (::buffer.isInitialized) MemoryUtil.memFree(buffer)

        // Initialise the intBuffer.
        buffer = bufferSupplier()
    }

    override fun getFabricId() = resourceIdentifier

    companion object {
        private val framebufferImageGetterFunctions = hashMapOf<KClass<out Buffer>, (Int, Int, Int, Int, Buffer) -> Unit>()

        init {
            registerFramebufferImageGetter<ByteBuffer>  () { x, l, f, t, b -> GL11.glGetTexImage(x, l, f, t, b as ByteBuffer) }
            registerFramebufferImageGetter<ShortBuffer> () { x, l, f, t, b -> GL11.glGetTexImage(x, l, f, t, b as ShortBuffer) }
            registerFramebufferImageGetter<IntBuffer>   () { x, l, f, t, b -> GL11.glGetTexImage(x, l, f, t, b as IntBuffer) }
            registerFramebufferImageGetter<FloatBuffer> () { x, l, f, t, b -> GL11.glGetTexImage(x, l, f, t, b as FloatBuffer) }
            registerFramebufferImageGetter<DoubleBuffer>() { x, l, f, t, b -> GL11.glGetTexImage(x, l, f, t, b as DoubleBuffer) }
        }

        private inline fun <reified T: Buffer> registerFramebufferImageGetter(noinline function: (Int, Int, Int, Int, Buffer) -> Unit) {
            framebufferImageGetterFunctions[T::class] = function
        }
    }

    /**
     * Loads the pixel data from a [Framebuffer] into the [buffer].
     *
     * Note that the pixels are loaded in as RGBA integers.
     *
     * @param framebuffer The framebuffer to load the image data from.
     */
    fun loadFramebufferPixels(framebuffer: Framebuffer) {
        framebufferImageGetterFunctions[buffer::class]?.let {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.colorAttachment)
            it(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)
        }
    }
}