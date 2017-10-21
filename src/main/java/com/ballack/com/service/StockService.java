package com.ballack.com.service;

import com.ballack.com.domain.Article;
import com.ballack.com.domain.Stock;
import com.ballack.com.repository.StockRepository;
import com.ballack.com.repository.search.StockSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;
    private final ArticleService articleService;
    private final StockSearchRepository stockSearchRepository;
    public StockService(StockRepository stockRepository, ArticleService articleService, StockSearchRepository stockSearchRepository) {
        this.stockRepository = stockRepository;
        this.articleService = articleService;
        this.stockSearchRepository = stockSearchRepository;
    }

    /**
     * Save a stock.
     *
     * @param stock the entity to save
     * @return the persisted entity
     */
    public Stock save(Stock stock) {
        log.debug("Request to save Stock : {}", stock);
        Stock result = stockRepository.save(stock);
        stockSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the stocks.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stock> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll(pageable);
    }
    /**
     *  Get all the stocks Alerte Stock.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stock> findAllAlerte(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findStockAlerte(pageable);
    }
    /**
     *  Get all the stocks Alerte Stock.
     *
     *   the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Set<Stock> findAllPeremption() {
        log.debug("Request to get all Stocks");
        List<Stock> stockList=stockRepository.findAll();
        Set<Stock> stockSet=new HashSet<>();

        for (Stock stock:stockList){
            if (stock.getDateperemption().compareTo(LocalDate.now())==1){
                stockSet.add(stock);
            }
        }
        return stockSet;
    }
    @Transactional(readOnly = true)
    public Page<Stock> findAllPeremption1(Pageable pageable) {
        log.debug("Request to get all Stocks");

        return stockRepository.findStockPeremp(pageable,LocalDate.now());
    }
    @Transactional(readOnly = true)
    public Page<Stock> findAllPeremption2(Pageable pageable) {
        log.debug("Request to get all Stocks");

        return stockRepository.findStockEvPeremp(pageable,LocalDate.now().plusMonths(1),LocalDate.now());
    }
    /**
     *  Get one stock by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Stock findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        return stockRepository.findOne(id);
    }
    /**
     *  Get one stock by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */

    public Stock activeStock(Long id) {
        log.debug("Request to get Stock : {}", id);
        Stock stock=stockRepository.findOne(id);
        if(!stock.isActif()){
            stock.setActif(true);
            Article article=articleService.findOne(stock.getArticle().getId());
            article.setPrixCourant(stock.getPrixArticle());
            article.setTaxeTva(stock.getTaxeTVA());
            articleService.saveAndFluch(article);
        }else {
            stock.setActif(false);
        }
        return stockRepository.saveAndFlush(stock);
    }
    public Stock closedStock(Long id) {
        log.debug("Request to get Stock : {}", id);
        Stock stock=stockRepository.findOne(id);
        if(!stock.isClosed()){
            stock.setClosed(true);
        }else {
            stock.setClosed(false);
        }
        return stockRepository.saveAndFlush(stock);
    }
    /**
     *  Get one stock by article where active.
     *
     *  @param article the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Stock findOneArticleActif(Article article) {
        log.debug("Request to get Stock : {}", article.getId());
        return stockRepository.findStockactif(article);
    }
    /**
     *  Delete the  stock by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.delete(id);
        stockSearchRepository.delete(id);
    }

    /**
     * Search for the stock corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Stock> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stocks for query {}", query);
        Page<Stock> result = stockSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
