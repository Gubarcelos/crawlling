package com.crawlling.backend.presentation.DTO;

import java.util.Objects;

public class PostSearchResponseDTO {
    String id ;

    public PostSearchResponseDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostSearchResponseDTO)) return false;
        PostSearchResponseDTO that = (PostSearchResponseDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
