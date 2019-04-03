package edu.search.fileparser;

import edu.search.vo.Statistics;
import edu.search.vo.WordCount;
import edu.search.vo.WordInFileCount;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileParser {

    /**
     * Collect word count related statistics of txt files contained in a folder
     * @param documentFolder
     * @return
     * @throws IOException
     */
    public Map<String, Set<WordInFileCount>> parseFiles(File documentFolder) {
        File[] textFiles = documentFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".txt");
            }
        });
        if (textFiles != null) {
            return Arrays.stream(textFiles)
                    .map(f -> parseFile(f))
                    .flatMap(m -> m.entrySet().stream())
                    .collect(Collectors.toMap(e -> e.getKey() ,
                            e -> new TreeSet<>(Arrays.asList(e.getValue())),
                            (s1,s2) -> {s1.addAll(s2); return s1;}));

        } else {
            return new HashMap<>(0);
        }
    }

    /**
     * collect word counts of a file
     * @param textFile
     * @return
     */
    Map<String, WordInFileCount> parseFile(File textFile) {

        try (Stream<String> lineStream = Files.lines(Paths.get(textFile.getAbsolutePath())))
        {
            return lineStream
                    .map(l -> parseLine(l))
                    .flatMap(m -> m.entrySet().stream())
                    .collect(Collectors.toMap(e -> e.getKey() ,
                            e -> new WordInFileCount(textFile.getName(), e.getValue()) ,
                            (c1,c2) -> c1.incrementCount(c2)));
        } catch (IOException e) {
            //TODO: log and proper exception handling
            System.out.println("Exception while processing text file " + textFile.getName());
            e.printStackTrace();
            return new HashMap<>(0);
        }

    }

    /**
     * Collect word counts of a line within a file
     * @param line
     * @return
     */
    Map<String, Integer> parseLine(String line) {
        Map<String, Integer> wordCounts = Stream.of(line.split(" "))
                .map(w -> cleanText(w))
                .flatMap(list -> list.stream())
                .filter(w -> w.length() > 0)
                .map(w -> new WordCount(w , 1))
                .collect(Collectors.toMap(wc -> wc.getWord() , wc -> wc.getCount() , (count1, count2) -> count1 + count2));
        return wordCounts;
    }

    private List<String> cleanText(String w) {
        return Arrays.asList(w.replaceAll("[^a-zA-Z ]", " ").toLowerCase().trim().split(" "));
    }


}
