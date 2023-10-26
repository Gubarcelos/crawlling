package com.crawlling.backend.domain.interfaces.services;

import com.crawlling.backend.domain.models.Search;

import java.util.concurrent.ExecutionException;

public interface SearchManagerService {
    public Search startNewSearch(String keyword) throws ExecutionException, InterruptedException;
    public Search getSearchById(String searchId);
    void shutdown();
}
