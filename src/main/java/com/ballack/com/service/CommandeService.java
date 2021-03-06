package com.ballack.com.service;

import com.ballack.com.config.ApplicationProperties;
import com.ballack.com.domain.Commande;
import com.ballack.com.domain.LigneCommande;
import com.ballack.com.domain.LigneSortieArticle;
import com.ballack.com.repository.CommandeRepository;
import com.ballack.com.repository.LigneCommandeRepository;
import com.ballack.com.repository.search.CommandeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Commande.
 */
@Service
@Transactional
public class CommandeService {

    private final Logger log = LoggerFactory.getLogger(CommandeService.class);

    private final CommandeRepository commandeRepository;
    private final LigneCommandeService ligneCommandeService;
    private final CommandeSearchRepository commandeSearchRepository;
    private final UserService userService;
    private final LigneCommandeRepository ligneCommandeRepository;
    private final ApplicationProperties applicationProperties;
    public CommandeService(CommandeRepository commandeRepository, LigneCommandeService ligneCommandeService, CommandeSearchRepository commandeSearchRepository, UserService userService, LigneCommandeRepository ligneCommandeRepository, ApplicationProperties applicationProperties) {
        this.commandeRepository = commandeRepository;
        this.ligneCommandeService = ligneCommandeService;
        this.commandeSearchRepository = commandeSearchRepository;
        this.userService = userService;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a commande.
     *
     * @param ligneCommandes the entity to save
     * @return the persisted entity
     */
    public Commande save(Set<LigneCommande> ligneCommandes) {
        log.debug("Request to save Commande : {}", ligneCommandes);
        Commande commande=new Commande();
/*        LocalDateTime now=LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S a");
        String fomat=now.format(formatter);*/
        //LocalDateTime formatDateTime = LocalDateTime.parse(iso8601);
        commande.setDatecommande(Instant.now());
        commande.setDatelimitlivraison(LocalDate.now().plusMonths(applicationProperties.getCommande().getDatelimite()));
        commande.setAgent(userService.getUserWithAuthorities());
        commande.setEtat(false);
        commande.setSoldee(false);
        Commande commande1 = commandeRepository.save(commande);

        double montantht=0.0;
        double montantttc=0.0;
        for (LigneCommande ligneCommande:ligneCommandes){
            ligneCommande.setDesignation("LNC/"+"CMD"+commande1.getId());
            ligneCommande.setCommande(commande1);
            LigneCommande ligneCommande1= ligneCommandeService.save(ligneCommande);
            montantht+=ligneCommande1.getMontanttotalht();
            montantttc+=ligneCommande1.getMontanttotalttc();
            if(commande1.getFournisseur()==null){
                commande1.setFournisseur(ligneCommande1.getFournisseur());
            }
            if(commande1.getMagasin()==null){
                commande1.setMagasin(ligneCommande1.getMagasin());
            }
        }
        commande1.setNumcommande("CMD_"+(applicationProperties.getFacture().getIndice()+commande1.getId()));
        commande1.setMontanttotalttc(montantttc);
        commande1.setMontanttotalht(montantht);
        Commande result = commandeRepository.save(commande);
        //commandeSearchRepository.save(result);
        return result;
    }
public Commande save(Commande commande){

    Commande result = commandeRepository.save(commande);
    //commandeSearchRepository.save(result);
    return result;
}
    /**
     *  Get all the commandes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Commande> findAll(Pageable pageable) {
        log.debug("Request to get all Commandes");
        return commandeRepository.findAll(pageable);
    }
    public HashMap statCommande() {
        HashMap<String,Integer> hashMap=new HashMap();
        log.debug("Request to get all SortieArticles");
        List<LigneCommande> ligneCommandeList=ligneCommandeRepository.findAll();
        List<HashMap<String,Integer>>hashMapList=new ArrayList<>();
        Comparator<Integer> valueComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer s1, Integer s2) {
                return s1.compareTo(s2);
            }
        };

        for (LigneCommande ligneCommande:ligneCommandeList){
            if (!hashMap.containsKey(ligneCommande.getArticle().getNomarticle())){
                hashMap.putIfAbsent(ligneCommande.getArticle().getNomarticle(),ligneCommande.getQuantite());
                hashMapList.add(hashMap);
            }else {

                hashMap.replace(ligneCommande.getArticle().getNomarticle(),hashMap.get(ligneCommande.getArticle().getNomarticle())+ligneCommande.getQuantite());
            }

        }
        //MapValueComparator<Article,Integer> mapComparator = new MapValueComparator<Article, Integer>(hashMap, valueComparator);


        return hashMap;
    }
    /**
     *  Get one commande by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Commande findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        return commandeRepository.findOne(id);
    }

    /**
     *  Delete the  commande by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        commandeRepository.delete(id);
        //commandeSearchRepository.delete(id);
    }

    /**
     * Search for the commande corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Commande> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Commandes for query {}", query);
        Page<Commande> result = commandeSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
