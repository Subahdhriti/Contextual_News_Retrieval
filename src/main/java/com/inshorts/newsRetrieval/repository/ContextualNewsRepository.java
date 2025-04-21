package com.inshorts.newsRetrieval.repository;

import com.inshorts.newsRetrieval.model.NewsArticle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContextualNewsRepository extends MongoRepository<NewsArticle, String> {
    List<NewsArticle> findByCategoryInOrderByPublicationDateDesc(List<String> category);

    List<NewsArticle> findByRelevanceScoreGreaterThanOrderByRelevanceScoreDesc(double threshold);

    List<NewsArticle> findByTitleOrderByPublicationDateDesc(String sourceName);

    @Query("{ $or: [ { title: { $regex: ?0, $options: 'i' } }, { description: { $regex: ?0, $options: 'i' } } ] }")
    List<NewsArticle> searchByText(String query);
}
