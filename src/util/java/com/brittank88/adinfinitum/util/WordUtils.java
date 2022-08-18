/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brittank88.adinfinitum.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Operations on Strings that contain words.
 *
 * <p>
 * This class tries to handle {@code null} input gracefully. An exception will not be thrown for a
 * {@code null} input. Each method documents its behavior in more detail.
 * </p>
 *
 * @since 1.1
 */
public class WordUtils {

    /**
     * Abbreviates the words nicely.
     * <br /><br />
     * This method searches for the first space after the lower limit and abbreviates the {@link String} there.
     * It will also append any {@link String} passed as a parameter to the end of the {@link String}. The upper limit can be specified to forcibly abbreviate a {@link String}.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />WordUtils.abbreviate("Now is the time for all good men", 0   , 40 , null  )); = "Now"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 10  , 40 , null  )); = "Now is the"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 20  , 40 , null  )); = "Now is the time for all"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 0   , 40 , ""    )); = "Now"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 10  , 40 , ""    )); = "Now is the"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 20  , 40 , ""    )); = "Now is the time for all"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 0   , 40 , " ...")); = "Now ..."
     * <br />WordUtils.abbreviate("Now is the time for all good men", 10  , 40 , " ...")); = "Now is the ..."
     * <br />WordUtils.abbreviate("Now is the time for all good men", 20  , 40 , " ...")); = "Now is the time for all ..."
     * <br />WordUtils.abbreviate("Now is the time for all good men", 0   , -1 , ""    )); = "Now"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 10  , -1 , ""    )); = "Now is the"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 20  , -1 , ""    )); = "Now is the time for all"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 50  , -1 , ""    )); = "Now is the time for all good men"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 1000, -1 , ""    )); = "Now is the time for all good men"
     * <br />WordUtils.abbreviate("Now is the time for all good men", 9   , -10, null  )); = IllegalArgumentException
     * <br />WordUtils.abbreviate("Now is the time for all good men", 10  , 5  , null  )); = IllegalArgumentException
     * </font></code>
     * <br /><br />
     *
     * @param str         The {@link String} to be abbreviated. If {@code null} is passed, {@code null} is returned.
     *                    If the empty {@link String} is passed, the empty {@link String} is returned.
     * @param lower       The lower limit; negative value is treated as zero.
     * @param upper       The upper limit; specify -1 if no limit is desired.
     *                    The upper limit cannot be lower than the lower limit.
     * @param appendToEnd {@link String} to be appended to the end of the abbreviated string.
     *                    This is appended ONLY if the string was indeed abbreviated.
     *                    The append operation does not count towards the lower or upper limits.
     *
     * @return The abbreviated {@link String}.
     */
    public static String abbreviate(final String str, int lower, int upper, final String appendToEnd) {
        Validate.isTrue(upper >= -1, "upper value cannot be less than -1");
        Validate.isTrue(upper >= lower || upper == -1, "upper value is less than lower value");
        if (StringUtils.isEmpty(str)) return str;

        // If the lower value is greater than the length of the string, set to the length of the string.
        if (lower > str.length()) lower = str.length();

        // If the upper value is -1 (i.e. no limit) or is greater than the length of the string, set to the length of the string.
        if (upper == -1 || upper > str.length()) upper = str.length();

        final StringBuilder result = new StringBuilder();
        final int index = StringUtils.indexOf(str, " ", lower);
        if (index == -1) {
            result.append(str, 0, upper);
            // Only if abbreviation has occurred do we append the appendToEnd value.
            if (upper != str.length()) result.append(StringUtils.defaultString(appendToEnd));
        } else {
            result.append(str, 0, Math.min(index, upper));
            result.append(StringUtils.defaultString(appendToEnd));
        }

        return result.toString();
    }

    /**
     * Capitalizes all the whitespace separated words in a {@link String}.
     * Only the first character of each word is changed. To convert the rest of each word to lowercase at the same time, use {@link #capitalizeFully(String)}.
     * <br /><br />
     * Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input {@link String} returns {@code null}.
     * Capitalization uses the Unicode title case, normally equivalent to upper case.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />WordUtils.capitalize(null       ) = null
     * <br />WordUtils.capitalize(""         ) = ""
     * <br />WordUtils.capitalize("i am FINE") = "I Am FINE"
     * </code></font>
     * <br /><br />
     *
     * @param str The {@link String} to capitalize, may be {@code null}.
     *
     * @return capitalized {@link String}, {@code null} if {@code null} {@link String} input.
     *
     * @see #uncapitalize(String)
     * @see #capitalizeFully(String)
     */
    public static String capitalize(final String str) {
        return capitalize(str, null);
    }

