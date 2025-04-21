package com.inshorts.newsRetrieval.service;

import com.inshorts.newsRetrieval.model.NewsArticle;
import com.inshorts.newsRetrieval.repository.ContextualNewsRepository;
import com.inshorts.newsRetrieval.util.HaversineAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
public class ContextualNewsService {

    @Autowired
    private ContextualNewsRepository repo;
    @Autowired
    private LLMService llmService;

    public List<NewsArticle> getByCategory(String category) {
        List<NewsArticle> articles = repo.findByCategoryInOrderByPublicationDateDesc(List.of(category));
        return addLLMSummary(articles);
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
        List<NewsArticle> articles = repo.findByTitleOrderByPublicationDateDesc(source);
        return addLLMSummary(articles);
    }

    public List<NewsArticle> getNearbyArticles(double lat, double lon, double radiusKm) {
        return repo.findAll().stream()
                .filter(article ->
                        HaversineAlgorithm.distance(lat, lon, article.getLatitude(), article.getLongitude()) <= radiusKm)
                .sorted(Comparator.comparingDouble(
                        a -> HaversineAlgorithm.distance(lat, lon, a.getLatitude(), a.getLongitude())))
                .toList();
    }


    private List<NewsArticle> addLLMSummary(List<NewsArticle> articles){
        articles.forEach(article -> {
                article.setSummary(llmService.getLLMSummary(article.getDescription()));
        });
        return articles;
    }
}
