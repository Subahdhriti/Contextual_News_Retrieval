package com.inshorts.newsRetrieval.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "articles")
@Data
public class NewsArticle {
    @Field("id")
    private String id;
    private String title;
    private String description;
    private String url;
    private LocalDateTime publication_date;
    private String source_name;
    private List<String> category;
    private double relevance_score;
    private double latitude;
    private double longitude;
    private String summary;
}
