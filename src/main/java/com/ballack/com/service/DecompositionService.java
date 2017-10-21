package com.ballack.com.service;

import com.ballack.com.domain.Article;
import com.ballack.com.domain.Decomposition;
import com.ballack.com.domain.Stock;
import com.ballack.com.repository.ArticleRepository;
import com.ballack.com.repository.DecompositionRepository;
import com.ballack.com.repository.StockRepository;
import com.ballack.com.repository.search.DecompositionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Decomposition.
 */
@Service
@Transactional
public class DecompositionService {

    private final Logger log = LoggerFactory.getLogger(DecompositionService.class);

    private final DecompositionRepository decompositionRepository;
    private final ArticleRepository articleRepository;
    private final StockRepository stockRepository;
    private final DecompositionSearchRepository decompositionSearchRepository;

    public DecompositionService(DecompositionRepository decompositionRepository, ArticleRepository articleRepository, StockRepository stockRepository, DecompositionSearchRepository decompositionSearchRepository) {
        this.decompositionRepository = decompositionRepository;
        this.articleRepository = articleRepository;
        this.stockRepository = stockRepository;
        this.decompositionSearchRepository = decompositionSearchRepository;
    }

    /**
     * Save a decomposition.
     *
     * @param decomposition the entity to save
     * @return the persisted entity
     */
    public Decomposition save(Decomposition decomposition) {
        log.debug("Request to save Decomposition : {}", decomposition);
        Decomposition result = decompositionRepository.save(decomposition);
        decompositionSearchRepository.save(result);
        return result;
    }

    /**
     * Save a decomposition.
     *
     * @param article the entity to save
     * @return the persisted entity
     */
    public Decomposition decomposition(Article article) {
        Decomposition decomposition = new Decomposition();
        log.debug("Request to save Decomposition : {}", article);
        Stock stock = stockRepository.findStockactif(article);
        decomposition.setArticledepart("Art/DEP" + article.getNomarticle() + "/" + stock.getQuantite() + "/" + stock.getQuantiteGros());
        decomposition.setArticle(article);

        int nbre = article.getFormeArticle().getQuantite();
        if (article.getFormeArticle().getNomForme().equals("simple")) {
            if (stock.getQuantiteGros() - 1 > 0) {
                stock.setQuantite(stock.getQuantiteGros());
                stock.setQuantiteGros(0);
                stockRepository.saveAndFlush(stock);
            } else {
                stock.setQuantite(stock.getQuantite());
                stock.setQuantiteGros(stock.getQuantiteGros());
            }
        } else {

            if (stock.getQuantiteGros() - 1 > 0) {
                stock.setQuantite(stock.getQuantite() + nbre);
                stock.setQuantiteGros(stock.getQuantiteGros() - 1);
                stockRepository.saveAndFlush(stock);
            } else {
                stock.setQuantite(stock.getQuantite());
                stock.setQuantiteGros(stock.getQuantiteGros());
            }
        }
        decomposition.setArticlefin("Art/FIN" + article.getNomarticle() + "/" + (stock.getQuantite() + nbre) + "/" + (stock.getQuantiteGros() - 1) + "/");
        Decomposition result = decompositionRepository.save(decomposition);
        decompositionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the decompositions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Decomposition> findAll(Pageable pageable) {
        log.debug("Request to get all Decompositions");
        return decompositionRepository.findAll(pageable);
    }

    /**
     * Get one decomposition by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Decomposition findOne(Long id) {
        log.debug("Request to get Decomposition : {}", id);
        return decompositionRepository.findOne(id);
    }

    /**
     * Delete the  decomposition by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Decomposition : {}", id);
        decompositionRepository.delete(id);
        decompositionSearchRepository.delete(id);
    }

    /**
     * Search for the decomposition corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Decomposition> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Decompositions for query {}", query);
        Page<Decomposition> result = decompositionSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
