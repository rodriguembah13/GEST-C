package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.LigneCommande;
import com.ballack.com.repository.LigneCommandeRepository;
import com.ballack.com.service.LigneCommandeService;
import com.ballack.com.repository.search.LigneCommandeSearchRepository;
import com.ballack.com.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LigneCommandeResource REST controller.
 *
 * @see LigneCommandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class LigneCommandeResourceIntTest {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Double DEFAULT_MONTANTTOTALHT = 1D;
    private static final Double UPDATED_MONTANTTOTALHT = 2D;

    private static final Double DEFAULT_MONTANTTOTALTTC = 1D;
    private static final Double UPDATED_MONTANTTOTALTTC = 2D;

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    private LigneCommandeService ligneCommandeService;

    @Autowired
    private LigneCommandeSearchRepository ligneCommandeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLigneCommandeMockMvc;

    private LigneCommande ligneCommande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LigneCommandeResource ligneCommandeResource = new LigneCommandeResource(ligneCommandeService);
        this.restLigneCommandeMockMvc = MockMvcBuilders.standaloneSetup(ligneCommandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneCommande createEntity(EntityManager em) {
        LigneCommande ligneCommande = new LigneCommande()
            .designation(DEFAULT_DESIGNATION)
            .quantite(DEFAULT_QUANTITE)
            .montanttotalht(DEFAULT_MONTANTTOTALHT)
            .montanttotalttc(DEFAULT_MONTANTTOTALTTC)
            .prix(DEFAULT_PRIX);
        return ligneCommande;
    }

    @Before
    public void initTest() {
        ligneCommandeSearchRepository.deleteAll();
        ligneCommande = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneCommande() throws Exception {
        int databaseSizeBeforeCreate = ligneCommandeRepository.findAll().size();

        // Create the LigneCommande
        restLigneCommandeMockMvc.perform(post("/api/ligne-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isCreated());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeCreate + 1);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneCommande.getMontanttotalht()).isEqualTo(DEFAULT_MONTANTTOTALHT);
        assertThat(testLigneCommande.getMontanttotalttc()).isEqualTo(DEFAULT_MONTANTTOTALTTC);
        assertThat(testLigneCommande.getPrix()).isEqualTo(DEFAULT_PRIX);

        // Validate the LigneCommande in Elasticsearch
        LigneCommande ligneCommandeEs = ligneCommandeSearchRepository.findOne(testLigneCommande.getId());
        assertThat(ligneCommandeEs).isEqualToComparingFieldByField(testLigneCommande);
    }

    @Test
    @Transactional
    public void createLigneCommandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneCommandeRepository.findAll().size();

        // Create the LigneCommande with an existing ID
        ligneCommande.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneCommandeMockMvc.perform(post("/api/ligne-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLigneCommandes() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        // Get all the ligneCommandeList
        restLigneCommandeMockMvc.perform(get("/api/ligne-commandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montanttotalht").value(hasItem(DEFAULT_MONTANTTOTALHT.doubleValue())))
            .andExpect(jsonPath("$.[*].montanttotalttc").value(hasItem(DEFAULT_MONTANTTOTALTTC.doubleValue())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }

    @Test
    @Transactional
    public void getLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeRepository.saveAndFlush(ligneCommande);

        // Get the ligneCommande
        restLigneCommandeMockMvc.perform(get("/api/ligne-commandes/{id}", ligneCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ligneCommande.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.montanttotalht").value(DEFAULT_MONTANTTOTALHT.doubleValue()))
            .andExpect(jsonPath("$.montanttotalttc").value(DEFAULT_MONTANTTOTALTTC.doubleValue()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLigneCommande() throws Exception {
        // Get the ligneCommande
        restLigneCommandeMockMvc.perform(get("/api/ligne-commandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeService.save(ligneCommande);

        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();

        // Update the ligneCommande
        LigneCommande updatedLigneCommande = ligneCommandeRepository.findOne(ligneCommande.getId());
        updatedLigneCommande
            .designation(UPDATED_DESIGNATION)
            .quantite(UPDATED_QUANTITE)
            .montanttotalht(UPDATED_MONTANTTOTALHT)
            .montanttotalttc(UPDATED_MONTANTTOTALTTC)
            .prix(UPDATED_PRIX);

        restLigneCommandeMockMvc.perform(put("/api/ligne-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLigneCommande)))
            .andExpect(status().isOk());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate);
        LigneCommande testLigneCommande = ligneCommandeList.get(ligneCommandeList.size() - 1);
        assertThat(testLigneCommande.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testLigneCommande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneCommande.getMontanttotalht()).isEqualTo(UPDATED_MONTANTTOTALHT);
        assertThat(testLigneCommande.getMontanttotalttc()).isEqualTo(UPDATED_MONTANTTOTALTTC);
        assertThat(testLigneCommande.getPrix()).isEqualTo(UPDATED_PRIX);

        // Validate the LigneCommande in Elasticsearch
        LigneCommande ligneCommandeEs = ligneCommandeSearchRepository.findOne(testLigneCommande.getId());
        assertThat(ligneCommandeEs).isEqualToComparingFieldByField(testLigneCommande);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneCommande() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeRepository.findAll().size();

        // Create the LigneCommande

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLigneCommandeMockMvc.perform(put("/api/ligne-commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneCommande)))
            .andExpect(status().isCreated());

        // Validate the LigneCommande in the database
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeService.save(ligneCommande);

        int databaseSizeBeforeDelete = ligneCommandeRepository.findAll().size();

        // Get the ligneCommande
        restLigneCommandeMockMvc.perform(delete("/api/ligne-commandes/{id}", ligneCommande.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ligneCommandeExistsInEs = ligneCommandeSearchRepository.exists(ligneCommande.getId());
        assertThat(ligneCommandeExistsInEs).isFalse();

        // Validate the database is empty
        List<LigneCommande> ligneCommandeList = ligneCommandeRepository.findAll();
        assertThat(ligneCommandeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneCommande() throws Exception {
        // Initialize the database
        ligneCommandeService.save(ligneCommande);

        // Search the ligneCommande
        restLigneCommandeMockMvc.perform(get("/api/_search/ligne-commandes?query=id:" + ligneCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montanttotalht").value(hasItem(DEFAULT_MONTANTTOTALHT.doubleValue())))
            .andExpect(jsonPath("$.[*].montanttotalttc").value(hasItem(DEFAULT_MONTANTTOTALTTC.doubleValue())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCommande.class);
        LigneCommande ligneCommande1 = new LigneCommande();
        ligneCommande1.setId(1L);
        LigneCommande ligneCommande2 = new LigneCommande();
        ligneCommande2.setId(ligneCommande1.getId());
        assertThat(ligneCommande1).isEqualTo(ligneCommande2);
        ligneCommande2.setId(2L);
        assertThat(ligneCommande1).isNotEqualTo(ligneCommande2);
        ligneCommande1.setId(null);
        assertThat(ligneCommande1).isNotEqualTo(ligneCommande2);
    }
}
