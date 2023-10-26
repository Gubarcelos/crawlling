package com.crawlling.backend.presentation.DTO;

import com.crawlling.backend.domain.models.SearchStatus;
import com.crawlling.backend.domain.models.Url;

import java.util.HashSet;

public class GetSearchResponseDTO {
    String id;
    SearchStatus status;
    HashSet <String> urls = new HashSet();

    public GetSearchResponseDTO(String id, SearchStatus status, HashSet <Url> urls) {
        this.id = id;
        this.status = status;
        addLinks(urls);
    }

    private void addLinks(HashSet <Url> foundedUrls) {
        for(Url url : foundedUrls) {
            urls.add(url.getLink());
        }
    }

}
