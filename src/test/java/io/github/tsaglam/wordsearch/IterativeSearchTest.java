package io.github.tsaglam.wordsearch;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.tsaglam.wordsearch.tree.IterativeTreeSearch;
import io.github.tsaglam.wordsearch.tree.ParallelPrefixTree;

/**
 * Test class for the {@link IterativeTreeSearch}.
 */
class IterativeSearchTest {
    private static final String TEST_PATTERN = "TEST";
    private List<String> combinations;
    private ParallelPrefixTree searchTree;

    @BeforeEach
    void setUp() {
        combinations = TestUtils.createTestData();
        Collections.shuffle(combinations);
        searchTree = new ParallelPrefixTree();
    }

    @Test
    @DisplayName("Test that iterative search behaves as non-iterative search.")
    void testIterativeSearch() {
        IterativeTreeSearch search = new IterativeTreeSearch(searchTree);
        for (int i = 0; i < TEST_PATTERN.length(); i++) {
            // baseline:
            String prefix = TEST_PATTERN.substring(0, i + 1);
            List<String> expectedResult = searchTree.findMatchingWords(prefix);

            // iterative search:
            char indexCharacter = TEST_PATTERN.charAt(i);
            List<String> iterativeResults = search.findMatchingWords(indexCharacter);

            assertIterableEquals(expectedResult, iterativeResults);
        }
    }

}
