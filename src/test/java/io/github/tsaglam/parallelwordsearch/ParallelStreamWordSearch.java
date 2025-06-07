package io.github.tsaglam.parallelwordsearch;

import java.util.List;

/**
 * Naive but parallel implementation of a searchable dictionary based on parallel streams.
 */
public class ParallelStreamWordSearch implements SearchableDictionary {

    private List<String> words;

    /**
     * Created the dictionary.
     * @param words specifies the content, cannot be null.
     */
    public ParallelStreamWordSearch(List<String> words) {
        if (words == null) {
            throw new IllegalArgumentException("Input words cannot be null.");
        }
        this.words = words;
    }

    @Override
    public List<String> findMatchingPrefixes(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        return words.stream().parallel().filter(it -> it.startsWith(pattern)).toList();
    }

}
