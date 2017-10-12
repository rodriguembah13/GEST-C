package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.LigneSortieArticle;
import com.ballack.com.repository.LigneSortieArticleRepository;
import com.ballack.com.service.LigneSortieArticleService;
import com.ballack.com.repository.search.LigneSortieArticleSearchRepository;
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
 * Test class for the LigneSortieArticleResource REST controller.
 *
 * @see LigneSortieArticleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class LigneSortieArticleResourceIntTest {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Double DEFAULT_MONTANTHT = 1D;
    private static final Double UPDATED_MONTANTHT = 2D;

    private static final Double DEFAULT_MONTANTTVA = 1D;
    private static final Double UPDATED_MONTANTTVA = 2D;

    private static final Double DEFAULT_MONTANTTTC = 1D;
    private static final Double UPDATED_MONTANTTTC = 2D;

    @Autowired
    private LigneSortieArticleRepository ligneSortieArticleRepository;

    @Autowired
    private LigneSortieArticleService ligneSortieArticleService;

    @Autowired
    private LigneSortieArticleSearchRepository ligneSortieArticleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLigneSortieArticleMockMvc;

    private LigneSortieArticle ligneSortieArticle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LigneSortieArticleResource ligneSortieArticleResource = new LigneSortieArticleResource(ligneSortieArticleService);
        this.restLigneSortieArticleMockMvc = MockMvcBuilders.standaloneSetup(ligneSortieArticleResource)
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
    public static LigneSortieArticle createEntity(EntityManager em) {
        LigneSortieArticle ligneSortieArticle = new LigneSortieArticle()
            .designation(DEFAULT_DESIGNATION)
            .quantite(DEFAULT_QUANTITE)
            .montantht(DEFAULT_MONTANTHT)
            .montanttva(DEFAULT_MONTANTTVA)
            .montantttc(DEFAULT_MONTANTTTC);
        return ligneSortieArticle;
    }

    @Before
    public void initTest() {
        ligneSortieArticleSearchRepository.deleteAll();
        ligneSortieArticle = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneSortieArticle() throws Exception {
        int databaseSizeBeforeCreate = ligneSortieArticleRepository.findAll().size();

        // Create the LigneSortieArticle
        restLigneSortieArticleMockMvc.perform(post("/api/ligne-sortie-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneSortieArticle)))
            .andExpect(status().isCreated());

        // Validate the LigneSortieArticle in the database
        List<LigneSortieArticle> ligneSortieArticleList = ligneSortieArticleRepository.findAll();
        assertThat(ligneSortieArticleList).hasSize(databaseSizeBeforeCreate + 1);
        LigneSortieArticle testLigneSortieArticle = ligneSortieArticleList.get(ligneSortieArticleList.size() - 1);
        assertThat(testLigneSortieArticle.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testLigneSortieArticle.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneSortieArticle.getMontantht()).isEqualTo(DEFAULT_MONTANTHT);
        assertThat(testLigneSortieArticle.getMontanttva()).isEqualTo(DEFAULT_MONTANTTVA);
        assertThat(testLigneSortieArticle.getMontantttc()).isEqualTo(DEFAULT_MONTANTTTC);

        // Validate the LigneSortieArticle in Elasticsearch
        LigneSortieArticle ligneSortieArticleEs = ligneSortieArticleSearchRepository.findOne(testLigneSortieArticle.getId());
        assertThat(ligneSortieArticleEs).isEqualToComparingFieldByField(testLigneSortieArticle);
    }

    @Test
    @Transactional
    public void createLigneSortieArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneSortieArticleRepository.findAll().size();

        // Create the LigneSortieArticle with an existing ID
        ligneSortieArticle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneSortieArticleMockMvc.perform(post("/api/ligne-sortie-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneSortieArticle)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LigneSortieArticle> ligneSortieArticleList = ligneSortieArticleRepository.findAll();
        assertThat(ligneSortieArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLigneSortieArticles() throws Exception {
        // Initialize the database
        ligneSortieArticleRepository.saveAndFlush(ligneSortieArticle);

        // Get all the ligneSortieArticleList
        restLigneSortieArticleMockMvc.perform(get("/api/ligne-sortie-articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneSortieArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montantht").value(hasItem(DEFAULT_MONTANTHT.doubleValue())))
            .andExpect(jsonPath("$.[*].montanttva").value(hasItem(DEFAULT_MONTANTTVA.doubleValue())))
            .andExpect(jsonPath("$.[*].montantttc").value(hasItem(DEFAULT_MONTANTTTC.doubleValue())));
    }

    @Test
    @Transactional
    public void getLigneSortieArticle() throws Exception {
        // Initialize the database
        ligneSortieArticleRepository.saveAndFlush(ligneSortieArticle);

        // Get the ligneSortieArticle
        restLigneSortieArticleMockMvc.perform(get("/api/ligne-sortie-articles/{id}", ligneSortieArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ligneSortieArticle.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.montantht").value(DEFAULT_MONTANTHT.doubleValue()))
            .andExpect(jsonPath("$.montanttva").value(DEFAULT_MONTANTTVA.doubleValue()))
            .andExpect(jsonPath("$.montantttc").value(DEFAULT_MONTANTTTC.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLigneSortieArticle() throws Exception {
        // Get the ligneSortieArticle
        restLigneSortieArticleMockMvc.perform(get("/api/ligne-sortie-articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneSortieArticle() throws Exception {
        // Initialize the database
        ligneSortieArticleService.save(ligneSortieArticle);

        int databaseSizeBeforeUpdate = ligneSortieArticleRepository.findAll().size();

        // Update the ligneSortieArticle
        LigneSortieArticle updatedLigneSortieArticle = ligneSortieArticleRepository.findOne(ligneSortieArticle.getId());
        updatedLigneSortieArticle
            .designation(UPDATED_DESIGNATION)
            .quantite(UPDATED_QUANTITE)
            .montantht(UPDATED_MONTANTHT)
            .montanttva(UPDATED_MONTANTTVA)
            .montantttc(UPDATED_MONTANTTTC);

        restLigneSortieArticleMockMvc.perform(put("/api/ligne-sortie-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLigneSortieArticle)))
            .andExpect(status().isOk());

        // Validate the LigneSortieArticle in the database
        List<LigneSortieArticle> ligneSortieArticleList = ligneSortieArticleRepository.findAll();
        assertThat(ligneSortieArticleList).hasSize(databaseSizeBeforeUpdate);
        LigneSortieArticle testLigneSortieArticle = ligneSortieArticleList.get(ligneSortieArticleList.size() - 1);
        assertThat(testLigneSortieArticle.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testLigneSortieArticle.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneSortieArticle.getMontantht()).isEqualTo(UPDATED_MONTANTHT);
        assertThat(testLigneSortieArticle.getMontanttva()).isEqualTo(UPDATED_MONTANTTVA);
        assertThat(testLigneSortieArticle.getMontantttc()).isEqualTo(UPDATED_MONTANTTTC);

        // Validate the LigneSortieArticle in Elasticsearch
        LigneSortieArticle ligneSortieArticleEs = ligneSortieArticleSearchRepository.findOne(testLigneSortieArticle.getId());
        assertThat(ligneSortieArticleEs).isEqualToComparingFieldByField(testLigneSortieArticle);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneSortieArticle() throws Exception {
        int databaseSizeBeforeUpdate = ligneSortieArticleRepository.findAll().size();

        // Create the LigneSortieArticle

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLigneSortieArticleMockMvc.perform(put("/api/ligne-sortie-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneSortieArticle)))
            .andExpect(status().isCreated());

        // Validate the LigneSortieArticle in the database
        List<LigneSortieArticle> ligneSortieArticleList = ligneSortieArticleRepository.findAll();
        assertThat(ligneSortieArticleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLigneSortieArticle() throws Exception {
        // Initialize the database
        ligneSortieArticleService.save(ligneSortieArticle);

        int databaseSizeBeforeDelete = ligneSortieArticleRepository.findAll().size();

        // Get the ligneSortieArticle
        restLigneSortieArticleMockMvc.perform(delete("/api/ligne-sortie-articles/{id}", ligneSortieArticle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ligneSortieArticleExistsInEs = ligneSortieArticleSearchRepository.exists(ligneSortieArticle.getId());
        assertThat(ligneSortieArticleExistsInEs).isFalse();

        // Validate the database is empty
        List<LigneSortieArticle> ligneSortieArticleList = ligneSortieArticleRepository.findAll();
        assertThat(ligneSortieArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLigneSortieArticle() throws Exception {
        // Initialize the database
        ligneSortieArticleService.save(ligneSortieArticle);

        // Search the ligneSortieArticle
        restLigneSortieArticleMockMvc.perform(get("/api/_search/ligne-sortie-articles?query=id:" + ligneSortieArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneSortieArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montantht").value(hasItem(DEFAULT_MONTANTHT.doubleValue())))
            .andExpect(jsonPath("$.[*].montanttva").value(hasItem(DEFAULT_MONTANTTVA.doubleValue())))
            .andExpect(jsonPath("$.[*].montantttc").value(hasItem(DEFAULT_MONTANTTTC.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneSortieArticle.class);
        LigneSortieArticle ligneSortieArticle1 = new LigneSortieArticle();
        ligneSortieArticle1.setId(1L);
        LigneSortieArticle ligneSortieArticle2 = new LigneSortieArticle();
        ligneSortieArticle2.setId(ligneSortieArticle1.getId());
        assertThat(ligneSortieArticle1).isEqualTo(ligneSortieArticle2);
        ligneSortieArticle2.setId(2L);
        assertThat(ligneSortieArticle1).isNotEqualTo(ligneSortieArticle2);
        ligneSortieArticle1.setId(null);
        assertThat(ligneSortieArticle1).isNotEqualTo(ligneSortieArticle2);
    }
}
