package com.crawlling.backend.domain.interfaces.services;

import com.crawlling.backend.domain.models.Url;

import java.io.IOException;
import java.util.HashSet;

public interface WebCrawlerService {
    String fetchPageContent(String url,String keyword) throws IOException;
    HashSet <Url> extractLinks(String htmlContent, String baseUrl);
    boolean searchKeyword(String htmlContent, String keyword);
}
