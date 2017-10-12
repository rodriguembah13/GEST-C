package com.ballack.com.repository.search;

import com.ballack.com.domain.LigneCommande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LigneCommande entity.
 */
public interface LigneCommandeSearchRepository extends ElasticsearchRepository<LigneCommande, Long> {
}
