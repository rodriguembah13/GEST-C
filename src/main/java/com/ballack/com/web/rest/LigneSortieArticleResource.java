package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.LigneSortieArticle;
import com.ballack.com.service.LigneSortieArticleService;
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
 * REST controller for managing LigneSortieArticle.
 */
@RestController
@RequestMapping("/api")
public class LigneSortieArticleResource {

    private final Logger log = LoggerFactory.getLogger(LigneSortieArticleResource.class);

    private static final String ENTITY_NAME = "ligneSortieArticle";

    private final LigneSortieArticleService ligneSortieArticleService;

    public LigneSortieArticleResource(LigneSortieArticleService ligneSortieArticleService) {
        this.ligneSortieArticleService = ligneSortieArticleService;
    }

    /**
     * POST  /ligne-sortie-articles : Create a new ligneSortieArticle.
     *
     * @param ligneSortieArticle the ligneSortieArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneSortieArticle, or with status 400 (Bad Request) if the ligneSortieArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ligne-sortie-articles")
    @Timed
    public ResponseEntity<LigneSortieArticle> createLigneSortieArticle(@RequestBody LigneSortieArticle ligneSortieArticle) throws URISyntaxException {
        log.debug("REST request to save LigneSortieArticle : {}", ligneSortieArticle);
        if (ligneSortieArticle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ligneSortieArticle cannot already have an ID")).body(null);
        }
        LigneSortieArticle result = ligneSortieArticleService.save(ligneSortieArticle);
        return ResponseEntity.created(new URI("/api/ligne-sortie-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-sortie-articles : Updates an existing ligneSortieArticle.
     *
     * @param ligneSortieArticle the ligneSortieArticle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneSortieArticle,
     * or with status 400 (Bad Request) if the ligneSortieArticle is not valid,
     * or with status 500 (Internal Server Error) if the ligneSortieArticle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ligne-sortie-articles")
    @Timed
    public ResponseEntity<LigneSortieArticle> updateLigneSortieArticle(@RequestBody LigneSortieArticle ligneSortieArticle) throws URISyntaxException {
        log.debug("REST request to update LigneSortieArticle : {}", ligneSortieArticle);
        if (ligneSortieArticle.getId() == null) {
            return createLigneSortieArticle(ligneSortieArticle);
        }
        LigneSortieArticle result = ligneSortieArticleService.save(ligneSortieArticle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ligneSortieArticle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-sortie-articles : get all the ligneSortieArticles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ligneSortieArticles in body
     */
    @GetMapping("/ligne-sortie-articles")
    @Timed
    public ResponseEntity<List<LigneSortieArticle>> getAllLigneSortieArticles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LigneSortieArticles");
        Page<LigneSortieArticle> page = ligneSortieArticleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligne-sortie-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-sortie-articles/:id : get the "id" ligneSortieArticle.
     *
     * @param id the id of the ligneSortieArticle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneSortieArticle, or with status 404 (Not Found)
     */
    @GetMapping("/ligne-sortie-articles/{id}")
    @Timed
    public ResponseEntity<LigneSortieArticle> getLigneSortieArticle(@PathVariable Long id) {
        log.debug("REST request to get LigneSortieArticle : {}", id);
        LigneSortieArticle ligneSortieArticle = ligneSortieArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ligneSortieArticle));
    }

    /**
     * DELETE  /ligne-sortie-articles/:id : delete the "id" ligneSortieArticle.
     *
     * @param id the id of the ligneSortieArticle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ligne-sortie-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteLigneSortieArticle(@PathVariable Long id) {
        log.debug("REST request to delete LigneSortieArticle : {}", id);
        ligneSortieArticleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligne-sortie-articles?query=:query : search for the ligneSortieArticle corresponding
     * to the query.
     *
     * @param query the query of the ligneSortieArticle search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ligne-sortie-articles")
    @Timed
    public ResponseEntity<List<LigneSortieArticle>> searchLigneSortieArticles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of LigneSortieArticles for query {}", query);
        Page<LigneSortieArticle> page = ligneSortieArticleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ligne-sortie-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
