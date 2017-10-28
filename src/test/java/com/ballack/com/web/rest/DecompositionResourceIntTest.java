package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.Decomposition;
import com.ballack.com.repository.DecompositionRepository;
import com.ballack.com.service.DecompositionService;
import com.ballack.com.repository.search.DecompositionSearchRepository;
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
 * Test class for the DecompositionResource REST controller.
 *
 * @see DecompositionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class DecompositionResourceIntTest {

    private static final String DEFAULT_ARTICLEDEPART = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLEDEPART = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLEFIN = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLEFIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Boolean DEFAULT_ETAT = false;
    private static final Boolean UPDATED_ETAT = true;

    @Autowired
    private DecompositionRepository decompositionRepository;

    @Autowired
    private DecompositionService decompositionService;

    @Autowired
    private DecompositionSearchRepository decompositionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDecompositionMockMvc;

    private Decomposition decomposition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DecompositionResource decompositionResource = new DecompositionResource(decompositionService);
        this.restDecompositionMockMvc = MockMvcBuilders.standaloneSetup(decompositionResource)
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
    public static Decomposition createEntity(EntityManager em) {
        Decomposition decomposition = new Decomposition()
            .articledepart(DEFAULT_ARTICLEDEPART)
            .articlefin(DEFAULT_ARTICLEFIN)
            .quantite(DEFAULT_QUANTITE)
            .etat(DEFAULT_ETAT);
        return decomposition;
    }

    @Before
    public void initTest() {
        decompositionSearchRepository.deleteAll();
        decomposition = createEntity(em);
    }

    @Test
    @Transactional
    public void createDecomposition() throws Exception {
        int databaseSizeBeforeCreate = decompositionRepository.findAll().size();

        // Create the Decomposition
        restDecompositionMockMvc.perform(post("/api/decomposition")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposition)))
            .andExpect(status().isCreated());

        // Validate the Decomposition in the database
        List<Decomposition> decompositionList = decompositionRepository.findAll();
        assertThat(decompositionList).hasSize(databaseSizeBeforeCreate + 1);
        Decomposition testDecomposition = decompositionList.get(decompositionList.size() - 1);
        assertThat(testDecomposition.getArticledepart()).isEqualTo(DEFAULT_ARTICLEDEPART);
        assertThat(testDecomposition.getArticlefin()).isEqualTo(DEFAULT_ARTICLEFIN);
        assertThat(testDecomposition.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testDecomposition.isEtat()).isEqualTo(DEFAULT_ETAT);

        // Validate the Decomposition in Elasticsearch
        //Decomposition decompositionEs = decompositionSearchRepository.findOne(testDecomposition.getId());
       // assertThat(decompositionEs).isEqualToComparingFieldByField(testDecomposition);
    }

    @Test
    @Transactional
    public void createDecompositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = decompositionRepository.findAll().size();

        // Create the Decomposition with an existing ID
        decomposition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecompositionMockMvc.perform(post("/api/decomposition")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposition)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Decomposition> decompositionList = decompositionRepository.findAll();
        assertThat(decompositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDecompositions() throws Exception {
        // Initialize the database
        decompositionRepository.saveAndFlush(decomposition);

        // Get all the decompositionList
        restDecompositionMockMvc.perform(get("/api/decompositions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decomposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].articledepart").value(hasItem(DEFAULT_ARTICLEDEPART.toString())))
            .andExpect(jsonPath("$.[*].articlefin").value(hasItem(DEFAULT_ARTICLEFIN.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.booleanValue())));
    }

    @Test
    @Transactional
    public void getDecomposition() throws Exception {
        // Initialize the database
        decompositionRepository.saveAndFlush(decomposition);

        // Get the decomposition
        restDecompositionMockMvc.perform(get("/api/decompositions/{id}", decomposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(decomposition.getId().intValue()))
            .andExpect(jsonPath("$.articledepart").value(DEFAULT_ARTICLEDEPART.toString()))
            .andExpect(jsonPath("$.articlefin").value(DEFAULT_ARTICLEFIN.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDecomposition() throws Exception {
        // Get the decomposition
        restDecompositionMockMvc.perform(get("/api/decompositions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDecomposition() throws Exception {
        // Initialize the database
        decompositionService.save(decomposition);

        int databaseSizeBeforeUpdate = decompositionRepository.findAll().size();

        // Update the decomposition
        Decomposition updatedDecomposition = decompositionRepository.findOne(decomposition.getId());
        updatedDecomposition
            .articledepart(UPDATED_ARTICLEDEPART)
            .articlefin(UPDATED_ARTICLEFIN)
            .quantite(UPDATED_QUANTITE)
            .etat(UPDATED_ETAT);

        restDecompositionMockMvc.perform(put("/api/decompositions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDecomposition)))
            .andExpect(status().isOk());

        // Validate the Decomposition in the database
        List<Decomposition> decompositionList = decompositionRepository.findAll();
        assertThat(decompositionList).hasSize(databaseSizeBeforeUpdate);
        Decomposition testDecomposition = decompositionList.get(decompositionList.size() - 1);
        assertThat(testDecomposition.getArticledepart()).isEqualTo(UPDATED_ARTICLEDEPART);
        assertThat(testDecomposition.getArticlefin()).isEqualTo(UPDATED_ARTICLEFIN);
        assertThat(testDecomposition.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testDecomposition.isEtat()).isEqualTo(UPDATED_ETAT);

        // Validate the Decomposition in Elasticsearch
        Decomposition decompositionEs = decompositionSearchRepository.findOne(testDecomposition.getId());
        assertThat(decompositionEs).isEqualToComparingFieldByField(testDecomposition);
    }

    @Test
    @Transactional
    public void updateNonExistingDecomposition() throws Exception {
        int databaseSizeBeforeUpdate = decompositionRepository.findAll().size();

        // Create the Decomposition

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDecompositionMockMvc.perform(put("/api/decompositions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposition)))
            .andExpect(status().isCreated());

        // Validate the Decomposition in the database
        List<Decomposition> decompositionList = decompositionRepository.findAll();
        assertThat(decompositionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDecomposition() throws Exception {
        // Initialize the database
        decompositionService.save(decomposition);

        int databaseSizeBeforeDelete = decompositionRepository.findAll().size();

        // Get the decomposition
        restDecompositionMockMvc.perform(delete("/api/decompositions/{id}", decomposition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean decompositionExistsInEs = decompositionSearchRepository.exists(decomposition.getId());
        assertThat(decompositionExistsInEs).isFalse();

        // Validate the database is empty
        List<Decomposition> decompositionList = decompositionRepository.findAll();
        assertThat(decompositionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDecomposition() throws Exception {
        // Initialize the database
        decompositionService.save(decomposition);

        // Search the decomposition
        restDecompositionMockMvc.perform(get("/api/_search/decompositions?query=id:" + decomposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decomposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].articledepart").value(hasItem(DEFAULT_ARTICLEDEPART.toString())))
            .andExpect(jsonPath("$.[*].articlefin").value(hasItem(DEFAULT_ARTICLEFIN.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Decomposition.class);
        Decomposition decomposition1 = new Decomposition();
        decomposition1.setId(1L);
        Decomposition decomposition2 = new Decomposition();
        decomposition2.setId(decomposition1.getId());
        assertThat(decomposition1).isEqualTo(decomposition2);
        decomposition2.setId(2L);
        assertThat(decomposition1).isNotEqualTo(decomposition2);
        decomposition1.setId(null);
        assertThat(decomposition1).isNotEqualTo(decomposition2);
    }
}
