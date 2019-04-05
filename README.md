# Case Study for Search Usecase

# Problem Statement
The goal of this exercise is to create a command line based program to search a set of documents (present under src/resources) for the given search term or phrase (single token), and return results in order of relevance.
Relevancy is defined as number of times the exact term or phrase appears in the document.
Create three methods for searching the documents:
- Simple string matching
- Text search using regular expressions
- Preprocess the content and then search the index

# Assumptions
* Case insensitive search
* Only text keys considered, not numbers and punctuations
* Support string match (freebird), prefix match(free*), suffix match(*bird), substring match(\*eebi\*)

# How to run using Gradle
The program works in two steps:
* Optional step: Parse the document files and create statistics file containing stats about each word - how many times it appears in each file in desc order of relevancy.
To run parsing utility:
**gradlew runStatisticsComputer  --args="D:\stats_v1.json"**
[where argument represents where to store the statistics file]

   Note: You can skip this step, since statistics file is already checked in the source control

* Run search engine
To run:
**gradlew --console=plain run --args="D:\stats_v1.json"**
[where argument represents where to load the statistics file from]

  Note: If no argument is specified, then statistics file under resources will be used ( it is checked in for convenience). You can just do - gradlew --console=plain run.

# How to run using Docker
 docker pull madsharm/search-target-case-study:latest
 docker run -it madsharm/search-target-case-study bash
 ./start.sh

# Design

## StatisticsComputer
It reads the file as line stream and process each word in line to compute stats for each word. Leverages Java8 Stream API functions (map, flatMap) to collect statistics.
Statistics for a word "other" as an example:
```
"other": [
		{
			"fileName": "warp_drive.txt",
			"relevancy": 2
		},
		{
			"fileName": "french_armed_forces.txt",
			"relevancy": 1
		}
	]
```
## Search Engine
Search Engine perform search using two ways - simple and indexed.
Both modes support string match, prefix match, suffix match, substring match
#### Simple Search Engine
+ Simple Search Engine loads all data in trie data structure. This happens once at initialization time
+ It leverages PatriciaTrie as its core data structore which is a compressed trie where each node represents part of key
+ It helps in bringing down the search to limit of key length
+ Search Support
    * Exact String Match - In a trie, exact match will take around O(M) where M is the length of the word in bits
    * Perfix String Match - Finding out a prefix in a trie takes around O(M) where M is the length of prefix in bits
    * Suffix and SubString Match - Trie is not meant for such searches, and hence in simple search mode, it queries all keys and perform pattern matching with each key. Worst case performance is O(N) where N is number of keys in trie. Note: This usecase will be optimized in Indexed Search Engine using indexes

Example of trie with words [france, frack, friend, fries]:
```
                        fr
                a---    | ---- ie
                |               |
        nce--------- ck   nd--------es
```
#### Indexed Search Engine
+ Indexed Search Engine leverages trie data structure as explained above. Along with simple search trie data structure, it leverages two indexes to optimize suffix and substring searches
+ Index creation happens once at initialization time
+ It leverages PatriciaTrie as its index data structure. There are two indexes-
    + **Suffix Index** : A trie that contains reversed search terms
    ```
    Example for suffix index for words [france, dance]
                              ance
                                |
             fr(value=france)-----d(value=dance)
    ```
    + **SubString Index** : A trie that contains possible sub strings of each search term
    ```
    Example for substring index for words [root, bot, torn]
                              o(value = [ro*,bo*])
                                |
            o (value = roo*)--------- r(value = tor*)
    ```
+ Search Support
    * Exact String Match - Similar to simple search
    * Prefix String Match - Similar to simple search
    * Suffix Match - Suffix search happens in two steps. Consider exmaple *ance.
        + Search for *ance will leverage suffix index and will perform prefix search for key with - ecna. Which in above example trie index, will return ancefr[france] and anced[dance].
        + Values returned from above suffix index search will be used to perform exact string match in core search trie and matched results will be returned
    * Sub String match - Search happens in two steps. Consider exmaple \*o\*.
        + Search for \*o\* will leverage substring index and will perform prefix search for key with - o. Which in above example trie index, will return o[ro*, bo*] , oo [roo*] and or [tor*].
        + Values returned from above substring index search will be used to perform prefix match in core search trie and matched results will be returned - [root, bot and torn].
    * Performance of suffix and sub string match using indexes have significantly improved.


# Performance Evaluation

Below is the stats from the performance test case:

```
Loading statistics from file D:\MS\target\search-target-case-study\out\production\resources\statistics.json
Average Time for regex search using Simple Search (ns) = 12908.5
Average Time for regex search using Indexed Search (ns) = 8038.5
Average Time for exact string search using Simple Search (ns) = 160.0
Average Time for exact string search using Indexed Search (ns) = 156.0
```

Observations are:
+ No significant difference in exact string match since both simple as indexed search uses same trie data structure for the search
+ Notice 60% improvement in prefix searches when using indexed based search. This is because of the suffix index and substring index that we have created. Due to this, iteration over entire set of keys is avoided.


# Scalability
+ The approach needs to cater to massive data as well as huge number of requests coming in.
+ To handle massive data, we can have some sort of distributed data storage (eg. cassandra or elastic) and use functions exposed by these data storage to perform the search
+ To handle massive requests coming in, we can scale up the search engine service by adding more nodes. Also, we should evaluate how many threads/connections can one node handle and optimize.
