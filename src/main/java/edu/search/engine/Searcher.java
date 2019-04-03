package edu.search.engine;

import edu.search.vo.SearchResult;
import edu.search.vo.TimedSearchResult;
import edu.search.vo.WordInFileCount;

import java.util.Map;
import java.util.Set;

public interface Searcher {


    /**
     * exact string match and regex match
     * @param searchTerm
     * @return
     */
    SearchResult search(String searchTerm);

    TimedSearchResult timedSearch(String searchTerm);

}
