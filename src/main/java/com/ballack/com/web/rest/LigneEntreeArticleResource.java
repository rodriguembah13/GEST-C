package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.LigneEntreeArticle;
import com.ballack.com.service.LigneEntreeArticleService;
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
 * REST controller for managing LigneEntreeArticle.
 */
@RestController
@RequestMapping("/api")
public class LigneEntreeArticleResource {

    private final Logger log = LoggerFactory.getLogger(LigneEntreeArticleResource.class);

    private static final String ENTITY_NAME = "ligneEntreeArticle";

    private final LigneEntreeArticleService ligneEntreeArticleService;

    public LigneEntreeArticleResource(LigneEntreeArticleService ligneEntreeArticleService) {
        this.ligneEntreeArticleService = ligneEntreeArticleService;
    }

    /**
     * POST  /ligne-entree-articles : Create a new ligneEntreeArticle.
     *
     * @param ligneEntreeArticle the ligneEntreeArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneEntreeArticle, or with status 400 (Bad Request) if the ligneEntreeArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ligne-entree-articles")
    @Timed
    public ResponseEntity<LigneEntreeArticle> createLigneEntreeArticle(@RequestBody LigneEntreeArticle ligneEntreeArticle) throws URISyntaxException {
        log.debug("REST request to save LigneEntreeArticle : {}", ligneEntreeArticle);
        if (ligneEntreeArticle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ligneEntreeArticle cannot already have an ID")).body(null);
        }
        LigneEntreeArticle result = ligneEntreeArticleService.save(ligneEntreeArticle);
        return ResponseEntity.created(new URI("/api/ligne-entree-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-entree-articles : Updates an existing ligneEntreeArticle.
     *
     * @param ligneEntreeArticle the ligneEntreeArticle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneEntreeArticle,
     * or with status 400 (Bad Request) if the ligneEntreeArticle is not valid,
     * or with status 500 (Internal Server Error) if the ligneEntreeArticle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ligne-entree-articles")
    @Timed
    public ResponseEntity<LigneEntreeArticle> updateLigneEntreeArticle(@RequestBody LigneEntreeArticle ligneEntreeArticle) throws URISyntaxException {
        log.debug("REST request to update LigneEntreeArticle : {}", ligneEntreeArticle);
        if (ligneEntreeArticle.getId() == null) {
            return createLigneEntreeArticle(ligneEntreeArticle);
        }
        LigneEntreeArticle result = ligneEntreeArticleService.save(ligneEntreeArticle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ligneEntreeArticle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-entree-articles : get all the ligneEntreeArticles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ligneEntreeArticles in body
     */
    @GetMapping("/ligne-entree-articles")
    @Timed
    public ResponseEntity<List<LigneEntreeArticle>> getAllLigneEntreeArticles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LigneEntreeArticles");
        Page<LigneEntreeArticle> page = ligneEntreeArticleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligne-entree-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-entree-articles/:id : get the "id" ligneEntreeArticle.
     *
     * @param id the id of the ligneEntreeArticle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneEntreeArticle, or with status 404 (Not Found)
     */
    @GetMapping("/ligne-entree-articles/{id}")
    @Timed
    public ResponseEntity<LigneEntreeArticle> getLigneEntreeArticle(@PathVariable Long id) {
        log.debug("REST request to get LigneEntreeArticle : {}", id);
        LigneEntreeArticle ligneEntreeArticle = ligneEntreeArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ligneEntreeArticle));
    }

    /**
     * DELETE  /ligne-entree-articles/:id : delete the "id" ligneEntreeArticle.
     *
     * @param id the id of the ligneEntreeArticle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ligne-entree-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteLigneEntreeArticle(@PathVariable Long id) {
        log.debug("REST request to delete LigneEntreeArticle : {}", id);
        ligneEntreeArticleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligne-entree-articles?query=:query : search for the ligneEntreeArticle corresponding
     * to the query.
     *
     * @param query the query of the ligneEntreeArticle search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ligne-entree-articles")
    @Timed
    public ResponseEntity<List<LigneEntreeArticle>> searchLigneEntreeArticles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of LigneEntreeArticles for query {}", query);
        Page<LigneEntreeArticle> page = ligneEntreeArticleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ligne-entree-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
