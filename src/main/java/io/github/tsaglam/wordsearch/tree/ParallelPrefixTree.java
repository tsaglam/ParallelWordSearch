package io.github.tsaglam.wordsearch.tree;

import java.util.List;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * A prefix tree (Trie) implementation that supports parallel prefix-based word search.
 */ // TODO comment on thread safety
public class ParallelPrefixTree extends PrefixTreeNode implements SearchableDictionary { // TODO extend jdoc

    private int size;

    /**
     * Constructs a prefix tree from the given list of words.
     * @param words the list of words to insert.
     */
    public ParallelPrefixTree(List<String> words) {
        this();
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
        super(0);
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
}
