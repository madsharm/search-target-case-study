package edu.search.vo;

import com.fasterxml.jackson.annotation.JsonGetter;

public class WordInFileCount implements Comparable<WordInFileCount>{

    private String fileName;
    private int count;

    public WordInFileCount(String fileName, int count) {
        this.fileName = fileName;
        this.count = count;
    }

    @JsonGetter("fileName")
    public String getFileName() {
        return fileName;
    }

    @JsonGetter("relevancy")
    public int getCount() {
        return count;
    }

    public WordInFileCount incrementCount(WordInFileCount c) {
        this.count = this.count + c.count;
        return this;
    }

    @Override
    public String toString() {
        return fileName + ":" + count;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof WordInFileCount)
                && (((WordInFileCount) obj).count == count
                && ((WordInFileCount) obj).fileName.equals(fileName));
    }

    @Override
    public int compareTo(WordInFileCount wc) {
        //ensure sorting happens in descending order of relevancy
        return wc.count - this.count;
    }

    @Override
    public int hashCode() {
        return fileName.hashCode() * count;
    }
}
