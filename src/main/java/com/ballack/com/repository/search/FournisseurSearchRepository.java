package com.ballack.com.repository.search;

import com.ballack.com.domain.Fournisseur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Fournisseur entity.
 */
public interface FournisseurSearchRepository extends ElasticsearchRepository<Fournisseur, Long> {
}
