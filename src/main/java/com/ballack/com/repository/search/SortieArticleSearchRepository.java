package com.ballack.com.repository.search;

import com.ballack.com.domain.SortieArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SortieArticle entity.
 */
public interface SortieArticleSearchRepository extends ElasticsearchRepository<SortieArticle, Long> {
}
