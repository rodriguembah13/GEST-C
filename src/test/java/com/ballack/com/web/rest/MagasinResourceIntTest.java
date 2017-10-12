package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.Magasin;
import com.ballack.com.repository.MagasinRepository;
import com.ballack.com.repository.search.MagasinSearchRepository;
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
 * Test class for the MagasinResource REST controller.
 *
 * @see MagasinResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class MagasinResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_MAGASIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MAGASIN = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    @Autowired
    private MagasinRepository magasinRepository;

    @Autowired
    private MagasinSearchRepository magasinSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMagasinMockMvc;

    private Magasin magasin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MagasinResource magasinResource = new MagasinResource(magasinRepository, magasinSearchRepository);
        this.restMagasinMockMvc = MockMvcBuilders.standaloneSetup(magasinResource)
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
    public static Magasin createEntity(EntityManager em) {
        Magasin magasin = new Magasin()
            .code(DEFAULT_CODE)
            .nomMagasin(DEFAULT_NOM_MAGASIN)
            .ville(DEFAULT_VILLE)
            .telephone(DEFAULT_TELEPHONE);
        return magasin;
    }

    @Before
    public void initTest() {
        magasinSearchRepository.deleteAll();
        magasin = createEntity(em);
    }

    @Test
    @Transactional
    public void createMagasin() throws Exception {
        int databaseSizeBeforeCreate = magasinRepository.findAll().size();

        // Create the Magasin
        restMagasinMockMvc.perform(post("/api/magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasin)))
            .andExpect(status().isCreated());

        // Validate the Magasin in the database
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeCreate + 1);
        Magasin testMagasin = magasinList.get(magasinList.size() - 1);
        assertThat(testMagasin.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMagasin.getNomMagasin()).isEqualTo(DEFAULT_NOM_MAGASIN);
        assertThat(testMagasin.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testMagasin.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Validate the Magasin in Elasticsearch
        Magasin magasinEs = magasinSearchRepository.findOne(testMagasin.getId());
        assertThat(magasinEs).isEqualToComparingFieldByField(testMagasin);
    }

    @Test
    @Transactional
    public void createMagasinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = magasinRepository.findAll().size();

        // Create the Magasin with an existing ID
        magasin.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMagasinMockMvc.perform(post("/api/magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasin)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMagasins() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);

        // Get all the magasinList
        restMagasinMockMvc.perform(get("/api/magasins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magasin.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].nomMagasin").value(hasItem(DEFAULT_NOM_MAGASIN.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())));
    }

    @Test
    @Transactional
    public void getMagasin() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);

        // Get the magasin
        restMagasinMockMvc.perform(get("/api/magasins/{id}", magasin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(magasin.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.nomMagasin").value(DEFAULT_NOM_MAGASIN.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMagasin() throws Exception {
        // Get the magasin
        restMagasinMockMvc.perform(get("/api/magasins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMagasin() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);
        magasinSearchRepository.save(magasin);
        int databaseSizeBeforeUpdate = magasinRepository.findAll().size();

        // Update the magasin
        Magasin updatedMagasin = magasinRepository.findOne(magasin.getId());
        updatedMagasin
            .code(UPDATED_CODE)
            .nomMagasin(UPDATED_NOM_MAGASIN)
            .ville(UPDATED_VILLE)
            .telephone(UPDATED_TELEPHONE);

        restMagasinMockMvc.perform(put("/api/magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMagasin)))
            .andExpect(status().isOk());

        // Validate the Magasin in the database
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeUpdate);
        Magasin testMagasin = magasinList.get(magasinList.size() - 1);
        assertThat(testMagasin.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMagasin.getNomMagasin()).isEqualTo(UPDATED_NOM_MAGASIN);
        assertThat(testMagasin.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testMagasin.getTelephone()).isEqualTo(UPDATED_TELEPHONE);

        // Validate the Magasin in Elasticsearch
        Magasin magasinEs = magasinSearchRepository.findOne(testMagasin.getId());
        assertThat(magasinEs).isEqualToComparingFieldByField(testMagasin);
    }

    @Test
    @Transactional
    public void updateNonExistingMagasin() throws Exception {
        int databaseSizeBeforeUpdate = magasinRepository.findAll().size();

        // Create the Magasin

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMagasinMockMvc.perform(put("/api/magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(magasin)))
            .andExpect(status().isCreated());

        // Validate the Magasin in the database
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMagasin() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);
        magasinSearchRepository.save(magasin);
        int databaseSizeBeforeDelete = magasinRepository.findAll().size();

        // Get the magasin
        restMagasinMockMvc.perform(delete("/api/magasins/{id}", magasin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean magasinExistsInEs = magasinSearchRepository.exists(magasin.getId());
        assertThat(magasinExistsInEs).isFalse();

        // Validate the database is empty
        List<Magasin> magasinList = magasinRepository.findAll();
        assertThat(magasinList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMagasin() throws Exception {
        // Initialize the database
        magasinRepository.saveAndFlush(magasin);
        magasinSearchRepository.save(magasin);

        // Search the magasin
        restMagasinMockMvc.perform(get("/api/_search/magasins?query=id:" + magasin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(magasin.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].nomMagasin").value(hasItem(DEFAULT_NOM_MAGASIN.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Magasin.class);
        Magasin magasin1 = new Magasin();
        magasin1.setId(1L);
        Magasin magasin2 = new Magasin();
        magasin2.setId(magasin1.getId());
        assertThat(magasin1).isEqualTo(magasin2);
        magasin2.setId(2L);
        assertThat(magasin1).isNotEqualTo(magasin2);
        magasin1.setId(null);
        assertThat(magasin1).isNotEqualTo(magasin2);
    }
}
