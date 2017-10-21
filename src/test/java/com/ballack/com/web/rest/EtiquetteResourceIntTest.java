package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.Etiquette;
import com.ballack.com.repository.EtiquetteRepository;
import com.ballack.com.repository.search.EtiquetteSearchRepository;
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
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.ballack.com.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EtiquetteResource REST controller.
 *
 * @see EtiquetteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class EtiquetteResourceIntTest {

    private static final String DEFAULT_ETIQUETTE = "AAAAAAAAAA";
    private static final String UPDATED_ETIQUETTE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_BARE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_BARE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ZonedDateTime DEFAULT_DATE_CREA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private EtiquetteRepository etiquetteRepository;

    @Autowired
    private EtiquetteSearchRepository etiquetteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEtiquetteMockMvc;

    private Etiquette etiquette;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EtiquetteResource etiquetteResource = new EtiquetteResource(etiquetteRepository, etiquetteSearchRepository);
        this.restEtiquetteMockMvc = MockMvcBuilders.standaloneSetup(etiquetteResource)
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
    public static Etiquette createEntity(EntityManager em) {
        Etiquette etiquette = new Etiquette()
            .etiquette(DEFAULT_ETIQUETTE)
            .codeBare(DEFAULT_CODE_BARE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateCrea(DEFAULT_DATE_CREA);
        return etiquette;
    }

    @Before
    public void initTest() {
        etiquetteSearchRepository.deleteAll();
        etiquette = createEntity(em);
    }

    @Test
    @Transactional
    public void createEtiquette() throws Exception {
        int databaseSizeBeforeCreate = etiquetteRepository.findAll().size();

        // Create the Etiquette
        restEtiquetteMockMvc.perform(post("/api/etiquettes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etiquette)))
            .andExpect(status().isCreated());

        // Validate the Etiquette in the database
        List<Etiquette> etiquetteList = etiquetteRepository.findAll();
        assertThat(etiquetteList).hasSize(databaseSizeBeforeCreate + 1);
        Etiquette testEtiquette = etiquetteList.get(etiquetteList.size() - 1);
        assertThat(testEtiquette.getEtiquette()).isEqualTo(DEFAULT_ETIQUETTE);
        assertThat(testEtiquette.getCodeBare()).isEqualTo(DEFAULT_CODE_BARE);
        assertThat(testEtiquette.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testEtiquette.getDateCrea()).isEqualTo(DEFAULT_DATE_CREA);

        // Validate the Etiquette in Elasticsearch
        Etiquette etiquetteEs = etiquetteSearchRepository.findOne(testEtiquette.getId());
        assertThat(etiquetteEs).isEqualToComparingFieldByField(testEtiquette);
    }

    @Test
    @Transactional
    public void createEtiquetteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = etiquetteRepository.findAll().size();

        // Create the Etiquette with an existing ID
        etiquette.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtiquetteMockMvc.perform(post("/api/etiquettes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etiquette)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Etiquette> etiquetteList = etiquetteRepository.findAll();
        assertThat(etiquetteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEtiquettes() throws Exception {
        // Initialize the database
        etiquetteRepository.saveAndFlush(etiquette);

        // Get all the etiquetteList
        restEtiquetteMockMvc.perform(get("/api/etiquettes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etiquette.getId().intValue())))
            .andExpect(jsonPath("$.[*].etiquette").value(hasItem(DEFAULT_ETIQUETTE.toString())))
            .andExpect(jsonPath("$.[*].codeBare").value(hasItem(DEFAULT_CODE_BARE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateCrea").value(hasItem(sameInstant(DEFAULT_DATE_CREA))));
    }

    @Test
    @Transactional
    public void getEtiquette() throws Exception {
        // Initialize the database
        etiquetteRepository.saveAndFlush(etiquette);

        // Get the etiquette
        restEtiquetteMockMvc.perform(get("/api/etiquettes/{id}", etiquette.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(etiquette.getId().intValue()))
            .andExpect(jsonPath("$.etiquette").value(DEFAULT_ETIQUETTE.toString()))
            .andExpect(jsonPath("$.codeBare").value(DEFAULT_CODE_BARE.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateCrea").value(sameInstant(DEFAULT_DATE_CREA)));
    }

    @Test
    @Transactional
    public void getNonExistingEtiquette() throws Exception {
        // Get the etiquette
        restEtiquetteMockMvc.perform(get("/api/etiquettes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtiquette() throws Exception {
        // Initialize the database
        etiquetteRepository.saveAndFlush(etiquette);
        etiquetteSearchRepository.save(etiquette);
        int databaseSizeBeforeUpdate = etiquetteRepository.findAll().size();

        // Update the etiquette
        Etiquette updatedEtiquette = etiquetteRepository.findOne(etiquette.getId());
        updatedEtiquette
            .etiquette(UPDATED_ETIQUETTE)
            .codeBare(UPDATED_CODE_BARE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateCrea(UPDATED_DATE_CREA);

        restEtiquetteMockMvc.perform(put("/api/etiquettes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEtiquette)))
            .andExpect(status().isOk());

        // Validate the Etiquette in the database
        List<Etiquette> etiquetteList = etiquetteRepository.findAll();
        assertThat(etiquetteList).hasSize(databaseSizeBeforeUpdate);
        Etiquette testEtiquette = etiquetteList.get(etiquetteList.size() - 1);
        assertThat(testEtiquette.getEtiquette()).isEqualTo(UPDATED_ETIQUETTE);
        assertThat(testEtiquette.getCodeBare()).isEqualTo(UPDATED_CODE_BARE);
        assertThat(testEtiquette.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testEtiquette.getDateCrea()).isEqualTo(UPDATED_DATE_CREA);

        // Validate the Etiquette in Elasticsearch
        Etiquette etiquetteEs = etiquetteSearchRepository.findOne(testEtiquette.getId());
        assertThat(etiquetteEs).isEqualToComparingFieldByField(testEtiquette);
    }

    @Test
    @Transactional
    public void updateNonExistingEtiquette() throws Exception {
        int databaseSizeBeforeUpdate = etiquetteRepository.findAll().size();

        // Create the Etiquette

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEtiquetteMockMvc.perform(put("/api/etiquettes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(etiquette)))
            .andExpect(status().isCreated());

        // Validate the Etiquette in the database
        List<Etiquette> etiquetteList = etiquetteRepository.findAll();
        assertThat(etiquetteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEtiquette() throws Exception {
        // Initialize the database
        etiquetteRepository.saveAndFlush(etiquette);
        etiquetteSearchRepository.save(etiquette);
        int databaseSizeBeforeDelete = etiquetteRepository.findAll().size();

        // Get the etiquette
        restEtiquetteMockMvc.perform(delete("/api/etiquettes/{id}", etiquette.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean etiquetteExistsInEs = etiquetteSearchRepository.exists(etiquette.getId());
        assertThat(etiquetteExistsInEs).isFalse();

        // Validate the database is empty
        List<Etiquette> etiquetteList = etiquetteRepository.findAll();
        assertThat(etiquetteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEtiquette() throws Exception {
        // Initialize the database
        etiquetteRepository.saveAndFlush(etiquette);
        etiquetteSearchRepository.save(etiquette);

        // Search the etiquette
        restEtiquetteMockMvc.perform(get("/api/_search/etiquettes?query=id:" + etiquette.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etiquette.getId().intValue())))
            .andExpect(jsonPath("$.[*].etiquette").value(hasItem(DEFAULT_ETIQUETTE.toString())))
            .andExpect(jsonPath("$.[*].codeBare").value(hasItem(DEFAULT_CODE_BARE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateCrea").value(hasItem(sameInstant(DEFAULT_DATE_CREA))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etiquette.class);
        Etiquette etiquette1 = new Etiquette();
        etiquette1.setId(1L);
        Etiquette etiquette2 = new Etiquette();
        etiquette2.setId(etiquette1.getId());
        assertThat(etiquette1).isEqualTo(etiquette2);
        etiquette2.setId(2L);
        assertThat(etiquette1).isNotEqualTo(etiquette2);
        etiquette1.setId(null);
        assertThat(etiquette1).isNotEqualTo(etiquette2);
    }
}
