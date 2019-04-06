FROM java

COPY build/libs/search-target-case-study.jar /
COPY src/main/resources/statistics.json /
COPY start.sh /

