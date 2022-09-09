package com.brittank88.adinfinitum.util.client.colour

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Range

@JvmInline value class Colour(val colour: Int) : Comparable<Colour> {

    override fun toString() = "${javaClass.simpleName}(colour=0x${"%08X".format(colour)})"  // TODO: Cleanup, potentially move hex formatting to int extension function.
    fun toARGBString() = "${javaClass.simpleName}(alpha=$alpha, red=$red, green=$green, blue=$blue))"

    val alpha : Int get() = (colour shr 24) and 0xFF
    val red   : Int get() = (colour shr 16) and 0xFF
    val green : Int get() = (colour shr 8 ) and 0xFF
    val blue  : Int get() = colour          and 0xFF

    constructor(colour: @Range(from = 0, to = Long.MAX_VALUE) Long): this(colour.toInt())
    constructor(
        red   : @Range(from = 0, to = 255) Int,
        green : @Range(from = 0, to = 255) Int,
        blue  : @Range(from = 0, to = 255) Int
    ): this(red, green, blue, 255)
    constructor(
        red   : @Range(from = 0, to = 255) Int,
        green : @Range(from = 0, to = 255) Int,
        blue  : @Range(from = 0, to = 255) Int,
        alpha : @Range(from = 0, to = 255) Int
    ): this(alpha shl 24 or (red shl 16) or (green shl 8) or blue)

    fun blend(other: Colour, pivot: Int) = blend(this, other, pivot)

    fun avg(others: List<Colour>) = Colour.avg(listOf(this) + others)

    /**
     * Volume in colour space.
     *
     * @see <a href="https://github.com/SvenWoltmann/color-thief-java/blob/cf21511eb10b060122fa70b1cc8810145ac0434b/src/main/java/de/androidpit/colorthief/MMCQ.java#L87">Color Thief's VBox Volume</a>
     */
    fun volume(other: Colour) = ((red - other.red + 1) * (green - other.green + 1) * (blue - other.blue + 1))

    operator fun plus (other: Colour) = Colour((red + other.red).coerceAtMost(255), (green + other.green).coerceAtMost(255), (blue + other.blue).coerceAtMost(255))
    operator fun minus(other: Colour) = Colour((red - other.red).coerceAtLeast(0), (green - other.green).coerceAtLeast(0), (blue - other.blue).coerceAtLeast(0))
    operator fun times(other: Colour) = Colour((red * other.red).coerceAtMost(255), (green * other.green).coerceAtMost(255), (blue * other.blue).coerceAtMost(255))
    operator fun div  (other: Colour) = Colour(red / other.red.coerceAtLeast(1), green / other.green.coerceAtLeast(1), blue / other.blue.coerceAtLeast(1))

    override operator fun compareTo(other: Colour) = colour.compareTo(other.colour)

    operator fun plus (other: Int) = Colour(red + other, green + other, blue + other)
    operator fun minus(other: Int) = Colour(red - other, green - other, blue - other)
    operator fun times(other: Int) = Colour(red * other, green * other, blue * other)
    operator fun div  (other: @Range(from = 1, to = Long.MAX_VALUE) Int) = Colour(red / other, green / other, blue / other)

    fun toDoubleArray() = toDoubleArray(this)

    companion object {

        val BLACK = Colour(0x000000FF)
        val WHITE = Colour(0xFFFFFFFF)
        val RED   = Colour(0xFF0000FF)
        val GREEN = Colour(0x00FF00FF)
        val BLUE  = Colour(0x0000FFFF)

        fun blend(
            colour1: Colour,
            colour2: Colour,
            pivot: @Range(from = 0, to = 255) Int
        ) = (pivot and 0xFF).let { Colour(
            (colour1.red * (255 - it) + colour2.red * it) / 255,
            (colour1.green * (255 - it) + colour2.green * it) / 255,
            (colour1.blue * (255 - it) + colour2.blue * it) / 255
        ) }

        fun avg(colours: List<Colour>): Colour {

            var red   = 0
            var green = 0
            var blue  = 0

            colours.forEach {
                red   += it.red
                green += it.green
                blue  += it.blue
            }

            return Colour(red, green, blue) / (colours.size + 1)
        }

        fun toDoubleArray(colour: Colour) = doubleArrayOf(colour.alpha.toDouble(), colour.red.toDouble(), colour.green.toDouble(), colour.blue.toDouble())
    }
}
