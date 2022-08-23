package com.brittank88.adinfinitum.util

import org.jetbrains.annotations.Range

private val THOUSANDS = arrayOf("", "M", "MM", "MMM")
private val HUNDREDS  = arrayOf("", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM")
private val TENS      = arrayOf("", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC")
private val UNITS     = arrayOf("", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX")

val @Range(from = 0, to = 3999) Int.asRomanNumerals get() = takeIf {
    it in 0 until 4000
}?.let {
    THOUSANDS[it / 1000] + HUNDREDS[it % 1000 / 100] + TENS[it % 100 / 10] + UNITS[it % 10]
}
