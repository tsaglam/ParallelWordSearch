package io.github.tsaglam.wordsearch.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private List<String> storedWords; // TODO avoid list, use counter for memory.

    /**
     * Creates a prefix tree node with a specified depth.
     * @param depth specifies which word index is used for the nodes children. Must be <code>parentDepth + 1</code>.
     */
    public PrefixTreeNode(int depth) {
        this.depth = depth;
        children = new ConcurrentHashMap<>();
        storedWords = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Adds a word to the node. The word is either added to the node directly or to a child node if the word is longer than
     * the current node's depth. <b>Thread safety:</b> This method is safe to call concurrently.
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
     * Returns all words contained directly or indirectly under this node. <b>Thread safety:</b> This method is safe to call
     * concurrently only if no modifications are made to the tree during its execution. Concurrent modifications may lead to
     * undefined behavior.
     * @return the list of words or an empty list if none exist.
     */
    public List<String> getContainedWords() {
        List<String> words = new ArrayList<>(storedWords);
        words.addAll(children.values().parallelStream().flatMap(it -> it.getContainedWords().stream()).toList());
        return words;
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
            return getContainedWords(); // all words at node and below match
        }
        char indexCharacter = pattern.charAt(depth);
        if (!children.containsKey(indexCharacter)) {
            return List.of(); // no matching words
        }
        return children.get(indexCharacter).findMatchingWords(pattern); // continue search
    }
}
