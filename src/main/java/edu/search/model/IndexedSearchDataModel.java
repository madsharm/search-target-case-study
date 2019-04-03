package edu.search.model;

import edu.search.vo.WordInFileCount;

import java.util.Map;
import java.util.Set;

public interface IndexedSearchDataModel extends SimpleSearchDataModel {

    /**
     * Load data in the index for fast access
     * @param data
     */
    void loadIndexes(Map<String, Set<WordInFileCount>> data);

}
