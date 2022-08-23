package com.brittank88.adinfinitum.util.client

import de.androidpit.colorthief.ColorThief
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
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
