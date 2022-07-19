package com.searchgi.SearchGi.repositories;

import com.searchgi.SearchGi.models.WebPage;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class SearchRepositoryImp implements SearchRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public List<WebPage> search(String textSearch) {

        String query = "FROM WebPage WHERE description like :textSearch";

        return entityManager.createQuery(query)
                .setParameter("textSearch", "%" + textSearch + "%")
                .getResultList();
    }

    @Transactional
    @Override
    public void save(WebPage webPage) {
        entityManager.merge(webPage);
    }

    @Transactional
    @Override
    public WebPage getByUrl(String url){
        String query = "FROM WebPage WHERE url = :url";

        List<WebPage> list = entityManager.createQuery(query).setParameter("url", url)
                .getResultList();

        return list.size() == 0 ? null : list.get(0);
    }

    @Transactional
    @Override
    public List<WebPage> getLinksToIndex() {

        String query = "FROM WebPage WHERE title is null AND description is null";

        return entityManager.createQuery(query).setMaxResults(100).getResultList();
    }

    @Transactional
    @Override
    public void deleteWebPageByUrl(String url) {
        WebPage webPage = getByUrl(url);

        if(webPage != null){
            entityManager.remove(webPage);
        }
    }

    @Override
    public boolean exist(String url) {
        return getByUrl(url) != null;
    }
}
