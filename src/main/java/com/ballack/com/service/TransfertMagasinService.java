package com.ballack.com.service;

import com.ballack.com.domain.TransfertMagasin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TransfertMagasin.
 */
public interface TransfertMagasinService {

    /**
     * Save a transfertMagasin.
     *
     * @param transfertMagasin the entity to save
     * @return the persisted entity
     */
    TransfertMagasin save(TransfertMagasin transfertMagasin);

    /**
     *  Get all the transfertMagasins.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TransfertMagasin> findAll(Pageable pageable);

    /**
     *  Get the "id" transfertMagasin.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TransfertMagasin findOne(Long id);

    /**
     *  Delete the "id" transfertMagasin.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the transfertMagasin corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TransfertMagasin> search(String query, Pageable pageable);
}
