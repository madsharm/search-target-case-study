package edu.search.vo;

import java.util.Map;
import java.util.Set;

public class TimedSearchResult extends SearchResult {

    private long timeElapsedInSearch;

    public TimedSearchResult(Map<String, Set<WordInFileCount>> searches, long timeElapsed) {
        super(searches);
        timeElapsedInSearch = timeElapsed;
    }

    public long getTimeElapsedInSearch() {
        return timeElapsedInSearch;
    }
}
