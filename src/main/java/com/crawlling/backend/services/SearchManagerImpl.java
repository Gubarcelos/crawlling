package com.crawlling.backend.services;

import com.crawlling.backend.domain.interfaces.services.SearchManagerService;
import com.crawlling.backend.domain.interfaces.services.SearchService;
import com.crawlling.backend.domain.models.Search;
import com.crawlling.backend.domain.models.SearchStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class SearchManagerImpl implements SearchManagerService {
    private List <Search> searches = new ArrayList <>();
    private SearchService searchService;
    private ExecutorService executor;

    public SearchManagerImpl(SearchService searchService){
        this.searchService = searchService;
    }

    public Search startNewSearch(String keyword) throws ExecutionException, InterruptedException {
        searches = Collections.synchronizedList(searches);
        Search newSearch = new Search();
        searches.add(newSearch);

        ExecutorService executor = Executors.newCachedThreadPool();

        Future<?> future = executor.submit(() -> {
            try {
                this.searchService.webCrawler(newSearch,keyword);
                newSearch.setStatus(SearchStatus.DONE);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                shutdown();
            }
        });

        int activeThreads = ((ThreadPoolExecutor) executor).getActiveCount();
        System.out.println("Número de threads ativas depois: " + activeThreads);
        return newSearch;
    }

    @Override
    public Search getSearchById(String searchId) {
        for(Search search : searches) {
            if(search.getId().equals(searchId)){
                return search;
            }
        }
        return null;
    }

    public void shutdown() {
        executor.shutdown();
    }
}
