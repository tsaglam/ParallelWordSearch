package io.github.tsaglam.wordsearch.tree;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * A prefix tree (Trie) implementation that supports parallel prefix-based word search. <b>Thread safety:</b> This class
 * supports concurrent use. Note that while adding concurrently with searching, result may vary (as to be expected).
 * When using {@link ParallelPrefixTree#ParallelPrefixTree(List)}, all words are added concurrently.
 */
public class ParallelPrefixTree extends PrefixTreeNode implements SearchableDictionary {

    private static final int INITIAL_DEPTH = 0;
    private AtomicInteger size;

    /**
     * Constructs a prefix tree concurrently from the given list of words.
     * @param words the list of words to insert.
     */
    public ParallelPrefixTree(List<String> words) {
        super(INITIAL_DEPTH);
        if (words == null) {
            throw new IllegalArgumentException("Words cannot be null!");
        }
        size = new AtomicInteger(words.size());
        words.stream().parallel().forEach(this::addWord);
    }

    /**
     * Constructs an empty prefix tree with an expected branching factor of 26.
     */
    public ParallelPrefixTree() {
        super(INITIAL_DEPTH);
        size = new AtomicInteger();
    }

    /**
     * Adds a word to the tree. <b>Thread safety:</b> This method is safe to call concurrently.
     * @param word is the word to add.
     */
    @Override
    public void addWord(String word) {
        size.incrementAndGet();
        super.addWord(word);
    }

    /**
     * Returns the number of words stored in the tree. <b>Thread safety:</b> This method is safe to call concurrently.
     * @return the total number of words.
     */
    public int size() {
        return size.get();
    }

    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Search pattern cannot be null!");
        }
        return super.findMatchingWords(pattern);
    }
}
