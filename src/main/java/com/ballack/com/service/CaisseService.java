package com.ballack.com.service;

import com.ballack.com.domain.Caisse;
import com.ballack.com.repository.CaisseRepository;
import com.ballack.com.repository.search.CaisseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Caisse.
 */
@Service
@Transactional
public class CaisseService {

    private final Logger log = LoggerFactory.getLogger(CaisseService.class);

    private final CaisseRepository caisseRepository;

    private final CaisseSearchRepository caisseSearchRepository;
    public CaisseService(CaisseRepository caisseRepository, CaisseSearchRepository caisseSearchRepository) {
        this.caisseRepository = caisseRepository;
        this.caisseSearchRepository = caisseSearchRepository;
    }

    /**
     * Save a caisse.
     *
     * @param caisse the entity to save
     * @return the persisted entity
     */
    public Caisse save(Caisse caisse) {
        log.debug("Request to save Caisse : {}", caisse);
        Caisse result = caisseRepository.save(caisse);
        caisseSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the caisses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Caisse> findAll(Pageable pageable) {
        log.debug("Request to get all Caisses");
        return caisseRepository.findAll(pageable);
    }

    /**
     *  Get one caisse by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Caisse findOne(Long id) {
        log.debug("Request to get Caisse : {}", id);
        return caisseRepository.findOne(id);
    }

    /**
     *  Delete the  caisse by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Caisse : {}", id);
        caisseRepository.delete(id);
        caisseSearchRepository.delete(id);
    }

    /**
     * Search for the caisse corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Caisse> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Caisses for query {}", query);
        Page<Caisse> result = caisseSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
