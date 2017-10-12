package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.TypeSortieArticle;
import com.ballack.com.repository.TypeSortieArticleRepository;
import com.ballack.com.repository.search.TypeSortieArticleSearchRepository;
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
 * Test class for the TypeSortieArticleResource REST controller.
 *
 * @see TypeSortieArticleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class TypeSortieArticleResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private TypeSortieArticleRepository typeSortieArticleRepository;

    @Autowired
    private TypeSortieArticleSearchRepository typeSortieArticleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTypeSortieArticleMockMvc;

    private TypeSortieArticle typeSortieArticle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeSortieArticleResource typeSortieArticleResource = new TypeSortieArticleResource(typeSortieArticleRepository, typeSortieArticleSearchRepository);
        this.restTypeSortieArticleMockMvc = MockMvcBuilders.standaloneSetup(typeSortieArticleResource)
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
    public static TypeSortieArticle createEntity(EntityManager em) {
        TypeSortieArticle typeSortieArticle = new TypeSortieArticle()
            .libelle(DEFAULT_LIBELLE);
        return typeSortieArticle;
    }

    @Before
    public void initTest() {
        typeSortieArticleSearchRepository.deleteAll();
        typeSortieArticle = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeSortieArticle() throws Exception {
        int databaseSizeBeforeCreate = typeSortieArticleRepository.findAll().size();

        // Create the TypeSortieArticle
        restTypeSortieArticleMockMvc.perform(post("/api/type-sortie-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeSortieArticle)))
            .andExpect(status().isCreated());

        // Validate the TypeSortieArticle in the database
        List<TypeSortieArticle> typeSortieArticleList = typeSortieArticleRepository.findAll();
        assertThat(typeSortieArticleList).hasSize(databaseSizeBeforeCreate + 1);
        TypeSortieArticle testTypeSortieArticle = typeSortieArticleList.get(typeSortieArticleList.size() - 1);
        assertThat(testTypeSortieArticle.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TypeSortieArticle in Elasticsearch
        TypeSortieArticle typeSortieArticleEs = typeSortieArticleSearchRepository.findOne(testTypeSortieArticle.getId());
        assertThat(typeSortieArticleEs).isEqualToComparingFieldByField(testTypeSortieArticle);
    }

    @Test
    @Transactional
    public void createTypeSortieArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeSortieArticleRepository.findAll().size();

        // Create the TypeSortieArticle with an existing ID
        typeSortieArticle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeSortieArticleMockMvc.perform(post("/api/type-sortie-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeSortieArticle)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TypeSortieArticle> typeSortieArticleList = typeSortieArticleRepository.findAll();
        assertThat(typeSortieArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTypeSortieArticles() throws Exception {
        // Initialize the database
        typeSortieArticleRepository.saveAndFlush(typeSortieArticle);

        // Get all the typeSortieArticleList
        restTypeSortieArticleMockMvc.perform(get("/api/type-sortie-articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSortieArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getTypeSortieArticle() throws Exception {
        // Initialize the database
        typeSortieArticleRepository.saveAndFlush(typeSortieArticle);

        // Get the typeSortieArticle
        restTypeSortieArticleMockMvc.perform(get("/api/type-sortie-articles/{id}", typeSortieArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeSortieArticle.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeSortieArticle() throws Exception {
        // Get the typeSortieArticle
        restTypeSortieArticleMockMvc.perform(get("/api/type-sortie-articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeSortieArticle() throws Exception {
        // Initialize the database
        typeSortieArticleRepository.saveAndFlush(typeSortieArticle);
        typeSortieArticleSearchRepository.save(typeSortieArticle);
        int databaseSizeBeforeUpdate = typeSortieArticleRepository.findAll().size();

        // Update the typeSortieArticle
        TypeSortieArticle updatedTypeSortieArticle = typeSortieArticleRepository.findOne(typeSortieArticle.getId());
        updatedTypeSortieArticle
            .libelle(UPDATED_LIBELLE);

        restTypeSortieArticleMockMvc.perform(put("/api/type-sortie-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTypeSortieArticle)))
            .andExpect(status().isOk());

        // Validate the TypeSortieArticle in the database
        List<TypeSortieArticle> typeSortieArticleList = typeSortieArticleRepository.findAll();
        assertThat(typeSortieArticleList).hasSize(databaseSizeBeforeUpdate);
        TypeSortieArticle testTypeSortieArticle = typeSortieArticleList.get(typeSortieArticleList.size() - 1);
        assertThat(testTypeSortieArticle.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TypeSortieArticle in Elasticsearch
        TypeSortieArticle typeSortieArticleEs = typeSortieArticleSearchRepository.findOne(testTypeSortieArticle.getId());
        assertThat(typeSortieArticleEs).isEqualToComparingFieldByField(testTypeSortieArticle);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeSortieArticle() throws Exception {
        int databaseSizeBeforeUpdate = typeSortieArticleRepository.findAll().size();

        // Create the TypeSortieArticle

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTypeSortieArticleMockMvc.perform(put("/api/type-sortie-articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeSortieArticle)))
            .andExpect(status().isCreated());

        // Validate the TypeSortieArticle in the database
        List<TypeSortieArticle> typeSortieArticleList = typeSortieArticleRepository.findAll();
        assertThat(typeSortieArticleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTypeSortieArticle() throws Exception {
        // Initialize the database
        typeSortieArticleRepository.saveAndFlush(typeSortieArticle);
        typeSortieArticleSearchRepository.save(typeSortieArticle);
        int databaseSizeBeforeDelete = typeSortieArticleRepository.findAll().size();

        // Get the typeSortieArticle
        restTypeSortieArticleMockMvc.perform(delete("/api/type-sortie-articles/{id}", typeSortieArticle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean typeSortieArticleExistsInEs = typeSortieArticleSearchRepository.exists(typeSortieArticle.getId());
        assertThat(typeSortieArticleExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeSortieArticle> typeSortieArticleList = typeSortieArticleRepository.findAll();
        assertThat(typeSortieArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeSortieArticle() throws Exception {
        // Initialize the database
        typeSortieArticleRepository.saveAndFlush(typeSortieArticle);
        typeSortieArticleSearchRepository.save(typeSortieArticle);

        // Search the typeSortieArticle
        restTypeSortieArticleMockMvc.perform(get("/api/_search/type-sortie-articles?query=id:" + typeSortieArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSortieArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSortieArticle.class);
        TypeSortieArticle typeSortieArticle1 = new TypeSortieArticle();
        typeSortieArticle1.setId(1L);
        TypeSortieArticle typeSortieArticle2 = new TypeSortieArticle();
        typeSortieArticle2.setId(typeSortieArticle1.getId());
        assertThat(typeSortieArticle1).isEqualTo(typeSortieArticle2);
        typeSortieArticle2.setId(2L);
        assertThat(typeSortieArticle1).isNotEqualTo(typeSortieArticle2);
        typeSortieArticle1.setId(null);
        assertThat(typeSortieArticle1).isNotEqualTo(typeSortieArticle2);
    }
}
