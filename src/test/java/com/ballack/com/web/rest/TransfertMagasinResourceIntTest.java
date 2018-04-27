package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.TransfertMagasin;
import com.ballack.com.repository.TransfertMagasinRepository;
import com.ballack.com.service.TransfertMagasinService;
import com.ballack.com.repository.search.TransfertMagasinSearchRepository;
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
 * Test class for the TransfertMagasinResource REST controller.
 *
 * @see TransfertMagasinResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class TransfertMagasinResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final LocalDate DEFAULT_DATE_TRANFERT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TRANFERT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TransfertMagasinRepository transfertMagasinRepository;

    @Autowired
    private TransfertMagasinService transfertMagasinService;

    @Autowired
    private TransfertMagasinSearchRepository transfertMagasinSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransfertMagasinMockMvc;

    private TransfertMagasin transfertMagasin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransfertMagasinResource transfertMagasinResource = new TransfertMagasinResource(transfertMagasinService);
        this.restTransfertMagasinMockMvc = MockMvcBuilders.standaloneSetup(transfertMagasinResource)
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
    public static TransfertMagasin createEntity(EntityManager em) {
        TransfertMagasin transfertMagasin = new TransfertMagasin()
            .libelle(DEFAULT_LIBELLE)
            .quantite(DEFAULT_QUANTITE)
            .date_tranfert(DEFAULT_DATE_TRANFERT);
        return transfertMagasin;
    }

    @Before
    public void initTest() {
        transfertMagasinSearchRepository.deleteAll();
        transfertMagasin = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransfertMagasin() throws Exception {
        int databaseSizeBeforeCreate = transfertMagasinRepository.findAll().size();

        // Create the TransfertMagasin
        restTransfertMagasinMockMvc.perform(post("/api/transfert-magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transfertMagasin)))
            .andExpect(status().isCreated());

        // Validate the TransfertMagasin in the database
        List<TransfertMagasin> transfertMagasinList = transfertMagasinRepository.findAll();
        assertThat(transfertMagasinList).hasSize(databaseSizeBeforeCreate + 1);
        TransfertMagasin testTransfertMagasin = transfertMagasinList.get(transfertMagasinList.size() - 1);
        assertThat(testTransfertMagasin.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTransfertMagasin.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testTransfertMagasin.getDate_tranfert()).isEqualTo(DEFAULT_DATE_TRANFERT);

        // Validate the TransfertMagasin in Elasticsearch
        TransfertMagasin transfertMagasinEs = transfertMagasinSearchRepository.findOne(testTransfertMagasin.getId());
        assertThat(transfertMagasinEs).isEqualToComparingFieldByField(testTransfertMagasin);
    }

    @Test
    @Transactional
    public void createTransfertMagasinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transfertMagasinRepository.findAll().size();

        // Create the TransfertMagasin with an existing ID
        transfertMagasin.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransfertMagasinMockMvc.perform(post("/api/transfert-magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transfertMagasin)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TransfertMagasin> transfertMagasinList = transfertMagasinRepository.findAll();
        assertThat(transfertMagasinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = transfertMagasinRepository.findAll().size();
        // set the field null
        transfertMagasin.setQuantite(null);

        // Create the TransfertMagasin, which fails.

        restTransfertMagasinMockMvc.perform(post("/api/transfert-magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transfertMagasin)))
            .andExpect(status().isBadRequest());

        List<TransfertMagasin> transfertMagasinList = transfertMagasinRepository.findAll();
        assertThat(transfertMagasinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransfertMagasins() throws Exception {
        // Initialize the database
        transfertMagasinRepository.saveAndFlush(transfertMagasin);

        // Get all the transfertMagasinList
        restTransfertMagasinMockMvc.perform(get("/api/transfert-magasins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfertMagasin.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].date_tranfert").value(hasItem(DEFAULT_DATE_TRANFERT.toString())));
    }

    @Test
    @Transactional
    public void getTransfertMagasin() throws Exception {
        // Initialize the database
        transfertMagasinRepository.saveAndFlush(transfertMagasin);

        // Get the transfertMagasin
        restTransfertMagasinMockMvc.perform(get("/api/transfert-magasins/{id}", transfertMagasin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transfertMagasin.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.date_tranfert").value(DEFAULT_DATE_TRANFERT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransfertMagasin() throws Exception {
        // Get the transfertMagasin
        restTransfertMagasinMockMvc.perform(get("/api/transfert-magasins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransfertMagasin() throws Exception {
        // Initialize the database
        transfertMagasinService.save(transfertMagasin);

        int databaseSizeBeforeUpdate = transfertMagasinRepository.findAll().size();

        // Update the transfertMagasin
        TransfertMagasin updatedTransfertMagasin = transfertMagasinRepository.findOne(transfertMagasin.getId());
        updatedTransfertMagasin
            .libelle(UPDATED_LIBELLE)
            .quantite(UPDATED_QUANTITE)
            .date_tranfert(UPDATED_DATE_TRANFERT);

        restTransfertMagasinMockMvc.perform(put("/api/transfert-magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransfertMagasin)))
            .andExpect(status().isOk());

        // Validate the TransfertMagasin in the database
        List<TransfertMagasin> transfertMagasinList = transfertMagasinRepository.findAll();
        assertThat(transfertMagasinList).hasSize(databaseSizeBeforeUpdate);
        TransfertMagasin testTransfertMagasin = transfertMagasinList.get(transfertMagasinList.size() - 1);
        assertThat(testTransfertMagasin.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTransfertMagasin.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testTransfertMagasin.getDate_tranfert()).isEqualTo(UPDATED_DATE_TRANFERT);

        // Validate the TransfertMagasin in Elasticsearch
        TransfertMagasin transfertMagasinEs = transfertMagasinSearchRepository.findOne(testTransfertMagasin.getId());
        assertThat(transfertMagasinEs).isEqualToComparingFieldByField(testTransfertMagasin);
    }

    @Test
    @Transactional
    public void updateNonExistingTransfertMagasin() throws Exception {
        int databaseSizeBeforeUpdate = transfertMagasinRepository.findAll().size();

        // Create the TransfertMagasin

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransfertMagasinMockMvc.perform(put("/api/transfert-magasins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transfertMagasin)))
            .andExpect(status().isCreated());

        // Validate the TransfertMagasin in the database
        List<TransfertMagasin> transfertMagasinList = transfertMagasinRepository.findAll();
        assertThat(transfertMagasinList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransfertMagasin() throws Exception {
        // Initialize the database
        transfertMagasinService.save(transfertMagasin);

        int databaseSizeBeforeDelete = transfertMagasinRepository.findAll().size();

        // Get the transfertMagasin
        restTransfertMagasinMockMvc.perform(delete("/api/transfert-magasins/{id}", transfertMagasin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean transfertMagasinExistsInEs = transfertMagasinSearchRepository.exists(transfertMagasin.getId());
        assertThat(transfertMagasinExistsInEs).isFalse();

        // Validate the database is empty
        List<TransfertMagasin> transfertMagasinList = transfertMagasinRepository.findAll();
        assertThat(transfertMagasinList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransfertMagasin() throws Exception {
        // Initialize the database
        transfertMagasinService.save(transfertMagasin);

        // Search the transfertMagasin
        restTransfertMagasinMockMvc.perform(get("/api/_search/transfert-magasins?query=id:" + transfertMagasin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfertMagasin.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].date_tranfert").value(hasItem(DEFAULT_DATE_TRANFERT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransfertMagasin.class);
        TransfertMagasin transfertMagasin1 = new TransfertMagasin();
        transfertMagasin1.setId(1L);
        TransfertMagasin transfertMagasin2 = new TransfertMagasin();
        transfertMagasin2.setId(transfertMagasin1.getId());
        assertThat(transfertMagasin1).isEqualTo(transfertMagasin2);
        transfertMagasin2.setId(2L);
        assertThat(transfertMagasin1).isNotEqualTo(transfertMagasin2);
        transfertMagasin1.setId(null);
        assertThat(transfertMagasin1).isNotEqualTo(transfertMagasin2);
    }
}
