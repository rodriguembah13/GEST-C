package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.Magasin;

import com.ballack.com.repository.MagasinRepository;
import com.ballack.com.repository.search.MagasinSearchRepository;
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
 * REST controller for managing Magasin.
 */
@RestController
@RequestMapping("/api")
public class MagasinResource {

    private final Logger log = LoggerFactory.getLogger(MagasinResource.class);

    private static final String ENTITY_NAME = "magasin";

    private final MagasinRepository magasinRepository;

    private final MagasinSearchRepository magasinSearchRepository;
    public MagasinResource(MagasinRepository magasinRepository, MagasinSearchRepository magasinSearchRepository) {
        this.magasinRepository = magasinRepository;
        this.magasinSearchRepository = magasinSearchRepository;
    }

    /**
     * POST  /magasins : Create a new magasin.
     *
     * @param magasin the magasin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new magasin, or with status 400 (Bad Request) if the magasin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/magasins")
    @Timed
    public ResponseEntity<Magasin> createMagasin(@RequestBody Magasin magasin) throws URISyntaxException {
        log.debug("REST request to save Magasin : {}", magasin);
        if (magasin.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new magasin cannot already have an ID")).body(null);
        }
        Magasin result = magasinRepository.save(magasin);
        magasinSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/magasins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /magasins : Updates an existing magasin.
     *
     * @param magasin the magasin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated magasin,
     * or with status 400 (Bad Request) if the magasin is not valid,
     * or with status 500 (Internal Server Error) if the magasin couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/magasins")
    @Timed
    public ResponseEntity<Magasin> updateMagasin(@RequestBody Magasin magasin) throws URISyntaxException {
        log.debug("REST request to update Magasin : {}", magasin);
        if (magasin.getId() == null) {
            return createMagasin(magasin);
        }
        Magasin result = magasinRepository.save(magasin);
        magasinSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, magasin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /magasins : get all the magasins.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of magasins in body
     */
    @GetMapping("/magasins")
    @Timed
    public ResponseEntity<List<Magasin>> getAllMagasins(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Magasins");
        Page<Magasin> page = magasinRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/magasins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /magasins/:id : get the "id" magasin.
     *
     * @param id the id of the magasin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the magasin, or with status 404 (Not Found)
     */
    @GetMapping("/magasins/{id}")
    @Timed
    public ResponseEntity<Magasin> getMagasin(@PathVariable Long id) {
        log.debug("REST request to get Magasin : {}", id);
        Magasin magasin = magasinRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(magasin));
    }

    /**
     * DELETE  /magasins/:id : delete the "id" magasin.
     *
     * @param id the id of the magasin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/magasins/{id}")
    @Timed
    public ResponseEntity<Void> deleteMagasin(@PathVariable Long id) {
        log.debug("REST request to delete Magasin : {}", id);
        magasinRepository.delete(id);
        magasinSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/magasins?query=:query : search for the magasin corresponding
     * to the query.
     *
     * @param query the query of the magasin search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/magasins")
    @Timed
    public ResponseEntity<List<Magasin>> searchMagasins(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Magasins for query {}", query);
        Page<Magasin> page = magasinSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/magasins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
