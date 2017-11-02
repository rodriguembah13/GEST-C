package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.LigneEntreeArticle;
import com.ballack.com.repository.LigneEntreeArticleRepository;
import com.ballack.com.service.LigneEntreeArticleService;
import com.ballack.com.repository.search.LigneEntreeArticleSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LigneEntreeArticleResource REST controller.
 *
 * @see LigneEntreeArticleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class LigneEntreeArticleResourceIntTest {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Double DEFAULT_MONTANTTOTALHT = 1D;
    private static final Double UPDATED_MONTANTTOTALHT = 2D;

    private static final Double DEFAULT_MONTANTTOTALTTC = 1D;
    private static final Double UPDATED_MONTANTTOTALTTC = 2D;

    private static final Double DEFAULT_PRIX_UNITAIRE = 1D;
    private static final Double UPDATED_PRIX_UNITAIRE = 2D;

    private static final Double DEFAULT_TAXE_TVA = 1D;
    private static final Double UPDATED_TAXE_TVA = 2D;

    private static final LocalDate DEFAULT_DATEPEREMPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEPEREMPTION = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_PRIX_ACHAT = 1D;
    private static final Double UPDATED_PRIX_ACHAT = 2D;

    @Autowired
    private LigneEntreeArticleRepository ligneEntreeArticleRepository;

    @Autowired
    private LigneEntreeArticleService ligneEntreeArticleService;

    @Autowired
    private LigneEntreeArticleSearchRepository ligneEntreeArticleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLigneEntreeArticleMockMvc;

    private LigneEntreeArticle ligneEntreeArticle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LigneEntreeArticleResource ligneEntreeArticleResource = new LigneEntreeArticleResource(ligneEntreeArticleService);
        this.restLigneEntreeArticleMockMvc = MockMvcBuilders.standaloneSetup(ligneEntreeArticleResource)
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
    public static LigneEntreeArticle createEntity(EntityManager em) {
        LigneEntreeArticle ligneEntreeArticle = new LigneEntreeArticle()
            .designation(DEFAULT_DESIGNATION)
            .quantite(DEFAULT_QUANTITE)
            .montanttotalht(DEFAULT_MONTANTTOTALHT)
            .montanttotalttc(DEFAULT_MONTANTTOTALTTC)
            .prix_unitaire(DEFAULT_PRIX_UNITAIRE)
            .taxeTVA(DEFAULT_TAXE_TVA)
            .dateperemption(DEFAULT_DATEPEREMPTION)
            .prixAchat(DEFAULT_PRIX_ACHAT);
        return ligneEntreeArticle;
    }

    @Before
    public void initTest() {
        //ligneEntreeArticleSearchRepository.deleteAll();
        ligneEntreeArticle = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneEntreeArticle() throws Exception {
        int databaseSizeBeforeCreate = ligneEntreeArticleRepository.findAll().size();

        // Create the LigneEntreeArticle
        restLigneEntreeArticleMockMvc.perform(post("/api/ligne-entree-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeArticle)))
            .andExpect(status().isCreated());

        // Validate the LigneEntreeArticle in the database
        List<LigneEntreeArticle> ligneEntreeArticleList = ligneEntreeArticleRepository.findAll();
        assertThat(ligneEntreeArticleList).hasSize(databaseSizeBeforeCreate + 1);
        LigneEntreeArticle testLigneEntreeArticle = ligneEntreeArticleList.get(ligneEntreeArticleList.size() - 1);
        assertThat(testLigneEntreeArticle.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testLigneEntreeArticle.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneEntreeArticle.getMontanttotalht()).isEqualTo(DEFAULT_PRIX_UNITAIRE*DEFAULT_QUANTITE);
        assertThat(testLigneEntreeArticle.getMontanttotalttc()).isEqualTo((DEFAULT_MONTANTTOTALHT*DEFAULT_TAXE_TVA)*0.01+DEFAULT_MONTANTTOTALHT);
        assertThat(testLigneEntreeArticle.getPrix_unitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testLigneEntreeArticle.getTaxeTVA()).isEqualTo(DEFAULT_TAXE_TVA);
        assertThat(testLigneEntreeArticle.getDateperemption()).isEqualTo(DEFAULT_DATEPEREMPTION);
        assertThat(testLigneEntreeArticle.getPrixAchat()).isEqualTo(DEFAULT_PRIX_ACHAT);

        // Validate the LigneEntreeArticle in Elasticsearch
        //LigneEntreeArticle ligneEntreeArticleEs = ligneEntreeArticleSearchRepository.findOne(testLigneEntreeArticle.getId());
        //assertThat(ligneEntreeArticleEs).isEqualToComparingFieldByField(testLigneEntreeArticle);
    }

    @Test
    @Transactional
    public void createLigneEntreeArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneEntreeArticleRepository.findAll().size();

        // Create the LigneEntreeArticle with an existing ID
        ligneEntreeArticle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneEntreeArticleMockMvc.perform(post("/api/ligne-entree-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeArticle)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LigneEntreeArticle> ligneEntreeArticleList = ligneEntreeArticleRepository.findAll();
        assertThat(ligneEntreeArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLigneEntreeArticles() throws Exception {
        // Initialize the database
        ligneEntreeArticleRepository.saveAndFlush(ligneEntreeArticle);

        // Get all the ligneEntreeArticleList
        restLigneEntreeArticleMockMvc.perform(get("/api/ligne-entree-articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneEntreeArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montanttotalht").value(hasItem(DEFAULT_MONTANTTOTALHT.doubleValue())))
            .andExpect(jsonPath("$.[*].montanttotalttc").value(hasItem(DEFAULT_MONTANTTOTALTTC.doubleValue())))
            .andExpect(jsonPath("$.[*].prix_unitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxeTVA").value(hasItem(DEFAULT_TAXE_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].dateperemption").value(hasItem(DEFAULT_DATEPEREMPTION.toString())))
            .andExpect(jsonPath("$.[*].prixAchat").value(hasItem(DEFAULT_PRIX_ACHAT.doubleValue())));
    }

    @Test
    @Transactional
    public void getLigneEntreeArticle() throws Exception {
        // Initialize the database
        ligneEntreeArticleRepository.saveAndFlush(ligneEntreeArticle);

        // Get the ligneEntreeArticle
        restLigneEntreeArticleMockMvc.perform(get("/api/ligne-entree-articles/{id}", ligneEntreeArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ligneEntreeArticle.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.montanttotalht").value(DEFAULT_MONTANTTOTALHT.doubleValue()))
            .andExpect(jsonPath("$.montanttotalttc").value(DEFAULT_MONTANTTOTALTTC.doubleValue()))
            .andExpect(jsonPath("$.prix_unitaire").value(DEFAULT_PRIX_UNITAIRE.doubleValue()))
            .andExpect(jsonPath("$.taxeTVA").value(DEFAULT_TAXE_TVA.doubleValue()))
            .andExpect(jsonPath("$.dateperemption").value(DEFAULT_DATEPEREMPTION.toString()))
            .andExpect(jsonPath("$.prixAchat").value(DEFAULT_PRIX_ACHAT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLigneEntreeArticle() throws Exception {
        // Get the ligneEntreeArticle
        restLigneEntreeArticleMockMvc.perform(get("/api/ligne-entree-articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneEntreeArticle() throws Exception {
        // Initialize the database
        ligneEntreeArticleService.save(ligneEntreeArticle);

        int databaseSizeBeforeUpdate = ligneEntreeArticleRepository.findAll().size();

        // Update the ligneEntreeArticle
        LigneEntreeArticle updatedLigneEntreeArticle = ligneEntreeArticleRepository.findOne(ligneEntreeArticle.getId());
        updatedLigneEntreeArticle
            .designation(UPDATED_DESIGNATION)
            .quantite(UPDATED_QUANTITE)
            .montanttotalht(UPDATED_MONTANTTOTALHT)
            .montanttotalttc(UPDATED_MONTANTTOTALTTC)
            .prix_unitaire(UPDATED_PRIX_UNITAIRE)
            .taxeTVA(UPDATED_TAXE_TVA)
            .dateperemption(UPDATED_DATEPEREMPTION)
            .prixAchat(UPDATED_PRIX_ACHAT);

        restLigneEntreeArticleMockMvc.perform(put("/api/ligne-entree-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLigneEntreeArticle)))
            .andExpect(status().isOk());

        // Validate the LigneEntreeArticle in the database
        List<LigneEntreeArticle> ligneEntreeArticleList = ligneEntreeArticleRepository.findAll();
        assertThat(ligneEntreeArticleList).hasSize(databaseSizeBeforeUpdate);
        LigneEntreeArticle testLigneEntreeArticle = ligneEntreeArticleList.get(ligneEntreeArticleList.size() - 1);
        assertThat(testLigneEntreeArticle.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testLigneEntreeArticle.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneEntreeArticle.getMontanttotalht()).isEqualTo(UPDATED_QUANTITE*UPDATED_PRIX_UNITAIRE);
        //assertThat(testLigneEntreeArticle.getMontanttotalttc()).isEqualTo((UPDATED_MONTANTTOTALHT*UPDATED_TAXE_TVA)*0.01+UPDATED_MONTANTTOTALHT);
        assertThat(testLigneEntreeArticle.getPrix_unitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testLigneEntreeArticle.getTaxeTVA()).isEqualTo(UPDATED_TAXE_TVA);
        assertThat(testLigneEntreeArticle.getDateperemption()).isEqualTo(UPDATED_DATEPEREMPTION);
        assertThat(testLigneEntreeArticle.getPrixAchat()).isEqualTo(UPDATED_PRIX_ACHAT);

        // Validate the LigneEntreeArticle in Elasticsearch
        //LigneEntreeArticle ligneEntreeArticleEs = ligneEntreeArticleSearchRepository.findOne(testLigneEntreeArticle.getId());
        //assertThat(ligneEntreeArticleEs).isEqualToComparingFieldByField(testLigneEntreeArticle);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneEntreeArticle() throws Exception {
        int databaseSizeBeforeUpdate = ligneEntreeArticleRepository.findAll().size();

        // Create the LigneEntreeArticle

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLigneEntreeArticleMockMvc.perform(put("/api/ligne-entree-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneEntreeArticle)))
            .andExpect(status().isCreated());

        // Validate the LigneEntreeArticle in the database
        List<LigneEntreeArticle> ligneEntreeArticleList = ligneEntreeArticleRepository.findAll();
        assertThat(ligneEntreeArticleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLigneEntreeArticle() throws Exception {
        // Initialize the database
        ligneEntreeArticleService.save(ligneEntreeArticle);

        int databaseSizeBeforeDelete = ligneEntreeArticleRepository.findAll().size();

        // Get the ligneEntreeArticle
        restLigneEntreeArticleMockMvc.perform(delete("/api/ligne-entree-articles/{id}", ligneEntreeArticle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        //boolean ligneEntreeArticleExistsInEs = ligneEntreeArticleSearchRepository.exists(ligneEntreeArticle.getId());
        //assertThat(ligneEntreeArticleExistsInEs).isFalse();

        // Validate the database is empty
        List<LigneEntreeArticle> ligneEntreeArticleList = ligneEntreeArticleRepository.findAll();
        assertThat(ligneEntreeArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }

/*
    @Test
    @Transactional
    public void searchLigneEntreeArticle() throws Exception {
        // Initialize the database
        ligneEntreeArticleService.save(ligneEntreeArticle);

        // Search the ligneEntreeArticle
        restLigneEntreeArticleMockMvc.perform(get("/api/_search/ligne-entree-articles?query=id:" + ligneEntreeArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneEntreeArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montanttotalht").value(hasItem(DEFAULT_MONTANTTOTALHT.doubleValue())))

            .andExpect(jsonPath("$.[*].prix_unitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].taxeTVA").value(hasItem(DEFAULT_TAXE_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].dateperemption").value(hasItem(DEFAULT_DATEPEREMPTION.toString())))
            .andExpect(jsonPath("$.[*].prixAchat").value(hasItem(DEFAULT_PRIX_ACHAT.doubleValue())));
    }
*/

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneEntreeArticle.class);
        LigneEntreeArticle ligneEntreeArticle1 = new LigneEntreeArticle();
        ligneEntreeArticle1.setId(1L);
        LigneEntreeArticle ligneEntreeArticle2 = new LigneEntreeArticle();
        ligneEntreeArticle2.setId(ligneEntreeArticle1.getId());
        assertThat(ligneEntreeArticle1).isEqualTo(ligneEntreeArticle2);
        ligneEntreeArticle2.setId(2L);
        assertThat(ligneEntreeArticle1).isNotEqualTo(ligneEntreeArticle2);
        ligneEntreeArticle1.setId(null);
        assertThat(ligneEntreeArticle1).isNotEqualTo(ligneEntreeArticle2);
    }
}
