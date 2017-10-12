package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.LigneCommande;
import com.ballack.com.service.LigneCommandeService;
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
 * REST controller for managing LigneCommande.
 */
@RestController
@RequestMapping("/api")
public class LigneCommandeResource {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeResource.class);

    private static final String ENTITY_NAME = "ligneCommande";

    private final LigneCommandeService ligneCommandeService;

    public LigneCommandeResource(LigneCommandeService ligneCommandeService) {
        this.ligneCommandeService = ligneCommandeService;
    }

    /**
     * POST  /ligne-commandes : Create a new ligneCommande.
     *
     * @param ligneCommande the ligneCommande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ligneCommande, or with status 400 (Bad Request) if the ligneCommande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ligne-commandes")
    @Timed
    public ResponseEntity<LigneCommande> createLigneCommande(@RequestBody LigneCommande ligneCommande) throws URISyntaxException {
        log.debug("REST request to save LigneCommande : {}", ligneCommande);
        if (ligneCommande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ligneCommande cannot already have an ID")).body(null);
        }
        LigneCommande result = ligneCommandeService.save(ligneCommande);
        return ResponseEntity.created(new URI("/api/ligne-commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ligne-commandes : Updates an existing ligneCommande.
     *
     * @param ligneCommande the ligneCommande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ligneCommande,
     * or with status 400 (Bad Request) if the ligneCommande is not valid,
     * or with status 500 (Internal Server Error) if the ligneCommande couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ligne-commandes")
    @Timed
    public ResponseEntity<LigneCommande> updateLigneCommande(@RequestBody LigneCommande ligneCommande) throws URISyntaxException {
        log.debug("REST request to update LigneCommande : {}", ligneCommande);
        if (ligneCommande.getId() == null) {
            return createLigneCommande(ligneCommande);
        }
        LigneCommande result = ligneCommandeService.save(ligneCommande);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ligneCommande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ligne-commandes : get all the ligneCommandes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ligneCommandes in body
     */
    @GetMapping("/ligne-commandes")
    @Timed
    public ResponseEntity<List<LigneCommande>> getAllLigneCommandes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LigneCommandes");
        Page<LigneCommande> page = ligneCommandeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ligne-commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ligne-commandes/:id : get the "id" ligneCommande.
     *
     * @param id the id of the ligneCommande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ligneCommande, or with status 404 (Not Found)
     */
    @GetMapping("/ligne-commandes/{id}")
    @Timed
    public ResponseEntity<LigneCommande> getLigneCommande(@PathVariable Long id) {
        log.debug("REST request to get LigneCommande : {}", id);
        LigneCommande ligneCommande = ligneCommandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ligneCommande));
    }

    /**
     * DELETE  /ligne-commandes/:id : delete the "id" ligneCommande.
     *
     * @param id the id of the ligneCommande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ligne-commandes/{id}")
    @Timed
    public ResponseEntity<Void> deleteLigneCommande(@PathVariable Long id) {
        log.debug("REST request to delete LigneCommande : {}", id);
        ligneCommandeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ligne-commandes?query=:query : search for the ligneCommande corresponding
     * to the query.
     *
     * @param query the query of the ligneCommande search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/ligne-commandes")
    @Timed
    public ResponseEntity<List<LigneCommande>> searchLigneCommandes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of LigneCommandes for query {}", query);
        Page<LigneCommande> page = ligneCommandeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ligne-commandes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
