package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.SortieArticle;
import com.ballack.com.service.SortieArticleService;
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
 * REST controller for managing SortieArticle.
 */
@RestController
@RequestMapping("/api")
public class SortieArticleResource {

    private final Logger log = LoggerFactory.getLogger(SortieArticleResource.class);

    private static final String ENTITY_NAME = "sortieArticle";

    private final SortieArticleService sortieArticleService;

    public SortieArticleResource(SortieArticleService sortieArticleService) {
        this.sortieArticleService = sortieArticleService;
    }

    /**
     * POST  /sortie-articles : Create a new sortieArticle.
     *
     * @param sortieArticle the sortieArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sortieArticle, or with status 400 (Bad Request) if the sortieArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sortie-articles")
    @Timed
    public ResponseEntity<SortieArticle> createSortieArticle(@RequestBody SortieArticle sortieArticle) throws URISyntaxException {
        log.debug("REST request to save SortieArticle : {}", sortieArticle);
        if (sortieArticle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sortieArticle cannot already have an ID")).body(null);
        }
        SortieArticle result = sortieArticleService.save(sortieArticle);
        return ResponseEntity.created(new URI("/api/sortie-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sortie-articles : Updates an existing sortieArticle.
     *
     * @param sortieArticle the sortieArticle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sortieArticle,
     * or with status 400 (Bad Request) if the sortieArticle is not valid,
     * or with status 500 (Internal Server Error) if the sortieArticle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sortie-articles")
    @Timed
    public ResponseEntity<SortieArticle> updateSortieArticle(@RequestBody SortieArticle sortieArticle) throws URISyntaxException {
        log.debug("REST request to update SortieArticle : {}", sortieArticle);
        if (sortieArticle.getId() == null) {
            return createSortieArticle(sortieArticle);
        }
        SortieArticle result = sortieArticleService.save(sortieArticle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sortieArticle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sortie-articles : get all the sortieArticles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sortieArticles in body
     */
    @GetMapping("/sortie-articles")
    @Timed
    public ResponseEntity<List<SortieArticle>> getAllSortieArticles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SortieArticles");
        Page<SortieArticle> page = sortieArticleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sortie-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sortie-articles/:id : get the "id" sortieArticle.
     *
     * @param id the id of the sortieArticle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sortieArticle, or with status 404 (Not Found)
     */
    @GetMapping("/sortie-articles/{id}")
    @Timed
    public ResponseEntity<SortieArticle> getSortieArticle(@PathVariable Long id) {
        log.debug("REST request to get SortieArticle : {}", id);
        SortieArticle sortieArticle = sortieArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sortieArticle));
    }

    /**
     * DELETE  /sortie-articles/:id : delete the "id" sortieArticle.
     *
     * @param id the id of the sortieArticle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sortie-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteSortieArticle(@PathVariable Long id) {
        log.debug("REST request to delete SortieArticle : {}", id);
        sortieArticleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sortie-articles?query=:query : search for the sortieArticle corresponding
     * to the query.
     *
     * @param query the query of the sortieArticle search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/sortie-articles")
    @Timed
    public ResponseEntity<List<SortieArticle>> searchSortieArticles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of SortieArticles for query {}", query);
        Page<SortieArticle> page = sortieArticleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/sortie-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
