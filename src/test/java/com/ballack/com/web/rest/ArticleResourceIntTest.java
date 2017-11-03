package com.ballack.com.web.rest;

import com.ballack.com.GestCApp;

import com.ballack.com.domain.Article;
import com.ballack.com.repository.ArticleRepository;
import com.ballack.com.service.ArticleService;
import com.ballack.com.repository.search.ArticleSearchRepository;
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
 * Test class for the ArticleResource REST controller.
 *
 * @see ArticleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestCApp.class)
public class ArticleResourceIntTest {

    private static final String DEFAULT_NUM_ARTICLE = "ART";
    private static final String UPDATED_NUM_ARTICLE = "ART";

    private static final Double DEFAULT_POIDS = 1D;
    private static final Double UPDATED_POIDS = 2D;

    private static final String DEFAULT_CODEBARRE = "AAAAAAAAAA";
    private static final String UPDATED_CODEBARRE = "BBBBBBBBBB";

    private static final String DEFAULT_MARQUE = "AAAAAAAAAA";
    private static final String UPDATED_MARQUE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATECREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATECREATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOMARTICLE = "AAAAAAAAAA";
    private static final String UPDATED_NOMARTICLE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRIX_COURANT = 1D;
    private static final Double UPDATED_PRIX_COURANT = 2D;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleSearchRepository articleSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArticleMockMvc;

    private Article article;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArticleResource articleResource = new ArticleResource(articleService);
        this.restArticleMockMvc = MockMvcBuilders.standaloneSetup(articleResource)
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
    public static Article createEntity(EntityManager em) {
        Article article = new Article()
            .numArticle(DEFAULT_NUM_ARTICLE)
            .poids(DEFAULT_POIDS)
            .codebarre(DEFAULT_CODEBARRE)
            .marque(DEFAULT_MARQUE)
            .datecreation(DEFAULT_DATECREATION)
            .nomarticle(DEFAULT_NOMARTICLE)
            .prixCourant(DEFAULT_PRIX_COURANT);
        return article;
    }

    @Before
    public void initTest() {
        //articleSearchRepository.deleteAll();
        article = createEntity(em);
    }

    @Test
    @Transactional
    public void createArticle() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // Create the Article
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
        Article testArticle = articleList.get(articleList.size() - 1);
        //assertThat(testArticle.getNumArticle()).isEqualTo("ART");
        assertThat(testArticle.getPoids()).isEqualTo(DEFAULT_POIDS);
        assertThat(testArticle.getCodebarre()).isEqualTo(DEFAULT_CODEBARRE);
        assertThat(testArticle.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testArticle.getNomarticle()).isEqualTo(DEFAULT_NOMARTICLE);
        assertThat(testArticle.getPrixCourant()).isEqualTo(DEFAULT_PRIX_COURANT);

        // Validate the Article in Elasticsearch
        //Article articleEs = articleSearchRepository.findOne(testArticle.getId());
       // assertThat(articleEs).isEqualToComparingFieldByField(testArticle);
    }

    @Test
    @Transactional
    public void createArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // Create the Article with an existing ID
        article.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate);
    }