    /**
     * Capitalizes all the delimiter separated words in a {@link String}.
     * Only the first character of each word is changed. To convert the rest of each word to lowercase at the same time, use {@link #capitalizeFully(String, char[])}.
     * <br /><br />
     * The delimiters represent a set of characters understood to separate words.
     * The first {@link String} character and the first non-delimiter character after a delimiter will be capitalized.
     * <br /><br />
     * A {@code null} input {@link String} returns {@code null}.
     * Capitalization uses the Unicode title case, normally equivalent to upper case.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />WordUtils.capitalize(null       , *           ) = null
     * <br />WordUtils.capitalize(""         , *           ) = ""
     * <br />WordUtils.capitalize(*          , new char[0] ) = *
     * <br />WordUtils.capitalize("i am fine", null        ) = "I Am Fine"
     * <br />WordUtils.capitalize("i aM.fine", {'.'}       ) = "I aM.Fine"
     * <br />WordUtils.capitalize("i am fine", new char[]{}) = "I am fine"
     * </font></code>
     * <br /><br />
     *
     * @param str The {@link String} to capitalize, may be {@code null}.
     * @param delimiters Set of characters to determine capitalization, {@code null} means whitespace.
     *
     * @return Capitalized {@link String}, {@code null} if {@code null} {@link String} input.
     *
     * @see #uncapitalize(String)
     * @see #capitalizeFully(String)
     */
    public static String capitalize(final String str, final char... delimiters) {
        if (StringUtils.isEmpty(str)) return str;
        final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;

        boolean capitalizeNext = true;
        for (int index = 0; index < strLen;) {
            final int codePoint = str.codePointAt(index);

            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = true;
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            } else if (capitalizeNext) {
                final int titleCaseCodePoint = Character.toTitleCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                capitalizeNext = false;
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }
        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * Converts all the whitespace separated words in a {@link String} into capitalized words,
     * that is each word is made up of a title-case character and then a series of lowercase characters.
     * <br /><br />
     * Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input {@link String} returns {@code null}.
     * Capitalization uses the Unicode title case, normally equivalent to upper case.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />WordUtils.capitalizeFully(null)        = null
     * <br />WordUtils.capitalizeFully("")          = ""
     * <br />WordUtils.capitalizeFully("i am FINE") = "I Am Fine"
     * </font></code>
     * <br /><br />
     *
     * @param str The {@link String} to capitalize, may be null.
     *
     * @return Capitalized {@link String}, {@code null} if {@code null} {@link String} input.
     */
    public static String capitalizeFully(final String str) {
        return capitalizeFully(str, null);
    }

    /**
     * Converts all the delimiter separated words in a {@link String} into capitalized words,
     * that is each word is made up of a title-case character and then a series of lowercase characters.
     * <br /><br />
     * The delimiters represent a set of characters understood to separate words.
     * The first {@link String} character and the first non-delimiter character after a delimiter will be capitalized.
     * <br /><br />
     * A {@code null} input String returns {@code null}.
     * Capitalization uses the Unicode title case, normally equivalent to upper case.
     * <br /><br />
     * Example:
     * <code><font size="-2">
     * <br />WordUtils.capitalizeFully(null       , *          ) = null
     * <br />WordUtils.capitalizeFully(""         , *          ) = ""
     * <br />WordUtils.capitalizeFully(*          , null       ) = *
     * <br />WordUtils.capitalizeFully(*          , new char[0]) = *
     * <br />WordUtils.capitalizeFully("i aM.fine", {'.'}      ) = "I am.Fine"
     * </font></code>
     * <br /><br />
     *
     * @param str The {@link String} to capitalize, may be {@code null}.
     * @param delimiters Set of characters to determine capitalization, {@code null} means whitespace.
     *
     * @return Capitalized {@link String}, {@code null} if {@code null} {@link String} input.
     */
    public static String capitalizeFully(String str, final char... delimiters) {
        if (StringUtils.isEmpty(str)) return str;
        return capitalize(str.toLowerCase(), delimiters);
    }

    /**
     * Checks if the {@link String} contains all words in the given array.
     * <br /><br />
     * A {@code null} {@link String} will return {@code false}. A {@code null}, zero length search array or if one element of array is {@code null} will return {@code false}.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />WordUtils.containsAllWords(null     , *           ) = false
     * <br />WordUtils.containsAllWords(""       , *           ) = false
     * <br />WordUtils.containsAllWords(*        , null        ) = false
     * <br />WordUtils.containsAllWords(*        , []          ) = false
     * <br />WordUtils.containsAllWords("abcd"   , "ab" , "cd" ) = false
     * <br />WordUtils.containsAllWords("abc def", "def", "abc") = true
     * </font></code>
     * <br /><br />
     *
     * @param word The {@link CharSequence} to check, may be {@code null}.
     * @param words The array of {@link String} words to search for, may be {@code null}.
     *
     * @return {@code true} if all search words are found, {@code false} otherwise.
     */
    public static boolean containsAllWords(final CharSequence word, final CharSequence... words) {
        if (StringUtils.isEmpty(word) || ArrayUtils.isEmpty(words)) return false;
        for (final CharSequence w : words) {
            if (StringUtils.isBlank(w)) return false;
            final Pattern p = Pattern.compile(".*\\b" + w + "\\b.*");
            if (!p.matcher(word).matches()) return false;
        }
        return true;
    }

    /**
     * Converts an array of delimiters to a {@link HashSet} of code points. Code point of {@code space(32)} is added as the default value if delimiters is {@code null}.
     * The generated {@link HashSet} provides O(1) lookup time.
     *
     * @param delimiters Set of characters to determine capitalization, {@code null} means whitespace.
     *
     * @return Set<Integer>
     */
    private static Set<Integer> generateDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<>();
        if (delimiters == null || delimiters.length == 0) {
            if (delimiters == null) delimiterHashSet.add(Character.codePointAt(new char[] {' '}, 0));
            return delimiterHashSet;
        }

        for (int index = 0; index < delimiters.length; index++) delimiterHashSet.add(Character.codePointAt(delimiters, index));
        return delimiterHashSet;
    }

