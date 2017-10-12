package com.ballack.com.service;

import com.ballack.com.domain.LigneSortieArticle;
import com.ballack.com.repository.LigneSortieArticleRepository;
import com.ballack.com.repository.search.LigneSortieArticleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LigneSortieArticle.
 */
@Service
@Transactional
public class LigneSortieArticleService {

    private final Logger log = LoggerFactory.getLogger(LigneSortieArticleService.class);

    private final LigneSortieArticleRepository ligneSortieArticleRepository;

    private final LigneSortieArticleSearchRepository ligneSortieArticleSearchRepository;
    public LigneSortieArticleService(LigneSortieArticleRepository ligneSortieArticleRepository, LigneSortieArticleSearchRepository ligneSortieArticleSearchRepository) {
        this.ligneSortieArticleRepository = ligneSortieArticleRepository;
        this.ligneSortieArticleSearchRepository = ligneSortieArticleSearchRepository;
    }

    /**
     * Save a ligneSortieArticle.
     *
     * @param ligneSortieArticle the entity to save
     * @return the persisted entity
     */
    public LigneSortieArticle save(LigneSortieArticle ligneSortieArticle) {
        log.debug("Request to save LigneSortieArticle : {}", ligneSortieArticle);
        LigneSortieArticle result = ligneSortieArticleRepository.save(ligneSortieArticle);
        ligneSortieArticleSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ligneSortieArticles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneSortieArticle> findAll(Pageable pageable) {
        log.debug("Request to get all LigneSortieArticles");
        return ligneSortieArticleRepository.findAll(pageable);
    }

    /**
     *  Get one ligneSortieArticle by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LigneSortieArticle findOne(Long id) {
        log.debug("Request to get LigneSortieArticle : {}", id);
        return ligneSortieArticleRepository.findOne(id);
    }

    /**
     *  Delete the  ligneSortieArticle by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneSortieArticle : {}", id);
        ligneSortieArticleRepository.delete(id);
        ligneSortieArticleSearchRepository.delete(id);
    }

    /**
     * Search for the ligneSortieArticle corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneSortieArticle> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LigneSortieArticles for query {}", query);
        Page<LigneSortieArticle> result = ligneSortieArticleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
