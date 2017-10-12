package com.ballack.com.repository.search;

import com.ballack.com.domain.LigneEntreeArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LigneEntreeArticle entity.
 */
public interface LigneEntreeArticleSearchRepository extends ElasticsearchRepository<LigneEntreeArticle, Long> {
}
