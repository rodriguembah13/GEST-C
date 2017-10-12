package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.EntreeArticle;
import com.ballack.com.service.EntreeArticleService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing EntreeArticle.
 */
@RestController
@RequestMapping("/api")
public class EntreeArticleResource {

    private final Logger log = LoggerFactory.getLogger(EntreeArticleResource.class);

    private static final String ENTITY_NAME = "entreeArticle";

    private final EntreeArticleService entreeArticleService;

    public EntreeArticleResource(EntreeArticleService entreeArticleService) {
        this.entreeArticleService = entreeArticleService;
    }

    /**
     * POST  /entree-articles : Create a new entreeArticle.
     *
     * @param entreeArticle the entreeArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entreeArticle, or with status 400 (Bad Request) if the entreeArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entree-articles")
    @Timed
    public ResponseEntity<EntreeArticle> createEntreeArticle(@RequestBody EntreeArticle entreeArticle) throws URISyntaxException {
        log.debug("REST request to save EntreeArticle : {}", entreeArticle);
        if (entreeArticle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entreeArticle cannot already have an ID")).body(null);
        }
        EntreeArticle result = entreeArticleService.save(entreeArticle);
        return ResponseEntity.created(new URI("/api/entree-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entree-articles : Updates an existing entreeArticle.
     *
     * @param entreeArticle the entreeArticle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entreeArticle,
     * or with status 400 (Bad Request) if the entreeArticle is not valid,
     * or with status 500 (Internal Server Error) if the entreeArticle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entree-articles")
    @Timed
    public ResponseEntity<EntreeArticle> updateEntreeArticle(@RequestBody EntreeArticle entreeArticle) throws URISyntaxException {
        log.debug("REST request to update EntreeArticle : {}", entreeArticle);
        if (entreeArticle.getId() == null) {
            return createEntreeArticle(entreeArticle);
        }
        EntreeArticle result = entreeArticleService.save(entreeArticle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entreeArticle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entree-articles : get all the entreeArticles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of entreeArticles in body
     */
    @GetMapping("/entree-articles")
    @Timed
    public ResponseEntity<List<EntreeArticle>> getAllEntreeArticles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of EntreeArticles");
        Page<EntreeArticle> page = entreeArticleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entree-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entree-articles/:id : get the "id" entreeArticle.
     *
     * @param id the id of the entreeArticle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entreeArticle, or with status 404 (Not Found)
     */
    @GetMapping("/entree-articles/{id}")
    @Timed
    public ResponseEntity<EntreeArticle> getEntreeArticle(@PathVariable Long id) {
        log.debug("REST request to get EntreeArticle : {}", id);
        EntreeArticle entreeArticle = entreeArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entreeArticle));
    }

    /**
     * DELETE  /entree-articles/:id : delete the "id" entreeArticle.
     *
     * @param id the id of the entreeArticle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entree-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntreeArticle(@PathVariable Long id) {
        log.debug("REST request to delete EntreeArticle : {}", id);
        entreeArticleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/entree-articles?query=:query : search for the entreeArticle corresponding
     * to the query.
     *
     * @param query the query of the entreeArticle search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/entree-articles")
    @Timed
    public ResponseEntity<List<EntreeArticle>> searchEntreeArticles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of EntreeArticles for query {}", query);
        Page<EntreeArticle> page = entreeArticleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/entree-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
