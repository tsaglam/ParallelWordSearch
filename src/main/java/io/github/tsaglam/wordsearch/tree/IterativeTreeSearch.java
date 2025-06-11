package io.github.tsaglam.wordsearch.tree;

import java.util.List;

/**
 * Iterative search on a {@link ParallelPrefixTree}. Allows searching character by character and immediately gaining
 * intermediate results.
 */
public class IterativeTreeSearch {

    private PrefixTreeNode tree;

    /**
     * Creates the search based on a {@link ParallelPrefixTree}.
     * @param tree is the tree to search. Can be {@link ParallelPrefixTree} or {@link PrefixTreeNode}.
     */
    public IterativeTreeSearch(PrefixTreeNode tree) {
        this.tree = tree;
    }

    /**
     * Conducts a single search operation based on a character of a pattern. Updates the search to the corresponding
     * subtree.
     * @param patternCharacter specifies the current character of the search pattern.
     * @return the search results for the current character.
     */
    public List<String> findMatchingWords(char patternCharacter) {
        tree = tree.getChildFor(patternCharacter);
        return tree.getContainedWords();
    }

}
