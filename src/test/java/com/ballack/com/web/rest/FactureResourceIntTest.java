package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.config.ApplicationProperties;
import com.ballack.com.domain.Facture;
import com.ballack.com.repository.FactureRepository;
import com.ballack.com.service.FactureService;
import com.ballack.com.repository.search.FactureSearchRepository;
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
 * Test class for the FactureResource REST controller.
 *
 * @see FactureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class FactureResourceIntTest {

    private static final String DEFAULT_NUMFACTURE = "AAAAAAAAAA";
    private static final String UPDATED_NUMFACTURE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANTTOTALHT = 1D;
    private static final Double UPDATED_MONTANTTOTALHT = 2D;

    private static final Double DEFAULT_MONTANTTVA = 1D;
    private static final Double UPDATED_MONTANTTVA = 2D;

    private static final Double DEFAULT_REDUCTION = 1D;
    private static final Double UPDATED_REDUCTION = 2D;

    private static final String DEFAULT_CODEBARRE = "AAAAAAAAAA";
    private static final String UPDATED_CODEBARRE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLEFACTURE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLEFACTURE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATEEDITION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEEDITION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATEFACTURATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEFACTURATION = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_MONTANTTOTALTTC = 1D;
    private static final Double UPDATED_MONTANTTOTALTTC = 2D;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureService factureService;

    @Autowired
    private FactureSearchRepository factureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFactureMockMvc;

    private Facture facture;
    @Autowired
    private  ApplicationProperties applicationProperties;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FactureResource factureResource = new FactureResource(factureService, applicationProperties);
        this.restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource)
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
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .numfacture(DEFAULT_NUMFACTURE)
            .montanttotalht(DEFAULT_MONTANTTOTALHT)
            .montanttva(DEFAULT_MONTANTTVA)
            .reduction(DEFAULT_REDUCTION)
            .codebarre(DEFAULT_CODEBARRE)
            .libellefacture(DEFAULT_LIBELLEFACTURE)
            .dateedition(DEFAULT_DATEEDITION)
            .datefacturation(DEFAULT_DATEFACTURATION)
            .montanttotalttc(DEFAULT_MONTANTTOTALTTC)
            .observation(DEFAULT_OBSERVATION);
        return facture;
    }

    @Before
    public void initTest() {
        factureSearchRepository.deleteAll();
        facture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getNumfacture()).isEqualTo(DEFAULT_NUMFACTURE);
        assertThat(testFacture.getMontanttotalht()).isEqualTo(DEFAULT_MONTANTTOTALHT);
        assertThat(testFacture.getMontanttva()).isEqualTo(DEFAULT_MONTANTTVA);
        assertThat(testFacture.getReduction()).isEqualTo(DEFAULT_REDUCTION);
        assertThat(testFacture.getCodebarre()).isEqualTo(DEFAULT_CODEBARRE);
        assertThat(testFacture.getLibellefacture()).isEqualTo(DEFAULT_LIBELLEFACTURE);
        assertThat(testFacture.getDateedition()).isEqualTo(DEFAULT_DATEEDITION);
        assertThat(testFacture.getDatefacturation()).isEqualTo(DEFAULT_DATEFACTURATION);
        assertThat(testFacture.getMontanttotalttc()).isEqualTo(DEFAULT_MONTANTTOTALTTC);
        assertThat(testFacture.getObservation()).isEqualTo(DEFAULT_OBSERVATION);

        // Validate the Facture in Elasticsearch
       // Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        //assertThat(factureEs).isEqualToComparingFieldByField(testFacture);
    }

    @Test
    @Transactional
    public void createFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture with an existing ID
        facture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].numfacture").value(hasItem(DEFAULT_NUMFACTURE.toString())))
            .andExpect(jsonPath("$.[*].montanttotalht").value(hasItem(DEFAULT_MONTANTTOTALHT.doubleValue())))
            .andExpect(jsonPath("$.[*].montanttva").value(hasItem(DEFAULT_MONTANTTVA.doubleValue())))
            .andExpect(jsonPath("$.[*].reduction").value(hasItem(DEFAULT_REDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].codebarre").value(hasItem(DEFAULT_CODEBARRE.toString())))
            .andExpect(jsonPath("$.[*].libellefacture").value(hasItem(DEFAULT_LIBELLEFACTURE.toString())))
            .andExpect(jsonPath("$.[*].dateedition").value(hasItem(DEFAULT_DATEEDITION.toString())))
            .andExpect(jsonPath("$.[*].datefacturation").value(hasItem(DEFAULT_DATEFACTURATION.toString())))
            .andExpect(jsonPath("$.[*].montanttotalttc").value(hasItem(DEFAULT_MONTANTTOTALTTC.doubleValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }

    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.numfacture").value(DEFAULT_NUMFACTURE.toString()))
            .andExpect(jsonPath("$.montanttotalht").value(DEFAULT_MONTANTTOTALHT.doubleValue()))
            .andExpect(jsonPath("$.montanttva").value(DEFAULT_MONTANTTVA.doubleValue()))
            .andExpect(jsonPath("$.reduction").value(DEFAULT_REDUCTION.doubleValue()))
            .andExpect(jsonPath("$.codebarre").value(DEFAULT_CODEBARRE.toString()))
            .andExpect(jsonPath("$.libellefacture").value(DEFAULT_LIBELLEFACTURE.toString()))
            .andExpect(jsonPath("$.dateedition").value(DEFAULT_DATEEDITION.toString()))
            .andExpect(jsonPath("$.datefacturation").value(DEFAULT_DATEFACTURATION.toString()))
            .andExpect(jsonPath("$.montanttotalttc").value(DEFAULT_MONTANTTOTALTTC.doubleValue()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findOne(facture.getId());
        updatedFacture
            .numfacture(UPDATED_NUMFACTURE)
            .montanttotalht(UPDATED_MONTANTTOTALHT)
            .montanttva(UPDATED_MONTANTTVA)
            .reduction(UPDATED_REDUCTION)
            .codebarre(UPDATED_CODEBARRE)
            .libellefacture(UPDATED_LIBELLEFACTURE)
            .dateedition(UPDATED_DATEEDITION)
            .datefacturation(UPDATED_DATEFACTURATION)
            .montanttotalttc(UPDATED_MONTANTTOTALTTC)
            .observation(UPDATED_OBSERVATION);

        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFacture)))
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getNumfacture()).isEqualTo(UPDATED_NUMFACTURE);
        assertThat(testFacture.getMontanttotalht()).isEqualTo(UPDATED_MONTANTTOTALHT);
        assertThat(testFacture.getMontanttva()).isEqualTo(UPDATED_MONTANTTVA);
        assertThat(testFacture.getReduction()).isEqualTo(UPDATED_REDUCTION);
        assertThat(testFacture.getCodebarre()).isEqualTo(UPDATED_CODEBARRE);
        assertThat(testFacture.getLibellefacture()).isEqualTo(UPDATED_LIBELLEFACTURE);
        assertThat(testFacture.getDateedition()).isEqualTo(UPDATED_DATEEDITION);
        assertThat(testFacture.getDatefacturation()).isEqualTo(UPDATED_DATEFACTURATION);
        assertThat(testFacture.getMontanttotalttc()).isEqualTo(UPDATED_MONTANTTOTALTTC);
        assertThat(testFacture.getObservation()).isEqualTo(UPDATED_OBSERVATION);

        // Validate the Facture in Elasticsearch
       // Facture factureEs = factureSearchRepository.findOne(testFacture.getId());
        //assertThat(factureEs).isEqualToComparingFieldByField(testFacture);
    }

    @Test
    @Transactional
    public void updateNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Create the Facture

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFactureMockMvc.perform(put("/api/factures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Get the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        //boolean factureExistsInEs = factureSearchRepository.exists(facture.getId());
        //assertThat(factureExistsInEs).isFalse();

        // Validate the database is empty
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    /*@Test
    @Transactional
    public void searchFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        // Search the facture
        restFactureMockMvc.perform(get("/api/_search/factures?query=id:" + facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].numfacture").value(hasItem(DEFAULT_NUMFACTURE.toString())))
            .andExpect(jsonPath("$.[*].montanttotalht").value(hasItem(DEFAULT_MONTANTTOTALHT.doubleValue())))
            .andExpect(jsonPath("$.[*].montanttva").value(hasItem(DEFAULT_MONTANTTVA.doubleValue())))
            .andExpect(jsonPath("$.[*].reduction").value(hasItem(DEFAULT_REDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].codebarre").value(hasItem(DEFAULT_CODEBARRE.toString())))
            .andExpect(jsonPath("$.[*].libellefacture").value(hasItem(DEFAULT_LIBELLEFACTURE.toString())))
            .andExpect(jsonPath("$.[*].dateedition").value(hasItem(DEFAULT_DATEEDITION.toString())))
            .andExpect(jsonPath("$.[*].datefacturation").value(hasItem(DEFAULT_DATEFACTURATION.toString())))
            .andExpect(jsonPath("$.[*].montanttotalttc").value(hasItem(DEFAULT_MONTANTTOTALTTC.doubleValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }*/

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facture.class);
        Facture facture1 = new Facture();
        facture1.setId(1L);
        Facture facture2 = new Facture();
        facture2.setId(facture1.getId());
        assertThat(facture1).isEqualTo(facture2);
        facture2.setId(2L);
        assertThat(facture1).isNotEqualTo(facture2);
        facture1.setId(null);
        assertThat(facture1).isNotEqualTo(facture2);
    }
}
