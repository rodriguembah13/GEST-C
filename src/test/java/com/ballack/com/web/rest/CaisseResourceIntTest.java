package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.Caisse;
import com.ballack.com.repository.CaisseRepository;
import com.ballack.com.service.CaisseService;
import com.ballack.com.repository.search.CaisseSearchRepository;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CaisseResource REST controller.
 *
 * @see CaisseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class CaisseResourceIntTest {

    private static final String DEFAULT_NUMCAISSE = "AAAAAAAAAA";
    private static final String UPDATED_NUMCAISSE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_OUVERTURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OUVERTURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FERMETURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FERMETURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_FONDCAISSE = 1D;
    private static final Double UPDATED_FONDCAISSE = 2D;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private CaisseRepository caisseRepository;

    @Autowired
    private CaisseService caisseService;

    @Autowired
    private CaisseSearchRepository caisseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCaisseMockMvc;

    private Caisse caisse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CaisseResource caisseResource = new CaisseResource(caisseService);
        this.restCaisseMockMvc = MockMvcBuilders.standaloneSetup(caisseResource)
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
    public static Caisse createEntity(EntityManager em) {
        Caisse caisse = new Caisse()
            .numcaisse(DEFAULT_NUMCAISSE)
            .dateOuverture(DEFAULT_DATE_OUVERTURE)
            .dateFermeture(DEFAULT_DATE_FERMETURE)
            .fondcaisse(DEFAULT_FONDCAISSE)
            .active(DEFAULT_ACTIVE);
        return caisse;
    }

    @Before
    public void initTest() {
        caisseSearchRepository.deleteAll();
        caisse = createEntity(em);
    }

    @Test
    @Transactional
    public void createCaisse() throws Exception {
        int databaseSizeBeforeCreate = caisseRepository.findAll().size();

        // Create the Caisse
        restCaisseMockMvc.perform(post("/api/caisses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isCreated());

        // Validate the Caisse in the database
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeCreate + 1);
        Caisse testCaisse = caisseList.get(caisseList.size() - 1);
        assertThat(testCaisse.getNumcaisse()).isEqualTo(DEFAULT_NUMCAISSE);
       // assertThat(testCaisse.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testCaisse.getDateFermeture()).isEqualTo(DEFAULT_DATE_FERMETURE);
        assertThat(testCaisse.getFondcaisse()).isEqualTo(DEFAULT_FONDCAISSE);
        assertThat(testCaisse.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Caisse in Elasticsearch
       /* Caisse caisseEs = caisseSearchRepository.findOne(testCaisse.getId());
        assertThat(caisseEs).isEqualToComparingFieldByField(testCaisse);*/
    }

    @Test
    @Transactional
    public void createCaisseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = caisseRepository.findAll().size();

        // Create the Caisse with an existing ID
        caisse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaisseMockMvc.perform(post("/api/caisses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCaisses() throws Exception {
        // Initialize the database
        caisseRepository.saveAndFlush(caisse);

        // Get all the caisseList
        restCaisseMockMvc.perform(get("/api/caisses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caisse.getId().intValue())))
            .andExpect(jsonPath("$.[*].numcaisse").value(hasItem(DEFAULT_NUMCAISSE.toString())))
            .andExpect(jsonPath("$.[*].dateOuverture").value(hasItem(DEFAULT_DATE_OUVERTURE.toString())))
            .andExpect(jsonPath("$.[*].dateFermeture").value(hasItem(DEFAULT_DATE_FERMETURE.toString())))
            .andExpect(jsonPath("$.[*].fondcaisse").value(hasItem(DEFAULT_FONDCAISSE.doubleValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCaisse() throws Exception {
        // Initialize the database
        caisseRepository.saveAndFlush(caisse);

        // Get the caisse
        restCaisseMockMvc.perform(get("/api/caisses/{id}", caisse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(caisse.getId().intValue()))
            .andExpect(jsonPath("$.numcaisse").value(DEFAULT_NUMCAISSE.toString()))
            .andExpect(jsonPath("$.dateOuverture").value(DEFAULT_DATE_OUVERTURE.toString()))
            .andExpect(jsonPath("$.dateFermeture").value(DEFAULT_DATE_FERMETURE.toString()))
            .andExpect(jsonPath("$.fondcaisse").value(DEFAULT_FONDCAISSE.doubleValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCaisse() throws Exception {
        // Get the caisse
        restCaisseMockMvc.perform(get("/api/caisses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCaisse() throws Exception {
        // Initialize the database
        caisseService.save(caisse);

        int databaseSizeBeforeUpdate = caisseRepository.findAll().size();

        // Update the caisse
        Caisse updatedCaisse = caisseRepository.findOne(caisse.getId());
        updatedCaisse
            .numcaisse(UPDATED_NUMCAISSE)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateFermeture(UPDATED_DATE_FERMETURE)
            .fondcaisse(UPDATED_FONDCAISSE)
            .active(UPDATED_ACTIVE);

        restCaisseMockMvc.perform(put("/api/caisses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCaisse)))
            .andExpect(status().isOk());

        // Validate the Caisse in the database
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeUpdate);
        Caisse testCaisse = caisseList.get(caisseList.size() - 1);
        assertThat(testCaisse.getNumcaisse()).isEqualTo(UPDATED_NUMCAISSE);
       // assertThat(testCaisse.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testCaisse.getDateFermeture()).isEqualTo(UPDATED_DATE_FERMETURE);
        assertThat(testCaisse.getFondcaisse()).isEqualTo(UPDATED_FONDCAISSE);
        assertThat(testCaisse.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Caisse in Elasticsearch
        //Caisse caisseEs = caisseSearchRepository.findOne(testCaisse.getId());
        //assertThat(caisseEs).isEqualToComparingFieldByField(testCaisse);
    }

    @Test
    @Transactional
    public void updateNonExistingCaisse() throws Exception {
        int databaseSizeBeforeUpdate = caisseRepository.findAll().size();

        // Create the Caisse

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCaisseMockMvc.perform(put("/api/caisses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(caisse)))
            .andExpect(status().isCreated());

        // Validate the Caisse in the database
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCaisse() throws Exception {
        // Initialize the database
        caisseService.save(caisse);

        int databaseSizeBeforeDelete = caisseRepository.findAll().size();

        // Get the caisse
        restCaisseMockMvc.perform(delete("/api/caisses/{id}", caisse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean caisseExistsInEs = caisseSearchRepository.exists(caisse.getId());
        assertThat(caisseExistsInEs).isFalse();

        // Validate the database is empty
        List<Caisse> caisseList = caisseRepository.findAll();
        assertThat(caisseList).hasSize(databaseSizeBeforeDelete - 1);
    }
/*
    @Test
    @Transactional
    public void searchCaisse() throws Exception {
        // Initialize the database
        caisseService.save(caisse);

        // Search the caisse
        restCaisseMockMvc.perform(get("/api/_search/caisses?query=id:" + caisse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caisse.getId().intValue())))
            .andExpect(jsonPath("$.[*].numcaisse").value(hasItem(DEFAULT_NUMCAISSE.toString())))
           .andExpect(jsonPath("$.[*].dateFermeture").value(hasItem(DEFAULT_DATE_FERMETURE.toString())))
            .andExpect(jsonPath("$.[*].fondcaisse").value(hasItem(DEFAULT_FONDCAISSE.doubleValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }*/

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caisse.class);
        Caisse caisse1 = new Caisse();
        caisse1.setId(1L);
        Caisse caisse2 = new Caisse();
        caisse2.setId(caisse1.getId());
        assertThat(caisse1).isEqualTo(caisse2);
        caisse2.setId(2L);
        assertThat(caisse1).isNotEqualTo(caisse2);
        caisse1.setId(null);
        assertThat(caisse1).isNotEqualTo(caisse2);
    }
}
