package com.inshorts.newsRetrieval.repository;

import com.inshorts.newsRetrieval.model.NewsArticle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContextualNewsRepositoryJpa extends MongoRepository<NewsArticle, String> {
}