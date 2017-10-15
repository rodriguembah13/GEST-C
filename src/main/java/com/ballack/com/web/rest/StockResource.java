package com.ballack.com.web.rest;

import com.ballack.com.domain.Article;
import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.Stock;
import com.ballack.com.service.StockService;
import com.ballack.com.web.rest.util.HeaderUtil;
import com.ballack.com.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Stock.
 */
@RestController
@RequestMapping("/api")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "stock";

    private final StockService stockService;

    public StockResource(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * POST  /stocks : Create a new stock.
     *
     * @param stock the stock to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stock, or with status 400 (Bad Request) if the stock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stocks")
    @Timed
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stock);
        if (stock.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stock cannot already have an ID")).body(null);
        }
        Stock result = stockService.save(stock);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stocks : Updates an existing stock.
     *
     * @param stock the stock to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stock,
     * or with status 400 (Bad Request) if the stock is not valid,
     * or with status 500 (Internal Server Error) if the stock couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stocks")
    @Timed
    public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stock);
        if (stock.getId() == null) {
            return createStock(stock);
        }
        Stock result = stockService.save(stock);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stock.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /stocks : Updates an existing stock.
     *
     * @param stock the stock to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stock,
     * or with status 400 (Bad Request) if the stock is not valid,
     * or with status 500 (Internal Server Error) if the stock couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stocksA")
    @Timed
    public ResponseEntity<Stock> updateStockActive(Stock stock) {
        log.debug("REST request to update Stock : {}", stock);

        Stock result = stockService.activeStock(stock.getId());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stock.getId().toString()))
            .body(result);
    }
    /**
     * GET  /stocks : get all the stocks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stocks in body
     */
    @GetMapping("/stocks")
    @Timed
    public ResponseEntity<List<Stock>> getAllStocks(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stocks");
        Page<Stock> page = stockService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stocks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /stocks : get all the stocks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stocks in body
     */
    @GetMapping("/stocksA")
    @Timed
    public ResponseEntity<List<Stock>> getAllStocksAlerte(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stocks");
        Page<Stock> page = stockService.findAllAlerte(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stocksA");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /stocks : get all the stocks.
     *
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stocks in body
     */
    @GetMapping("/stocksP1")
    @Timed
    public ResponseEntity<Set<Stock>> getAllStocksPeremption() {
        log.debug("REST request to get a page of Stocks");
        Set<Stock> page = stockService.findAllPeremption();

        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    @GetMapping("/stocksP")
    @Timed
    public ResponseEntity<List<Stock>> getAllPeremption(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stocks");
        Page<Stock> page = stockService.findAllPeremption1(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stocksP");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/stocksP2")
    @Timed
    public ResponseEntity<List<Stock>> getAllPeremption2(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stocks");
        Page<Stock> page = stockService.findAllPeremption2(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stocksP2");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /stocks/:id : get the "id" stock.
     *
     * @param id the id of the stock to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stock, or with status 404 (Not Found)
     */
    @GetMapping("/stocks/{id}")
    @Timed
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Stock stock = stockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stock));
    }
    /**
     * GET  /stocks/:id : get the "id" stock.
     *
     * @param article the id of the stock to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stock, or with status 404 (Not Found)
     */
    @GetMapping("/stocksActif")
    @Timed
    public ResponseEntity<Stock> getStockActive(Article article) {
        log.debug("REST request to get Stock : {}", article);
        Stock stock = stockService.findOneArticleActif(article);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(stock));
    }

    /**
     * DELETE  /stocks/:id : delete the "id" stock.
     *
     * @param id the id of the stock to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stocks/{id}")
    @Timed
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stocks?query=:query : search for the stock corresponding
     * to the query.
     *
     * @param query the query of the stock search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stocks")
    @Timed
    public ResponseEntity<List<Stock>> searchStocks(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Stocks for query {}", query);
        Page<Stock> page = stockService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stocks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
