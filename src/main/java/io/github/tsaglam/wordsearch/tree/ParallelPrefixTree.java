package io.github.tsaglam.wordsearch.tree;

import java.util.List;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * A prefix tree (Trie) implementation that supports parallel prefix-based word search.
 */ // TODO comment on thread safety
public class ParallelPrefixTree extends PrefixTreeNode implements SearchableDictionary { // TODO extend jdoc

    // TODO iterative search

    private static final int INITIAL_DEPTH = 0;
    private int size;

    /**
     * Constructs a prefix tree from the given list of words.
     * @param words the list of words to insert.
     */
    public ParallelPrefixTree(List<String> words) {
        super(INITIAL_DEPTH);
        if (words == null) {
            throw new IllegalArgumentException("Words cannot be null!");
        }
        size = words.size();
        // TODO make in parallel.
        for (String word : words) {
            addWord(word);
        }
    }

    /**
     * Constructs an empty prefix tree with an expected branching factor of 26.
     */
    public ParallelPrefixTree() {
        super(INITIAL_DEPTH);
    }

    /**
     * Adds a word to the tree.
     * @param word is the word to add.
     */
    @Override
    public void addWord(String word) { // TODO comment on thread safety
        size++;
        super.addWord(word);
    }

    /**
     * Returns the number of words stored in the tree.
     * @return the total number of words.
     */
    public int size() {  // TODO comment on thread safety
        return size;
    }

    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Search pattern cannot be null!");
        }
        return super.findMatchingWords(pattern);
    }
}
