package com.inshorts.newsRetrieval.controller.v1;

import com.inshorts.newsRetrieval.model.NewsArticle;
import com.inshorts.newsRetrieval.service.ContextualNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/news")
public class ContextualNewsControllerV1 {

    @Autowired
    private ContextualNewsService newsService;

    @GetMapping("/category")
    public List<NewsArticle> getByCategory(@RequestParam String category) {
        return newsService.getByCategory(category);
    }

    @GetMapping("/score")
    public List<NewsArticle> getByRelevanceScore(@RequestParam(defaultValue = "0.7") double threshold) {
        return newsService.getByRelevanceScore(threshold);
    }

    @GetMapping("/search")
    public List<NewsArticle> searchArticles(@RequestParam String query) {
        return newsService.searchArticles(query);
    }

    @GetMapping("/source")
    public List<NewsArticle> getBySource(@RequestParam String source) {
        return newsService.getBySource(source);
    }

    @GetMapping("/nearby")
    public List<NewsArticle> getNearbyArticles(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "10.0") double radiusKm
    ) {
        return newsService.getNearbyArticles(lat, lon, radiusKm);
    }
}
