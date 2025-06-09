package io.github.tsaglam.wordsearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.tsaglam.wordsearch.impl.ForestWordSearch;
import io.github.tsaglam.wordsearch.impl.NaiveWordSearch;
import io.github.tsaglam.wordsearch.impl.ParallelHashingTreeSearch;
import io.github.tsaglam.wordsearch.impl.ParallelStreamWordSearch;
import io.github.tsaglam.wordsearch.impl.TreeSetWordSearch;

/**
 * Functional tests for the parallel word search.
 */
class SearchableDictionaryTest {

    private static final String TEST_PATTERN = "TEST";
    private static final String TEST_PREFIX = "TES";
    private List<String> combinationsUnsorted;
    private List<String> combinations;

    @BeforeEach
    void setUp() {
        combinationsUnsorted = new ArrayList<>();
        for (char first = 'A'; first <= 'Z'; first++) {
            for (char second = 'A'; second <= 'Z'; second++) {
                for (char third = 'A'; third <= 'Z'; third++) {
                    for (char fourth = 'A'; fourth <= 'Z'; fourth++) {
                        combinationsUnsorted.add("" + first + second + third + fourth);
                    }
                }
            }
        }
        combinations = new ArrayList<>(combinationsUnsorted);
        Collections.shuffle(combinations);
    }

    static Stream<Arguments> provideDictionaryConstructors() {
        return Stream.of(Arguments.of("Naive", (DictionarySupplier) NaiveWordSearch::new),
                Arguments.of("ParallelStream", (DictionarySupplier) ParallelStreamWordSearch::new),
                Arguments.of("TreeSet", (DictionarySupplier) TreeSetWordSearch::new),
                Arguments.of("ForestBased", (DictionarySupplier) ForestWordSearch::new),
                Arguments.of("PrefixHashing", (DictionarySupplier) ParallelHashingTreeSearch::new));
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for a word that occurs once.")
    @MethodSource("provideDictionaryConstructors")
    void testExamplePattern(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(combinations);
        List<String> results = search.findMatchingPrefixes(TEST_PATTERN);
        assertIterableEquals(List.of(TEST_PATTERN), results);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for a three-letter prefix.")
    @MethodSource("provideDictionaryConstructors")
    void testPrefix(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(combinations);
        List<String> results = search.findMatchingPrefixes(TEST_PREFIX);
        assertEquals(26, results.size());
        results.forEach(it -> it.startsWith(TEST_PREFIX));
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for an empty string.")
    @MethodSource("provideDictionaryConstructors")
    void testEmptyPattern(String name, DictionarySupplier supplier) {
        String pattern = "";
        SearchableDictionary search = supplier.create(combinations);
        List<String> results = new ArrayList<>(search.findMatchingPrefixes(pattern));
        Collections.sort(results);
        assertEquals(combinationsUnsorted.size(), results.size());
        assertIterableEquals(combinationsUnsorted, results);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for null.")
    @MethodSource("provideDictionaryConstructors")
    void testNullPattern(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(combinations);
        assertThrowsExactly(IllegalArgumentException.class, () -> search.findMatchingPrefixes(null));

    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test empty dictionary.")
    @MethodSource("provideDictionaryConstructors")
    void testEmptyDictionary(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(Collections.emptyList());
        List<String> results = search.findMatchingPrefixes(TEST_PATTERN);
        assertIterableEquals(List.of(), results);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test null-valued dictionary.")
    @MethodSource("provideDictionaryConstructors")
    void testNullDictionary(String name, DictionarySupplier supplier) {
        assertThrowsExactly(IllegalArgumentException.class, () -> supplier.create(null));
    }

    @Disabled("Not part of the specifed behavior.")
    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for a word that occurs twice.")
    @MethodSource("provideDictionaryConstructors")
    void testDuplicateWord(String name, DictionarySupplier supplier) {
        combinations.add(TEST_PATTERN);
        SearchableDictionary search = supplier.create(combinations);
        List<String> results = search.findMatchingPrefixes(TEST_PATTERN);
        assertIterableEquals(List.of(TEST_PATTERN, TEST_PATTERN), results);
    }
}
