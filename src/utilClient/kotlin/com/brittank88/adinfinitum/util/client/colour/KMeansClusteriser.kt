package com.brittank88.adinfinitum.util.client.colour

import com.google.common.collect.ImmutableList
import net.minecraft.client.texture.NativeImage
import java.util.random.RandomGenerator
import kotlin.math.pow

data class Cluster(var currentCentroid: Pixel) {

    private val _pixels = mutableListOf<Pixel>()
    private var _lastCentroid = currentCentroid.copy()

    val pixels get() = ImmutableList.copyOf(_pixels)
    val count get() = _pixels.size

    val minPixelByColour get() = _pixels.minByOrNull { it.colour }
    val maxPixelByColour get() = _pixels.maxByOrNull { it.colour }

    val volume get() = maxPixelByColour?.let { minPixelByColour?.colour?.volume(it.colour) } ?: 0

    fun addPixel(pixel: Pixel) = _pixels.add(pixel)

    val unchanged get() = currentCentroid == _lastCentroid

    /**
     * calculates new color for this centroid
     * new color is average of R, G, B
     * also clear list of pixels of this centroid
     * for new centroid will be calculated new list of pixels
     * updates last centroid
     */
    fun newColour() {
        _lastCentroid.colour    = currentCentroid.colour
        currentCentroid.colour = Colour.avg(_pixels.map(Pixel::colour))
        _pixels.clear()
    }

    companion object {

        /**
         * Compares clusters by population.
         *
         * @see <a href="https://github.com/SvenWoltmann/color-thief-java/blob/cf21511eb10b060122fa70b1cc8810145ac0434b/src/main/java/de/androidpit/colorthief/MMCQ.java#L505">Color Thief's COMPARATOR_COUNT</a>
         */
        object CountComparator : Comparator<Cluster> {
            override fun compare(o1: Cluster, o2: Cluster) = o2.count - o1.count
        }

        /**
         * Compares clusters by the product of pixel occupancy times the size in color space.
         *
         * @see <a href="https://github.com/SvenWoltmann/color-thief-java/blob/cf21511eb10b060122fa70b1cc8810145ac0434b/src/main/java/de/androidpit/colorthief/MMCQ.java#L512">Color Thief's COMPARATOR_PRODUCT</a>
         */
        object ProductComparator : Comparator<Cluster> {
            override fun compare(o1: Cluster, o2: Cluster) = if (o1.count == o2.count) o1.volume.compareTo(o2.volume) else (o1.count.toLong() * o1.volume).compareTo(o2.count.toLong() * o2.volume)
        }
    }
}

class KMeansClusteriser(private val sourceImage: NativeImage, private val clusterCount: Int = 4, private val maxIterations: Int = 10) {

    private val clusters = ArrayList<Cluster>()

    fun produceClusters(): List<Cluster> {

        initCentroids()
        newCentroids()

        return ImmutableList.copyOf(clusters)
    }

    /**
     * Chooses a random pixel from the image:
     * - If the same pixel has already been picked, it will be replaced.
     * - Otherwise, add this pixel as the centroid for current cluster.
     */
    private fun initCentroids() {

        repeat(clusterCount) {

            var count = 0
            var cluster: Cluster

            do {
                val x     = XOR_CHURRO.nextInt(sourceImage.width)
                val y     = XOR_CHURRO.nextInt(sourceImage.height)
                val pixel = Pixel(x, y, Colour(sourceImage.getColor(x, y)))
                cluster   = Cluster(pixel)

                count++
            } while ((cluster in clusters || checkClustersForRepeats(pixel.colour)) && count != 42)

            clusters.add(cluster)
        }
        clustersFilling()
    }

    // TODO: Simplify logic - seems like cluster val doesn't need to be created to begin with.
    private fun clustersFilling() {

        sourceImage.pixelIterator().forEach { pixel ->

            var min = Int.MAX_VALUE
            var cluster = Cluster(clusters[0].currentCentroid)

            clusters.forEach {

                val d = distance(pixel.colour, it)
                if (d < min) {
                    min = d
                    cluster = it
                }
            }

            cluster.addPixel(pixel)
        }
    }

    /**
     * Recalculates the new centroid`s color for each of clusters, and then fills it again:
     * - If (count of iteration > 20 or ALL cur and last centroid equals), cycle stops.
     * - Otherwise, cycle continue and recalculates more suitable colors for centroids.
     */
    private fun newCentroids() {

        repeat(maxIterations) {

            clusters.forEach(Cluster::newColour)
            clustersFilling()

            if (clusters.all(Cluster::unchanged)) return
        }
    }

    /**
     * The function calculates color distance between random pixels:
     * - If distance < 1500 (i.e., big enough) selected colour is accepted.
     * - Otherwise, return `false` and choose new random color.
     */
    private fun checkClustersForRepeats(colour: Colour) = clusters.any { distance(colour, it) < 1500 }

    /**
     * Calculates distance between current pixel and centroid.
     */
    private fun distance(color: Colour, cluster: Cluster) = (
            (color.red - cluster.currentCentroid.colour.red).toDouble().pow(2)
                    + (color.green - cluster.currentCentroid.colour.green).toDouble().pow(2)
                    + (color.blue - cluster.currentCentroid.colour.blue).toDouble().pow(2)
            ).toInt()

    companion object {

        // Inside joke. :)
        private val XOR_CHURRO by lazy { RandomGenerator.of("Xoshiro256PlusPlus") }
    }
}
