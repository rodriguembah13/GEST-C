package com.ballack.com.repository.search;

import com.ballack.com.domain.LigneSortieArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LigneSortieArticle entity.
 */
public interface LigneSortieArticleSearchRepository extends ElasticsearchRepository<LigneSortieArticle, Long> {
}
