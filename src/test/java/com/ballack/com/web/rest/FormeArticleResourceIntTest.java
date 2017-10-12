package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.FormeArticle;
import com.ballack.com.repository.FormeArticleRepository;
import com.ballack.com.repository.search.FormeArticleSearchRepository;
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
 * Test class for the FormeArticleResource REST controller.
 *
 * @see FormeArticleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class FormeArticleResourceIntTest {

    private static final String DEFAULT_NOM_FORME = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FORME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    @Autowired
    private FormeArticleRepository formeArticleRepository;

    @Autowired
    private FormeArticleSearchRepository formeArticleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFormeArticleMockMvc;

    private FormeArticle formeArticle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormeArticleResource formeArticleResource = new FormeArticleResource(formeArticleRepository, formeArticleSearchRepository);
        this.restFormeArticleMockMvc = MockMvcBuilders.standaloneSetup(formeArticleResource)
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
    public static FormeArticle createEntity(EntityManager em) {
        FormeArticle formeArticle = new FormeArticle()
            .nomForme(DEFAULT_NOM_FORME)
            .quantite(DEFAULT_QUANTITE);
        return formeArticle;
    }

    @Before
    public void initTest() {
        formeArticleSearchRepository.deleteAll();
        formeArticle = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormeArticle() throws Exception {
        int databaseSizeBeforeCreate = formeArticleRepository.findAll().size();

        // Create the FormeArticle
        restFormeArticleMockMvc.perform(post("/api/forme-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formeArticle)))
            .andExpect(status().isCreated());

        // Validate the FormeArticle in the database
        List<FormeArticle> formeArticleList = formeArticleRepository.findAll();
        assertThat(formeArticleList).hasSize(databaseSizeBeforeCreate + 1);
        FormeArticle testFormeArticle = formeArticleList.get(formeArticleList.size() - 1);
        assertThat(testFormeArticle.getNomForme()).isEqualTo(DEFAULT_NOM_FORME);
        assertThat(testFormeArticle.getQuantite()).isEqualTo(DEFAULT_QUANTITE);

        // Validate the FormeArticle in Elasticsearch
        FormeArticle formeArticleEs = formeArticleSearchRepository.findOne(testFormeArticle.getId());
        assertThat(formeArticleEs).isEqualToComparingFieldByField(testFormeArticle);
    }

    @Test
    @Transactional
    public void createFormeArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formeArticleRepository.findAll().size();

        // Create the FormeArticle with an existing ID
        formeArticle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormeArticleMockMvc.perform(post("/api/forme-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formeArticle)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FormeArticle> formeArticleList = formeArticleRepository.findAll();
        assertThat(formeArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFormeArticles() throws Exception {
        // Initialize the database
        formeArticleRepository.saveAndFlush(formeArticle);

        // Get all the formeArticleList
        restFormeArticleMockMvc.perform(get("/api/forme-articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formeArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomForme").value(hasItem(DEFAULT_NOM_FORME.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)));
    }

    @Test
    @Transactional
    public void getFormeArticle() throws Exception {
        // Initialize the database
        formeArticleRepository.saveAndFlush(formeArticle);

        // Get the formeArticle
        restFormeArticleMockMvc.perform(get("/api/forme-articles/{id}", formeArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formeArticle.getId().intValue()))
            .andExpect(jsonPath("$.nomForme").value(DEFAULT_NOM_FORME.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE));
    }

    @Test
    @Transactional
    public void getNonExistingFormeArticle() throws Exception {
        // Get the formeArticle
        restFormeArticleMockMvc.perform(get("/api/forme-articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormeArticle() throws Exception {
        // Initialize the database
        formeArticleRepository.saveAndFlush(formeArticle);
        formeArticleSearchRepository.save(formeArticle);
        int databaseSizeBeforeUpdate = formeArticleRepository.findAll().size();

        // Update the formeArticle
        FormeArticle updatedFormeArticle = formeArticleRepository.findOne(formeArticle.getId());
        updatedFormeArticle
            .nomForme(UPDATED_NOM_FORME)
            .quantite(UPDATED_QUANTITE);

        restFormeArticleMockMvc.perform(put("/api/forme-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormeArticle)))
            .andExpect(status().isOk());

        // Validate the FormeArticle in the database
        List<FormeArticle> formeArticleList = formeArticleRepository.findAll();
        assertThat(formeArticleList).hasSize(databaseSizeBeforeUpdate);
        FormeArticle testFormeArticle = formeArticleList.get(formeArticleList.size() - 1);
        assertThat(testFormeArticle.getNomForme()).isEqualTo(UPDATED_NOM_FORME);
        assertThat(testFormeArticle.getQuantite()).isEqualTo(UPDATED_QUANTITE);

        // Validate the FormeArticle in Elasticsearch
        FormeArticle formeArticleEs = formeArticleSearchRepository.findOne(testFormeArticle.getId());
        assertThat(formeArticleEs).isEqualToComparingFieldByField(testFormeArticle);
    }

    @Test
    @Transactional
    public void updateNonExistingFormeArticle() throws Exception {
        int databaseSizeBeforeUpdate = formeArticleRepository.findAll().size();

        // Create the FormeArticle

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFormeArticleMockMvc.perform(put("/api/forme-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formeArticle)))
            .andExpect(status().isCreated());

        // Validate the FormeArticle in the database
        List<FormeArticle> formeArticleList = formeArticleRepository.findAll();
        assertThat(formeArticleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFormeArticle() throws Exception {
        // Initialize the database
        formeArticleRepository.saveAndFlush(formeArticle);
        formeArticleSearchRepository.save(formeArticle);
        int databaseSizeBeforeDelete = formeArticleRepository.findAll().size();

        // Get the formeArticle
        restFormeArticleMockMvc.perform(delete("/api/forme-articles/{id}", formeArticle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean formeArticleExistsInEs = formeArticleSearchRepository.exists(formeArticle.getId());
        assertThat(formeArticleExistsInEs).isFalse();

        // Validate the database is empty
        List<FormeArticle> formeArticleList = formeArticleRepository.findAll();
        assertThat(formeArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFormeArticle() throws Exception {
        // Initialize the database
        formeArticleRepository.saveAndFlush(formeArticle);
        formeArticleSearchRepository.save(formeArticle);

        // Search the formeArticle
        restFormeArticleMockMvc.perform(get("/api/_search/forme-articles?query=id:" + formeArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formeArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomForme").value(hasItem(DEFAULT_NOM_FORME.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormeArticle.class);
        FormeArticle formeArticle1 = new FormeArticle();
        formeArticle1.setId(1L);
        FormeArticle formeArticle2 = new FormeArticle();
        formeArticle2.setId(formeArticle1.getId());
        assertThat(formeArticle1).isEqualTo(formeArticle2);
        formeArticle2.setId(2L);
        assertThat(formeArticle1).isNotEqualTo(formeArticle2);
        formeArticle1.setId(null);
        assertThat(formeArticle1).isNotEqualTo(formeArticle2);
    }
}
