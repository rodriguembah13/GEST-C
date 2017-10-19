package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.Commande;
import com.ballack.com.repository.CommandeRepository;
import com.ballack.com.service.CommandeService;
import com.ballack.com.repository.search.CommandeSearchRepository;
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
import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CommandeResource REST controller.
 *
 * @see CommandeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class CommandeResourceIntTest {

    private static final String DEFAULT_NUMCOMMANDE = "AAAAAAAAAA";
    private static final String UPDATED_NUMCOMMANDE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANTTOTALHT = 1D;
    private static final Double UPDATED_MONTANTTOTALHT = 2D;

    private static final String DEFAULT_CODEBARRE = "AAAAAAAAAA";
    private static final String UPDATED_CODEBARRE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANTTOTALTTC = 1D;
    private static final Double UPDATED_MONTANTTOTALTTC = 2D;

    private static final LocalDate DEFAULT_DATELIMITLIVRAISON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATELIMITLIVRAISON = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_DATECOMMANDE = Instant.now();
    private static final Instant UPDATED_DATECOMMANDE = Instant.now(Clock.system(ZoneId.systemDefault()));

    private static final Boolean DEFAULT_ETAT = false;
    private static final Boolean UPDATED_ETAT = true;

    private static final Boolean DEFAULT_SOLDEE = false;
    private static final Boolean UPDATED_SOLDEE = true;

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private CommandeSearchRepository commandeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommandeMockMvc;

    private Commande commande;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommandeResource commandeResource = new CommandeResource(commandeService);
        this.restCommandeMockMvc = MockMvcBuilders.standaloneSetup(commandeResource)
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
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
            .numcommande(DEFAULT_NUMCOMMANDE)
            .montanttotalht(DEFAULT_MONTANTTOTALHT)
            .codebarre(DEFAULT_CODEBARRE)
            .libelle(DEFAULT_LIBELLE)
            .montanttotalttc(DEFAULT_MONTANTTOTALTTC)
            .datelimitlivraison(DEFAULT_DATELIMITLIVRAISON)
            .datecommande(DEFAULT_DATECOMMANDE)
            .etat(DEFAULT_ETAT)
            .soldee(DEFAULT_SOLDEE);
        return commande;
    }

    @Before
    public void initTest() {
        commandeSearchRepository.deleteAll();
        commande = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande
        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getNumcommande()).isEqualTo(DEFAULT_NUMCOMMANDE);
        assertThat(testCommande.getMontanttotalht()).isEqualTo(DEFAULT_MONTANTTOTALHT);
        assertThat(testCommande.getCodebarre()).isEqualTo(DEFAULT_CODEBARRE);
        assertThat(testCommande.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCommande.getMontanttotalttc()).isEqualTo(DEFAULT_MONTANTTOTALTTC);
        assertThat(testCommande.getDatelimitlivraison()).isEqualTo(DEFAULT_DATELIMITLIVRAISON);
        assertThat(testCommande.getDatecommande()).isEqualTo(DEFAULT_DATECOMMANDE);
        assertThat(testCommande.isEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testCommande.isSoldee()).isEqualTo(DEFAULT_SOLDEE);

        // Validate the Commande in Elasticsearch
        Commande commandeEs = commandeSearchRepository.findOne(testCommande.getId());
        assertThat(commandeEs).isEqualToComparingFieldByField(testCommande);
    }

    @Test
    @Transactional
    public void createCommandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande with an existing ID
        commande.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].numcommande").value(hasItem(DEFAULT_NUMCOMMANDE.toString())))
            .andExpect(jsonPath("$.[*].montanttotalht").value(hasItem(DEFAULT_MONTANTTOTALHT.doubleValue())))
            .andExpect(jsonPath("$.[*].codebarre").value(hasItem(DEFAULT_CODEBARRE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].montanttotalttc").value(hasItem(DEFAULT_MONTANTTOTALTTC.doubleValue())))
            .andExpect(jsonPath("$.[*].datelimitlivraison").value(hasItem(DEFAULT_DATELIMITLIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].datecommande").value(hasItem(DEFAULT_DATECOMMANDE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.booleanValue())))
            .andExpect(jsonPath("$.[*].soldee").value(hasItem(DEFAULT_SOLDEE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.numcommande").value(DEFAULT_NUMCOMMANDE.toString()))
            .andExpect(jsonPath("$.montanttotalht").value(DEFAULT_MONTANTTOTALHT.doubleValue()))
            .andExpect(jsonPath("$.codebarre").value(DEFAULT_CODEBARRE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.montanttotalttc").value(DEFAULT_MONTANTTOTALTTC.doubleValue()))
            .andExpect(jsonPath("$.datelimitlivraison").value(DEFAULT_DATELIMITLIVRAISON.toString()))
            .andExpect(jsonPath("$.datecommande").value(DEFAULT_DATECOMMANDE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.booleanValue()))
            .andExpect(jsonPath("$.soldee").value(DEFAULT_SOLDEE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommande() throws Exception {
        // Initialize the database
        commandeService.save(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findOne(commande.getId());
        updatedCommande
            .numcommande(UPDATED_NUMCOMMANDE)
            .montanttotalht(UPDATED_MONTANTTOTALHT)
            .codebarre(UPDATED_CODEBARRE)
            .libelle(UPDATED_LIBELLE)
            .montanttotalttc(UPDATED_MONTANTTOTALTTC)
            .datelimitlivraison(UPDATED_DATELIMITLIVRAISON)
            .datecommande(UPDATED_DATECOMMANDE)
            .etat(UPDATED_ETAT)
            .soldee(UPDATED_SOLDEE);

        restCommandeMockMvc.perform(put("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommande)))
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getNumcommande()).isEqualTo(UPDATED_NUMCOMMANDE);
        assertThat(testCommande.getMontanttotalht()).isEqualTo(UPDATED_MONTANTTOTALHT);
        assertThat(testCommande.getCodebarre()).isEqualTo(UPDATED_CODEBARRE);
        assertThat(testCommande.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCommande.getMontanttotalttc()).isEqualTo(UPDATED_MONTANTTOTALTTC);
        assertThat(testCommande.getDatelimitlivraison()).isEqualTo(UPDATED_DATELIMITLIVRAISON);
        assertThat(testCommande.getDatecommande()).isEqualTo(UPDATED_DATECOMMANDE);
        assertThat(testCommande.isEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testCommande.isSoldee()).isEqualTo(UPDATED_SOLDEE);

        // Validate the Commande in Elasticsearch
        Commande commandeEs = commandeSearchRepository.findOne(testCommande.getId());
        assertThat(commandeEs).isEqualToComparingFieldByField(testCommande);
    }

    @Test
    @Transactional
    public void updateNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Create the Commande

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommandeMockMvc.perform(put("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommande() throws Exception {
        // Initialize the database
        commandeService.save(commande);

        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Get the commande
        restCommandeMockMvc.perform(delete("/api/commandes/{id}", commande.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean commandeExistsInEs = commandeSearchRepository.exists(commande.getId());
        assertThat(commandeExistsInEs).isFalse();

        // Validate the database is empty
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommande() throws Exception {
        // Initialize the database
        commandeService.save(commande);

        // Search the commande
        restCommandeMockMvc.perform(get("/api/_search/commandes?query=id:" + commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].numcommande").value(hasItem(DEFAULT_NUMCOMMANDE.toString())))
            .andExpect(jsonPath("$.[*].montanttotalht").value(hasItem(DEFAULT_MONTANTTOTALHT.doubleValue())))
            .andExpect(jsonPath("$.[*].codebarre").value(hasItem(DEFAULT_CODEBARRE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].montanttotalttc").value(hasItem(DEFAULT_MONTANTTOTALTTC.doubleValue())))
            .andExpect(jsonPath("$.[*].datelimitlivraison").value(hasItem(DEFAULT_DATELIMITLIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].datecommande").value(hasItem(DEFAULT_DATECOMMANDE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.booleanValue())))
            .andExpect(jsonPath("$.[*].soldee").value(hasItem(DEFAULT_SOLDEE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commande.class);
        Commande commande1 = new Commande();
        commande1.setId(1L);
        Commande commande2 = new Commande();
        commande2.setId(commande1.getId());
        assertThat(commande1).isEqualTo(commande2);
        commande2.setId(2L);
        assertThat(commande1).isNotEqualTo(commande2);
        commande1.setId(null);
        assertThat(commande1).isNotEqualTo(commande2);
    }
}