    /**
     * Extracts the initial characters from each word in the {@link String}.
     * <br /><br />
     * All first characters after whitespace are returned as a new {@link String}. Their case is not changed.
     * <br /><br />
     * Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input {@link String} returns {@code null}.
     * <br /><br />
     * Example:
     * <code><font size="-2">
     * <br />WordUtils.initials(null          ) = null
     * <br />WordUtils.initials(""            ) = ""
     * <br />WordUtils.initials("Ben John Lee") = "BJL"
     * <br />WordUtils.initials("Ben J.Lee"   ) = "BJ"
     * </font></code>
     * <br /><br />
     *
     * @param str The {@link String} to get initials from, may be {@code null}.
     *
     * @return {@link String} of initial letters, {@code null} if {@code null} {@link String} input.
     *
     * @see #initials(String,char[])
     */
    public static String initials(final String str) {
        return initials(str, null);
    }

    /**
     * Extracts the initial characters from each word in the {@link String}.
     * <br /><br />
     * All first characters after the defined delimiters are returned as a new {@link String}. Their case is not changed.
     * <br /><br />
     * If the delimiters array is {@code null}, then Whitespace is used. Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A {@code null} input {@link String} returns {@code null}. An empty delimiter array returns an empty {@link String}.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />WordUtils.initials(null          , *          ) = null
     * <br />WordUtils.initials(""            , *          ) = ""
     * <br />WordUtils.initials("Ben John Lee", null       ) = "BJL"
     * <br />WordUtils.initials("Ben J.Lee"   , null       ) = "BJ"
     * <br />WordUtils.initials("Ben J.Lee"   , [' ','.']  ) = "BJL"
     * <br />WordUtils.initials(*             , new char[0]) = ""
     * </font></code>
     * <br /><br />
     *
     * @param str The {@link String} to get initials from, may be {@code null}.
     * @param delimiters Set of characters to determine words, {@code null} means whitespace.
     *
     * @return {@link String} of initial characters, {@code null} if {@code null} {@link String} input.
     *
     * @see #initials(String)
     */
    public static String initials(final String str, final char... delimiters) {
        if (StringUtils.isEmpty(str)) return str;
        if (delimiters != null && delimiters.length == 0) return StringUtils.EMPTY;
        final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen / 2 + 1];
        int count = 0;
        boolean lastWasGap = true;
        for (int i = 0; i < strLen;) {
            final int codePoint = str.codePointAt(i);

            if (delimiterSet.contains(codePoint) || delimiters == null && Character.isWhitespace(codePoint)) lastWasGap = true;
            else if (lastWasGap) {
                newCodePoints[count++] = codePoint;
                lastWasGap = false;
            }

            i += Character.charCount(codePoint);
        }
        return new String(newCodePoints, 0, count);
    }

    /**
     * Is the character a delimiter.
     *
     * @param ch The character to check.
     * @param delimiters The delimiters.
     *
     * @return {@code true} if it is a delimiter
     *
     * @deprecated As of 1.2 and will be removed in 2.0.
     */
    @Deprecated public static boolean isDelimiter(final char ch, final char[] delimiters) {
        if (delimiters == null) return Character.isWhitespace(ch);
        for (final char delimiter : delimiters) {
            if (ch == delimiter) return true;
        }
        return false;
    }

    /**
     * Is the codePoint a delimiter.
     *
     * @param codePoint The codePoint to check.
     * @param delimiters The delimiters.
     *
     * @return {@code true} if it is a delimiter.
     *
     * @deprecated As of 1.2 and will be removed in 2.0.
     */
    @Deprecated public static boolean isDelimiter(final int codePoint, final char[] delimiters) {
        if (delimiters == null) return Character.isWhitespace(codePoint);
        for (int index = 0; index < delimiters.length; index++) {
            final int delimiterCodePoint = Character.codePointAt(delimiters, index);
            if (delimiterCodePoint == codePoint) return true;
        }
        return false;
    }

    /**
     * Swaps the case of a {@link String} using a word-based algorithm.
     *
     * <ul>
     *     <li>Upper case character converts to Lower case.                             </li>
     *     <li>Title case character converts to Lower case.                             </li>
     *     <li>Lower case character after Whitespace or at start converts to Title case.</li>
     *     <li>Other Lower case character converts to Upper case.                       </li>
     * </ul>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * A {@code null} input String returns {@code null}.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />StringUtils.swapCase(null)                 = null
     * <br />StringUtils.swapCase("")                   = ""
     * <br />StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
     * </font></code>
     * <br /><br />
     *
     * @param str The {@link String} to swap case, may be {@code null}.
     *
     * @return The changed {@link String}, {@code null} if {@code null} {@link String} input.
     */
    public static String swapCase(final String str) {
        if (StringUtils.isEmpty(str)) return str;
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        boolean whitespace = true;
        for (int index = 0; index < strLen;) {
            final int oldCodepoint = str.codePointAt(index);
            final int newCodePoint;
            if (Character.isUpperCase(oldCodepoint) || Character.isTitleCase(oldCodepoint)) {
                newCodePoint = Character.toLowerCase(oldCodepoint);
                whitespace = false;
            } else if (Character.isLowerCase(oldCodepoint)) {
                if (whitespace) {
                    newCodePoint = Character.toTitleCase(oldCodepoint);
                    whitespace = false;
                } else newCodePoint = Character.toUpperCase(oldCodepoint);
            } else {
                whitespace = Character.isWhitespace(oldCodepoint);
                newCodePoint = oldCodepoint;
            }
            newCodePoints[outOffset++] = newCodePoint;
            index += Character.charCount(newCodePoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * Un-capitalizes all the whitespace separated words in a {@link String}. Only the first character of each word is changed.
     * <br /><br />
     * Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input {@link String} returns {@code null}.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />WordUtils.uncapitalize(null)        = null
     * <br />WordUtils.uncapitalize("")          = ""
     * <br />WordUtils.uncapitalize("I Am FINE") = "i am fINE"
     * </font></code>
     * <br /><br />
     * @param str The {@link String} to uncapitalize, may be {@code null}.
     *
     * @return Uncapitalized {@link String}, {@code null} if {@code null} {@link String} input.
     *
     * @see #capitalize(String)
     */
    public static String uncapitalize(final String str) {
        return uncapitalize(str, null);
    }

    /**
     * Un-capitalizes all the whitespace separated words in a {@link String}. Only the first character of each word is changed.
     * <br /><br />
     * The delimiters represent a set of characters understood to separate words.
     * The first {@link String} character and the first non-delimiter character after a delimiter will be uncapitalized.
     * <br /><br />
     * Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null} input String returns {@code null}.
     * <br /><br />
     * Examples:
     * <code><font size="-2">
     * <br />WordUtils.uncapitalize(null       , *           ) = null
     * <br />WordUtils.uncapitalize(""         , *           ) = ""
     * <br />WordUtils.uncapitalize(*          , null        ) = *
     * <br />WordUtils.uncapitalize(*          , new char[0] ) = *
     * <br />WordUtils.uncapitalize("I AM.FINE", {'.'}       ) = "i AM.fINE"
     * <br />WordUtils.uncapitalize("I am fine", new char[]{}) = "i am fine"
     * </font></code>
     * <br /><br />
     *
     * @param str The {@link String} to uncapitalize, may be {@code null}.
     * @param delimiters Set of characters to determine un-capitalization, {@code null} means whitespace.
     *
     * @return Uncapitalized {@link String}, {@code null} if {@code null} {@link String} input.
     *
     * @see #capitalize(String)
     */
    public static String uncapitalize(final String str, final char... delimiters) {
        if (StringUtils.isEmpty(str)) return str;
        final Set<Integer> delimiterSet = generateDelimiterSet(delimiters);
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;

        boolean uncapitalizeNext = true;
        for (int index = 0; index < strLen;) {
            final int codePoint = str.codePointAt(index);

            if (delimiterSet.contains(codePoint)) {
                uncapitalizeNext = true;
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            } else if (uncapitalizeNext) {
                final int titleCaseCodePoint = Character.toLowerCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                uncapitalizeNext = false;
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }
        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * Wraps a single line of text, identifying words by {@code ' '}.
     * <br /><br />
     * New lines will be separated by the system property line separator. Very long words, such as URLs will <i>not</i> be wrapped.
     * <br /><br />
     * Leading spaces on a new line are stripped. Trailing spaces are not stripped.
     * <br /><br />
     * <font size="-2"><table border="1">
     *     <caption>Examples</caption>
     *     <tr>
     *         <th>input</th><th>wrapLength</th><th>result</th>
     *     </tr>
     *     <tr>
     *         <td>null</td><td>*</td><td>null</td>
     *     </tr>
     *     <tr>
     *         <td>""</td><td>*</td><td>""</td>
     *     </tr>
     *     <tr>
     *         <td>"Here is one line of text that is going to be wrapped after 20 columns."</td><td>20</td><td>"Here is one line of\ntext that is going\nto be wrapped after\n20 columns."</td>
     *     </tr>
     *     <tr>
     *         <td>"Click here to jump to the commons website - https://commons.apache.org"</td><td>20</td><td>"Click here to jump\nto the commons\nwebsite -\nhttps://commons.apache.org"</td>
     *     </tr>
     *     <tr>
     *         <td>"Click here, https://commons.apache.org, to jump to the commons website"</td><td>20</td><td>"Click here,\nhttps://commons.apache.org,\nto jump to the\ncommons website"</td>
     *     </tr>
     * </table></font>
     * <br />(assuming that '\n' is the systems line separator)
     * <br /><br />
     *
     * @param str The {@link String} to be word wrapped, may be {@code null}.
     * @param wrapLength The column to wrap the words at, less than 1 is treated as 1.
     *
     * @return A line with newlines inserted, {@code null} if {@code null} input.
     */
    public static String wrap(final String str, final int wrapLength) {
        return wrap(str, wrapLength, null, false);
    }

    /**
     * Wraps a single line of text, identifying words by {@code ' '}.
     * <br /><br />
     * Leading spaces on a new line are stripped. Trailing spaces are not stripped.
     * <br /><br />
     * <font size="-2"><table border="1">
     *     <caption>Examples</caption>
     *     <tr>
     *         <th>input</th><th>wrapLength</th><th>newLineString</th><th>wrapLongWords</th><th>result</th>
     *     </tr>
     *     <tr>
     *         <td>null</td><td>*</td><td>*</td><td>true/false</td><td>null</td>
     *     </tr>
     *     <tr>
     *         <td>""</td><td>*</td><td>*</td><td>true/false</td><td>""</td>
     *     </tr>
     *     <tr>
     *         <td>"Here is one line of text that is going to be wrapped after 20 columns."</td><td>20</td><td>"\n"</td><td>true/false</td><td>"Here is one line of\ntext that is going\nto be wrapped after\n20 columns."</td>
     *     </tr>
     *     <tr>
     *         <td>"Here is one line of text that is going to be wrapped after 20 columns."</td><td>20</td><td>"&lt;br /&gt;"</td><td>true/false</td><td>"Here is one line of&lt;br /&gt;text that is going&lt;br /&gt;to be wrapped after&lt;br /&gt;20 columns."</td>
     *     </tr>
     *     <tr>
     *         <td>"Here is one line of text that is going to be wrapped after 20 columns."</td><td>20</td><td>null</td><td>true/false</td><td>"Here is one line of" + systemNewLine + "text that is going" + systemNewLine + "to be wrapped after" + systemNewLine + "20 columns."</td>
     *     </tr>
     *     <tr>
     *         <td>"Click here to jump to the commons website - https://commons.apache.org"</td><td>20</td><td>"\n"</td><td>false</td><td>"Click here to jump\nto the commons\nwebsite -\nhttps://commons.apache.org"</td>
     *     </tr>
     *     <tr>
     *         <td>"Click here to jump to the commons website - https://commons.apache.org"</td><td>20</td><td>"\n"</td><td>true</td><td>"Click here to jump\nto the commons\nwebsite -\nhttp://commons.apach\ne.org"</td>
     *     </tr>
     * </table></font>
     *
     * @param str The {@link String} to be word wrapped, may be {@code null}.
     * @param wrapLength The column to wrap the words at, less than 1 is treated as 1.
     * @param newLineStr The {@link String} to insert for a new line, {@code null} uses the system property line separator.
     * @param wrapLongWords {@code true} if long words (such as URLs) should be wrapped.
     *
     * @return A line with newlines inserted, {@code null} if {@code null} input.
     */
    public static String wrap(final String str,  final int wrapLength, final String newLineStr, final boolean wrapLongWords) {
        return wrap(str, wrapLength, newLineStr, wrapLongWords, " ");
    }

    /**
     * Wraps a single line of text, identifying words by {@code wrapOn}.
     * <br /><br />
     * Leading spaces on a new line are stripped. Trailing spaces are not stripped.
     * <br /><br />
     * <font size="-2"><table border="1">
     *     <caption>Examples</caption>
     *     <tr>
     *         <th>input</th><th>wrapLength</th><th>newLineString</th><th>wrapLongWords</th><th>wrapOn</th><th>result</th>
     *     </tr>
     *     <tr>
     *         <td>null</td><td>*</td><td>*</td><td>true/false</td><td>*</td><td>null</td>
     *     </tr>
     *     <tr>
     *         <td>""</td><td>*</td><td>*</td><td>true/false</td><td>*</td><td>""</td>
     *     </tr>
     *     <tr>
     *         <td>"Here is one line of text that is going to be wrapped after 20 columns."</td><td>20</td><td>"\n"</td><td>true/false</td><td>" "</td><td>"Here is one line of\ntext that is going\nto be wrapped after\n20 columns."</td>
     *     </tr>
     *     <tr>
     *         <td>"Here is one line of text that is going to be wrapped after 20 columns."</td><td>20</td><td>"&lt;br /&gt;"</td><td>true/false</td><td>" "</td><td>"Here is one line of&lt;br /&gt;text that is going&lt;br /&gt;to be wrapped after&lt;br /&gt;20 columns."</td>
     *     </tr>
     *     <tr>
     *         <td>"Here is one line of text that is going to be wrapped after 20 columns."</td><td>20</td><td>null</td><td>true/false</td><td>" "</td><td>"Here is one line of" + systemNewLine + "text that is going" + systemNewLine + "to be wrapped after" + systemNewLine + "20 columns."</td>
     *     </tr>
     *     <tr>
     *         <td>"Click here to jump to the commons website - https://commons.apache.org"</td><td>20</td><td>"\n"</td><td>false</td><td>" "</td><td>"Click here to jump\nto the commons\nwebsite -\nhttps://commons.apache.org"</td>
     *     </tr>
     *     <tr>
     *         <td>"Click here to jump to the commons website - https://commons.apache.org"</td><td>20</td><td>"\n"</td><td>true</td><td>" "</td><td>"Click here to jump\nto the commons\nwebsite -\nhttp://commons.apach\ne.org"</td>
     *     </tr>
     *     <tr>
     *         <td>"flammable/inflammable"</td><td>20</td><td>"\n"</td><td>true</td><td>"/"</td><td>"flammable\ninflammable"</td>
     *     </tr>
     * </table></font>
     *
     * @param str The {@link String} to be word wrapped, may be {@code null}.
     * @param wrapLength The column to wrap the words at, less than 1 is treated as 1.
     * @param newLineStr The {@link String} to insert for a new line, {@code null} uses the system property line separator.
     * @param wrapLongWords {@code true} if long words (such as URLs) should be wrapped.
     * @param wrapOn Regex expression to be used as a breakable characters, if blank {@link String} is provided a space character will be used.
     *
     * @return A line with newlines inserted, {@code null} if {@code null} input.
     */
    public static String wrap(final String str, int wrapLength, String newLineStr, final boolean wrapLongWords, String wrapOn) {
        if (str == null) return null;
        if (newLineStr == null) newLineStr = System.lineSeparator();
        if (wrapLength < 1) wrapLength = 1;
        if (StringUtils.isBlank(wrapOn)) wrapOn = " ";
        final Pattern patternToWrapOn = Pattern.compile(wrapOn);
        final int inputLineLength = str.length();
        int offset = 0;
        final StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
        int matcherSize = -1;

        while (offset < inputLineLength) {
            int spaceToWrapAt = -1;
            Matcher matcher = patternToWrapOn.matcher(str.substring(offset, Math.min((int) Math.min(Integer.MAX_VALUE, offset + wrapLength + 1L), inputLineLength)));
            if (matcher.find()) {
                if (matcher.start() == 0) {
                    matcherSize = matcher.end();
                    if (matcherSize != 0) {
                        offset += matcher.end();
                        continue;
                    }
                    offset += 1;
                }
                spaceToWrapAt = matcher.start() + offset;
            }

            // only last line without leading spaces is left
            if (inputLineLength - offset <= wrapLength) break;

            while (matcher.find()) spaceToWrapAt = matcher.start() + offset;

            if (spaceToWrapAt >= offset) {
                // Normal case.
                wrappedLine.append(str, offset, spaceToWrapAt);
                wrappedLine.append(newLineStr);
                offset = spaceToWrapAt + 1;
            } else // Really long word or URL.
                if (wrapLongWords) {
                    if (matcherSize == 0) offset--;

                    // Wrap really long word one line at a time.
                    wrappedLine.append(str, offset, wrapLength + offset);
                    wrappedLine.append(newLineStr);
                    offset += wrapLength;
                    matcherSize = -1;
                } else {
                    // Do not wrap really long word, just extend beyond limit.
                    matcher = patternToWrapOn.matcher(str.substring(offset + wrapLength));
                    if (matcher.find()) {
                        matcherSize = matcher.end() - matcher.start();
                        spaceToWrapAt = matcher.start() + offset + wrapLength;
                    }

                    if (matcherSize == 0 && offset != 0) offset--;
                    if (spaceToWrapAt >= 0) {
                        wrappedLine.append(str, offset, spaceToWrapAt);
                        wrappedLine.append(newLineStr);
                        offset = spaceToWrapAt + 1;
                    } else {
                        wrappedLine.append(str, offset, str.length());
                        offset = inputLineLength;
                        matcherSize = -1;
                    }
                }
        }

        if (matcherSize == 0 && offset < inputLineLength) offset--;

        // Whatever is left in line is short enough to just pass through.
        wrappedLine.append(str, offset, str.length());

        return wrappedLine.toString();
    }

    /**
     * {@link WordUtils} instances should NOT be constructed in standard programming.
     * <br /><br />
     * Instead, the class should be used as {@code WordUtils.wrap("foo bar", 20);}.
     *
     * @throws IllegalAccessException if this constructor is invoked.
     */
    private WordUtils() throws IllegalAccessException { throw new IllegalAccessException("This class should not be instantiated!"); }
}