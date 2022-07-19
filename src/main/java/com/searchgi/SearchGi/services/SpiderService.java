package com.searchgi.SearchGi.services;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.searchgi.SearchGi.models.WebPage;
import org.springframework.stereotype.Service;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class SpiderService {

    private final SearchService searchService;

    public SpiderService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void indexWebPages(){

        List<WebPage> linksToIndex = searchService.getLinksToIndex();

        linksToIndex.stream().parallel().forEach(webPage -> {
            try {
                indexWebPage(webPage);
            } catch (Exception e) {
                searchService.deleteWebPageByUrl(webPage.getUrl());
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } );
        /* <meta name="DESCRIPTION" content="Clarin.com. Noticias de la Argentina y el mundo.
         Información actualizada las 24 horas y en español. Informate ya"/>
         <title>Noticias. Últimas noticias de Argentina y el Mundo | Clarín</title>
         */
    }

    private void indexWebPage(WebPage webPage) throws Exception {
        String url = webPage.getUrl();
        String content = getWebContent(url);
        if(isBlank(content)){
            return;
        }
        saveWebpage(url,content);
        String domian = getDomain(url);
        saveLInks(content, domian);
    }

    private String getDomain(String url) {

        String[] aux = url.split("/");
        return aux[0] + "//" + aux[2];
    }

    public void saveLInks(String content, String domain){
        List<String> links = getLinks(content, domain);
        links.stream().filter(link -> !searchService.exist(link)).map(link -> new WebPage(link))
                .forEach(searchService::save);
    }

    public void saveWebpage(String url, String content ){

        String title = getTitle(content);
        String description = getDescription(content);

        WebPage webPage = new WebPage();
        webPage.setTitle(title);
        webPage.setDescription(description);
        webPage.setUrl(url);
        

        searchService.save(webPage);
    }

    public String getTitle(String content){
        String[] aux = content.split("<title>");
        String[] aux2 = aux[1].split("</title>");
        return aux2[0];
    }
    public String getDescription(String content){
        String[] aux = content.split("<meta name=\"description\" content=\"");

        if(aux.length >= 1){
            String[] aux2 = aux[1].split("\">");
            String[] aux3 = aux2[0].split(".");
            return aux3[0];
        }
        return "";
    }

    public List<String> getLinks(String content, String domain){
        List<String> links = new ArrayList<>();

        String[] aux = content.split("href=\"");

        Arrays.stream(aux).forEach(partLink -> {
            String[] aux2 = partLink.split("\"");

            if (aux2[0].startsWith("http") || aux2[0].startsWith("/")){
                links.add(aux2[0]);
            }
        });
            return cleanLinks(links, domain);
    }

    public List<String> cleanLinks(List<String> links, String domain){
        String[] excludeExtensions = new String[]{"css","js","json","jpn","png","woff2"};

        List<String> result = links.stream().filter(link -> Arrays.stream(excludeExtensions)
                .noneMatch(link::endsWith))
                .map(link -> link.startsWith("http") || link.startsWith("/") ? domain + link : link)
                .collect(Collectors.toList());

        List<String> cleanLinks =  new ArrayList<String>();
        cleanLinks.addAll(new HashSet<String>(result));

        return cleanLinks;
    }

    private static String getWebContent(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String encoding = conn.getContentEncoding();

            InputStream input = conn.getInputStream();

            Stream<String> lines = new BufferedReader(new InputStreamReader(input))
                    .lines();

            return lines.collect(Collectors.joining());
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return "";
    }
}
