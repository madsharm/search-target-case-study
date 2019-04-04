package edu.search.perf;

import edu.search.app.search.Search;
import edu.search.engine.SearcherFactory;
import edu.search.vo.TimedSearchResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SearchPerformanceTest {

    public static void main(String[] args) throws IOException {

        Search search = new Search();
        search.initSearchEngine(args);
        List<String> testData = loadTestData(search, args);

        int numSearches = 2000000;
        compareSearchPerformance(search, testData, numSearches,"regex");
        compareSearchPerformance(search, testData, numSearches,"exact string");

    }

    public static void compareSearchPerformance(Search search, List<String> testData, int numSearches, String type) {
        double totalTimeWithIndexedSearch = 0;
        double totalTimeWithSimpleSearch = 0;

        //simple and indexed search with regex match comparison
        for(int i =0 ; i< numSearches ; i++ ) {
            String term = generateRandomData(testData, type);
            TimedSearchResult indexedSearch = search.search(term, SearcherFactory.MODE.INDEXED);
            TimedSearchResult simpleSearch = search.search(term, SearcherFactory.MODE.SIMPLE);
            Assert.assertEquals(indexedSearch.getResult().size() , simpleSearch.getResult().size());
            totalTimeWithIndexedSearch += indexedSearch.getTimeElapsedInSearch();
            totalTimeWithSimpleSearch += simpleSearch.getTimeElapsedInSearch();

        }

        System.out.println("Average Time for "+ type +" search using Simple Search (ns) = " + (totalTimeWithSimpleSearch/numSearches));
        System.out.println("Average Time for "+ type +" search using Indexed Search (ns) = " + (totalTimeWithIndexedSearch/numSearches));
    }


    static String generateRandomData(List<String> keys , String type) {


        if (type.equals("regex")) {
            Random random = new Random();
            String randomKey = null;
            do {
                int randomKeyPos = random.nextInt(keys.size());
                randomKey = keys.get(randomKeyPos);
            } while (randomKey== null || randomKey.length() < 3);

            // Do we want prefix/suffix/substring match?
            int randomMatchType = random.nextInt(3);
            if(randomMatchType == 0) {
                //prefix
                return randomKey.substring(0, random.nextInt(randomKey.length()-2)+1) + "*";
            } else if(randomMatchType == 1) {
                //suffix
                return "*" + randomKey.substring(random.nextInt(randomKey.length()-2)+1 , randomKey.length());
            } else {
                return "*" + RandomStringUtils.randomAlphabetic(random.nextInt(5)+1).toLowerCase() + "*";
            }
        } else {
            //exact
            Random random = new Random();
            String randomKey = null;
            do {
                int randomKeyPos = random.nextInt(keys.size());
                randomKey = keys.get(randomKeyPos);
            } while (randomKey== null || randomKey.length() < 2);
            return randomKey;
        }


    }

    static List<String> loadTestData(Search search, String[] args) throws IOException {
        Set<String> keys = search.loadStatisticsData(args).keySet();
        return new ArrayList<>(keys);

    }




}
