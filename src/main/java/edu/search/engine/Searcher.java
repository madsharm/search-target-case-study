package edu.search.engine;

import edu.search.vo.SearchResult;
import edu.search.vo.TimedSearchResult;

public interface Searcher {


    /**
     * exact string match and regex match
     * @param searchTerm
     * @return
     */
    SearchResult search(String searchTerm);

    TimedSearchResult timedSearch(String searchTerm);

}
