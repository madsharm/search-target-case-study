package edu.search.vo;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Map;
import java.util.Set;

public class SearchResult {

    private Map<String, Set<WordInFileCount>> result;

    public SearchResult(Map<String, Set<WordInFileCount>> result) {
        this.result = result;
    }

    @JsonGetter(value = "searchResults")
    public Map<String, Set<WordInFileCount>> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return result.toString();
    }
}
