package com.ballack.com.service;

import com.ballack.com.domain.Caisse;
import com.ballack.com.domain.User;
import com.ballack.com.repository.CaisseRepository;
import com.ballack.com.repository.search.CaisseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Caisse.
 */
@Service
@Transactional
public class CaisseService {

    private final Logger log = LoggerFactory.getLogger(CaisseService.class);

    private final CaisseRepository caisseRepository;
    private final UserService userService;
    private final CaisseSearchRepository caisseSearchRepository;
    public CaisseService(CaisseRepository caisseRepository, UserService userService, CaisseSearchRepository caisseSearchRepository) {
        this.caisseRepository = caisseRepository;
        this.userService = userService;
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
        caisse.setDateOuverture(LocalDate.now());

        Caisse result = caisseRepository.save(caisse);
        caisseSearchRepository.save(result);
        return result;
    }
    /**
     * Save a caisse.
     *
     * @return the persisted entity
     */
    public Caisse create() {
        User user=userService.getUserWithAuthorities();
        log.debug("Request to save Caisse : {}", user);
        int te=0;//compte les caisse de l'user actif
        List<Caisse> caisseList=caisseRepository.findByUserIsCurrentUser();
        for (Caisse caisse:caisseList){
            if (caisse.isActive()){
                te+=1;
            }
        }
        Caisse result = new Caisse();
        if (te<=0){
            Caisse caisse=new Caisse();
            caisse.setUser(user);
            caisse.setDateOuverture(LocalDate.now());
            caisse.setNumcaisse("CAISSE_"+arrondi2(Math.random(),4));
            caisse.setActive(true);
            result = caisseRepository.save(caisse);
            caisseSearchRepository.save(result);
        }
        return result;
    }
    public Caisse ferme() {

        Caisse caisse=caisseRepository.findByUserIsCurrentActif();
        caisse.setDateFermeture(LocalDate.now());
        caisse.setActive(false);
        Caisse result = caisseRepository.saveAndFlush(caisse);
        caisseSearchRepository.save(result);
        return result;
    }
    public static int arrondi2(double A, int B) {
        return ((int) (A * Math.pow(10, B) + .5));
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
     *  Get one caisse by id.
     *
     *
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Caisse findCaisseActif() {
        log.debug("Request to get Caisse : {}");
        return caisseRepository.findByUserIsCurrentActif();
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
