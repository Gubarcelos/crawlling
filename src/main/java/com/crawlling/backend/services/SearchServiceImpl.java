package com.crawlling.backend.services;

import com.crawlling.backend.domain.interfaces.services.WebCrawlerService;
import com.crawlling.backend.domain.models.Search;
import com.crawlling.backend.domain.interfaces.services.SearchService;
import com.crawlling.backend.domain.models.SearchStatus;
import com.crawlling.backend.domain.models.Url;
import com.crawlling.backend.infra.AppConfigs;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchServiceImpl implements SearchService {

    private WebCrawlerService webCrawler;
    private ExecutorService executor = null;
    private AtomicInteger searchCounter = new AtomicInteger(0);

    public SearchServiceImpl(WebCrawlerService webCrawler) {
        this.webCrawler = webCrawler;
        this.executor = Executors.newCachedThreadPool();
    }

    public SearchServiceImpl() {}
    @Override
    public Search webCrawler(Search newSearch,String keyword) throws IOException {
        String baseURL = AppConfigs.getBaseURL();
        Url baseUrl = new Url();
        baseUrl.setLink(baseURL);
        Set <Url> urls = Collections.synchronizedSet(new HashSet<>());
        urls.add(baseUrl);
        Set<String> visitedLinks = Collections.synchronizedSet( new HashSet<>());

        Set <String>  foundedLinks = Collections.synchronizedSet(new HashSet <>());
        while (!urls.isEmpty()) {

            Url currentUrl = urls.iterator().next();
            urls.remove(currentUrl);

            String link = currentUrl.getLink();

            if(visitedLinks.contains(link)) {
                continue;
            }
            visitedLinks.add(link);

            searchCounter.incrementAndGet();
            Future<?> future  = executor.submit(()-> {
                //            System.out.println("analizando o link: " + link);
                String crawledContent = null;
                try {
                    searchCounter.decrementAndGet();
                    crawledContent = webCrawler.fetchPageContent(link, keyword);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(crawledContent != null) {
                    HashSet<Url> extractedLinks = webCrawler.extractLinks(crawledContent,baseURL);
                    urls.addAll(extractedLinks);
                    boolean keywordFound = webCrawler.searchKeyword(crawledContent, keyword);

                    if (keywordFound) {
//                    System.out.println(link);
                        foundedLinks.add(link);
                        newSearch.getFoundUrls().add(currentUrl);
                    }
                }
            });

//            System.out.println("Número de threads ativas: " + searchCounter.get());
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("erro :" +e);
            }

//            System.out.println("tem isso de links analizados: "+visitedLinks.size());
//            System.out.println("foram achados em isso de links:  "+foundedLinks.size());
        }

        executor.shutdown();

        System.out.println("urls: " + newSearch.getFoundUrls().size());
        System.out.println("links: " + foundedLinks.size());
        System.out.println("foram achados em isso de links:  "+foundedLinks);
        System.out.println("foram achados em isso de links classe url:  "+newSearch.getFoundUrls());

        newSearch.setStatus(SearchStatus.DONE);

        return newSearch;
    }

}
