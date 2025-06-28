package io.github.tsaglam.wordsearch.tree;

import java.util.ArrayList;
import java.util.List;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * A virtual prefix tree (Trie) based on multiple trees that supports parallel prefix-based word search. <b>Thread
 * safety:</b> This class supports concurrent use.
 * @see ParallelPrefixTree
 */
public class ParallelPrefixForest implements SearchableDictionary {

    private final List<ParallelPrefixTree> dictionaries;

    /**
     * Creates the forest.
     * @param words specifies the content, cannot be null.
     * @param numberOfTrees specifies how many trees are managed in parallel.
     * @throws IllegalArgumentException if words is null.
     */
    public ParallelPrefixForest(List<String> words, int numberOfTrees) {
        if (words == null) {
            throw new IllegalArgumentException("Input words cannot be null.");
        }

        int bucketSize = Math.ceilDivExact(words.size(), numberOfTrees);
        List<List<String>> sublists = new ArrayList<>();
        for (int i = 0; i < words.size(); i += bucketSize) {
            int end = Math.min(i + bucketSize, words.size());
            sublists.add(words.subList(i, end));
        }

        dictionaries = sublists.parallelStream().map(ParallelPrefixTree::new).toList();
    }

    /**
     * Creates the forest with the number of trees based on the number of threads.
     * @param words specifies the content, cannot be null.
     */
    public ParallelPrefixForest(List<String> words) {
        this(words, Runtime.getRuntime().availableProcessors());
    }

    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        return dictionaries.stream().parallel().flatMap(it -> it.findMatchingWords(pattern).stream()).toList();
    }

}
