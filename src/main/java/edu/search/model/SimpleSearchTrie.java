package edu.search.model;

import edu.search.vo.WordInFileCount;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Trie based data structure to store keys to facilitate prefix based searches
 * Works well for string match as well as prefix matches
 * Unoptimized implementation of suffix and substring matches
 */
public class SimpleSearchTrie implements SimpleSearchDataModel {

    protected PatriciaTrie<Set<WordInFileCount>> searchTrie;
    private static SimpleSearchTrie instance = new SimpleSearchTrie();

    SimpleSearchTrie() {

    }

    public static SimpleSearchDataModel getInstance() {
        return instance;
    }

    @Override
    public synchronized void loadData(Map<String, Set<WordInFileCount>> data) {
        Objects.requireNonNull(data);
        //create prefix tree

        searchTrie = new PatriciaTrie<>();
        data.entrySet().stream().forEach(e -> {
            searchTrie.put(e.getKey(), e.getValue());
        });
    }


    /**
     * exact string match
     * @param searchTerm
     * @return
     */
    public Map<String,Set<WordInFileCount>> stringMatch(String searchTerm) {
        Map<String,Set<WordInFileCount>> searchResult = new HashMap<>(1);
        searchResult.put(searchTerm, searchTrie.get(searchTerm));
        return searchResult;
    }

    /**
     * Regex based match
     * @param searchTerm
     * @return
     */
    public Map<String,Set<WordInFileCount>> prefixMatch(String searchTerm) {
        return searchTrie.prefixMap(searchTerm);
    }

    /**
     * Regex based match
     * @param searchTerm
     * @return
     */
    public Map<String,Set<WordInFileCount>> suffixMatch(String searchTerm) {
        Set<String> matchingKeys = searchTrie.keySet().stream().filter(k -> k.endsWith(searchTerm)).collect(Collectors.toSet());
        Map<String,Set<WordInFileCount>> results = new HashMap<>();
        matchingKeys.stream().forEach(k -> {
            results.put(k, searchTrie.get(k));
        });
        return results;
    }

    /**
     * sub string match
     * @param searchTerm
     * @return
     */
    public Map<String,Set<WordInFileCount>> substringMatch(String searchTerm) {
        Set<String> matchingKeys = searchTrie.keySet().stream().filter(k -> k.contains(searchTerm)).collect(Collectors.toSet());
        Map<String,Set<WordInFileCount>> results = new HashMap<>();
        matchingKeys.stream().forEach(k -> {
            results.put(k, searchTrie.get(k));
        });
        return results;
    }


}
