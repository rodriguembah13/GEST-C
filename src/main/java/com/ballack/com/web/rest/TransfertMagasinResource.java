package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.TransfertMagasin;
import com.ballack.com.service.TransfertMagasinService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TransfertMagasin.
 */
@RestController
@RequestMapping("/api")
public class TransfertMagasinResource {

    private final Logger log = LoggerFactory.getLogger(TransfertMagasinResource.class);

    private static final String ENTITY_NAME = "transfertMagasin";

    private final TransfertMagasinService transfertMagasinService;

    public TransfertMagasinResource(TransfertMagasinService transfertMagasinService) {
        this.transfertMagasinService = transfertMagasinService;
    }

    /**
     * POST  /transfert-magasins : Create a new transfertMagasin.
     *
     * @param transfertMagasin the transfertMagasin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transfertMagasin, or with status 400 (Bad Request) if the transfertMagasin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transfert-magasins")
    @Timed
    public ResponseEntity<TransfertMagasin> createTransfertMagasin(@Valid @RequestBody TransfertMagasin transfertMagasin) throws URISyntaxException {
        log.debug("REST request to save TransfertMagasin : {}", transfertMagasin);
        if (transfertMagasin.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new transfertMagasin cannot already have an ID")).body(null);
        }
        TransfertMagasin result = transfertMagasinService.save(transfertMagasin);
        return ResponseEntity.created(new URI("/api/transfert-magasins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transfert-magasins : Updates an existing transfertMagasin.
     *
     * @param transfertMagasin the transfertMagasin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transfertMagasin,
     * or with status 400 (Bad Request) if the transfertMagasin is not valid,
     * or with status 500 (Internal Server Error) if the transfertMagasin couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transfert-magasins")
    @Timed
    public ResponseEntity<TransfertMagasin> updateTransfertMagasin(@Valid @RequestBody TransfertMagasin transfertMagasin) throws URISyntaxException {
        log.debug("REST request to update TransfertMagasin : {}", transfertMagasin);
        if (transfertMagasin.getId() == null) {
            return createTransfertMagasin(transfertMagasin);
        }
        TransfertMagasin result = transfertMagasinService.save(transfertMagasin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transfertMagasin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transfert-magasins : get all the transfertMagasins.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transfertMagasins in body
     */
    @GetMapping("/transfert-magasins")
    @Timed
    public ResponseEntity<List<TransfertMagasin>> getAllTransfertMagasins(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TransfertMagasins");
        Page<TransfertMagasin> page = transfertMagasinService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transfert-magasins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transfert-magasins/:id : get the "id" transfertMagasin.
     *
     * @param id the id of the transfertMagasin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transfertMagasin, or with status 404 (Not Found)
     */
    @GetMapping("/transfert-magasins/{id}")
    @Timed
    public ResponseEntity<TransfertMagasin> getTransfertMagasin(@PathVariable Long id) {
        log.debug("REST request to get TransfertMagasin : {}", id);
        TransfertMagasin transfertMagasin = transfertMagasinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transfertMagasin));
    }

    /**
     * DELETE  /transfert-magasins/:id : delete the "id" transfertMagasin.
     *
     * @param id the id of the transfertMagasin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transfert-magasins/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransfertMagasin(@PathVariable Long id) {
        log.debug("REST request to delete TransfertMagasin : {}", id);
        transfertMagasinService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transfert-magasins?query=:query : search for the transfertMagasin corresponding
     * to the query.
     *
     * @param query the query of the transfertMagasin search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/transfert-magasins")
    @Timed
    public ResponseEntity<List<TransfertMagasin>> searchTransfertMagasins(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TransfertMagasins for query {}", query);
        Page<TransfertMagasin> page = transfertMagasinService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/transfert-magasins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
