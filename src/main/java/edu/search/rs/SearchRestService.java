package edu.search.rs;

import edu.search.app.search.Search;
import edu.search.engine.SearcherFactory;
import edu.search.exception.InvalidSearchModeException;
import edu.search.vo.TimedSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@RestController
@ComponentScan(basePackages = {"edu.search"})
@SpringBootApplication
public class SearchRestService {

    @Autowired
    Search search;

    @RequestMapping(value= "/", method = RequestMethod.GET, produces = "application/json")
    String index() {
        return "Invoke endpoint as /search/{simple/indexed}/{string/regex(ab*/*ab/*ab*)}";
    }

    @RequestMapping(value = "/search/{mode}/{term}" , method = RequestMethod.GET, produces = "application/json")
    TimedSearchResult search(@PathVariable(name = "mode") String mode, @PathVariable(name = "term") String term) {
        SearcherFactory.MODE searchMode = SearcherFactory.MODE.getMode(mode);
        if (searchMode == null) {
            throw new InvalidSearchModeException("Invalid search mode " + searchMode);
        }
        return search.search(term, searchMode);
    }

    @RequestMapping(value = "/search" , method = RequestMethod.POST, produces = "application/json")
    TimedSearchResult search(@RequestBody SearchRequest searchRequest) {
        return search(searchRequest.getMode(), searchRequest.getTerm());
    }

    public static void main(String[] args) {
        SpringApplication.run(SearchRestService.class, args);
    }

}
