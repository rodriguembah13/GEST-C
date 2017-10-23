package com.ballack.com.service;

import com.ballack.com.domain.CustomUser;
import com.ballack.com.repository.CustomUserRepository;
import com.ballack.com.repository.search.CustomUserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CustomUser.
 */
@Service
@Transactional
public class CustomUserService {

    private final Logger log = LoggerFactory.getLogger(CustomUserService.class);

    private final CustomUserRepository customUserRepository;

    private final CustomUserSearchRepository customUserSearchRepository;
    public CustomUserService(CustomUserRepository customUserRepository, CustomUserSearchRepository customUserSearchRepository) {
        this.customUserRepository = customUserRepository;
        this.customUserSearchRepository = customUserSearchRepository;
    }

    /**
     * Save a customUser.
     *
     * @param customUser the entity to save
     * @return the persisted entity
     */
    public CustomUser save(CustomUser customUser) {
        log.debug("Request to save CustomUser : {}", customUser);
        CustomUser result = customUserRepository.save(customUser);
        customUserSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the customUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomUser> findAll(Pageable pageable) {
        log.debug("Request to get all CustomUsers");
        return customUserRepository.findAll(pageable);
    }

    /**
     *  Get one customUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CustomUser findOne(Long id) {
        log.debug("Request to get CustomUser : {}", id);
        return customUserRepository.findOne(id);
    }

    /**
     *  Delete the  customUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomUser : {}", id);
        customUserRepository.delete(id);
        customUserSearchRepository.delete(id);
    }

    /**
     * Search for the customUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomUsers for query {}", query);
        Page<CustomUser> result = customUserSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
