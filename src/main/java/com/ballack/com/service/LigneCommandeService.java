package com.ballack.com.service;

import com.ballack.com.domain.LigneCommande;
import com.ballack.com.repository.LigneCommandeRepository;
import com.ballack.com.repository.search.LigneCommandeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LigneCommande.
 */
@Service
@Transactional
public class LigneCommandeService {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeService.class);

    private final LigneCommandeRepository ligneCommandeRepository;
    private final UserService userService;
    private final LigneCommandeSearchRepository ligneCommandeSearchRepository;
    public LigneCommandeService(LigneCommandeRepository ligneCommandeRepository, UserService userService, LigneCommandeSearchRepository ligneCommandeSearchRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.userService = userService;
        this.ligneCommandeSearchRepository = ligneCommandeSearchRepository;
    }

    /**
     * Save a ligneCommande.
     *
     * @param ligneCommande the entity to save
     * @return the persisted entity
     */
    public LigneCommande save(LigneCommande ligneCommande) {
        ligneCommande.setMontanttotalht(ligneCommande.getPrix()*ligneCommande.getQuantite());
        ligneCommande.setMontanttotalttc((ligneCommande.getMontanttotalht()*ligneCommande.getTaxeTva())*0.01+ligneCommande.getMontanttotalht());
        ligneCommande.setAgent(userService.getUserWithAuthorities());
        log.debug("Request to save LigneCommande : {}", ligneCommande);
        LigneCommande result = ligneCommandeRepository.save(ligneCommande);
        ligneCommandeSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the ligneCommandes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneCommande> findAll(Pageable pageable) {
        log.debug("Request to get all LigneCommandes");
        return ligneCommandeRepository.findAll(pageable);
    }

    /**
     *  Get one ligneCommande by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LigneCommande findOne(Long id) {
        log.debug("Request to get LigneCommande : {}", id);
        return ligneCommandeRepository.findOne(id);
    }

    /**
     *  Delete the  ligneCommande by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneCommande : {}", id);
        ligneCommandeRepository.delete(id);
        ligneCommandeSearchRepository.delete(id);
    }

    /**
     * Search for the ligneCommande corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LigneCommande> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LigneCommandes for query {}", query);
        Page<LigneCommande> result = ligneCommandeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
