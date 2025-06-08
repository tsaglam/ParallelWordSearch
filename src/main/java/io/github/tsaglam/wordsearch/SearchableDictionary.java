package io.github.tsaglam.wordsearch;

import java.util.List;

/**
 * Dictionary of strings that can be searched efficiently for string that start with a pattern.
 */
public interface SearchableDictionary {

    /**
     * Searches for all words in the dictionary that have the pattern as prefix.
     * @param pattern specifies the search pattern.
     * @return the matching words or an empty list if none match.
     */
    List<String> findMatchingPrefixes(String pattern);
}
