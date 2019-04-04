package edu.search.app.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.search.engine.SearcherFactory;
import edu.search.fileparser.StatisticsDeserializer;
import edu.search.model.IndexedSearchDataModel;
import edu.search.model.IndexedSearchTrie;
import edu.search.model.SimpleSearchDataModel;
import edu.search.model.SimpleSearchTrie;
import edu.search.vo.TimedSearchResult;
import edu.search.vo.WordInFileCount;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Search {

    public Search() {

    }

    public static void main(String[] args) throws IOException {
        Search search = new Search();
        search.initSearchEngine(args);
        try (Scanner in = new Scanner(System.in)) {
            do {
            } while (search.promptUser(in));
        }


    }

    public void initSearchEngine(String[] args) throws IOException {
        Map<String, Set<WordInFileCount>> statistics = loadStatisticsData(args);
        initializeSearchDataModel(statistics);
    }

    private boolean promptUser(Scanner in) {

            String term = null;
            do {
                System.out.println("Enter the search term: ");
                term = in.nextLine();
            } while (term == null || term.trim().isEmpty());

            int choice = -1;
            do {
                System.out.println("Enter the search mode: 1)String/RegEx(ab*/*ab*/*ab)  2)Indexed Search ");
                choice = in.nextInt();
            } while (choice != 1 && choice != 2);

        TimedSearchResult result = search(term, SearcherFactory.MODE.getMode(choice));

        System.out.println("Search Results are : " + result.getResult());
            System.out.println("Time Elapsed in Search (ms) : " + (result.getTimeElapsedInSearch()/1000));

            return true;

    }

    public TimedSearchResult search(String term, SearcherFactory.MODE mode) {
        return SearcherFactory.getInstance().getSearcher(mode).timedSearch(term);
    }

    public Map<String, Set<WordInFileCount>> loadStatisticsData(String[] args) throws IOException {
        String statisticsLocation = null;
        if (args.length > 0) {
            statisticsLocation = args[0];
        } else {
            ClassLoader classLoader = Search.class.getClassLoader();
            File defaultFile = new File(classLoader.getResource("statistics.json").getFile());
            statisticsLocation = defaultFile.getAbsolutePath();
        }

        System.out.println("Loading statistics from file " + statisticsLocation);
        //Read statistics and load in the data structure
        byte[] bytes = Files.readAllBytes(Paths.get(statisticsLocation));
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Map.class, new StatisticsDeserializer());
        objectMapper.registerModule(module);
        return objectMapper.readValue(bytes, Map.class);
    }

    private void initializeSearchDataModel(Map<String, Set<WordInFileCount>> statistics) {
        //load both simple and indexed data structures during init and then we can compare performances of both
        SimpleSearchDataModel trie = SimpleSearchTrie.getInstance();
        trie.loadData(statistics);
        IndexedSearchDataModel indexedTrie = IndexedSearchTrie.getInstance();
        indexedTrie.loadData(statistics);
    }


}

