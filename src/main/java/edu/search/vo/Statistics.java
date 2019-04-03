package edu.search.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

public class Statistics {
    private String word;

    private Set<WordInFileCount> wordCounts = new TreeSet();

    public Statistics(String word) {
        this.word = word;
    }

    public Statistics(String word, WordInFileCount wordCount) {
        this.word = word;
        this.wordCounts.add(wordCount);
    }

    @JsonIgnore
    public String getWord() {
        return word;
    }

    @JsonGetter("count")
    @JsonDeserialize(as=TreeSet.class)
    @JsonSerialize(as=TreeSet.class)
    public Set<WordInFileCount> getWordCounts() {
        return wordCounts;
    }

    public Statistics addWordInFileCount(WordInFileCount wordCount) {
        this.wordCounts.add(wordCount);
        return this;
    }
    public Statistics addWordInFileCount(Set<WordInFileCount> wordCount) {
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
