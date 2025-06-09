package io.github.tsaglam.wordsearch;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import io.github.tsaglam.wordsearch.impl.MultiTreeSetWordSearch;
import io.github.tsaglam.wordsearch.impl.NaiveWordSearch;
import io.github.tsaglam.wordsearch.impl.ParallelHashingTreeSearch;
import io.github.tsaglam.wordsearch.impl.ParallelStreamWordSearch;
import io.github.tsaglam.wordsearch.impl.TreeSetWordSearch;
import io.github.tsaglam.wordsearch.tree.ParallelPrefixForest;
import io.github.tsaglam.wordsearch.tree.ParallelPrefixTree;

/**
 * Utility class to provide inputs for the parameterized testing of multiple search implementations.
 */
public final class TestUtils {

    // Private constructor to prevent instantiation
    private TestUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Method source for constructors of search algorithms.
     */
    public static Stream<Arguments> provideDictionaryConstructors() {
        return Stream.of(Arguments.of("Naive", (DictionarySupplier) NaiveWordSearch::new),
                Arguments.of("ParallelStream", (DictionarySupplier) ParallelStreamWordSearch::new),
                Arguments.of("TreeSet", (DictionarySupplier) TreeSetWordSearch::new),
                Arguments.of("MultiTreeSet", (DictionarySupplier) MultiTreeSetWordSearch::new),
                Arguments.of("PrefixHashing", (DictionarySupplier) ParallelHashingTreeSearch::new),
                Arguments.of("ParallelPrefixTree", (DictionarySupplier) ParallelPrefixTree::new),
                Arguments.of("ParallelPrefixForest", (DictionarySupplier) ParallelPrefixForest::new));
    }
}
