# search-target-case-study
Case Study for Search Usecase

#Problem Statement:
The goal of this exercise is to create a command line based program to search a set of documents (present under src/resources) for the given search term or phrase (single token), and return results in order of relevance.
Relevancy is defined as number of times the exact term or phrase appears in the document.
Create three methods for searching the documents:
•	Simple string matching
•	Text search using regular expressions
•	Preprocess the content and then search the index

#Assumptions:
a) Case insensitive search
b) Only text keys considered, not numbers and punctuations
c) Support string match (freebird), prefix match(free*), suffix match(*bird), substring match(*eebi*)

#Approach
The program works in two steps:
1) Optional step: Parse the document files and create statistics file containing stats about each word - how many times it appears in each file in desc order of relevancy.
To run parsing utility:
gradle runStatisticsComputer  --args="D:\stats_v1.json"
[where argument represents where to store the statistics file]

Note: You can skip this step, since statistics file is already checked in the source control

2) Run search engine
To run:
gradle --console=plain run --args="D:\stats_v1.json"
[where argument represents where to load the statistics file from]

Note: If no argument is specified, then statistics file under resources will be used ( it is checked in for convenience). You can just do - gradle --console=plain run.

#Design

##StatisticsComputer
It reads the file as line stream and process each word in line to compute stats for each word. Leverages Java8 Stream API functions (map, flatMap) to collect statistics.
Sample Statistics for a word "other"
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

##Search Engine
Search Engine perform search using two ways - simple and indexed.
Both modes support string match, prefix match, suffix match, substring match

######Simple Search Engine
######Indexed Search Engine




TODO:
a) Adding logging and appropriate exception handling
