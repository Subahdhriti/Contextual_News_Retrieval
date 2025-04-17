package com.inshorts.newsRetrieval.service;

import com.inshorts.newsRetrieval.model.NewsArticle;
import com.inshorts.newsRetrieval.repository.ContextualNewsRepository;
import com.inshorts.newsRetrieval.util.HaversineAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ContextualNewsService {

    @Autowired
    private ContextualNewsRepository repo;

    public List<NewsArticle> getByCategory(String category) {
        return repo.findByCategoryInOrderByPublicationDateDesc(List.of(category));
    }

    public List<NewsArticle> getByRelevanceScore(double threshold) {
        return repo.findByRelevanceScoreGreaterThanOrderByRelevanceScoreDesc(threshold);
    }

    public List<NewsArticle> searchArticles(String query) {
        return repo.searchByText(query).stream()
                .sorted(Comparator.comparingDouble(NewsArticle::getRelevanceScore).reversed())
                .toList();
    }

    public List<NewsArticle> getBySource(String source) {
        return repo.findBySourceNameOrderByPublicationDateDesc(source);
    }

    public List<NewsArticle> getNearbyArticles(double lat, double lon, double radiusKm) {
        return repo.findAll().stream()
                .filter(article ->
                        HaversineAlgorithm.distance(lat, lon, article.getLatitude(), article.getLongitude()) <= radiusKm)
                .sorted(Comparator.comparingDouble(
                        a -> HaversineAlgorithm.distance(lat, lon, a.getLatitude(), a.getLongitude())))
                .toList();
    }
}
