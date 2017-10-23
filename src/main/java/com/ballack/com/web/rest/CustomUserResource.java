package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.CustomUser;
import com.ballack.com.service.CustomUserService;
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
 * REST controller for managing CustomUser.
 */
@RestController
@RequestMapping("/api")
public class CustomUserResource {

    private final Logger log = LoggerFactory.getLogger(CustomUserResource.class);

    private static final String ENTITY_NAME = "customUser";

    private final CustomUserService customUserService;

    public CustomUserResource(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    /**
     * POST  /custom-users : Create a new customUser.
     *
     * @param customUser the customUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customUser, or with status 400 (Bad Request) if the customUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/custom-users")
    @Timed
    public ResponseEntity<CustomUser> createCustomUser(@RequestBody CustomUser customUser) throws URISyntaxException {
        log.debug("REST request to save CustomUser : {}", customUser);
        if (customUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new customUser cannot already have an ID")).body(null);
        }
        CustomUser result = customUserService.save(customUser);
        return ResponseEntity.created(new URI("/api/custom-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /custom-users : Updates an existing customUser.
     *
     * @param customUser the customUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customUser,
     * or with status 400 (Bad Request) if the customUser is not valid,
     * or with status 500 (Internal Server Error) if the customUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/custom-users")
    @Timed
    public ResponseEntity<CustomUser> updateCustomUser(@RequestBody CustomUser customUser) throws URISyntaxException {
        log.debug("REST request to update CustomUser : {}", customUser);
        if (customUser.getId() == null) {
            return createCustomUser(customUser);
        }
        CustomUser result = customUserService.save(customUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /custom-users : get all the customUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customUsers in body
     */
    @GetMapping("/custom-users")
    @Timed
    public ResponseEntity<List<CustomUser>> getAllCustomUsers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CustomUsers");
        Page<CustomUser> page = customUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/custom-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /custom-users/:id : get the "id" customUser.
     *
     * @param id the id of the customUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customUser, or with status 404 (Not Found)
     */
    @GetMapping("/custom-users/{id}")
    @Timed
    public ResponseEntity<CustomUser> getCustomUser(@PathVariable Long id) {
        log.debug("REST request to get CustomUser : {}", id);
        CustomUser customUser = customUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customUser));
    }

    /**
     * DELETE  /custom-users/:id : delete the "id" customUser.
     *
     * @param id the id of the customUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/custom-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomUser(@PathVariable Long id) {
        log.debug("REST request to delete CustomUser : {}", id);
        customUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/custom-users?query=:query : search for the customUser corresponding
     * to the query.
     *
     * @param query the query of the customUser search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/custom-users")
    @Timed
    public ResponseEntity<List<CustomUser>> searchCustomUsers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CustomUsers for query {}", query);
        Page<CustomUser> page = customUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/custom-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
