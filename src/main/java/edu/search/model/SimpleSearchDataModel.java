package edu.search.model;

import edu.search.vo.WordInFileCount;

import java.util.Map;
import java.util.Set;

public interface SimpleSearchDataModel {

    /**
     * Return searches for given search term
     * @param searchTerm term
     * @return search results in descending order of relevancy
     */
    default Map<String,Set<WordInFileCount>>  getSearches(String searchTerm) {
        if(searchTerm.contains("*")) {
            if(searchTerm.startsWith("*") && searchTerm.endsWith("*")) {
                return substringMatch(searchTerm.replace("*",""));
            } else if(searchTerm.startsWith("*")) {
                return suffixMatch(searchTerm.replace("*",""));

            } else if(searchTerm.endsWith("*")) {
                return prefixMatch(searchTerm.replace("*",""));

            }

            throw new UnsupportedOperationException("Only prefix, suffix and substring matches are supported - ab*/*ab/*ab*");

        } else {
            return stringMatch(searchTerm);
        }
    }


    /**
     * exact string match
     * @param searchTerm
     * @return
     */
    Map<String,Set<WordInFileCount>>  stringMatch(String searchTerm);

    /**
     * Regex based match
     * @param searchTerm
     * @return
     */
    Map<String,Set<WordInFileCount>> prefixMatch(String searchTerm);

    /**
     * Regex based match
     * @param searchTerm
     * @return
     */
    Map<String,Set<WordInFileCount>> suffixMatch(String searchTerm);


    /**
     * Regex based match
     * @param searchTerm
     * @return
     */
    Map<String,Set<WordInFileCount>> substringMatch(String searchTerm);

    /**
     * Load data in the model for further searches
     * @param data
     */
    void loadData(Map<String, Set<WordInFileCount>> data);

}
