package com.ballack.com.service;

import com.ballack.com.domain.Article;
import com.ballack.com.domain.LigneSortieArticle;
import com.ballack.com.domain.SortieArticle;
import com.ballack.com.repository.LigneSortieArticleRepository;
import com.ballack.com.repository.SortieArticleRepository;
import com.ballack.com.repository.search.SortieArticleSearchRepository;
import com.ballack.com.web.rest.util.MapValueComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

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
    private final CaisseService caisseService;
    private final SortieArticleSearchRepository sortieArticleSearchRepository;
    private final LigneSortieArticleRepository ligneRepo;
    public SortieArticleService(SortieArticleRepository sortieArticleRepository, LigneSortieArticleService ligneSortieArticleService, UserService userService, FactureService factureService, CaisseService caisseService, SortieArticleSearchRepository sortieArticleSearchRepository, LigneSortieArticleRepository ligneRepo) {
        this.sortieArticleRepository = sortieArticleRepository;
        this.ligneSortieArticleService = ligneSortieArticleService;
        this.userService = userService;
        this.factureService = factureService;
        this.caisseService = caisseService;
        this.sortieArticleSearchRepository = sortieArticleSearchRepository;
        this.ligneRepo = ligneRepo;
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
        caisseService.update(montantttc);
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
    @Transactional(readOnly = true)
    public HashMap statVenteClient() {
        HashMap<String,Integer> hashMap=new HashMap();
        log.debug("Request to get all SortieArticles");
        List<LigneSortieArticle> ligneSortieArticleList=ligneRepo.findAll();
        List<HashMap<String,Integer>>hashMapList=new ArrayList<>();
        Comparator<Integer> valueComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer s1, Integer s2) {
                return s1.compareTo(s2);
            }
        };
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList){
            if (!hashMap.containsKey(ligneSortieArticle.getClient().getNom())){
                hashMap.putIfAbsent(ligneSortieArticle.getClient().getNom(),ligneSortieArticle.getQuantite());
                hashMapList.add(hashMap);
            }else {

                hashMap.replace(ligneSortieArticle.getClient().getNom(),hashMap.get(ligneSortieArticle.getClient().getNom())+ligneSortieArticle.getQuantite());
            }

        }
        //MapValueComparator<Article,Integer> mapComparator = new MapValueComparator<Article, Integer>(hashMap, valueComparator);


        return hashMap;
    }
    @Transactional(readOnly = true)
    public HashMap statVenteByyear(int year) {
        //int year =localDate.getYear();

        int qte = 0;
        HashMap<String,Integer> hashMap=new HashMap();

        //hashMap.put(janvier,ligneRepo.findLigneByDate(LocalDate.of(year,1,1),LocalDate.of(year,1,31)));
        log.debug("Request to get all Sortie");
        //janvier
        List<LigneSortieArticle> ligneSortieArticleList=ligneRepo.findLigneByDate(LocalDate.of(year,1,1),LocalDate.of(year,1,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList){
                qte=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("janvier",qte);

        //Fevrier
        int qte1 = 0;int jour=LocalDate.of(year,2,2).getMonth().length(false);
        List<LigneSortieArticle> ligneSortieArticleList1=ligneRepo.findLigneByDate(LocalDate.of(year,2,1),LocalDate.of(year,2,jour));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList1){
            qte1=qte1+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Fevrier",qte1);
        //Mars
        int qte2 = 0;
        List<LigneSortieArticle> ligneSortieArticleList2=ligneRepo.findLigneByDate(LocalDate.of(year,3,1),LocalDate.of(year,3,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList2){
            qte2=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Mars",qte2);
        //Avril
        int qte3 = 0;
        List<LigneSortieArticle> ligneSortieArticleList3=ligneRepo.findLigneByDate(LocalDate.of(year,4,1),LocalDate.of(year,4,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList3){
            qte3=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Avril",qte3);
        //Mai
        int qte4 = 0;
        List<LigneSortieArticle> ligneSortieArticleList4=ligneRepo.findLigneByDate(LocalDate.of(year,5,1),LocalDate.of(year,5,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList4){
            qte4=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Mai",qte4);
        //Juin
        int qte5 = 0;
        List<LigneSortieArticle> ligneSortieArticleList5=ligneRepo.findLigneByDate(LocalDate.of(year,4,1),LocalDate.of(year,4,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList5){
            qte5=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Juin",qte5);
        //Juillet
        int qte6 = 0;
        List<LigneSortieArticle> ligneSortieArticleList6=ligneRepo.findLigneByDate(LocalDate.of(year,7,1),LocalDate.of(year,7,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList6){
            qte6=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Juillet",qte6);
        //Aout
        int qte7 = 0;
        List<LigneSortieArticle> ligneSortieArticleList7=ligneRepo.findLigneByDate(LocalDate.of(year,8,1),LocalDate.of(year,8,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList7){
            qte7=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Aout",qte7);
        //Septembre
        int qte8 = 0;
        List<LigneSortieArticle> ligneSortieArticleList8=ligneRepo.findLigneByDate(LocalDate.of(year,9,1),LocalDate.of(year,9,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList8){
            qte8=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Septembre",qte8);
        //Octobre
        int qte10=0;
        List<LigneSortieArticle> ligneSortieArticleList9=ligneRepo.findLigneByDate(LocalDate.of(year,10,1),LocalDate.of(year,10,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList9){
            qte10+=ligneSortieArticle.getQuantite();
        }
        hashMap.put("Octobre",qte10);
        //Novembre
        int qte11=0;
        List<LigneSortieArticle> ligneSortieArticleList10=ligneRepo.findLigneByDate(LocalDate.of(year,11,1),LocalDate.of(year,11,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList10){
            qte11=+ligneSortieArticle.getQuantite();
        }
        hashMap.put("Novembre",qte11);
        //Decembre
        int qte12=0;
        List<LigneSortieArticle> ligneSortieArticleList11=ligneRepo.findLigneByDate(LocalDate.of(year,12,1),LocalDate.of(year,12,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList11){
            qte12+=ligneSortieArticle.getQuantite();
        }
        hashMap.put("Decembre",qte12);
        return hashMap;
    }
    @Transactional(readOnly = true)
    public HashMap statVenteByyearMontant(LocalDate localDate) {
        int year =localDate.getYear();

        double qte = 0.0;
        HashMap<String,Double> hashMap=new HashMap();

        //hashMap.put(janvier,ligneRepo.findLigneByDate(LocalDate.of(year,1,1),LocalDate.of(year,1,31)));
        log.debug("Request to get all Sortie");
        //janvier
        List<LigneSortieArticle> ligneSortieArticleList=ligneRepo.findLigneByDate(LocalDate.of(year,1,1),LocalDate.of(year,1,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList){
            qte=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("janvier",qte);

        //Fevrier
        double qte1 = 0.0;int jour=LocalDate.of(year,2,2).getMonth().length(false);
        List<LigneSortieArticle> ligneSortieArticleList1=ligneRepo.findLigneByDate(LocalDate.of(year,2,1),LocalDate.of(year,2,jour));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList1){
            qte1=qte1+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Fevrier",qte1);
        //Mars
        double qte2 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList2=ligneRepo.findLigneByDate(LocalDate.of(year,3,1),LocalDate.of(year,3,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList2){
            qte2=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Mars",qte2);
        //Avril
        double qte3 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList3=ligneRepo.findLigneByDate(LocalDate.of(year,4,1),LocalDate.of(year,4,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList3){
            qte3=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Avril",qte3);
        //Mai
        double qte4 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList4=ligneRepo.findLigneByDate(LocalDate.of(year,5,1),LocalDate.of(year,5,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList4){
            qte4=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Mai",qte4);
        //Juin
        double qte5 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList5=ligneRepo.findLigneByDate(LocalDate.of(year,4,1),LocalDate.of(year,4,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList5){
            qte5=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Juin",qte5);
        //Juillet
        double qte6 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList6=ligneRepo.findLigneByDate(LocalDate.of(year,7,1),LocalDate.of(year,7,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList6){
            qte6=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Juillet",qte6);
        //Aout
        double qte7 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList7=ligneRepo.findLigneByDate(LocalDate.of(year,8,1),LocalDate.of(year,8,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList7){
            qte7=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Aout",qte7);
        //Septembre
        double qte8 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList8=ligneRepo.findLigneByDate(LocalDate.of(year,9,1),LocalDate.of(year,9,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList8){
            qte8=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Septembre",qte8);
        //Octobre
        double qte10=0.0;
        List<LigneSortieArticle> ligneSortieArticleList9=ligneRepo.findLigneByDate(LocalDate.of(year,10,1),LocalDate.of(year,10,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList9){
            qte10+=ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Octobre",qte10);
        //Novembre
        double qte11=0.0;
        List<LigneSortieArticle> ligneSortieArticleList10=ligneRepo.findLigneByDate(LocalDate.of(year,11,1),LocalDate.of(year,11,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList10){
            qte11=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Novembre",qte11);
        //Decembre
        double qte12=0.0;
        List<LigneSortieArticle> ligneSortieArticleList11=ligneRepo.findLigneByDate(LocalDate.of(year,12,1),LocalDate.of(year,12,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList11){
            qte12+=ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Decembre",qte12);
        return hashMap;
    }
    @Transactional(readOnly = true)
    public HashMap statVente() {
        HashMap<String,Integer> hashMap=new HashMap();
        log.debug("Request to get all SortieArticles");
        List<LigneSortieArticle> ligneSortieArticleList=ligneRepo.findAll();
        List<HashMap<String,Integer>>hashMapList=new ArrayList<>();
        Comparator<Integer> valueComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer s1, Integer s2) {
                return s1.compareTo(s2);
            }
        };

        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList){
            if (!hashMap.containsKey(ligneSortieArticle.getArticle().getNomarticle())){
                hashMap.putIfAbsent(ligneSortieArticle.getArticle().getNomarticle(),ligneSortieArticle.getQuantite());
                hashMapList.add(hashMap);
            }else {

                hashMap.replace(ligneSortieArticle.getArticle().getNomarticle(),hashMap.get(ligneSortieArticle.getArticle().getNomarticle())+ligneSortieArticle.getQuantite());
            }

        }
        //MapValueComparator<Article,Integer> mapComparator = new MapValueComparator<Article, Integer>(hashMap, valueComparator);
        for (HashMap.Entry<String,Integer> map:hashMap.entrySet()){

            if (map.getValue()==1){}
        }

        return hashMap;
    }@Transactional(readOnly = true)
    public HashMap statVente(LocalDate datedebut,LocalDate datefin) {
        HashMap<String,Integer> hashMap=new HashMap();
        log.debug("Request to get all SortieArticles");
        List<LigneSortieArticle> ligneSortieArticleList=ligneRepo.findLigneByDate(datedebut,datefin);
        List<HashMap<String,Integer>>hashMapList=new ArrayList<>();
        Comparator<Integer> valueComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer s1, Integer s2) {
                return s1.compareTo(s2);
            }
        };

        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList){
            if (!hashMap.containsKey(ligneSortieArticle.getArticle())){
                hashMap.putIfAbsent(ligneSortieArticle.getArticle().getNomarticle(),ligneSortieArticle.getQuantite());

                hashMapList.add(hashMap);
            }else {

                hashMap.replace(ligneSortieArticle.getArticle().getNomarticle(),hashMap.get(ligneSortieArticle.getArticle())+ligneSortieArticle.getQuantite());
            }

        }
        //MapValueComparator<Article,Integer> mapComparator = new MapValueComparator<Article, Integer>(hashMap, valueComparator);


        return hashMap;
    }

    public HashMap statVenteByyearMontantParam(int year) {
        double qte = 0.0;
        HashMap<String,Double> hashMap=new HashMap();

        //hashMap.put(janvier,ligneRepo.findLigneByDate(LocalDate.of(year,1,1),LocalDate.of(year,1,31)));
        log.debug("Request to get all Sortie");
        //janvier
        List<LigneSortieArticle> ligneSortieArticleList=ligneRepo.findLigneByDate(LocalDate.of(year,1,1),LocalDate.of(year,1,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList){
            qte=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("janvier",qte);

        //Fevrier
        double qte1 = 0.0;int jour=LocalDate.of(year,2,2).getMonth().length(false);
        List<LigneSortieArticle> ligneSortieArticleList1=ligneRepo.findLigneByDate(LocalDate.of(year,2,1),LocalDate.of(year,2,jour));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList1){
            qte1=qte1+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Fevrier",qte1);
        //Mars
        double qte2 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList2=ligneRepo.findLigneByDate(LocalDate.of(year,3,1),LocalDate.of(year,3,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList2){
            qte2=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Mars",qte2);
        //Avril
        double qte3 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList3=ligneRepo.findLigneByDate(LocalDate.of(year,4,1),LocalDate.of(year,4,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList3){
            qte3=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Avril",qte3);
        //Mai
        double qte4 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList4=ligneRepo.findLigneByDate(LocalDate.of(year,5,1),LocalDate.of(year,5,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList4){
            qte4=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Mai",qte4);
        //Juin
        double qte5 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList5=ligneRepo.findLigneByDate(LocalDate.of(year,4,1),LocalDate.of(year,4,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList5){
            qte5=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Juin",qte5);
        //Juillet
        double qte6 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList6=ligneRepo.findLigneByDate(LocalDate.of(year,7,1),LocalDate.of(year,7,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList6){
            qte6=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Juillet",qte6);
        //Aout
        double qte7 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList7=ligneRepo.findLigneByDate(LocalDate.of(year,8,1),LocalDate.of(year,8,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList7){
            qte7=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Aout",qte7);
        //Septembre
        double qte8 = 0.0;
        List<LigneSortieArticle> ligneSortieArticleList8=ligneRepo.findLigneByDate(LocalDate.of(year,9,1),LocalDate.of(year,9,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList8){
            qte8=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Septembre",qte8);
        //Octobre
        double qte10=0.0;
        List<LigneSortieArticle> ligneSortieArticleList9=ligneRepo.findLigneByDate(LocalDate.of(year,10,1),LocalDate.of(year,10,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList9){
            qte10+=ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Octobre",qte10);
        //Novembre
        double qte11=0.0;
        List<LigneSortieArticle> ligneSortieArticleList10=ligneRepo.findLigneByDate(LocalDate.of(year,11,1),LocalDate.of(year,11,30));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList10){
            qte11=+ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Novembre",qte11);
        //Decembre
        double qte12=0.0;
        List<LigneSortieArticle> ligneSortieArticleList11=ligneRepo.findLigneByDate(LocalDate.of(year,12,1),LocalDate.of(year,12,31));
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList11){
            qte12+=ligneSortieArticle.getMontantttc();
        }
        hashMap.put("Decembre",qte12);
        return hashMap;
    }

    public HashMap statVenteBymonthMontant(int year, int monthValue) {
        HashMap<String,Double> hashMap=new HashMap();
        log.debug("Request to get all SortieArticles");
        int jour=LocalDate.of(year,monthValue,2).getMonth().length(false);
        //System.out.println(LocalDate.of(year,monthValue,2).getMonth().maxLength());
        List<LigneSortieArticle> ligneSortieArticleList=ligneRepo.findLigneByDate(LocalDate.of(year,monthValue,1),LocalDate.of(year,monthValue,jour));
        List<HashMap<String,Double>>hashMapList=new ArrayList<>();
        for (LigneSortieArticle ligneSortieArticle:ligneSortieArticleList){
            if (!hashMap.containsKey(ligneSortieArticle.getArticle().getNomarticle())){
                hashMap.putIfAbsent(ligneSortieArticle.getArticle().getNomarticle(),ligneSortieArticle.getMontantttc());
                hashMapList.add(hashMap);
            }else {

                hashMap.replace(ligneSortieArticle.getArticle().getNomarticle(),hashMap.get(ligneSortieArticle.getArticle().getNomarticle())+ligneSortieArticle.getMontantttc());
            }

        }
                return hashMap;
    }
}
