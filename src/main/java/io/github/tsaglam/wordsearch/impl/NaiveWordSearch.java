package io.github.tsaglam.wordsearch.impl;

import java.util.List;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * Naive implementation of a searchable dictionary based on lists and sequential search.
 */
public class NaiveWordSearch implements SearchableDictionary {

    private List<String> words;

    /**
     * Creates the dictionary.
     * @param words specifies the content, cannot be null.
     */
    public NaiveWordSearch(List<String> words) {
        if (words == null) {
            throw new IllegalArgumentException("Input words cannot be null.");
        }
        this.words = words;
    }

    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        return words.stream().filter(it -> it.startsWith(pattern)).toList();
    }

}
