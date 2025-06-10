package io.github.tsaglam.wordsearch;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Performance benchmark for the parallel word search. Does not contain classical unit tests.
 */
class PerformanceBenchmarkTest {
    private static final String METHOD_SOURCE = "io.github.tsaglam.wordsearch.TestUtils#provideDictionaryConstructors";
    private static final int BENCHMARK_REPETITIONS = 5;
    private static final String CSV_HEADER = "name;time;size";
    private static List<String> combinations;
    private static List<String> testPrefixes;

    @BeforeAll
    static void setUpClass() {
        combinations = new ArrayList<>();
        for (char first = 'A'; first <= 'Z'; first++) {
            for (char second = 'A'; second <= 'Z'; second++) {
                for (char third = 'A'; third <= 'Z'; third++) {
                    for (char fourth = 'A'; fourth <= 'Z'; fourth++) {
                        combinations.add("" + first + second + third + fourth); // 456,976 words
                    }
                }
            }
        }
        Collections.shuffle(combinations);
        testPrefixes = createTestPrefixes();
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Search 25x performance.")
    @MethodSource(METHOD_SOURCE)
    void testPrefixSearch(String name, DictionarySupplier supplier) {
        SearchableDictionary testDictionary = supplier.create(combinations);
        double durationInSeconds = measure(() -> {
            for (String prefix : testPrefixes) {
                testDictionary.findMatchingWords(prefix);
            }
        });
        durationInSeconds /= testPrefixes.size();
        System.out.println("search 25x in " + name + ": " + String.format("%.6f", durationInSeconds) + "s");
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Datastructure creation performance.")
    @MethodSource(METHOD_SOURCE)
    void testDataStructureCreation(String name, DictionarySupplier supplier) {
        double durationInSeconds = measure(() -> supplier.create(combinations));
        System.out.println("creation of " + name + ": " + durationInSeconds + "s");
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Datastructure creation + search performance.")
    @MethodSource(METHOD_SOURCE)
    void testBoth(String name, DictionarySupplier supplier) throws InterruptedException {
        List<String> csvLines = new ArrayList<>();
        csvLines.add(CSV_HEADER);
        suggestGarbageCollection();

        for (int numberOfSearches : List.of(3000, 2000, 1000, 500, 200, 100, 10, 1)) {
            List<String> inputs = generateInputs(numberOfSearches);
            double durationInSeconds = measure(() -> {
                for (int i = 0; i < BENCHMARK_REPETITIONS; i++) {
                    SearchableDictionary dictionary = supplier.create(combinations);
                    inputs.parallelStream().forEach(prefix -> dictionary.findMatchingWords(prefix));
                }
            });
            durationInSeconds = durationInSeconds / Double.valueOf(BENCHMARK_REPETITIONS);
            System.out.println(name + " " + numberOfSearches + ": " + durationInSeconds + "s");
            csvLines.add(name + ";" + durationInSeconds + ";" + numberOfSearches);
        }
        writeCsvFile("both", name, csvLines);

        suggestGarbageCollection();
    }

    private List<String> generateInputs(int numberOfSearches) {
        return Stream.concat( //
                IntStream.range(0, numberOfSearches / testPrefixes.size()).mapToObj(i -> testPrefixes).flatMap(List::stream),
                testPrefixes.stream().limit(numberOfSearches % testPrefixes.size())).toList();
    }

    private void suggestGarbageCollection() throws InterruptedException {
        System.gc();
        Thread.sleep(100);
    }

    private double measure(Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000.0;
    }

    private static List<String> createTestPrefixes() {
        List<String> testPrefixes = new ArrayList<>();
        for (char letter = 'A'; letter < 'Z'; letter++) {
            testPrefixes.add(String.valueOf(letter).repeat(3));
        }
        return testPrefixes;
    }

    private static void writeCsvFile(String test, String approach, List<String> content) {
        try {
            Path output = Path.of("plots", "input", test + "-" + approach + ".csv");
            output.getParent().toFile().mkdirs();
            Files.write(output, content);
        } catch (IOException exception) {
            exception.printStackTrace();
            fail();
        }
    }

}