/*
    @Test
    @Transactional
    public void checkNumArticleIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setNumArticle(null);

        // Create the Article, which fails.

        restArticleMockMvc.perform(post("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }
*/

    @Test
    @Transactional
    public void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].numArticle").value(hasItem(DEFAULT_NUM_ARTICLE.toString())))
            .andExpect(jsonPath("$.[*].poids").value(hasItem(DEFAULT_POIDS.doubleValue())))
            .andExpect(jsonPath("$.[*].codebarre").value(hasItem(DEFAULT_CODEBARRE.toString())))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE.toString())))
            .andExpect(jsonPath("$.[*].datecreation").value(hasItem(DEFAULT_DATECREATION.toString())))
            .andExpect(jsonPath("$.[*].nomarticle").value(hasItem(DEFAULT_NOMARTICLE.toString())))
            .andExpect(jsonPath("$.[*].prixCourant").value(hasItem(DEFAULT_PRIX_COURANT.doubleValue())));
    }

    @Test
    @Transactional
    public void getArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.numArticle").value(DEFAULT_NUM_ARTICLE.toString()))
            .andExpect(jsonPath("$.poids").value(DEFAULT_POIDS.doubleValue()))
            .andExpect(jsonPath("$.codebarre").value(DEFAULT_CODEBARRE.toString()))
            .andExpect(jsonPath("$.marque").value(DEFAULT_MARQUE.toString()))
            .andExpect(jsonPath("$.datecreation").value(DEFAULT_DATECREATION.toString()))
            .andExpect(jsonPath("$.nomarticle").value(DEFAULT_NOMARTICLE.toString()))
            .andExpect(jsonPath("$.prixCourant").value(DEFAULT_PRIX_COURANT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticle() throws Exception {
        // Initialize the database
        articleService.save(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article
        Article updatedArticle = articleRepository.findOne(article.getId());
        updatedArticle
            .numArticle(UPDATED_NUM_ARTICLE)
            .poids(UPDATED_POIDS)
            .codebarre(UPDATED_CODEBARRE)
            .marque(UPDATED_MARQUE)
            .datecreation(UPDATED_DATECREATION)
            .nomarticle(UPDATED_NOMARTICLE)
            .prixCourant(UPDATED_PRIX_COURANT);

        restArticleMockMvc.perform(put("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArticle)))
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        //assertThat(testArticle.getNumArticle()).isEqualTo(UPDATED_NUM_ARTICLE);
        assertThat(testArticle.getPoids()).isEqualTo(UPDATED_POIDS);
        assertThat(testArticle.getCodebarre()).isEqualTo(UPDATED_CODEBARRE);
        assertThat(testArticle.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testArticle.getDatecreation()).isEqualTo(UPDATED_DATECREATION);
        assertThat(testArticle.getNomarticle()).isEqualTo(UPDATED_NOMARTICLE);
        assertThat(testArticle.getPrixCourant()).isEqualTo(UPDATED_PRIX_COURANT);

        // Validate the Article in Elasticsearch
       /* Article articleEs = articleSearchRepository.findOne(testArticle.getId());
        assertThat(articleEs).isEqualToComparingFieldByField(testArticle);*/
    }

    @Test
    @Transactional
    public void updateNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Create the Article

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArticleMockMvc.perform(put("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArticle() throws Exception {
        // Initialize the database
        articleService.save(article);

        int databaseSizeBeforeDelete = articleRepository.findAll().size();

        // Get the article
        restArticleMockMvc.perform(delete("/api/articles/{id}", article.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean articleExistsInEs = articleSearchRepository.exists(article.getId());
        assertThat(articleExistsInEs).isFalse();

        // Validate the database is empty
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeDelete - 1);
    }

/*    @Test
    @Transactional
    public void searchArticle() throws Exception {
        // Initialize the database
        articleService.save(article);

        // Search the article
        restArticleMockMvc.perform(get("/api/_search/articles?query=id:" + article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].numArticle").value(hasItem(DEFAULT_NUM_ARTICLE.toString())))
            .andExpect(jsonPath("$.[*].poids").value(hasItem(DEFAULT_POIDS.doubleValue())))
            .andExpect(jsonPath("$.[*].codebarre").value(hasItem(DEFAULT_CODEBARRE.toString())))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE.toString())))
            .andExpect(jsonPath("$.[*].nomarticle").value(hasItem(DEFAULT_NOMARTICLE.toString())))
            .andExpect(jsonPath("$.[*].prixCourant").value(hasItem(DEFAULT_PRIX_COURANT.doubleValue())));
    }*/

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Article.class);
        Article article1 = new Article();
        article1.setId(1L);
        Article article2 = new Article();
        article2.setId(article1.getId());
        assertThat(article1).isEqualTo(article2);
        article2.setId(2L);
        assertThat(article1).isNotEqualTo(article2);
        article1.setId(null);
        assertThat(article1).isNotEqualTo(article2);
    }
}
