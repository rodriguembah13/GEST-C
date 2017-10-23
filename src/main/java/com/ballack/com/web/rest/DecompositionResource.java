package com.ballack.com.web.rest;

import com.ballack.com.domain.Article;
import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.Decomposition;
import com.ballack.com.service.DecompositionService;
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
 * REST controller for managing Decomposition.
 */
@RestController
@RequestMapping("/api")
public class DecompositionResource {

    private final Logger log = LoggerFactory.getLogger(DecompositionResource.class);

    private static final String ENTITY_NAME = "decomposition";

    private final DecompositionService decompositionService;

    public DecompositionResource(DecompositionService decompositionService) {
        this.decompositionService = decompositionService;
    }

    /**
     * POST  /decomposition : Create a new decomposition.
     *
     * @param decomposition the decomposition to create
     * @return the ResponseEntity with status 201 (Created) and with body the new decomposition, or with status 400 (Bad Request) if the decomposition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/decomposition")
    @Timed
    public ResponseEntity<Decomposition> createDecomposition(@RequestBody Decomposition decomposition) throws URISyntaxException {
        log.debug("REST request to save Decomposition : {}", decomposition);
        if (decomposition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new decomposition cannot already have an ID")).body(null);
        }
        Decomposition result = decompositionService.save(decomposition);
        return ResponseEntity.created(new URI("/api/decompositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /decompositions : Updates an existing decomposition.
     *
     * @param decomposition the decomposition to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated decomposition,
     * or with status 400 (Bad Request) if the decomposition is not valid,
     * or with status 500 (Internal Server Error) if the decomposition couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/decompositions")
    @Timed
    public ResponseEntity<Decomposition> updateDecomposition(@RequestBody Decomposition decomposition) throws URISyntaxException {
        log.debug("REST request to update Decomposition : {}", decomposition);
        if (decomposition.getId() == null) {
            return createDecomposition(decomposition);
        }
        Decomposition result = decompositionService.save(decomposition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, decomposition.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /decompositions : Updates an existing decomposition.
     *
     * @param article the decomposition to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated decomposition,
     * or with status 400 (Bad Request) if the decomposition is not valid,
     * or with status 500 (Internal Server Error) if the decomposition couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/decompositions")
    @Timed
    public ResponseEntity<Decomposition> decomposition(@RequestBody Article article) throws URISyntaxException {
        log.debug("REST request to update Decomposition : {}", article);
        Decomposition decomposition = decompositionService.decomposition(article);

        Decomposition result = decompositionService.save(decomposition);
        return ResponseEntity.created(new URI("/api/decompositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * GET  /decompositions : get all the decompositions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of decompositions in body
     */
    @GetMapping("/decompositions")
    @Timed
    public ResponseEntity<List<Decomposition>> getAllDecompositions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Decompositions");
        Page<Decomposition> page = decompositionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/decompositions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /decompositions/:id : get the "id" decomposition.
     *
     * @param id the id of the decomposition to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the decomposition, or with status 404 (Not Found)
     */
    @GetMapping("/decompositions/{id}")
    @Timed
    public ResponseEntity<Decomposition> getDecomposition(@PathVariable Long id) {
        log.debug("REST request to get Decomposition : {}", id);
        Decomposition decomposition = decompositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(decomposition));
    }

    /**
     * DELETE  /decompositions/:id : delete the "id" decomposition.
     *
     * @param id the id of the decomposition to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/decompositions/{id}")
    @Timed
    public ResponseEntity<Void> deleteDecomposition(@PathVariable Long id) {
        log.debug("REST request to delete Decomposition : {}", id);
        decompositionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/decompositions?query=:query : search for the decomposition corresponding
     * to the query.
     *
     * @param query the query of the decomposition search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/decompositions")
    @Timed
    public ResponseEntity<List<Decomposition>> searchDecompositions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Decompositions for query {}", query);
        Page<Decomposition> page = decompositionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/decompositions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
