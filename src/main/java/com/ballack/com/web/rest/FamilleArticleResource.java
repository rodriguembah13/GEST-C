package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.FamilleArticle;

import com.ballack.com.repository.FamilleArticleRepository;
import com.ballack.com.repository.search.FamilleArticleSearchRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing FamilleArticle.
 */
@RestController
@RequestMapping("/api")
public class FamilleArticleResource {

    private final Logger log = LoggerFactory.getLogger(FamilleArticleResource.class);

    private static final String ENTITY_NAME = "familleArticle";

    private final FamilleArticleRepository familleArticleRepository;

    private final FamilleArticleSearchRepository familleArticleSearchRepository;
    public FamilleArticleResource(FamilleArticleRepository familleArticleRepository, FamilleArticleSearchRepository familleArticleSearchRepository) {
        this.familleArticleRepository = familleArticleRepository;
        this.familleArticleSearchRepository = familleArticleSearchRepository;
    }

    /**
     * POST  /famille-articles : Create a new familleArticle.
     *
     * @param familleArticle the familleArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new familleArticle, or with status 400 (Bad Request) if the familleArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/famille-articles")
    @Timed
    public ResponseEntity<FamilleArticle> createFamilleArticle(@RequestBody FamilleArticle familleArticle) throws URISyntaxException {
        log.debug("REST request to save FamilleArticle : {}", familleArticle);
        if (familleArticle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new familleArticle cannot already have an ID")).body(null);
        }
        FamilleArticle result = familleArticleRepository.save(familleArticle);
        familleArticleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/famille-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /famille-articles : Updates an existing familleArticle.
     *
     * @param familleArticle the familleArticle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated familleArticle,
     * or with status 400 (Bad Request) if the familleArticle is not valid,
     * or with status 500 (Internal Server Error) if the familleArticle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/famille-articles")
    @Timed
    public ResponseEntity<FamilleArticle> updateFamilleArticle(@RequestBody FamilleArticle familleArticle) throws URISyntaxException {
        log.debug("REST request to update FamilleArticle : {}", familleArticle);
        if (familleArticle.getId() == null) {
            return createFamilleArticle(familleArticle);
        }
        FamilleArticle result = familleArticleRepository.save(familleArticle);
        familleArticleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, familleArticle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /famille-articles : get all the familleArticles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of familleArticles in body
     */
    @GetMapping("/famille-articles")
    @Timed
    public ResponseEntity<List<FamilleArticle>> getAllFamilleArticles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FamilleArticles");
        Page<FamilleArticle> page = familleArticleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/famille-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /famille-articles/:id : get the "id" familleArticle.
     *
     * @param id the id of the familleArticle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the familleArticle, or with status 404 (Not Found)
     */
    @GetMapping("/famille-articles/{id}")
    @Timed
    public ResponseEntity<FamilleArticle> getFamilleArticle(@PathVariable Long id) {
        log.debug("REST request to get FamilleArticle : {}", id);
        FamilleArticle familleArticle = familleArticleRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(familleArticle));
    }

    /**
     * DELETE  /famille-articles/:id : delete the "id" familleArticle.
     *
     * @param id the id of the familleArticle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/famille-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteFamilleArticle(@PathVariable Long id) {
        log.debug("REST request to delete FamilleArticle : {}", id);
        familleArticleRepository.delete(id);
        familleArticleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/famille-articles?query=:query : search for the familleArticle corresponding
     * to the query.
     *
     * @param query the query of the familleArticle search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/famille-articles")
    @Timed
    public ResponseEntity<List<FamilleArticle>> searchFamilleArticles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of FamilleArticles for query {}", query);
        Page<FamilleArticle> page = familleArticleSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/famille-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
