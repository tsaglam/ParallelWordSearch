package io.github.tsaglam.wordsearch.tree;

import java.util.List;

/**
 * Iterative search on a {@link ParallelPrefixTree}. Allows searching character by character and immediately gaining
 * intermediate results.
 */
public class IterativeTreeSearch {

    private PrefixTreeNode node;

    /**
     * Creates the search based on a {@link ParallelPrefixTree}.
     * @param tree is the tree to search.
     */
    public IterativeTreeSearch(ParallelPrefixTree tree) {
        this.node = tree; // tree is a (root) node
    }

    /**
     * Conducts a single search operation based on a character of a pattern. Updates the search to the corresponding
     * subtree.
     * @param patternCharacter specifies the current character of the search pattern.
     * @return the search results for the current character.
     */
    public List<String> findMatchingWords(char patternCharacter) {
        node = node.getChildFor(patternCharacter);
        return node.getContainedWords();
    }

}
