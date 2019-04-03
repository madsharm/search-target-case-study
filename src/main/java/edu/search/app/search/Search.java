package edu.search.app.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.search.fileparser.StatisticsDeserializer;
import edu.search.model.IndexedSearchDataModel;
import edu.search.model.IndexedSearchTrie;
import edu.search.model.SimpleSearchDataModel;
import edu.search.model.SimpleSearchTrie;
import edu.search.vo.WordInFileCount;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

public class Search {

    public Search() {

    }

    public static void main(String[] args) throws IOException {

        Map<String, Set<WordInFileCount>> statistics = loadStatisticsData(args);
        initializeSearchDataModel(statistics);
        promptUser();


    }

    private static void promptUser() {
        
    }

    private static Map<String, Set<WordInFileCount>> loadStatisticsData(String[] args) throws IOException {
        String statisticsLocation = null;
        if (args.length > 0) {
            statisticsLocation = args[0];
        } else {
            ClassLoader classLoader = Search.class.getClassLoader();
            File defaultFile = new File(classLoader.getResource("statistics.json").getFile());
            statisticsLocation = defaultFile.getAbsolutePath();
        }

        //Read statistics and load in the data structure
        byte[] bytes = Files.readAllBytes(Paths.get(statisticsLocation));
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Map.class, new StatisticsDeserializer());
        objectMapper.registerModule(module);
        return objectMapper.readValue(bytes, Map.class);
    }

    private static void initializeSearchDataModel(Map<String, Set<WordInFileCount>> statistics) {
        //load both simple and indexed data structures during init and then we can compare performances of both
        SimpleSearchDataModel trie = SimpleSearchTrie.getInstance();
        trie.loadData(statistics);
        IndexedSearchDataModel indexedTrie = IndexedSearchTrie.getInstance();
        indexedTrie.loadData(statistics);
    }


}

