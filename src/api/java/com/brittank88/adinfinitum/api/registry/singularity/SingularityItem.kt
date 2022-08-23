package com.brittank88.adinfinitum.api.registry.singularity

import com.brittank88.adinfinitum.util.asRomanNumerals
import io.wispforest.owo.itemgroup.OwoItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.util.function.Function
import java.util.function.IntFunction

/**
 * Abstract implementation of a singularity item.
 * <br></br><br></br>
 * Allows for the creation of singularity items with custom properties.
 *
 * @author brittank88
 */
class SingularityItem @JvmOverloads constructor(
    val material : ItemStack,
    val tier     : Int = DEFAULT_TIER,
    settings     : (Int) -> OwoItemSettings = { OwoItemSettings() }
) : Item(settings(tier)) {

    /**
     * Returns the tier of the singularity, expressed as capitalised Roman numerals.
     *
     * @return The tier of the singularity, expressed as capitalised Roman numerals.
     * @see Int.asRomanNumerals
     */
    val tierNumeral get() = tier.asRomanNumerals

    companion object {
        /** The [SingularityItem]'s default tier.  */
        const val DEFAULT_TIER = 1

        /** The default maximum tier that any [SingularityItem] can have.  */
        const val DEFAULT_MAXIMUM_TIER = 5
    }
}