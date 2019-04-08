package edu.search.engine;

import edu.search.model.IndexedSearchTrie;
import edu.search.model.SimpleSearchTrie;

public class SearcherFactory {

    private static SearcherFactory instance = new SearcherFactory();

    public enum MODE {
        SIMPLE(1), INDEXED(2);

        private final int choice;

        MODE(int choice) {
            this.choice = choice;
        }
        public static MODE getMode(int choice) {
            return choice == 1 ? SIMPLE : INDEXED;
        }
        public static MODE getMode(String mode) {
            return mode.equals(SIMPLE.toString().toLowerCase()) ? SIMPLE :
                    (mode.equals(INDEXED.toString().toLowerCase()) ? INDEXED :
                    null);
        }
    }

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
