package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.FormeArticle;

import com.ballack.com.repository.FormeArticleRepository;
import com.ballack.com.repository.search.FormeArticleSearchRepository;
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
 * REST controller for managing FormeArticle.
 */
@RestController
@RequestMapping("/api")
public class FormeArticleResource {

    private final Logger log = LoggerFactory.getLogger(FormeArticleResource.class);

    private static final String ENTITY_NAME = "formeArticle";

    private final FormeArticleRepository formeArticleRepository;

    private final FormeArticleSearchRepository formeArticleSearchRepository;
    public FormeArticleResource(FormeArticleRepository formeArticleRepository, FormeArticleSearchRepository formeArticleSearchRepository) {
        this.formeArticleRepository = formeArticleRepository;
        this.formeArticleSearchRepository = formeArticleSearchRepository;
    }

    /**
     * POST  /forme-articles : Create a new formeArticle.
     *
     * @param formeArticle the formeArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formeArticle, or with status 400 (Bad Request) if the formeArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/forme-articles")
    @Timed
    public ResponseEntity<FormeArticle> createFormeArticle(@RequestBody FormeArticle formeArticle) throws URISyntaxException {
        log.debug("REST request to save FormeArticle : {}", formeArticle);
        if (formeArticle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new formeArticle cannot already have an ID")).body(null);
        }
        FormeArticle result = formeArticleRepository.save(formeArticle);
        formeArticleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/forme-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /forme-articles : Updates an existing formeArticle.
     *
     * @param formeArticle the formeArticle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formeArticle,
     * or with status 400 (Bad Request) if the formeArticle is not valid,
     * or with status 500 (Internal Server Error) if the formeArticle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/forme-articles")
    @Timed
    public ResponseEntity<FormeArticle> updateFormeArticle(@RequestBody FormeArticle formeArticle) throws URISyntaxException {
        log.debug("REST request to update FormeArticle : {}", formeArticle);
        if (formeArticle.getId() == null) {
            return createFormeArticle(formeArticle);
        }
        FormeArticle result = formeArticleRepository.save(formeArticle);
        formeArticleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formeArticle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /forme-articles : get all the formeArticles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of formeArticles in body
     */
    @GetMapping("/forme-articles")
    @Timed
    public ResponseEntity<List<FormeArticle>> getAllFormeArticles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FormeArticles");
        Page<FormeArticle> page = formeArticleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/forme-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /forme-articles/:id : get the "id" formeArticle.
     *
     * @param id the id of the formeArticle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formeArticle, or with status 404 (Not Found)
     */
    @GetMapping("/forme-articles/{id}")
    @Timed
    public ResponseEntity<FormeArticle> getFormeArticle(@PathVariable Long id) {
        log.debug("REST request to get FormeArticle : {}", id);
        FormeArticle formeArticle = formeArticleRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(formeArticle));
    }

    /**
     * DELETE  /forme-articles/:id : delete the "id" formeArticle.
     *
     * @param id the id of the formeArticle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/forme-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteFormeArticle(@PathVariable Long id) {
        log.debug("REST request to delete FormeArticle : {}", id);
        formeArticleRepository.delete(id);
        formeArticleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/forme-articles?query=:query : search for the formeArticle corresponding
     * to the query.
     *
     * @param query the query of the formeArticle search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/forme-articles")
    @Timed
    public ResponseEntity<List<FormeArticle>> searchFormeArticles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of FormeArticles for query {}", query);
        Page<FormeArticle> page = formeArticleSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/forme-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
