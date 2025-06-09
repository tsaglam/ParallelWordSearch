package io.github.tsaglam.wordsearch;

import java.util.List;

/**
 * Dictionary of strings that can be searched efficiently for strings that start with a pattern.
 */
public interface SearchableDictionary {

    /**
     * Searches for all words in the dictionary that have the pattern as prefix.
     * @param pattern specifies the search pattern.
     * @return the matching words or an empty list if none match.
     * @throws IllegalArgumentException if the pattern is null.
     */
    List<String> findMatchingWords(String pattern);
}
