package com.searchgi.SearchGi.services;

import com.searchgi.SearchGi.models.WebPage;
import com.searchgi.SearchGi.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {


    private final SearchRepository repository;

    public SearchService(SearchRepository repository) {
        this.repository = repository;
    }

    public List<WebPage> search(String textSearch){
        return repository.search(textSearch);
    }

    public void save(WebPage webPage){
        repository.save(webPage);
    }


    public boolean exist(String link) {
        return repository.exist(link);
    }

    public List<WebPage> getLinksToIndex(){

        return repository.getLinksToIndex();
    }

    public  void deleteWebPageByUrl(String url){
        repository.deleteWebPageByUrl(url);
    };
}
