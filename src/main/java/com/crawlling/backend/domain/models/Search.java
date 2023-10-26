package com.crawlling.backend.domain.models;

import java.util.HashSet;
import java.util.UUID;

public class Search {
        private String id;

        private HashSet <Url> foundedUrls;
        private SearchStatus status;



        public Search() {
                this.id = generateSearchId();
                this.status = SearchStatus.ACTIVE;
                this.foundedUrls = new HashSet <>();
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public HashSet <Url> getFoundUrls() {
                return foundedUrls;
        }

        public void setFoundUrls(HashSet <Url> foundUrls) {
                this.foundedUrls = foundUrls;
        }

        public SearchStatus getStatus() {
                return status;
        }

        public void setStatus(SearchStatus status) {
                this.status = status;
        }

        private String generateSearchId() {
                return UUID.randomUUID().toString().substring(0,8);
        }
}
