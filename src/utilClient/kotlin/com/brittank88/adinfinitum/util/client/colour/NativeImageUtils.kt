package com.brittank88.adinfinitum.util.client.colour

import net.minecraft.client.texture.NativeImage
import smile.clustering.kmodes

data class Pixel(val x: Int, val y: Int, var colour: Colour) {
    override fun toString() = "($x, $y) -> $colour"
}

class NativeImagePixelIterator(private val image: NativeImage): Iterator<Pixel> {

    private val maxX = image.width
    private val maxY = image.height

    private var x = 0
    private var y = 0

    override fun hasNext() = x < maxX || y < maxY
    override fun next()    = Pixel(x, y, Colour(image.getColor(x, y))).also {
        x = (x + 1) % maxX
        if (x == 0) y++
    }
}

class NativeImagePixelRowIterator(private val image: NativeImage): Iterator<List<Pixel>> {

    private var y = 0

    override fun hasNext() = y < image.height
    override fun next()    = (0 until image.width).map { x -> Pixel(x, y, Colour(image.getColor(x, y))) }.also { y++ }
}

fun NativeImage.pixelIterator()    = NativeImagePixelIterator(this)
fun NativeImage.pixelRowIterator() = NativeImagePixelRowIterator(this)

fun NativeImage.applyKModes(k: Int, maxIter: Int = 100, runs: Int = 10) = kmodes(
    this.pixelRowIterator().asSequence().toList().map { row -> row.map { it.colour.colour }.toIntArray() }.toTypedArray(),
    k, maxIter, runs)

fun NativeImage.quantise() = KMeansClusteriser(this).produceClusters().toSortedSet(Cluster.Companion.CountComparator)
