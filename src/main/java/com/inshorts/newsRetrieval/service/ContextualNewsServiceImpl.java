package com.inshorts.newsRetrieval.service;

import com.inshorts.newsRetrieval.model.NewsArticle;
import com.inshorts.newsRetrieval.repository.ContextualNewsRepository;
import com.inshorts.newsRetrieval.repository.ContextualNewsRepositoryJpa;
import com.inshorts.newsRetrieval.util.HaversineAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class ContextualNewsServiceImpl implements ContextualNewsService{

    @Autowired
    private ContextualNewsRepository repo;
    @Autowired
    private ContextualNewsRepositoryJpa jpaRepo;
    @Autowired
    private LLMService llmService;

    public List<NewsArticle> getByCategory(String category) {
        List<NewsArticle> articles = repo.findByCategory(List.of(category));
        return addLLMSummary(articles);
    }

    public List<NewsArticle> getByRelevanceScore(double threshold) {
        List<NewsArticle> articles = repo.findByRelevanceScore(threshold);
        return addLLMSummary(articles);
    }

    public List<NewsArticle> searchArticles(String query) {
        List<NewsArticle> articles = repo.searchByText(query).stream()
                .sorted(Comparator.comparingDouble(NewsArticle::getRelevance_score).reversed())
                .toList();
        return addLLMSummary(articles);
    }

    public List<NewsArticle> getBySource(String source) {
        List<NewsArticle> articles = repo.findBySource(source);
        return addLLMSummary(articles);
    }

    public List<NewsArticle> getNearbyArticles(double lat, double lon, double radiusInKm) {
        List<NewsArticle> articles = jpaRepo.findAll().stream()
                .filter(article ->
                        HaversineAlgorithm.distance(lat, lon, article.getLatitude(), article.getLongitude()) <= radiusInKm)
                .sorted(Comparator.comparingDouble(
                        a -> HaversineAlgorithm.distance(lat, lon, a.getLatitude(), a.getLongitude())))
                .toList();
        return addLLMSummary(articles);
    }

    /**
     * This function is to add LLM summary to the news articles.
     * Completable future is used generate summaries asynchronously.
     * @param articles The list of news articles fetched from DB
     * @return The list of news articles fetched from DB, summary will be added if service is able to get
     * LLM summary, otherwise empty string
     */
    private List<NewsArticle> addLLMSummary(List<NewsArticle> articles) {
        List<CompletableFuture<NewsArticle>> futures = articles.stream()
                .map(article -> CompletableFuture.supplyAsync(() -> {
                    String summary = llmService.getLLMSummary(article.getDescription());
                    article.setSummary(summary);
                    return article;
                }))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

}
