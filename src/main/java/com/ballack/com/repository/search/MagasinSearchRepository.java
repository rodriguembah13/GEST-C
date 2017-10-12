package com.ballack.com.repository.search;

import com.ballack.com.domain.Magasin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Magasin entity.
 */
public interface MagasinSearchRepository extends ElasticsearchRepository<Magasin, Long> {
}
