package com.searchgi.SearchGi.repositories;

import com.searchgi.SearchGi.models.WebPage;

import java.util.List;

public interface SearchRepository {


    List<WebPage> search(String textSearch);

    void save(WebPage webPage);

    boolean exist(String link);

    WebPage getByUrl(String url);

    List<WebPage> getLinksToIndex();

    void deleteWebPageByUrl(String url);
}
