package io.github.tsaglam.wordsearch.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * Parallel implementation of a searchable dictionary based on hashing.
 */
public class ParallelHashingTreeSearch implements SearchableDictionary {

    private final Map<String, List<String>> prefixToWords;

    /**
     * Creates the dictionary.
     * @param words specifies the content, cannot be null.
     * @throws IllegalArgumentException if words is null.
     */
    public ParallelHashingTreeSearch(List<String> words) {
        if (words == null) {
            throw new IllegalArgumentException("Input words cannot be null.");
        }

        prefixToWords = new ConcurrentHashMap<>();
        words.parallelStream().forEach(word -> {
            for (int i = 1; i <= word.length(); i++) {
                String prefix = word.substring(0, i);
                prefixToWords.computeIfAbsent(prefix, key -> Collections.synchronizedList(new ArrayList<>())).add(word);
            }
        });
        prefixToWords.put("", words);
    }

    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        return prefixToWords.getOrDefault(pattern, List.of());
    }

}
