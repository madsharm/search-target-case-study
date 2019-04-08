package edu.search.rs;

public class SearchRequest {
    String mode;
    String term;

    public SearchRequest() {
    }

    public SearchRequest(String mode, String term) {
        this.mode = mode;
        this.term = term;
    }

    public String getMode() {
        return mode;
    }

    public String getTerm() {
        return term;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
