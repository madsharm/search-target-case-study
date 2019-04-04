package edu.search.engine;

import edu.search.model.SimpleSearchDataModel;
import edu.search.vo.SearchResult;
import edu.search.vo.TimedSearchResult;
import edu.search.vo.WordInFileCount;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

public class SearcherImpl implements Searcher {

    private SimpleSearchDataModel model;
    SearcherImpl(SimpleSearchDataModel model) {
        this.model = model;
    }

    public SearchResult search(String searchTerm) {
        return new SearchResult(model.getSearches(searchTerm));
    }

    public TimedSearchResult timedSearch(String searchTerm) {
        Instant start = Instant.now();
        Map<String, Set<WordInFileCount>> searches = model.getSearches(searchTerm);
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toNanos();
        return new TimedSearchResult(searches, timeElapsed);
    }

}
