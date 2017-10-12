package com.ballack.com.repository.search;

import com.ballack.com.domain.FormeArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FormeArticle entity.
 */
public interface FormeArticleSearchRepository extends ElasticsearchRepository<FormeArticle, Long> {
}
