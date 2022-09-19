package com.brittank88.adinfinitum.util.client.colour

/**
 * A BGRA colour represented as a 32-bit integer.
 *
 * @author Brittank88
 */
@JvmInline value class Colour(private val _integer: Int) {

    val integer get() = _integer

    val blue  get() = (_integer shr 24) and 0xFF
    val green get() = (_integer shr 16) and 0xFF
    val red   get() = (_integer shr 8 ) and 0xFF
    val alpha get() =  _integer         and 0xFF

    val integerABGR get() = _integer.rotateRight(Byte.SIZE_BITS)
    val integerARGB get() = Integer.reverseBytes(_integer)
    val integerRGBA get() = integerARGB.rotateRight(Byte.SIZE_BITS)

    val minRGB  get() = minOf(red, green, blue)
    val maxRGB  get() = maxOf(red, green, blue)

    fun hsl() = HSL(this)
    fun hsb() = HSB(this)
}

/**
 * HSL colour internally represented as a 32-bit BGR integer.
 * @author Brittank88
 * @see <a href="https://www.niwa.nu/2013/05/math-behind-colorspace-conversions-rgb-hsl/">Math behind colorspace conversions, RGB-HSL</a>
 */
@JvmInline value class HSL(private val _colour: Colour) {

    val colour  get() = _colour
    val integer get() = _colour.integer

    /** Range: `[0, 360]` */
    val hue get() = 60 * when (_colour.maxRGB) {
        _colour.red   -> ((_colour.green - _colour.blue ) * 255f) / (_colour.red   - _colour.minRGB)
        _colour.green -> ((_colour.blue  - _colour.red  ) * 255f) / (_colour.green - _colour.minRGB) + 2f
        else          -> ((_colour.red   - _colour.green) * 255f) / (_colour.blue  - _colour.minRGB) + 4f
    }.let { if (it < 0) it + 360 else it }

    /** Range: `[0, 1]` */
    val saturation get() = run {

        val minRGB = _colour.minRGB / 255f
        val maxRGB = _colour.maxRGB / 255f

        when {
            minRGB == maxRGB                  -> 0f
            luminance(minRGB, maxRGB) <= 0.5f -> (maxRGB - minRGB) / (maxRGB + minRGB)
            else                              -> (maxRGB - minRGB) / (2 - maxRGB - minRGB)
        }
    }

    /** Range: `[0, 1]` */
    val luminance get() = luminance(_colour.maxRGB / 255f, _colour.minRGB / 255f)

    companion object {
        private fun luminance(max: Float, min: Float) = (max + min) * 0.5f
    }
}

/**
 * HSB colour internally represented as a 32-bit BGR integer.
 * @author Brittank88
 * @see <a href="https://stackoverflow.com/a/14733008">Leszek Szary's Branch-less RGB to HSV Algorithm</a>
 */
@JvmInline value class HSB(private val _colour: Colour) {

    val colour  get() = _colour
    val integer get() = _colour.integer

    /** Range: `[0, 255]` */
    val hue get() = when (_colour.maxRGB) {
        _colour.red   -> 43  * (_colour.green - _colour.blue ) / (_colour.red   - _colour.minRGB)
        _colour.green -> 128 * (_colour.blue  - _colour.red  ) / (_colour.green - _colour.minRGB)
        else          -> 214 * (_colour.red   - _colour.green) / (_colour.blue  - _colour.minRGB)
    }

    /** Range: `[0, 255]` */
    val saturation get() = 255 * (1 - _colour.minRGB / _colour.maxRGB)

    /** Range: `[0, 255]` */
    val brightness get() = _colour.maxRGB
}

/**
 * Compares colours by visual dominance (Coloured -> Near White -> Near Black).
 * @author Brittank88
 */
class DominanceComparator(private val saturationThreshold: Float = 25.5f): Comparator<HSB> {

    override fun compare(a: HSB, b: HSB) =
        if (a.saturation < saturationThreshold && b.saturation < saturationThreshold) a.brightness - b.brightness
        else a.saturation * a.brightness - b.saturation * b.brightness
}
