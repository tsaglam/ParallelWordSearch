package io.github.tsaglam.wordsearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Functional tests for the parallel word search.
 */
class SearchableDictionaryTest {

    private static final String METHOD_SOURCE = "io.github.tsaglam.wordsearch.TestUtils#provideDictionaryConstructors";
    private static final String TEST_PATTERN = "TEST";
    private static final String TEST_PREFIX = "TES";
    private static final String EMPTY_WORD = "";
    private List<String> combinationsUnsorted;
    private List<String> combinations;

    @BeforeEach
    void setUp() {
        combinationsUnsorted = TestUtils.createTestData();
        combinations = new ArrayList<>(combinationsUnsorted);
        Collections.shuffle(combinations);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for a word that occurs once.")
    @MethodSource(METHOD_SOURCE)
    void testExamplePattern(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(combinations);
        List<String> results = search.findMatchingWords(TEST_PATTERN);
        assertIterableEquals(List.of(TEST_PATTERN), results);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for a three-letter prefix.")
    @MethodSource(METHOD_SOURCE)
    void testPrefix(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(combinations);
        List<String> results = search.findMatchingWords(TEST_PREFIX);
        assertEquals(26, results.size());
        results.forEach(it -> it.startsWith(TEST_PREFIX));
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test dictionary with empty word.")
    @MethodSource(METHOD_SOURCE)
    void testEmptyString(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(List.of(EMPTY_WORD));
        List<String> results = search.findMatchingWords(EMPTY_WORD);
        assertEquals(1, results.size());
        assertIterableEquals(List.of(EMPTY_WORD), results);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for an empty pattern.")
    @MethodSource(METHOD_SOURCE)
    void testEmptyPattern(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(combinations);
        List<String> results = new ArrayList<>(search.findMatchingWords(EMPTY_WORD));
        Collections.sort(results);
        assertEquals(combinationsUnsorted.size(), results.size());
        assertIterableEquals(combinationsUnsorted, results);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for null.")
    @MethodSource(METHOD_SOURCE)
    void testNullPattern(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(combinations);
        assertThrowsExactly(IllegalArgumentException.class, () -> search.findMatchingWords(null));

    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test empty dictionary.")
    @MethodSource(METHOD_SOURCE)
    void testEmptyDictionary(String name, DictionarySupplier supplier) {
        SearchableDictionary search = supplier.create(Collections.emptyList());
        List<String> results = search.findMatchingWords(TEST_PATTERN);
        assertIterableEquals(List.of(), results);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Test null-valued dictionary.")
    @MethodSource(METHOD_SOURCE)
    void testNullDictionary(String name, DictionarySupplier supplier) {
        assertThrowsExactly(IllegalArgumentException.class, () -> supplier.create(null));
    }

    // @Disabled("Not part of the specified behavior.")
    @ParameterizedTest(name = "{0}")
    @DisplayName("Test searching for a word that occurs more than once.")
    @MethodSource(METHOD_SOURCE)
    void testDuplicateWord(String name, DictionarySupplier supplier) {
        combinations.add(TEST_PATTERN);
        combinations.add(TEST_PATTERN); // add two more times to ensure it lands in same bucket
        SearchableDictionary search = supplier.create(combinations);
        List<String> results = search.findMatchingWords(TEST_PATTERN);
        assertIterableEquals(List.of(TEST_PATTERN, TEST_PATTERN, TEST_PATTERN), results);
    }
}
