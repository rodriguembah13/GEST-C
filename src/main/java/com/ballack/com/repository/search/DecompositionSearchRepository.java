package com.ballack.com.repository.search;

import com.ballack.com.domain.Decomposition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Decomposition entity.
 */
public interface DecompositionSearchRepository extends ElasticsearchRepository<Decomposition, Long> {
}
