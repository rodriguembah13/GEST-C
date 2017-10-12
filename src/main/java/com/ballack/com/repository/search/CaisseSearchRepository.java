package com.ballack.com.repository.search;

import com.ballack.com.domain.Caisse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Caisse entity.
 */
public interface CaisseSearchRepository extends ElasticsearchRepository<Caisse, Long> {
}
