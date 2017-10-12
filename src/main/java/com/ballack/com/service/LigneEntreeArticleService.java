package com.ballack.com.service;

import com.ballack.com.domain.LigneEntreeArticle;
import com.ballack.com.repository.LigneEntreeArticleRepository;
import com.ballack.com.repository.search.LigneEntreeArticleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LigneEntreeArticle.
 */
@Service
@Transactional
public class LigneEntreeArticleService {

    private final Logger log = LoggerFactory.getLogger(LigneEntreeArticleService.class);

    private final LigneEntreeArticleRepository ligneEntreeArticleRepository;

    private final LigneEntreeArticleSearchRepository ligneEntreeArticleSearchRepository;
    public LigneEntreeArticleService(LigneEntreeArticleRepository ligneEntreeArticleRepository, LigneEntreeArticleSearchRepository ligneEntreeArticleSearchRepository) {
        this.ligneEntreeArticleRepository = ligneEntreeArticleRepository;
        this.ligneEntreeArticleSearchRepository = ligneEntreeArticleSearchRepository;
    }

    /**
     * Save a ligneEntreeArticle.
     *
     * @param ligneEntreeArticle the entity to save
     * @return the persisted entity
     */
    public LigneEntreeArticle save(LigneEntreeArticle ligneEntreeArticle) {
        log.debug("Request to save LigneEntreeArticle : {}", ligneEntreeArticle);
        LigneEntreeArticle result = ligneEntreeArticleRepository.save(ligneEntreeArticle);
        ligneEntreeArticleSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ligneEntreeArticles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneEntreeArticle> findAll(Pageable pageable) {
        log.debug("Request to get all LigneEntreeArticles");
        return ligneEntreeArticleRepository.findAll(pageable);
    }

    /**
     *  Get one ligneEntreeArticle by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LigneEntreeArticle findOne(Long id) {
        log.debug("Request to get LigneEntreeArticle : {}", id);
        return ligneEntreeArticleRepository.findOne(id);
    }

    /**
     *  Delete the  ligneEntreeArticle by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneEntreeArticle : {}", id);
        ligneEntreeArticleRepository.delete(id);
        ligneEntreeArticleSearchRepository.delete(id);
    }

    /**
     * Search for the ligneEntreeArticle corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneEntreeArticle> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LigneEntreeArticles for query {}", query);
        Page<LigneEntreeArticle> result = ligneEntreeArticleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
