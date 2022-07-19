package com.searchgi.SearchGi.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "webpage")
@Getter @Setter
@ToString
public class WebPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_web_page", nullable = false)
    private Long idWebPage;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    public WebPage() {
    }

    public WebPage(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WebPage webPage = (WebPage) o;
        return idWebPage != null && Objects.equals(idWebPage, webPage.idWebPage);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
