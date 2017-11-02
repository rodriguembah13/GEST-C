package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.Stock;
import com.ballack.com.repository.StockRepository;
import com.ballack.com.service.StockService;
import com.ballack.com.repository.search.StockSearchRepository;
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
 * Test class for the StockResource REST controller.
 *
 * @see StockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class StockResourceIntTest {

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final Integer DEFAULT_QUANTITE_ALERTE = 1;
    private static final Integer UPDATED_QUANTITE_ALERTE = 2;

    private static final Double DEFAULT_PRIX_ARTICLE = 1D;
    private static final Double UPDATED_PRIX_ARTICLE = 2D;

    private static final LocalDate DEFAULT_DATEPEREMPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEPEREMPTION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final Double DEFAULT_PRIX_ACHAT = 1D;
    private static final Double UPDATED_PRIX_ACHAT = 2D;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final Double DEFAULT_TAXE_TVA = 1D;
    private static final Double UPDATED_TAXE_TVA = 2D;

    private static final Integer DEFAULT_QUANTITE_GROS = 1;
    private static final Integer UPDATED_QUANTITE_GROS = 2;

    private static final Integer DEFAULT_QUANTITE_ALERTE_GROS = 1;
    private static final Integer UPDATED_QUANTITE_ALERTE_GROS = 2;

    private static final Boolean DEFAULT_CLOSED = false;
    private static final Boolean UPDATED_CLOSED = true;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockSearchRepository stockSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockMockMvc;

    private Stock stock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockResource stockResource = new StockResource(stockService);
        this.restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
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
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .quantite(DEFAULT_QUANTITE)
            .quantiteAlerte(DEFAULT_QUANTITE_ALERTE)
            .prixArticle(DEFAULT_PRIX_ARTICLE)
            .dateperemption(DEFAULT_DATEPEREMPTION)
            .actif(DEFAULT_ACTIF)
            .prixAchat(DEFAULT_PRIX_ACHAT)
            .observation(DEFAULT_OBSERVATION)
            .taxeTVA(DEFAULT_TAXE_TVA)
            .quantiteGros(DEFAULT_QUANTITE_GROS)
            .quantiteAlerteGros(DEFAULT_QUANTITE_ALERTE_GROS)
            .closed(DEFAULT_CLOSED);
        return stock;
    }

    @Before
    public void initTest() {
        // stockSearchRepository.deleteAll();
        stock = createEntity(em);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testStock.getQuantiteAlerte()).isEqualTo(DEFAULT_QUANTITE_ALERTE);
        assertThat(testStock.getPrixArticle()).isEqualTo(DEFAULT_PRIX_ARTICLE);
        assertThat(testStock.getDateperemption()).isEqualTo(DEFAULT_DATEPEREMPTION);
        assertThat(testStock.isActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testStock.getPrixAchat()).isEqualTo(DEFAULT_PRIX_ACHAT);
        assertThat(testStock.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testStock.getTaxeTVA()).isEqualTo(DEFAULT_TAXE_TVA);
        assertThat(testStock.getQuantiteGros()).isEqualTo(DEFAULT_QUANTITE_GROS);
        assertThat(testStock.getQuantiteAlerteGros()).isEqualTo(DEFAULT_QUANTITE_ALERTE_GROS);
        assertThat(testStock.isClosed()).isEqualTo(DEFAULT_CLOSED);

        // Validate the Stock in Elasticsearch
        // Stock stockEs = stockSearchRepository.findOne(testStock.getId());
        //assertThat(stockEs).isEqualToComparingFieldByField(testStock);
    }

    @Test
    @Transactional
    public void createStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock with an existing ID
        stock.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].quantiteAlerte").value(hasItem(DEFAULT_QUANTITE_ALERTE)))
            .andExpect(jsonPath("$.[*].prixArticle").value(hasItem(DEFAULT_PRIX_ARTICLE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateperemption").value(hasItem(DEFAULT_DATEPEREMPTION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].prixAchat").value(hasItem(DEFAULT_PRIX_ACHAT.doubleValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())))
            .andExpect(jsonPath("$.[*].taxeTVA").value(hasItem(DEFAULT_TAXE_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].quantiteGros").value(hasItem(DEFAULT_QUANTITE_GROS)))
            .andExpect(jsonPath("$.[*].quantiteAlerteGros").value(hasItem(DEFAULT_QUANTITE_ALERTE_GROS)))
            .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.booleanValue())));
    }

    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.quantiteAlerte").value(DEFAULT_QUANTITE_ALERTE))
            .andExpect(jsonPath("$.prixArticle").value(DEFAULT_PRIX_ARTICLE.doubleValue()))
            .andExpect(jsonPath("$.dateperemption").value(DEFAULT_DATEPEREMPTION.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()))
            .andExpect(jsonPath("$.prixAchat").value(DEFAULT_PRIX_ACHAT.doubleValue()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()))
            .andExpect(jsonPath("$.taxeTVA").value(DEFAULT_TAXE_TVA.doubleValue()))
            .andExpect(jsonPath("$.quantiteGros").value(DEFAULT_QUANTITE_GROS))
            .andExpect(jsonPath("$.quantiteAlerteGros").value(DEFAULT_QUANTITE_ALERTE_GROS))
            .andExpect(jsonPath("$.closed").value(DEFAULT_CLOSED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findOne(stock.getId());
        updatedStock
            .quantite(UPDATED_QUANTITE)
            .quantiteAlerte(UPDATED_QUANTITE_ALERTE)
            .prixArticle(UPDATED_PRIX_ARTICLE)
            .dateperemption(UPDATED_DATEPEREMPTION)
            .actif(UPDATED_ACTIF)
            .prixAchat(UPDATED_PRIX_ACHAT)
            .observation(UPDATED_OBSERVATION)
            .taxeTVA(UPDATED_TAXE_TVA)
            .quantiteGros(UPDATED_QUANTITE_GROS)
            .quantiteAlerteGros(UPDATED_QUANTITE_ALERTE_GROS)
            .closed(UPDATED_CLOSED);

        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStock)))
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testStock.getQuantiteAlerte()).isEqualTo(UPDATED_QUANTITE_ALERTE);
        assertThat(testStock.getPrixArticle()).isEqualTo(UPDATED_PRIX_ARTICLE);
        assertThat(testStock.getDateperemption()).isEqualTo(UPDATED_DATEPEREMPTION);
        assertThat(testStock.isActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testStock.getPrixAchat()).isEqualTo(UPDATED_PRIX_ACHAT);
        assertThat(testStock.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testStock.getTaxeTVA()).isEqualTo(UPDATED_TAXE_TVA);
        assertThat(testStock.getQuantiteGros()).isEqualTo(UPDATED_QUANTITE_GROS);
        assertThat(testStock.getQuantiteAlerteGros()).isEqualTo(UPDATED_QUANTITE_ALERTE_GROS);
        assertThat(testStock.isClosed()).isEqualTo(UPDATED_CLOSED);

        // Validate the Stock in Elasticsearch
        //Stock stockEs = stockSearchRepository.findOne(testStock.getId());
        //assertThat(stockEs).isEqualToComparingFieldByField(testStock);
    }

    @Test
    @Transactional
    public void updateNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Create the Stock

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Get the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        //boolean stockExistsInEs = stockSearchRepository.exists(stock.getId());
        //assertThat(stockExistsInEs).isFalse();

        // Validate the database is empty
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }

   /* @Test
    @Transactional
    public void searchStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        // Search the stock
        restStockMockMvc.perform(get("/api/_search/stocks?query=id:" + stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].quantiteAlerte").value(hasItem(DEFAULT_QUANTITE_ALERTE)))
            .andExpect(jsonPath("$.[*].prixArticle").value(hasItem(DEFAULT_PRIX_ARTICLE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateperemption").value(hasItem(DEFAULT_DATEPEREMPTION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].prixAchat").value(hasItem(DEFAULT_PRIX_ACHAT.doubleValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())))
            .andExpect(jsonPath("$.[*].taxeTVA").value(hasItem(DEFAULT_TAXE_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].quantiteGros").value(hasItem(DEFAULT_QUANTITE_GROS)))
            .andExpect(jsonPath("$.[*].quantiteAlerteGros").value(hasItem(DEFAULT_QUANTITE_ALERTE_GROS)))
            .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.booleanValue())));
    }
*/
    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stock.class);
        Stock stock1 = new Stock();
        stock1.setId(1L);
        Stock stock2 = new Stock();
        stock2.setId(stock1.getId());
        assertThat(stock1).isEqualTo(stock2);
        stock2.setId(2L);
        assertThat(stock1).isNotEqualTo(stock2);
        stock1.setId(null);
        assertThat(stock1).isNotEqualTo(stock2);
    }
}
