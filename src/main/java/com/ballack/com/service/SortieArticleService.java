package com.ballack.com.service;

import com.ballack.com.domain.LigneSortieArticle;
import com.ballack.com.domain.SortieArticle;
import com.ballack.com.repository.SortieArticleRepository;
import com.ballack.com.repository.search.SortieArticleSearchRepository;
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
 * Service Implementation for managing SortieArticle.
 */
@Service
@Transactional
public class SortieArticleService {

    private final Logger log = LoggerFactory.getLogger(SortieArticleService.class);

    private final SortieArticleRepository sortieArticleRepository;
    private final LigneSortieArticleService ligneSortieArticleService;
    private final UserService userService;
    private final FactureService factureService;
    private final SortieArticleSearchRepository sortieArticleSearchRepository;
    public SortieArticleService(SortieArticleRepository sortieArticleRepository, LigneSortieArticleService ligneSortieArticleService, UserService userService, FactureService factureService, SortieArticleSearchRepository sortieArticleSearchRepository) {
        this.sortieArticleRepository = sortieArticleRepository;
        this.ligneSortieArticleService = ligneSortieArticleService;
        this.userService = userService;
        this.factureService = factureService;
        this.sortieArticleSearchRepository = sortieArticleSearchRepository;
    }

    /**
     * Save a sortieArticle.
     *
     * @param ligneSortieArticles the entity to save
     * @return the persisted entity
     */
    public SortieArticle saveAll(Set<LigneSortieArticle> ligneSortieArticles) {
        log.debug("Request to save SortieArticle : {}", ligneSortieArticles);
        SortieArticle sortieArticle=new SortieArticle();
        sortieArticle.setDatesortie(LocalDate.now());
        sortieArticle.setAgent(userService.getUserWithAuthorities());

        SortieArticle sortieArticle1 = sortieArticleRepository.save(sortieArticle);
        double montantht=0.0;
        double montantttc=0.0;
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticles){
            ligneSortieArticle.setSortieArticle(sortieArticle1);
            if (sortieArticle1.getClient()==null){
                sortieArticle1.setClient(ligneSortieArticle.getClient());
            }
            LigneSortieArticle ligneSortieArticle1= ligneSortieArticleService.save(ligneSortieArticle);
            montantht+=ligneSortieArticle1.getMontantht();
            montantttc+=ligneSortieArticle1.getMontantttc();

        }
        sortieArticle1.setMontantttc(montantttc);
        sortieArticle1.setMontanttva(montantttc-montantht);
        sortieArticle1.setMontanttotal(montantht);
        sortieArticle1.setLibelle("OPÉRATION VENTE"+sortieArticle1.getId());
        sortieArticle1.setNumsortie("N°VM/"+sortieArticle1.getId());
        SortieArticle result = sortieArticleRepository.saveAndFlush(sortieArticle1);
        sortieArticleSearchRepository.save(result);
        factureService.saveFactureSortie(result);
        return result;
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
     *  Get all the sortieArticles by user.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SortieArticle> findAllbyUser(Pageable pageable) {
        log.debug("Request to get all SortieArticles");
        return sortieArticleRepository.findByAgentIsCurrentUser(pageable);
    }


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
