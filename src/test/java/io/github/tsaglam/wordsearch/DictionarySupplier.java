package io.github.tsaglam.wordsearch;

import java.util.List;

/**
 * Functional interface for testing, allows to encapsulate the constructor call to any {@link SearchableDictionary}.
 */
@FunctionalInterface
public interface DictionarySupplier {

    /**
     * Creates a instance of {@link SearchableDictionary} based on a list of words.
     * @param words is the word list.
     * @return the instance.
     */
    SearchableDictionary create(List<String> words);
}
