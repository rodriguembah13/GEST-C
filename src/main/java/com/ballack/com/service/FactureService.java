package com.ballack.com.service;

import com.ballack.com.domain.Facture;
import com.ballack.com.domain.SortieArticle;
import com.ballack.com.repository.CaisseRepository;
import com.ballack.com.repository.FactureRepository;
import com.ballack.com.repository.search.FactureSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Facture.
 */
@Service
@Transactional
public class FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureService.class);
    private final CaisseRepository caisseRepository;
    private final FactureRepository factureRepository;

    private final FactureSearchRepository factureSearchRepository;
    public FactureService(CaisseRepository caisseRepository, FactureRepository factureRepository, FactureSearchRepository factureSearchRepository) {
        this.caisseRepository = caisseRepository;
        this.factureRepository = factureRepository;
        this.factureSearchRepository = factureSearchRepository;
    }

    /**
     * Save a facture.
     *
     * @param facture the entity to save
     * @return the persisted entity
     */
    public Facture save(Facture facture) {
        log.debug("Request to save Facture : {}", facture);
        Facture result = factureRepository.save(facture);
        factureSearchRepository.save(result);
        return result;
    }
    /**
     * Save a facture for sortir article.
     *
     * @param sortieArticle the entity to save
     * @return the persisted entity
     */
    public Facture saveFactureSortie(SortieArticle sortieArticle) {
        log.debug("Request to save Facture : {}", sortieArticle);
        Facture facture=new Facture();
        facture.datefacturation(LocalDate.now());
        facture.setMagasin(sortieArticle.getMagasin());
        facture.setUser(sortieArticle.getAgent());
        facture.setClient(sortieArticle.getClient());
        facture.setCaisse(caisseRepository.findByUserIsCurrentActif());
        facture.setMontanttotalttc(sortieArticle.getMontantttc());
        facture.setMontanttotalht(sortieArticle.getMontanttotal());
        facture.setMagasin(sortieArticle.getMagasin());
        Facture result = factureRepository.save(facture);
        factureSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the factures.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Facture> findAll(Pageable pageable) {
        log.debug("Request to get all Factures");
        return factureRepository.findAll(pageable);
    }
    /**
     *  Get all the factures.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Facture> findAllbyUser(Pageable pageable) {
        log.debug("Request to get all Factures");
        return factureRepository.findByUserIsCurrentUser(pageable);
    }
    /**
     *  Get one facture by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Facture findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        return factureRepository.findOne(id);
    }

    /**
     *  Delete the  facture by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.delete(id);
        factureSearchRepository.delete(id);
    }

    /**
     * Search for the facture corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Facture> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Factures for query {}", query);
        Page<Facture> result = factureSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
