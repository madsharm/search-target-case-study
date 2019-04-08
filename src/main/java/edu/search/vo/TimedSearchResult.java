package edu.search.vo;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.Map;
import java.util.Set;

public class TimedSearchResult extends SearchResult {

    private long timeElapsedInSearch;

    public TimedSearchResult(Map<String, Set<WordInFileCount>> searches, long timeElapsed) {
        super(searches);
        timeElapsedInSearch = timeElapsed;
    }

    @JsonGetter(value = "timeElapsedInNanoSec")
    public long getTimeElapsedInSearch() {
        return timeElapsedInSearch;
    }

    @Override
    public String toString() {
        return "Result in (ns) " + timeElapsedInSearch + " " + super.toString();
    }
}
