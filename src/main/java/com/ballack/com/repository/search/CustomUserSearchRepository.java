package com.ballack.com.repository.search;

import com.ballack.com.domain.CustomUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomUser entity.
 */
public interface CustomUserSearchRepository extends ElasticsearchRepository<CustomUser, Long> {
}
