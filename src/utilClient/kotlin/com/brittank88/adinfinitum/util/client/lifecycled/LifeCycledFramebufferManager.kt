package com.brittank88.adinfinitum.util.client.lifecycled

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.gl.Framebuffer
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.resource.SynchronousResourceReloader
import net.minecraft.util.Identifier

/**
 * A framebuffer that is aware of resource reloads.
 *
 * @param resourceIdentifier  The resource identifier used for the resource reload listener.
 * @param framebufferSupplier A supplier for the framebuffer.
 *
 * @author Brittank88
 */
class LifeCycledFramebufferManager <T: Framebuffer>(
    private val resourceIdentifier  : Identifier,
    private val framebufferSupplier : () -> T
) : SynchronousResourceReloader, IdentifiableResourceReloadListener {

    init {
        // Clear the framebuffer when the game exits.
        ClientLifecycleEvents.CLIENT_STOPPING.register { if (::framebuffer.isInitialized) framebuffer.delete() }

        // Register this as a resource reload listener.
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(this)
    }

    lateinit var framebuffer: T
        private set

    override fun reload(resourceManager: ResourceManager) {

        // Delete the old framebuffer.
        if (::framebuffer.isInitialized) framebuffer.delete()

        // Recreate the framebuffer.
        framebuffer = framebufferSupplier()
    }

    override fun getFabricId() = resourceIdentifier
}