package com.searchgi.SearchGi.controllers;

import com.searchgi.SearchGi.models.WebPage;
import com.searchgi.SearchGi.services.SearchService;
import com.searchgi.SearchGi.services.SpiderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    private final SearchService searchService;
    private final SpiderService spiderService;

    public SearchController(SearchService searchService, SpiderService spiderService) {
        this.searchService = searchService;
        this.spiderService = spiderService;
    }

    @RequestMapping(value = "api/search", method = RequestMethod.GET)
    public List<WebPage> search(@RequestParam Map<String,String> params){
        String query = params.get("query");
        return searchService.search(query);
    }

    @RequestMapping(value = "api/spider", method = RequestMethod.GET)
    public String spider(){
        spiderService.indexWebPages();

        return "Ok";
    }
}
