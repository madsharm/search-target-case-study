package edu.search.app.search;

import java.util.Optional;

public class Search {

    public Search() {

    }

    public static void main(String[] args) {
        String statisticsLocation = Optional.ofNullable(args).map(a -> a[0])
                .orElseThrow(() -> new IllegalArgumentException("Provide file where statistics are stored"));

        //Read statistics and load in the data structure

    }
}
