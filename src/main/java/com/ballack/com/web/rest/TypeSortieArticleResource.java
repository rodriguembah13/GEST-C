package com.ballack.com.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.TypeSortieArticle;

import com.ballack.com.repository.TypeSortieArticleRepository;
import com.ballack.com.repository.search.TypeSortieArticleSearchRepository;
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
 * REST controller for managing TypeSortieArticle.
 */
@RestController
@RequestMapping("/api")
public class TypeSortieArticleResource {

    private final Logger log = LoggerFactory.getLogger(TypeSortieArticleResource.class);

    private static final String ENTITY_NAME = "typeSortieArticle";

    private final TypeSortieArticleRepository typeSortieArticleRepository;

    private final TypeSortieArticleSearchRepository typeSortieArticleSearchRepository;
    public TypeSortieArticleResource(TypeSortieArticleRepository typeSortieArticleRepository, TypeSortieArticleSearchRepository typeSortieArticleSearchRepository) {
        this.typeSortieArticleRepository = typeSortieArticleRepository;
        this.typeSortieArticleSearchRepository = typeSortieArticleSearchRepository;
    }

    /**
     * POST  /type-sortie-articles : Create a new typeSortieArticle.
     *
     * @param typeSortieArticle the typeSortieArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeSortieArticle, or with status 400 (Bad Request) if the typeSortieArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/type-sortie-articles")
    @Timed
    public ResponseEntity<TypeSortieArticle> createTypeSortieArticle(@RequestBody TypeSortieArticle typeSortieArticle) throws URISyntaxException {
        log.debug("REST request to save TypeSortieArticle : {}", typeSortieArticle);
        if (typeSortieArticle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new typeSortieArticle cannot already have an ID")).body(null);
        }
        TypeSortieArticle result = typeSortieArticleRepository.save(typeSortieArticle);
        typeSortieArticleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-sortie-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-sortie-articles : Updates an existing typeSortieArticle.
     *
     * @param typeSortieArticle the typeSortieArticle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeSortieArticle,
     * or with status 400 (Bad Request) if the typeSortieArticle is not valid,
     * or with status 500 (Internal Server Error) if the typeSortieArticle couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/type-sortie-articles")
    @Timed
    public ResponseEntity<TypeSortieArticle> updateTypeSortieArticle(@RequestBody TypeSortieArticle typeSortieArticle) throws URISyntaxException {
        log.debug("REST request to update TypeSortieArticle : {}", typeSortieArticle);
        if (typeSortieArticle.getId() == null) {
            return createTypeSortieArticle(typeSortieArticle);
        }
        TypeSortieArticle result = typeSortieArticleRepository.save(typeSortieArticle);
        typeSortieArticleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, typeSortieArticle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-sortie-articles : get all the typeSortieArticles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeSortieArticles in body
     */
    @GetMapping("/type-sortie-articles")
    @Timed
    public ResponseEntity<List<TypeSortieArticle>> getAllTypeSortieArticles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TypeSortieArticles");
        Page<TypeSortieArticle> page = typeSortieArticleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type-sortie-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /type-sortie-articles/:id : get the "id" typeSortieArticle.
     *
     * @param id the id of the typeSortieArticle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeSortieArticle, or with status 404 (Not Found)
     */
    @GetMapping("/type-sortie-articles/{id}")
    @Timed
    public ResponseEntity<TypeSortieArticle> getTypeSortieArticle(@PathVariable Long id) {
        log.debug("REST request to get TypeSortieArticle : {}", id);
        TypeSortieArticle typeSortieArticle = typeSortieArticleRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(typeSortieArticle));
    }

    /**
     * DELETE  /type-sortie-articles/:id : delete the "id" typeSortieArticle.
     *
     * @param id the id of the typeSortieArticle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/type-sortie-articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteTypeSortieArticle(@PathVariable Long id) {
        log.debug("REST request to delete TypeSortieArticle : {}", id);
        typeSortieArticleRepository.delete(id);
        typeSortieArticleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-sortie-articles?query=:query : search for the typeSortieArticle corresponding
     * to the query.
     *
     * @param query the query of the typeSortieArticle search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/type-sortie-articles")
    @Timed
    public ResponseEntity<List<TypeSortieArticle>> searchTypeSortieArticles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TypeSortieArticles for query {}", query);
        Page<TypeSortieArticle> page = typeSortieArticleSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/type-sortie-articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
