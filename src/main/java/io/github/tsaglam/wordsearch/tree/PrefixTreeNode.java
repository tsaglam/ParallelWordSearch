package io.github.tsaglam.wordsearch.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final AtomicInteger numberOfWords;

    /**
     * Creates a prefix tree node with a specified depth.
     * @param depth specifies which word index is used for the nodes children. Must be <code>parentDepth + 1</code>.
     */
    public PrefixTreeNode(int depth) {
        this.depth = depth;
        children = new ConcurrentHashMap<>(32, 1.0f);
        numberOfWords = new AtomicInteger();
    }

    /**
     * Adds a word to the node. The word is either added to the node directly or to a child node if the word is longer than
     * the current node's depth. <b>Thread safety:</b> This method is safe to call concurrently.
     * @param word is the word to add.
     */
    public void addWord(String word) {
        PrefixTreeNode current = this;

        while (current.depth != word.length()) {
            char indexCharacter = word.charAt(current.depth);
            int currentDepth = current.depth;
            current = current.children.computeIfAbsent(indexCharacter, key -> new PrefixTreeNode(currentDepth + 1));
        }
        current.numberOfWords.incrementAndGet();

    }

    /**
     * Returns all words contained directly or indirectly under this node. <b>Thread safety:</b> This method is safe to call
     * concurrently only if no modifications are made to the tree during its execution. Concurrent modifications may lead to
     * undefined behavior.
     * @param prefix is the prefix of all nodes up to including this node.
     * @return the list of words or an empty list if none exist.
     */
    public List<String> getContainedWords(String prefix) {
        List<String> results = Collections.synchronizedList(new ArrayList<>());
        collectWords(results, prefix);
        return results;
    }

    private void collectWords(List<String> results, String prefix) {
        results.addAll(Collections.nCopies(numberOfWords.get(), prefix));
        children.entrySet().parallelStream().forEach(entry -> entry.getValue().collectWords(results, prefix + entry.getKey()));
    }

    /**
     * Returns all words contained directly or indirectly under this node that start with the specified pattern. <b>Thread
     * safety:</b> This method is safe to call concurrently only if no modifications are made to the tree during its
     * execution. Concurrent modifications may lead to undefined behavior.
     * @param pattern is the specified pattern or prefix.
     * @return return the list of words or an empty list if none exist.
     */
    @Override
    public List<String> findMatchingWords(String pattern) {
        if (pattern.length() == depth) {
            return getContainedWords(pattern); // all words at node and below match
        }
        char indexCharacter = pattern.charAt(depth);

        if (!children.containsKey(indexCharacter)) {
            return List.of(); // no matching words
        }
        return children.get(indexCharacter).findMatchingWords(pattern); // continue search
    }

    /**
     * Returns the subtree of this node that corresponds to the specified character.
     * @param character is the index character of the child node.
     * @return the specified node, or a new empty node if none existed.
     */
    /* package-private */ PrefixTreeNode getChildFor(char character) {
        return children.computeIfAbsent(character, key -> new PrefixTreeNode(depth + 1));
    }
}
