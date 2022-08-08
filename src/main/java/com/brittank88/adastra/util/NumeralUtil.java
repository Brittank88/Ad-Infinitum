package com.brittank88.adastra.util;

import java.util.TreeMap;

public abstract class NumeralUtil {

    /**
     * Returns the Roman numeral representation of the given number.
     * <br /><br />
     * Credits to <a href="https://stackoverflow.com/a/19759564">this Stack Overflow post</a>.
     */
    public abstract static class RomanNumeral {
        private final static TreeMap<Integer, String> NUMERAL_MAP = new TreeMap<Integer, String>();

        static {
            NUMERAL_MAP.put(1000, "M");
            NUMERAL_MAP.put(900, "CM");
            NUMERAL_MAP.put(500, "D");
            NUMERAL_MAP.put(400, "CD");
            NUMERAL_MAP.put(100, "C");
            NUMERAL_MAP.put(90, "XC");
            NUMERAL_MAP.put(50, "L");
            NUMERAL_MAP.put(40, "XL");
            NUMERAL_MAP.put(10, "X");
            NUMERAL_MAP.put(9, "IX");
            NUMERAL_MAP.put(5, "V");
            NUMERAL_MAP.put(4, "IV");
            NUMERAL_MAP.put(1, "I");
        }

        public static String toRoman(int number) {
            int l = NUMERAL_MAP.floorKey(number);
            return number == l ? NUMERAL_MAP.get(number) : NUMERAL_MAP.get(l) + toRoman(number-l);
        }
    }
}
