package edu.search.app.statistics;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.search.fileparser.FileParser;
import edu.search.vo.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class StatisticsComputer {

    /**
     * Tool to be run before hand to compute statistics related to word count
     * It reads files under document folder and stores statistics in a file
     * for further consumption with search component.
     *
     * @param args args[0] accepts absolute file path where statistics needs to be stored. If not provided,
     *             stats are stored in statistics.json in the same folder where text files are present.
     */
    public static void main(String[] args) throws IOException {

        FileParser parser = new FileParser();
        ClassLoader classLoader = FileParser.class.getClassLoader();
        File documentFolder = new File(classLoader.getResource("documents").getFile());
        Map<String, Statistics> statistics = parser.parseFiles(documentFolder);

        String statsLocation = Optional.ofNullable(args).map(a -> a[0]).orElse(documentFolder.getAbsolutePath()+"/statistics.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(statsLocation) , new StatisticsHolder(statistics));

    }

    static class StatisticsHolder
    {
        Map<String, Statistics> statistics;

        public StatisticsHolder(Map<String, Statistics> statistics) {
            this.statistics = statistics;
        }

        @JsonGetter("statistics")
        public Map<String, Statistics> getStatistics() {
            return statistics;
        }
    }
}
