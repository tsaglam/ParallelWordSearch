# Parallel Word Search

A suite of multithreaded implementations of efficient prefix-based search in large sets of strings. The project explores and benchmarks various sequential and parallel implementations, with a highlight on the highly concurrent [ParallelPrefixTree](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/main/java/io/github/tsaglam/wordsearch/tree/ParallelPrefixTree.java) Trie data structure. All approaches are ready-to-benchmark can be compared via JUnit tests.

## Implementations

This project includes the following `SearchableDictionary` implementations:

- **[NaiveWordSearch](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/main/java/io/github/tsaglam/wordsearch/impl/NaiveWordSearch.java)**: Naive sequential search via a stream.
- **[ParallelStreamWordSearch](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/main/java/io/github/tsaglam/wordsearch/impl/ParallelStreamWordSearch.java)**: Simple search using parallel streams.
- **[TreeSetWordSearch](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/main/java/io/github/tsaglam/wordsearch/impl/TreeSetWordSearch.java)**: Sequential search using a sorted set (TreeSet).
- **[MultiTreeSetWordSearch](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/main/java/io/github/tsaglam/wordsearch/impl/MultiTreeSetWordSearch.java)**: Parallelized search using a forest of TreeSets.
- **[ParallelHashingTreeSearch](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/main/java/io/github/tsaglam/wordsearch/impl/ParallelHashingTreeSearch.java)**: Parallel search using hash-based prefix indexing.
- **[ParallelPrefixTree](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/main/java/io/github/tsaglam/wordsearch/tree/ParallelPrefixTree.java)**: A thread-safe parallel Trie (highlight).
- **[ParallelPrefixForest](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/main/java/io/github/tsaglam/wordsearch/tree/ParallelPrefixForest.java)**: A forest of parallel Tries for further parallelism.

## Testing

There are two main types of unit test classes in this project:

- **Functional Tests ([SearchableDictionaryTest](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/test/java/io/github/tsaglam/wordsearch/SearchableDictionaryTest.java))**: These tests verify that each implementation correctly implements the specified behavior and edge cases.
- **Performance Benchmark ([PerformanceBenchmarkTest](https://github.com/tsaglam/ParallelWordSearch/blob/main/src/test/java/io/github/tsaglam/wordsearch/PerformanceBenchmarkTest.java))**: These tests measure the performance of different implementations for both word search and data structure construction. The combined benchmark outputs CSV data, which can be used to generate plots.

## CI

The CI workflow includes the build and continuous benchmarking:

- Running Java maven build.
- Running the functional tests and the performance benchmark.
- Running the plotting scripts.
- Attaching the plotted PDF files as artifacts.

## Usage

### Build and Test

This project uses Maven. To build and run all tests and benchmarks:

```sh
mvn clean test
```

To apply auto formatting:

```sh
mvn spotless:apply
```

### Plotting

For the benchmark tests, optional plotting is available. For this, you need Rlang installed and optionally RStudio. To plot the CSV files in the `plots/input` directory, run `plots/main.R`.

### Requirements

- Java 21+
- Maven 3.x
- Rlang, e.g., 4.5.x (for plotting; optional but required if you want to generate plots)
