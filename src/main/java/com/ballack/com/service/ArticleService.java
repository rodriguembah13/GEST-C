package com.ballack.com.service;

import com.ballack.com.domain.Article;
import com.ballack.com.repository.ArticleRepository;
import com.ballack.com.repository.search.ArticleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Article.
 */
@Service
@Transactional
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    private final ArticleSearchRepository articleSearchRepository;
    public ArticleService(ArticleRepository articleRepository, ArticleSearchRepository articleSearchRepository) {
        this.articleRepository = articleRepository;
        this.articleSearchRepository = articleSearchRepository;
    }

    /**
     * Save a article.
     *
     * @param article the entity to save
     * @return the persisted entity
     */
    public Article save(Article article) {
        int index=1000;
        log.debug("Request to save Article : {}", article);
        article.setDatecreation(LocalDate.now());

        Article result = articleRepository.save(article);
        result.setNumArticle(""+index+result.getId());
        //articleSearchRepository.save(result);
        return articleRepository.saveAndFlush(result);
    }
    public Article saveAndFluch(Article article) {
        log.debug("Request to save Article : {}", article);
        Article result = articleRepository.saveAndFlush(article);
        //articleSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the articles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Article> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable);
    }


    /**
     *  get all the articles where Etiquette is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Article> findAllWhereEtiquetteIsNull() {
        log.debug("Request to get all articles where Etiquette is null");
        return StreamSupport
            .stream(articleRepository.findAll().spliterator(), false)
            .filter(article -> article.getEtiquette() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one article by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Article findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findOne(id);
    }

    /**
     *  Delete the  article by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.delete(id);
        articleSearchRepository.delete(id);
    }

    /**
     * Search for the article corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Article> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Articles for query {}", query);
        Page<Article> result = articleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
