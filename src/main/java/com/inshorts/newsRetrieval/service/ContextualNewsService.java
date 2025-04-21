package com.inshorts.newsRetrieval.service;

import com.inshorts.newsRetrieval.model.NewsArticle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContextualNewsService {
    public List<NewsArticle> getByCategory(String category);

    public List<NewsArticle> getByRelevanceScore(double threshold);

    public List<NewsArticle> searchArticles(String query);

    public List<NewsArticle> getBySource(String source);

    public List<NewsArticle> getNearbyArticles(double lat, double lon, double radiusKm);
}
