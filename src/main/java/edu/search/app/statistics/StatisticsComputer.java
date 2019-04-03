package edu.search.app.statistics;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import edu.search.fileparser.FileParser;
import edu.search.vo.Statistics;
import edu.search.vo.WordInFileCount;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        Map<String, Set<WordInFileCount>> statistics = parser.parseFiles(documentFolder);

        String statsLocation = Optional.ofNullable(args).map(a -> a[0]).orElse(documentFolder.getAbsolutePath()+"/statistics.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(statsLocation) , statistics);

    }


}
