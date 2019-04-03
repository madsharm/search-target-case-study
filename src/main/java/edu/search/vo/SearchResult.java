package edu.search.vo;

import java.util.Map;
import java.util.Set;

public class SearchResult {

    private Map<String, Set<WordInFileCount>> result;

    public SearchResult(Map<String, Set<WordInFileCount>> result) {
        this.result = result;
    }

    public Map<String, Set<WordInFileCount>> getResult() {
        return result;
    }
}