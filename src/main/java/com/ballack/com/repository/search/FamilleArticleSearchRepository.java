package com.ballack.com.repository.search;

import com.ballack.com.domain.FamilleArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FamilleArticle entity.
 */
public interface FamilleArticleSearchRepository extends ElasticsearchRepository<FamilleArticle, Long> {
}
