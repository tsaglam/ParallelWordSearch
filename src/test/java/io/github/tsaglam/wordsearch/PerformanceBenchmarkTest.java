package io.github.tsaglam.wordsearch;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private static final int BENCHMARK_REPETITIONS = 10;
    private static final String CSV_HEADER = "name;time;size";
    private static List<String> combinations;
    private static List<String> testPrefixes;

    @BeforeAll
    static void setUpClass() {
        combinations = TestUtils.createTestData();
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
        assertTrue(durationInSeconds < 0.02);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Data structure creation performance.")
    @MethodSource(METHOD_SOURCE)
    void testDataStructureCreation(String name, DictionarySupplier supplier) {
        double durationInSeconds = measure(() -> supplier.create(combinations));
        System.out.println("creation of " + name + ": " + durationInSeconds + "s");
        assertTrue(durationInSeconds < 0.5);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("Datastructure creation + search performance.")
    @MethodSource(METHOD_SOURCE)
    void testBoth(String name, DictionarySupplier supplier) {
        List<String> csvLines = new ArrayList<>();
        csvLines.add(CSV_HEADER);

        for (int numberOfSearches : List.of(3000, 2000, 1000, 500, 200, 100, 10, 1)) {
            List<String> inputs = generateInputs(numberOfSearches);
            double durationInSeconds = measure(() -> {
                for (int i = 0; i < BENCHMARK_REPETITIONS; i++) {
                    SearchableDictionary dictionary = supplier.create(combinations);
                    inputs.stream().forEach(prefix -> dictionary.findMatchingWords(prefix));
                }
            });
            durationInSeconds = durationInSeconds / Double.valueOf(BENCHMARK_REPETITIONS);
            System.out.println(name + " " + numberOfSearches + ": " + durationInSeconds + "s");
            csvLines.add(name + ";" + durationInSeconds + ";" + numberOfSearches);
        }
        writeCsvFile("both", name, csvLines);
    }

    /**
     * Measures the runtime of any given runnable.
     */
    private double measure(Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000.0;
    }

    /**
     * Creates n search inputs based on {@link PerformanceBenchmarkTest#createTestPrefixes()}.
     */
    private List<String> generateInputs(int numberOfSearches) {
        return Stream.concat( //
                IntStream.range(0, numberOfSearches / testPrefixes.size()).mapToObj(i -> testPrefixes).flatMap(List::stream),
                testPrefixes.stream().limit(numberOfSearches % testPrefixes.size())).toList();
    }

    /**
     * Creates 25 three letter search inputs form AAA to YYY.
     */
    private static List<String> createTestPrefixes() {
        List<String> testPrefixes = new ArrayList<>();
        for (char letter = 'A'; letter < 'Z'; letter++) {
            testPrefixes.add(String.valueOf(letter).repeat(3));
        }
        return testPrefixes;
    }

    /**
     * Writes results in a CSV file for plotting.
     */
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
