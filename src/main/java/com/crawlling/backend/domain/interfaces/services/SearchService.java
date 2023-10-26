package com.crawlling.backend.domain.interfaces.services;

import com.crawlling.backend.domain.models.Search;

import java.io.IOException;

public interface SearchService {
    Search webCrawler(Search seach,String keyword) throws IOException;
}
