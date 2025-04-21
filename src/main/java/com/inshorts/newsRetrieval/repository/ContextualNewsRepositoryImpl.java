package com.inshorts.newsRetrieval.repository;

import com.inshorts.newsRetrieval.model.NewsArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class ContextualNewsRepositoryImpl implements ContextualNewsRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<NewsArticle> findByCategory(List<String> categories) {
        Query query = new Query();
        query.addCriteria(Criteria.where("category").in(categories));
        query.with(Sort.by(Sort.Direction.DESC, "publication_date"));
        return mongoTemplate.find(query, NewsArticle.class);
    }

    @Override
    public List<NewsArticle> findByRelevanceScore(double threshold) {
        Query query = new Query();
        query.addCriteria(Criteria.where("relevance_score").gt(threshold));
        query.with(Sort.by(Sort.Direction.DESC, "relevance_score"));
        return mongoTemplate.find(query, NewsArticle.class);
    }

    @Override
    public List<NewsArticle> findBySource(String sourceName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("source_name").is(sourceName));
        query.with(Sort.by(Sort.Direction.DESC, "publication_date"));
        return mongoTemplate.find(query, NewsArticle.class);
    }

    @Override
    public List<NewsArticle> searchByText(String query) {
        Criteria textCriteria = new Criteria().orOperator(
                Criteria.where("title").regex(Pattern.quote(query)),
                Criteria.where("description").regex(Pattern.quote(query))
        );
        return mongoTemplate.find(new Query(textCriteria), NewsArticle.class);
    }
}
