package io.github.tsaglam.wordsearch.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.tsaglam.wordsearch.SearchableDictionary;

/**
 * A single node in a {@link ParallelPrefixTree}, representing one level of character depth in a prefix tree (Trie).
 * Each {@code PrefixTreeNode} has a depth value representing the character index this node corresponds to. It stores
 * children in a map keyed by a character at the next depth level. Each node directly stores a list of words that
 * terminate at this depth.
 */
public class PrefixTreeNode implements SearchableDictionary {

    private final Map<Character, PrefixTreeNode> children;
    private final int depth;
    private List<String> storedWords;

    public PrefixTreeNode(int depth) {
        this.depth = depth;
        children = new HashMap<>();
        storedWords = new ArrayList<>();
    }

    /**
     * Adds a word to the node. The word is either added to the node directly or to a child node if the word is longer than
     * the current node's depth.
     * @param word is the word to add.
     */
    public void addWord(String word) {
        if (word.length() == depth) {
            storedWords.add(word);
        } else if (word.length() > depth) {
            char indexCharacter = word.charAt(depth);
            children.computeIfAbsent(indexCharacter, key -> new PrefixTreeNode(depth + 1)).addWord(word);
        } else if (word.length() < depth) {
            throw new IllegalStateException("Must never occur, word should be stored in (transitive) parent.");
        }
    }

    /**
     * Returns all words contained directly or indirectly under this node.
     * @return the list of words or an empty list if none exist.
     */
    public List<String> getContainedWords() {
        List<String> words = new ArrayList<>(storedWords);
        for (PrefixTreeNode child : children.values()) { // TODO do in parallel
            words.addAll(child.getContainedWords());
        }
        return storedWords;
    }

    /**
     * Returns all words contained directly or indirectly under this node that start with the specified pattern.
     * @param pattern is the specified pattern or prefix.
     * @return return the list of words or an empty list if none exist.
     */
    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern.length() == depth) {
            return getContainedWords();
        }
        char indexCharacter = pattern.charAt(depth);
        return children.get(indexCharacter).findMatchingWords(pattern);
    }
}
