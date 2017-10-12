package com.ballack.com.service;

import com.ballack.com.domain.SortieArticle;
import com.ballack.com.repository.SortieArticleRepository;
import com.ballack.com.repository.search.SortieArticleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SortieArticle.
 */
@Service
@Transactional
public class SortieArticleService {

    private final Logger log = LoggerFactory.getLogger(SortieArticleService.class);

    private final SortieArticleRepository sortieArticleRepository;

    private final SortieArticleSearchRepository sortieArticleSearchRepository;
    public SortieArticleService(SortieArticleRepository sortieArticleRepository, SortieArticleSearchRepository sortieArticleSearchRepository) {
        this.sortieArticleRepository = sortieArticleRepository;
        this.sortieArticleSearchRepository = sortieArticleSearchRepository;
    }

    /**
     * Save a sortieArticle.
     *
     * @param sortieArticle the entity to save
     * @return the persisted entity
     */
    public SortieArticle save(SortieArticle sortieArticle) {
        log.debug("Request to save SortieArticle : {}", sortieArticle);
        SortieArticle result = sortieArticleRepository.save(sortieArticle);
        sortieArticleSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the sortieArticles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SortieArticle> findAll(Pageable pageable) {
        log.debug("Request to get all SortieArticles");
        return sortieArticleRepository.findAll(pageable);
    }

    /**
     *  Get one sortieArticle by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SortieArticle findOne(Long id) {
        log.debug("Request to get SortieArticle : {}", id);
        return sortieArticleRepository.findOne(id);
    }

    /**
     *  Delete the  sortieArticle by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SortieArticle : {}", id);
        sortieArticleRepository.delete(id);
        sortieArticleSearchRepository.delete(id);
    }

    /**
     * Search for the sortieArticle corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SortieArticle> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SortieArticles for query {}", query);
        Page<SortieArticle> result = sortieArticleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
