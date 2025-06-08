package io.github.tsaglam.wordsearch;

import java.util.List;

/**
 * Functional interface for testing, allows to encapsulate the constructor call to any {@link SearchableDictionary}.
 */
@FunctionalInterface
public interface DictionarySupplier {
    SearchableDictionary create(List<String> words);
}
