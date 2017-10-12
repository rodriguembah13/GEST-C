package com.ballack.com.repository.search;

import com.ballack.com.domain.EntreeArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EntreeArticle entity.
 */
public interface EntreeArticleSearchRepository extends ElasticsearchRepository<EntreeArticle, Long> {
}
