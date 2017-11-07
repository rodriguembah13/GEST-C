package com.ballack.com.service;

/**
 * Created by ballack on 06/11/2017.
 */

import com.ballack.com.domain.Etiquette;
import com.ballack.com.repository.EtiquetteRepository;
import com.ballack.com.repository.search.EtiquetteSearchRepository;
import com.ballack.com.service.util.StringTab;
import org.hibernate.id.UUIDGenerationStrategy;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.UID;
import java.time.Instant;
import java.util.UUID;

/**
 * Service Implementation for managing Etiquette.
 */
@Service
@Transactional
public class EtiquetteService {
    private final EtiquetteRepository etiquetteRepository;
    private final Logger log = LoggerFactory.getLogger(EtiquetteService.class);
    private final EtiquetteSearchRepository etiquetteSearchRepository;

    public EtiquetteService(EtiquetteRepository etiquetteRepository, EtiquetteSearchRepository etiquetteSearchRepository) {
        this.etiquetteRepository = etiquetteRepository;
        this.etiquetteSearchRepository = etiquetteSearchRepository;
    }
    /**
     * Save a etiquette.
     *
     * @param etiquette the entity to save
     * @return the persisted entity
     */
    public Etiquette save(Etiquette etiquette) {
        UUID uuid = UUID.randomUUID();
        StringTab stringTab = new StringTab();
        log.debug("Request to save Etiquette : {}", etiquette);
        etiquette.setDateCreation(Instant.now());
        etiquette.setCodeBare(stringTab.getString(etiquette.getArticle().getNomarticle(),12));
        etiquette.setEtiquette(etiquette.getArticle().getNomarticle());
        Etiquette result = etiquetteRepository.save(etiquette);
        etiquetteSearchRepository.save(result);
        return result;
    }
    /**
     * Get all the decompositions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Etiquette> findAll(Pageable pageable) throws Exception{
        UUID uuid = UUID.randomUUID();
        StringTab stringTab = new StringTab();
        String s="30KGNATI";

        //Double test=Math.random();
       /* UUID uuid1 = UUID.fromString(uuid.toString());
        UUID uuid2 = UUID.fromString("bbbbb");*/

/*        System.out.println(stringTab.getTabString("ballack"));
        char [] tabchar=stringTab.getTabString("ballack");
        int[] tab = new int [9];
        for (int i = 0; i < 9; i++) {
            tab[i] = stringTab.getIntTab(tabchar[i]);
            System.out.println(tab[i]);
        }System.out.println(tab.length);*/
        System.out.println(stringTab.getString(s.toLowerCase(),10));
        //System.out.println(tab.hashCode());
        //System.out.println(tab);
        ;/*
        System.out.println(uuid1);
        System.out.println(uuid1.clockSequence());
        System.out.println(uuid1.getMostSignificantBits());
        System.out.println(uuid2);
        System.out.println(uuid2.clockSequence());
        System.out.println(uuid2.getMostSignificantBits());*/
     /*   for (int i=0;i<10;i++){
            UUIDGenerator uuidGenerator=UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator();
            UUIDHexGenerator uuidHexGenerator= new UUIDHexGenerator();
            Double test=Math.random();
           // System.out.println(uuidGenerator.hashCode());
           // System.out.println(uuidHexGenerator.hashCode());
            System.out.println(test);
            System.out.println(test.hashCode());
            System.out.println(test.intValue());
        }*/
        log.debug("Request to get all Etiquettes");
        return etiquetteRepository.findAll(pageable);
    }

}
