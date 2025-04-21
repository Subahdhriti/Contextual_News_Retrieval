package com.inshorts.newsRetrieval.repository;

import com.inshorts.newsRetrieval.model.NewsArticle;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContextualNewsRepository {
    List<NewsArticle> findByCategory(List<String> category);

    List<NewsArticle> findByRelevanceScore(double threshold);

    List<NewsArticle> findBySource(String sourceName);

    @Query("{ $or: [ { title: { $regex: ?0, $options: 'i' } }, { description: { $regex: ?0, $options: 'i' } } ] }")
    List<NewsArticle> searchByText(String query);
}
