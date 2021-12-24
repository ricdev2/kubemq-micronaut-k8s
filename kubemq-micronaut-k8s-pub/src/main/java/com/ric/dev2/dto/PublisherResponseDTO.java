package com.ric.dev2.dto;

import java.io.Serializable;

public final class PublisherResponseDTO implements Serializable {

    private String id;

    public PublisherResponseDTO() {
        this.id = id;
    }

    public PublisherResponseDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
