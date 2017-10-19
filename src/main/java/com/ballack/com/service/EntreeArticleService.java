package com.ballack.com.service;

import com.ballack.com.domain.EntreeArticle;
import com.ballack.com.domain.LigneEntreeArticle;
import com.ballack.com.repository.EntreeArticleRepository;
import com.ballack.com.repository.search.EntreeArticleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing EntreeArticle.
 */
@Service
@Transactional
public class EntreeArticleService {

    private final Logger log = LoggerFactory.getLogger(EntreeArticleService.class);

    private final EntreeArticleRepository entreeArticleRepository;
    private final LigneEntreeArticleService ligneEntreeArticleService;
    private final UserService userService;

    private final EntreeArticleSearchRepository entreeArticleSearchRepository;
    public EntreeArticleService(EntreeArticleRepository entreeArticleRepository, LigneEntreeArticleService ligneEntreeArticleService, UserService userService, EntreeArticleSearchRepository entreeArticleSearchRepository) {
        this.entreeArticleRepository = entreeArticleRepository;
        this.ligneEntreeArticleService = ligneEntreeArticleService;
        this.userService = userService;
        this.entreeArticleSearchRepository = entreeArticleSearchRepository;
    }

    /**
     * Save a entreeArticle.
     *
     * @param ligneEntreeArticleSet the entity to save
     * @return the persisted entity
     */
    public EntreeArticle saveAll(Set<LigneEntreeArticle> ligneEntreeArticleSet) {
        log.debug("Request to save EntreeArticle : {}", ligneEntreeArticleSet);
        EntreeArticle entreeArticle=new EntreeArticle();
        entreeArticle.setDateentre(LocalDate.now());
        entreeArticle.setAgent(userService.getUserWithAuthorities());
        entreeArticle.setObservation("Bordereau D'ENTRÉ");
        EntreeArticle entreeArticle1 = entreeArticleRepository.save(entreeArticle);
        double montantht=0.0;
        double montantttc=0.0;
        int ite=1;
        for (LigneEntreeArticle ligneEntreeArticle:ligneEntreeArticleSet){
            ligneEntreeArticle.setDesignation("LE/"+entreeArticle1.getTitre()+"/"+ite);
            ite++;
            ligneEntreeArticle.setEntreeArticle(entreeArticle1);
            LigneEntreeArticle ligneEntreeArticle1=ligneEntreeArticleService.save(ligneEntreeArticle);
            montantht+=ligneEntreeArticle1.getMontanttotalht();
            montantttc+=ligneEntreeArticle1.getMontanttotalttc();
        }
        entreeArticle1.setMontant_ht(montantht);
        entreeArticle1.setMontant_ttc(montantttc);
        entreeArticle1.setTitre("BD/EA"+entreeArticle.getId());
        EntreeArticle result = entreeArticleRepository.save(entreeArticle1);
        entreeArticleSearchRepository.save(result);
        return result;
    }
    /**
     * Save a entreeArticle.
     *
     * @param entreeArticle the entity to save
     * @return the persisted entity
     */
    public EntreeArticle save(EntreeArticle entreeArticle) {
        log.debug("Request to save EntreeArticle : {}", entreeArticle);
        EntreeArticle result = entreeArticleRepository.save(entreeArticle);
        entreeArticleSearchRepository.save(result);
        return result;
    }
    /**
     *  Get all the entreeArticles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EntreeArticle> findAll(Pageable pageable) {
        log.debug("Request to get all EntreeArticles");
        return entreeArticleRepository.findAll(pageable);
    }

    /**
     *  Get one entreeArticle by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EntreeArticle findOne(Long id) {
        log.debug("Request to get EntreeArticle : {}", id);
        return entreeArticleRepository.findOne(id);
    }

    /**
     *  Delete the  entreeArticle by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EntreeArticle : {}", id);
        entreeArticleRepository.delete(id);
        entreeArticleSearchRepository.delete(id);
    }

    /**
     * Search for the entreeArticle corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EntreeArticle> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EntreeArticles for query {}", query);
        Page<EntreeArticle> result = entreeArticleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
