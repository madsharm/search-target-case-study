package edu.search.engine;

import edu.search.model.IndexedSearchTrie;
import edu.search.model.SimpleSearchTrie;

public class SearcherFactory {

    private static SearcherFactory instance = new SearcherFactory();

    public enum MODE {SIMPLE, INDEXED}

    public static SearcherFactory getInstance() {
        return instance;
    }

    public Searcher getSearcher(MODE mode) {
        if ( mode.equals(MODE.SIMPLE)) {
            return new SearcherImpl(SimpleSearchTrie.getInstance());
        } if ( mode.equals(MODE.INDEXED)) {
            return new SearcherImpl(IndexedSearchTrie.getInstance());
        } else {
            throw new UnsupportedOperationException("Unsupported search mode");
        }
    }


}
