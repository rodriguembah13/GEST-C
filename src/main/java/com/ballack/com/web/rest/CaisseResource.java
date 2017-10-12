package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.Caisse;
import com.ballack.com.service.CaisseService;
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
 * REST controller for managing Caisse.
 */
@RestController
@RequestMapping("/api")
public class CaisseResource {

    private final Logger log = LoggerFactory.getLogger(CaisseResource.class);

    private static final String ENTITY_NAME = "caisse";

    private final CaisseService caisseService;

    public CaisseResource(CaisseService caisseService) {
        this.caisseService = caisseService;
    }

    /**
     * POST  /caisses : Create a new caisse.
     *
     * @param caisse the caisse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new caisse, or with status 400 (Bad Request) if the caisse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/caisses")
    @Timed
    public ResponseEntity<Caisse> createCaisse(@RequestBody Caisse caisse) throws URISyntaxException {
        log.debug("REST request to save Caisse : {}", caisse);
        if (caisse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new caisse cannot already have an ID")).body(null);
        }
        Caisse result = caisseService.save(caisse);
        return ResponseEntity.created(new URI("/api/caisses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /caisses : Updates an existing caisse.
     *
     * @param caisse the caisse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated caisse,
     * or with status 400 (Bad Request) if the caisse is not valid,
     * or with status 500 (Internal Server Error) if the caisse couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/caisses")
    @Timed
    public ResponseEntity<Caisse> updateCaisse(@RequestBody Caisse caisse) throws URISyntaxException {
        log.debug("REST request to update Caisse : {}", caisse);
        if (caisse.getId() == null) {
            return createCaisse(caisse);
        }
        Caisse result = caisseService.save(caisse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, caisse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /caisses : get all the caisses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of caisses in body
     */
    @GetMapping("/caisses")
    @Timed
    public ResponseEntity<List<Caisse>> getAllCaisses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Caisses");
        Page<Caisse> page = caisseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/caisses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /caisses/:id : get the "id" caisse.
     *
     * @param id the id of the caisse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the caisse, or with status 404 (Not Found)
     */
    @GetMapping("/caisses/{id}")
    @Timed
    public ResponseEntity<Caisse> getCaisse(@PathVariable Long id) {
        log.debug("REST request to get Caisse : {}", id);
        Caisse caisse = caisseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(caisse));
    }

    /**
     * DELETE  /caisses/:id : delete the "id" caisse.
     *
     * @param id the id of the caisse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/caisses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCaisse(@PathVariable Long id) {
        log.debug("REST request to delete Caisse : {}", id);
        caisseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/caisses?query=:query : search for the caisse corresponding
     * to the query.
     *
     * @param query the query of the caisse search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/caisses")
    @Timed
    public ResponseEntity<List<Caisse>> searchCaisses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Caisses for query {}", query);
        Page<Caisse> page = caisseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/caisses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
