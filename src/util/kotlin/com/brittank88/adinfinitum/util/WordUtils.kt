package com.brittank88.adinfinitum.util

/**
 * Capitalises each word in a [String].
 *
 * @return The fully capitalised [String].
 */
fun String.capitaliseFully() = split(' ').joinToString(" ") { it.replaceFirstChar(Char::titlecase) }
