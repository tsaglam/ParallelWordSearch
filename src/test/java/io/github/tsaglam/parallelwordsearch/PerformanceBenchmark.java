package io.github.tsaglam.parallelwordsearch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Performance benchmark for the parallel word search.
 */
class PerformanceBenchmark {

    private static final String TEST_PREFIX = "TEST";
    private static List<String> combinations;

    @BeforeAll
    static void setUpClass() {
        combinations = new ArrayList<>();
        for (char first = 'A'; first <= 'Z'; first++) {
            for (char second = 'A'; second <= 'Z'; second++) {
                for (char third = 'A'; third <= 'Z'; third++) {
                    for (char fourth = 'A'; fourth <= 'Z'; fourth++) {
                        for (char fifth = 'A'; fifth <= 'Z'; fifth++) {
                            combinations.add("" + first + second + third + fourth + fifth);
                        }
                    }
                }
            }
        }
        Collections.shuffle(combinations);
    }

    static Stream<SearchableDictionary> testDictionaries() {
        return Stream.of(new NaiveWordSearch(combinations), new ParallelStreamWordSearch(combinations));
    }

    @ParameterizedTest
    @MethodSource("testDictionaries")
    @DisplayName("Test performance for searching with a four-letter prefix.")
    void testPrefix(SearchableDictionary testDictionary) {

        long startTime = System.currentTimeMillis();
        List<String> results = testDictionary.findMatchingPrefixes(TEST_PREFIX);
        long endTime = System.currentTimeMillis();
        double durationInSeconds = (endTime - startTime) / 1000.0;

        System.out.println(testDictionary.getClass().getSimpleName() + ": " + durationInSeconds + "s");

        assertEquals(26, results.size());
        results.forEach(it -> it.startsWith(TEST_PREFIX));
    }

}
