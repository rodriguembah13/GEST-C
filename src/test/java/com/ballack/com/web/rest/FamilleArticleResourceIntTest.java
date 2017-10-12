package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.FamilleArticle;
import com.ballack.com.repository.FamilleArticleRepository;
import com.ballack.com.repository.search.FamilleArticleSearchRepository;
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
 * Test class for the FamilleArticleResource REST controller.
 *
 * @see FamilleArticleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class FamilleArticleResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private FamilleArticleRepository familleArticleRepository;

    @Autowired
    private FamilleArticleSearchRepository familleArticleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFamilleArticleMockMvc;

    private FamilleArticle familleArticle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FamilleArticleResource familleArticleResource = new FamilleArticleResource(familleArticleRepository, familleArticleSearchRepository);
        this.restFamilleArticleMockMvc = MockMvcBuilders.standaloneSetup(familleArticleResource)
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
    public static FamilleArticle createEntity(EntityManager em) {
        FamilleArticle familleArticle = new FamilleArticle()
            .libelle(DEFAULT_LIBELLE)
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION);
        return familleArticle;
    }

    @Before
    public void initTest() {
        familleArticleSearchRepository.deleteAll();
        familleArticle = createEntity(em);
    }

    @Test
    @Transactional
    public void createFamilleArticle() throws Exception {
        int databaseSizeBeforeCreate = familleArticleRepository.findAll().size();

        // Create the FamilleArticle
        restFamilleArticleMockMvc.perform(post("/api/famille-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familleArticle)))
            .andExpect(status().isCreated());

        // Validate the FamilleArticle in the database
        List<FamilleArticle> familleArticleList = familleArticleRepository.findAll();
        assertThat(familleArticleList).hasSize(databaseSizeBeforeCreate + 1);
        FamilleArticle testFamilleArticle = familleArticleList.get(familleArticleList.size() - 1);
        assertThat(testFamilleArticle.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testFamilleArticle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testFamilleArticle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the FamilleArticle in Elasticsearch
        FamilleArticle familleArticleEs = familleArticleSearchRepository.findOne(testFamilleArticle.getId());
        assertThat(familleArticleEs).isEqualToComparingFieldByField(testFamilleArticle);
    }

    @Test
    @Transactional
    public void createFamilleArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = familleArticleRepository.findAll().size();

        // Create the FamilleArticle with an existing ID
        familleArticle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilleArticleMockMvc.perform(post("/api/famille-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familleArticle)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FamilleArticle> familleArticleList = familleArticleRepository.findAll();
        assertThat(familleArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFamilleArticles() throws Exception {
        // Initialize the database
        familleArticleRepository.saveAndFlush(familleArticle);

        // Get all the familleArticleList
        restFamilleArticleMockMvc.perform(get("/api/famille-articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familleArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getFamilleArticle() throws Exception {
        // Initialize the database
        familleArticleRepository.saveAndFlush(familleArticle);

        // Get the familleArticle
        restFamilleArticleMockMvc.perform(get("/api/famille-articles/{id}", familleArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(familleArticle.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFamilleArticle() throws Exception {
        // Get the familleArticle
        restFamilleArticleMockMvc.perform(get("/api/famille-articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamilleArticle() throws Exception {
        // Initialize the database
        familleArticleRepository.saveAndFlush(familleArticle);
        familleArticleSearchRepository.save(familleArticle);
        int databaseSizeBeforeUpdate = familleArticleRepository.findAll().size();

        // Update the familleArticle
        FamilleArticle updatedFamilleArticle = familleArticleRepository.findOne(familleArticle.getId());
        updatedFamilleArticle
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);

        restFamilleArticleMockMvc.perform(put("/api/famille-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFamilleArticle)))
            .andExpect(status().isOk());

        // Validate the FamilleArticle in the database
        List<FamilleArticle> familleArticleList = familleArticleRepository.findAll();
        assertThat(familleArticleList).hasSize(databaseSizeBeforeUpdate);
        FamilleArticle testFamilleArticle = familleArticleList.get(familleArticleList.size() - 1);
        assertThat(testFamilleArticle.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testFamilleArticle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testFamilleArticle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the FamilleArticle in Elasticsearch
        FamilleArticle familleArticleEs = familleArticleSearchRepository.findOne(testFamilleArticle.getId());
        assertThat(familleArticleEs).isEqualToComparingFieldByField(testFamilleArticle);
    }

    @Test
    @Transactional
    public void updateNonExistingFamilleArticle() throws Exception {
        int databaseSizeBeforeUpdate = familleArticleRepository.findAll().size();

        // Create the FamilleArticle

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFamilleArticleMockMvc.perform(put("/api/famille-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familleArticle)))
            .andExpect(status().isCreated());

        // Validate the FamilleArticle in the database
        List<FamilleArticle> familleArticleList = familleArticleRepository.findAll();
        assertThat(familleArticleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFamilleArticle() throws Exception {
        // Initialize the database
        familleArticleRepository.saveAndFlush(familleArticle);
        familleArticleSearchRepository.save(familleArticle);
        int databaseSizeBeforeDelete = familleArticleRepository.findAll().size();

        // Get the familleArticle
        restFamilleArticleMockMvc.perform(delete("/api/famille-articles/{id}", familleArticle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean familleArticleExistsInEs = familleArticleSearchRepository.exists(familleArticle.getId());
        assertThat(familleArticleExistsInEs).isFalse();

        // Validate the database is empty
        List<FamilleArticle> familleArticleList = familleArticleRepository.findAll();
        assertThat(familleArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFamilleArticle() throws Exception {
        // Initialize the database
        familleArticleRepository.saveAndFlush(familleArticle);
        familleArticleSearchRepository.save(familleArticle);

        // Search the familleArticle
        restFamilleArticleMockMvc.perform(get("/api/_search/famille-articles?query=id:" + familleArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familleArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilleArticle.class);
        FamilleArticle familleArticle1 = new FamilleArticle();
        familleArticle1.setId(1L);
        FamilleArticle familleArticle2 = new FamilleArticle();
        familleArticle2.setId(familleArticle1.getId());
        assertThat(familleArticle1).isEqualTo(familleArticle2);
        familleArticle2.setId(2L);
        assertThat(familleArticle1).isNotEqualTo(familleArticle2);
        familleArticle1.setId(null);
        assertThat(familleArticle1).isNotEqualTo(familleArticle2);
    }
}
