package com.ballack.com.service.impl;

import com.ballack.com.service.TransfertMagasinService;
import com.ballack.com.domain.TransfertMagasin;
import com.ballack.com.repository.TransfertMagasinRepository;
import com.ballack.com.repository.search.TransfertMagasinSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TransfertMagasin.
 */
@Service
@Transactional
public class TransfertMagasinServiceImpl implements TransfertMagasinService{

    private final Logger log = LoggerFactory.getLogger(TransfertMagasinServiceImpl.class);

    private final TransfertMagasinRepository transfertMagasinRepository;

    private final TransfertMagasinSearchRepository transfertMagasinSearchRepository;
    public TransfertMagasinServiceImpl(TransfertMagasinRepository transfertMagasinRepository, TransfertMagasinSearchRepository transfertMagasinSearchRepository) {
        this.transfertMagasinRepository = transfertMagasinRepository;
        this.transfertMagasinSearchRepository = transfertMagasinSearchRepository;
    }

    /**
     * Save a transfertMagasin.
     *
     * @param transfertMagasin the entity to save
     * @return the persisted entity
     */
    @Override
    public TransfertMagasin save(TransfertMagasin transfertMagasin) {
        log.debug("Request to save TransfertMagasin : {}", transfertMagasin);
        TransfertMagasin result = transfertMagasinRepository.save(transfertMagasin);
        transfertMagasinSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the transfertMagasins.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransfertMagasin> findAll(Pageable pageable) {
        log.debug("Request to get all TransfertMagasins");
        return transfertMagasinRepository.findAll(pageable);
    }

    /**
     *  Get one transfertMagasin by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransfertMagasin findOne(Long id) {
        log.debug("Request to get TransfertMagasin : {}", id);
        return transfertMagasinRepository.findOne(id);
    }

    /**
     *  Delete the  transfertMagasin by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransfertMagasin : {}", id);
        transfertMagasinRepository.delete(id);
        transfertMagasinSearchRepository.delete(id);
    }

    /**
     * Search for the transfertMagasin corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransfertMagasin> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TransfertMagasins for query {}", query);
        Page<TransfertMagasin> result = transfertMagasinSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
