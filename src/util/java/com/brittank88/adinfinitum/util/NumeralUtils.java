package com.brittank88.adinfinitum.util;

import org.jetbrains.annotations.Contract;

public abstract class NumeralUtils {

    /**
     * Returns the roman numeral representation of a given number, either in lowercase or uppercase.
     * <br /><br />
     * <i>Credits to <a href="https://stackoverflow.com/a/12646864">André Kramer Orten</a> for the original algorithm,
     * which considers this as a unary problem rather than a number problem.</i>
     *
     * @param number The number to convert to a roman numerals.
     * @param capitalised Whether to return the roman numeral in capitalised form.
     * @return The roman numerals representation of the number.
     */
    @Contract(pure = true)
    @SuppressWarnings("SpellCheckingInspection")
    public static String toRomanNumeral(int number, boolean capitalised) {
        String romanNumeralsUpper = "I".repeat(number)
                .replace("IIIII", "V" )
                .replace("IIII" , "IV")
                .replace("VV"   , "X" )
                .replace("VIV"  , "IX")
                .replace("XXXXX", "L" )
                .replace("XXXX" , "XL")
                .replace("LL"   , "C" )
                .replace("LXL"  , "XC")
                .replace("CCCCC", "D" )
                .replace("CCCC" , "CD")
                .replace("DD"   , "M" )
                .replace("DCD"  , "CM");
        return capitalised ? romanNumeralsUpper : romanNumeralsUpper.toLowerCase();
    }
}
