package com.ballack.com.repository.search;

import com.ballack.com.domain.TransfertMagasin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TransfertMagasin entity.
 */
public interface TransfertMagasinSearchRepository extends ElasticsearchRepository<TransfertMagasin, Long> {
}
