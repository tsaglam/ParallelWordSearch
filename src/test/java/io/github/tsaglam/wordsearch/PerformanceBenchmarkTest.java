package io.github.tsaglam.wordsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.tsaglam.wordsearch.impl.ForestWordSearch;
import io.github.tsaglam.wordsearch.impl.NaiveWordSearch;
import io.github.tsaglam.wordsearch.impl.ParallelStreamWordSearch;
import io.github.tsaglam.wordsearch.impl.TreeSetWordSearch;

/**
 * Performance benchmark for the parallel word search. Does not contain classical unit tests.
 */
public class PerformanceBenchmarkTest {

    private static List<String> combinations;
    private static List<String> testPrefixes;

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
        testPrefixes = createTestPrefixes();
    }

    static Stream<Arguments> provideDictionaries() {
        return Stream.of(Arguments.of("Naive", new NaiveWordSearch(combinations)),
                Arguments.of("ParallelStream", new ParallelStreamWordSearch(combinations)),
                Arguments.of("TreeSet", new TreeSetWordSearch(combinations)), Arguments.of("ForestBased", new ForestWordSearch(combinations)));
    }

    static Stream<Arguments> provideDictionaryConstructors() {
        return Stream.of(Arguments.of("Naive", (DictionarySupplier) NaiveWordSearch::new),
                Arguments.of("ParallelStream", (DictionarySupplier) ParallelStreamWordSearch::new),
                Arguments.of("TreeSet", (DictionarySupplier) TreeSetWordSearch::new),
                Arguments.of("ForestBased", (DictionarySupplier) ForestWordSearch::new));
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Search performance with a four-letter prefix.")
    @MethodSource("provideDictionaries")
    void testPrefixSearch(String name, SearchableDictionary testDictionary) {
        double durationInSeconds = measure(() -> {
            for (String prefix : testPrefixes) {
                testDictionary.findMatchingPrefixes(prefix);
            }
        });
        durationInSeconds /= testPrefixes.size();
        System.out.println(name + ": " + String.format("%.6f", durationInSeconds) + "s");
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Datastructure creation performance.")
    @MethodSource("provideDictionaryConstructors")
    void testDataStructureCreation(String name, DictionarySupplier supplier) {
        double durationInSeconds = measure(() -> supplier.create(combinations));
        System.out.println(name + ": " + durationInSeconds + "s");
    }

    private double measure(Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000.0;
    }

    private static List<String> createTestPrefixes() {
        List<String> testPrefixes = new ArrayList<>();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            testPrefixes.add(String.valueOf(letter).repeat(4));
        }
        return testPrefixes;
    }

}
