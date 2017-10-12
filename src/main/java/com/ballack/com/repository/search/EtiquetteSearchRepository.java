package com.ballack.com.repository.search;

import com.ballack.com.domain.Etiquette;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Etiquette entity.
 */
public interface EtiquetteSearchRepository extends ElasticsearchRepository<Etiquette, Long> {
}
