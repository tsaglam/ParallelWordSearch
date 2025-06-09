package io.github.tsaglam.wordsearch.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * Parallel implementation of a searchable dictionary based on a forest of tree sets.
 */
public class MultiTreeSetWordSearch implements SearchableDictionary {

    private List<TreeSet<String>> dictionaries;

    /**
     * Creates the dictionary.
     * @param words specifies the content, cannot be null.
     * @param numberOfTrees specifies how many trees are managed in parallel.
     */
    public MultiTreeSetWordSearch(List<String> words, int numberOfTrees) {
        if (words == null) {
            throw new IllegalArgumentException("Input words cannot be null.");
        }

        int bucketSize = Math.ceilDivExact(words.size(), numberOfTrees);
        List<List<String>> sublists = new ArrayList<>();
        for (int i = 0; i < words.size(); i += bucketSize) {
            int end = Math.min(i + bucketSize, words.size());
            sublists.add(words.subList(i, end));
        }

        dictionaries = sublists.parallelStream().map(it -> new TreeSet<>(it)).toList();
    }

    /**
     * Creates the dictionary with the number of trees based on the number of threads.
     * @param words specifies the content, cannot be null.
     */
    public MultiTreeSetWordSearch(List<String> words) {
        this(words, Runtime.getRuntime().availableProcessors());
    }

    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        String nextPrefix = pattern + Character.MAX_VALUE;
        return dictionaries.stream().parallel().flatMap(it -> it.subSet(pattern, nextPrefix).stream()).toList();
    }

}
