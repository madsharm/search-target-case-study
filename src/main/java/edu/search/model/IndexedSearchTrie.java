package edu.search.model;

import edu.search.vo.WordInFileCount;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Trie based data structure to store keys to facilitate prefix based searches
 * Works well for string match as well as prefix matches
 * Uses additional indexes to optimize implementation of suffix and substring matches
 */
@Repository
@Qualifier("indexedSearchTrie")
public class IndexedSearchTrie extends SimpleSearchTrie implements IndexedSearchDataModel {

    private static IndexedSearchTrie instance = new IndexedSearchTrie();
    protected PatriciaTrie<String> suffixTreeIndex;
    protected PatriciaTrie<Set<String>> subStringPrefixTreeIndex;


    IndexedSearchTrie() {

    }

    public static IndexedSearchDataModel getInstance() {
        return instance;
    }


    @Override
    public Map<String, Set<WordInFileCount>> suffixMatch(String searchTerm) {
     // Query suffix tree index to get exact keys
     String reversedSearchTerm = reverse(searchTerm);
     Map<String, String> keys = suffixTreeIndex.prefixMap(reversedSearchTerm);

     //Query actual search trie
     Map<String, Set<WordInFileCount>> results = new HashMap<>();
     keys.entrySet().stream().forEach(e-> {
         results.put(e.getValue(), searchTrie.get(e.getValue()));
     });
     return results;
    }

    @Override
    public Map<String, Set<WordInFileCount>> substringMatch(String searchTerm) {
        Map<String, Set<WordInFileCount>> results = new HashMap<>();
        //Query prefix and suffix results
        results.putAll(prefixMatch(searchTerm));
        results.putAll(suffixMatch(searchTerm));

        //Query sub string index to get all applicable prefixes
        Map<String, Set<String>> prefixes = subStringPrefixTreeIndex.prefixMap(searchTerm);

        //Query actual search trie for substring matches
        prefixes.entrySet().stream().forEach(e-> {
            e.getValue().stream().forEach(p-> {
                results.putAll(prefixMatch(p));

            });
        });
        return results;
    }

    @Override
    public synchronized void loadData(Map<String, Set<WordInFileCount>> data) {
        super.loadData(data);
        loadIndexes(data);
    }

    @Override
    public synchronized void loadIndexes(Map<String, Set<WordInFileCount>> data) {
        suffixTreeIndex = new PatriciaTrie<>();
        data.entrySet().stream().forEach(e -> {
            suffixTreeIndex.put(reverse(e.getKey()), e.getKey());
        });

        subStringPrefixTreeIndex = new PatriciaTrie<>();
        data.entrySet().stream().forEach(e -> {
            Map<String,String> subStringPrefixes = allMidSubStrings(e.getKey());
            subStringPrefixes.entrySet().forEach(ne -> {
                Set<String> prefixSet = new HashSet<>(1);
                prefixSet.add(ne.getValue());
                subStringPrefixTreeIndex.merge(ne.getKey(), prefixSet, (v1, v2) -> {
                    v1.addAll(v2); return v1;
                });
            });
        });
    }

    private String reverse(String term) {
        return new StringBuilder(term).reverse().toString();
    }

    /**
     * Returns substring along with its prefix representation
     * Example : frog -> [r:fr , ro: fro, o: fro ]
     * @param term
     */
    private static Map<String,String> allMidSubStrings(String term) {
        Map<String,String> subStringPrefixes = new HashMap<>(term.length() * term.length()-1);
        for (int c = 1; c < term.length()-1; c++)
        {
            for(int i = 1; i >= 0 && i <= term.length() - 1 - c; i++)
            {
                String substring = term.substring(c, c + i);
                String prefix = term.substring(0, c + i);
                subStringPrefixes.put(substring, prefix);

            }

        }
        return subStringPrefixes;
    }
}
