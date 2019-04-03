package edu.search.vo;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Statistics {
    private String word;

    private List<WordInFileCount> wordCounts = new ArrayList<>(10);

    public Statistics(String word) {
        this.word = word;
    }

    public Statistics(String word, WordInFileCount wordCount) {
        this.word = word;
        this.wordCounts.add(wordCount);
    }

    @JsonGetter("word")
    public String getWord() {
        return word;
    }

    @JsonGetter("count")
    public List<WordInFileCount> getWordCounts() {
        return wordCounts;
    }

    public Statistics addWordInFileCount(WordInFileCount wordCount) {
        this.wordCounts.add(wordCount);
        return this;
    }
    public Statistics addWordInFileCount(List<WordInFileCount> wordCount) {
        this.wordCounts.addAll(wordCount);
        return this;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("," , "[" , "]");
        wordCounts.stream().forEach(wc -> joiner.add(wc.toString()));
        return joiner.toString();
    }
}
