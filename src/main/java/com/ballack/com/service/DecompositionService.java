package com.ballack.com.service;

import com.ballack.com.domain.Decomposition;
import com.ballack.com.repository.DecompositionRepository;
import com.ballack.com.repository.search.DecompositionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Decomposition.
 */
@Service
@Transactional
public class DecompositionService {

    private final Logger log = LoggerFactory.getLogger(DecompositionService.class);

    private final DecompositionRepository decompositionRepository;

    private final DecompositionSearchRepository decompositionSearchRepository;
    public DecompositionService(DecompositionRepository decompositionRepository, DecompositionSearchRepository decompositionSearchRepository) {
        this.decompositionRepository = decompositionRepository;
        this.decompositionSearchRepository = decompositionSearchRepository;
    }

    /**
     * Save a decomposition.
     *
     * @param decomposition the entity to save
     * @return the persisted entity
     */
    public Decomposition save(Decomposition decomposition) {
        log.debug("Request to save Decomposition : {}", decomposition);
        Decomposition result = decompositionRepository.save(decomposition);
        decompositionSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the decompositions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Decomposition> findAll(Pageable pageable) {
        log.debug("Request to get all Decompositions");
        return decompositionRepository.findAll(pageable);
    }

    /**
     *  Get one decomposition by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Decomposition findOne(Long id) {
        log.debug("Request to get Decomposition : {}", id);
        return decompositionRepository.findOne(id);
    }

    /**
     *  Delete the  decomposition by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Decomposition : {}", id);
        decompositionRepository.delete(id);
        decompositionSearchRepository.delete(id);
    }

    /**
     * Search for the decomposition corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Decomposition> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Decompositions for query {}", query);
        Page<Decomposition> result = decompositionSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
