package io.github.tsaglam.wordsearch.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * Sequential implementation of a searchable dictionary based on a tree set. High construction time but fast search.
 */
public class TreeSetWordSearch implements SearchableDictionary {

    private TreeSet<String> dictionary;

    /**
     * Creates the dictionary.
     * @param words specifies the content, cannot be null.
     */
    public TreeSetWordSearch(List<String> words) {
        if (words == null) {
            throw new IllegalArgumentException("Input words cannot be null.");
        }

        dictionary = new TreeSet<>();
        dictionary.addAll(words);

    }

    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        String nextPrefix = pattern + Character.MAX_VALUE;
        return new ArrayList<>(dictionary.subSet(pattern, nextPrefix));
    }

}
