package com.inshorts.newsRetrieval.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "articles")
@Data
public class NewsArticle {
    @Id
    private String id;
    private String title;
    private String description;
    private String url;
    @JsonProperty("publication_date")
    private LocalDateTime publicationDate;
    @JsonProperty("source_name")
    private String sourceName;
    private List<String> category;
    @JsonProperty("relevance_score")
    private double relevanceScore;
    private double latitude;
    private double longitude;
    private String summary;
}
