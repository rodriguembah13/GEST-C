package com.ballack.com.repository.search;

import com.ballack.com.domain.TypeSortieArticle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TypeSortieArticle entity.
 */
public interface TypeSortieArticleSearchRepository extends ElasticsearchRepository<TypeSortieArticle, Long> {
}
