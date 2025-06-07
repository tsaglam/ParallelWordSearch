package io.github.tsaglam.parallelwordsearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Functional tests for the parallel word search.
 */
class SearchableDictionaryTest {

    private static final String TEST_PATTERN = "TEST";
    private static final String TEST_PREFIX = "TES";
    private List<String> combinations;

    @BeforeEach
    void setUp() {
        combinations = new ArrayList<>();
        for (char first = 'A'; first <= 'Z'; first++) {
            for (char second = 'A'; second <= 'Z'; second++) {
                for (char third = 'A'; third <= 'Z'; third++) {
                    for (char fourth = 'A'; fourth <= 'Z'; fourth++) {
                        combinations.add("" + first + second + third + fourth);
                    }
                }
            }
        }
        Collections.shuffle(combinations);
    }

    @Test
    @DisplayName("Test searching for a word that occurs once.")
    void testExamplePattern() {
        SearchableDictionary search = new NaiveWordSearch(combinations);
        List<String> results = search.findMatchingPrefixes(TEST_PATTERN);
        assertIterableEquals(List.of(TEST_PATTERN), results);
    }

    @Test
    @DisplayName("Test searching for a word that occurs twice.")
    void testDuplicateWord() {
        combinations.add(TEST_PATTERN);
        SearchableDictionary search = new NaiveWordSearch(combinations);
        List<String> results = search.findMatchingPrefixes(TEST_PATTERN);
        assertIterableEquals(List.of(TEST_PATTERN, TEST_PATTERN), results);
    }

    @Test
    @DisplayName("Test searching for a three-letter prefix.")
    void testPrefix() {
        SearchableDictionary search = new NaiveWordSearch(combinations);
        List<String> results = search.findMatchingPrefixes(TEST_PREFIX);
        assertEquals(26, results.size());
        results.forEach(it -> it.startsWith(TEST_PREFIX));
    }

    @Test
    @DisplayName("Test searching for an empty string.")
    void testEmptyPattern() {
        String pattern = "";
        SearchableDictionary search = new NaiveWordSearch(combinations);
        List<String> results = search.findMatchingPrefixes(pattern);
        assertIterableEquals(combinations, results);
    }

    @Test
    @DisplayName("Test searching for null.")
    void testNullPattern() {
        SearchableDictionary search = new NaiveWordSearch(combinations);
        assertThrowsExactly(IllegalArgumentException.class, () -> search.findMatchingPrefixes(null));

    }

    @Test
    @DisplayName("Test empty dictionary.")
    void testEmptyDictionary() {
        SearchableDictionary search = new NaiveWordSearch(Collections.emptyList());
        List<String> results = search.findMatchingPrefixes(TEST_PATTERN);
        assertIterableEquals(List.of(), results);
    }

    @Test
    @DisplayName("Test null-valued dictionary.")
    void testNullDictionary() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new NaiveWordSearch(null));
    }

}
