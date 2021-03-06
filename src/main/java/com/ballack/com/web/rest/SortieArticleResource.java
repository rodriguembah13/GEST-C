package com.ballack.com.web.rest;

import com.ballack.com.config.ApplicationProperties;
import com.ballack.com.domain.LigneSortieArticle;
import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.SortieArticle;
import com.ballack.com.service.SortieArticleService;
import com.ballack.com.web.rest.util.HeaderUtil;
import com.ballack.com.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * REST controller for managing SortieArticle.
 */
@RestController
@RequestMapping("/api")
public class SortieArticleResource {

    private final Logger log = LoggerFactory.getLogger(SortieArticleResource.class);

    private static final String ENTITY_NAME = "sortieArticle";

    private final SortieArticleService sortieArticleService;
    private final ApplicationProperties applicationProperties;
    @Autowired
    ApplicationContext context;
    public SortieArticleResource(SortieArticleService sortieArticleService, ApplicationProperties applicationProperties) {
        this.sortieArticleService = sortieArticleService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /sortie-articles : Create a new sortieArticle.
     *
     * @param sortieArticle the sortieArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sortieArticle, or with status 400 (Bad Request) if the sortieArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sortie-article1")
    @Timed
    public ResponseEntity<SortieArticle> createSortieArticle(@RequestBody SortieArticle sortieArticle) throws URISyntaxException {
        log.debug("REST request to save SortieArticle : {}", sortieArticle);
        if (sortieArticle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sortieArticle cannot already have an ID")).body(null);
        }
        SortieArticle result = sortieArticleService.save(sortieArticle);
        return ResponseEntity.created(new URI("/api/sortie-articles1/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * POST  /sortie-articles : Create a new sortieArticle.
     *
     * @param ligneSortieArticles the sortieArticle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sortieArticle, or with status 400 (Bad Request) if the sortieArticle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sortie-articles")
    @Timed
    public ResponseEntity<SortieArticle> createSortieArticleA(@RequestBody Set<LigneSortieArticle> ligneSortieArticles) throws URISyntaxException {
        log.debug("REST request to save SortieArticle : {}", ligneSortieArticles);
        SortieArticle result = sortieArticleService.saveAll(ligneSortieArticles);
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
     * GET  /sortie-articles-bydate : get all the sortieArticles bybdate.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sortieArticles in body
     */
    @GetMapping("/sortie-articles-bydate")
    @Timed
    public ResponseEntity<List<SortieArticle>> getAllSortieArticlesByDate(@ApiParam Pageable pageable,
                                                                          @RequestParam(value = "dateDebut")LocalDate dateDebut,
                                                                          @RequestParam(value = "dateFin")LocalDate dateFin) {
        log.debug("REST request to get a page of SortieArticles");
        Page<SortieArticle> page = sortieArticleService.findAllByDate(pageable, dateDebut, dateFin);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sortie-articles-bydate");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/sortie-articles-bydate-user")
    @Timed
    public ResponseEntity<List<SortieArticle>> getAllSortieArticlesByDateByUser(@ApiParam Pageable pageable,
                                                                          @RequestParam(value = "dateDebut")LocalDate dateDebut,
                                                                          @RequestParam(value = "dateFin")LocalDate dateFin) {
        log.debug("REST request to get a page of SortieArticles");
        Page<SortieArticle> page = sortieArticleService.findAllByDateByuser(pageable, dateDebut, dateFin);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sortie-articles-bydate-user");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /sortie-articles : get all the sortieArticles by user.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sortieArticles in body
     */
    @GetMapping("/sortie-articleuser")
    @Timed
    public ResponseEntity<List<SortieArticle>> getAllSortieArticlesUser(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SortieArticles");
        Page<SortieArticle> page = sortieArticleService.findAllbyUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "sortie-articleuser");
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
    @GetMapping("/stat-produit-vente")
    @Timed
    public HashMap getStatVente() {
        HashMap hashMap=sortieArticleService.statVente();
        return hashMap;
    }
    @GetMapping("/stat-client-vente")
    @Timed
    public HashMap getStatVenteClient() {
        HashMap hashMap=sortieArticleService.statVenteClient();
        return hashMap;
    }
    @GetMapping("/stat-vente-year")
    @Timed
    public HashMap getStatVenteyear() {
        int year =LocalDate.now().getYear();
        HashMap hashMap=sortieArticleService.statVenteByyear(year);
        return hashMap;
    }
    @GetMapping("/stat-vente-year-param")
    @Timed
    public HashMap getStatVenteyearParam(@RequestParam(value = "year")int year) {

        HashMap hashMap=sortieArticleService.statVenteByyear(year);
        return hashMap;
    }
    @GetMapping("/stat-vente-montant-mois")
    @Timed
    public HashMap getStatVentemoisEnMomtant() {
        int monthValue=LocalDate.now().getMonthValue();
        int year=LocalDate.now().getYear();
        HashMap hashMap=sortieArticleService.statVenteBymonthMontant(year,monthValue);
        return hashMap;
    }
    @GetMapping("/stat-vente-montant-mois-param")
    @Timed
    public HashMap getStatVentemoisEnMomtantParam(@RequestParam(value = "year")int year,@RequestParam(value = "monthValue")int monthValue) {

        HashMap hashMap=sortieArticleService.statVenteBymonthMontant(year,monthValue);
        return hashMap;
    }
    @GetMapping("/stat-vente-montant-year")
    @Timed
    public HashMap getStatVenteyearEnMomtant() {
        HashMap hashMap=sortieArticleService.statVenteByyearMontant(LocalDate.now());
        return hashMap;
    }
    @GetMapping("/stat-vente-montant-year-param")
    @Timed
    public HashMap getStatVenteyearEnMomtantParam(@RequestParam(value = "year")int year) {
        HashMap hashMap=sortieArticleService.statVenteByyearMontantParam(year);
        return hashMap;
    }
    @GetMapping("/stat-produit-ventebydate")
    @Timed
    public HashMap getStatVente2(LocalDate datedebut, LocalDate datefin) {
        HashMap hashMap=sortieArticleService.statVente(datedebut,datefin);
        return hashMap;
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
    @GetMapping(value = "/printListeVentePdf")
    @Timed
    public void printListeVentePdf(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");

            String lg=applicationProperties.getFacture().getCheminImage();
            connection = DriverManager.getConnection(url1, login1, password1);
            File file = new File(applicationProperties.getFacture().getCheminJasper());
            FileInputStream fis = new FileInputStream(new File(file, "Liste_vente.jasper"));
            Map<String,Object> parameterMap= new HashedMap();
            parameterMap.put("logo",lg);
            JasperPrint jasperPrint= JasperFillManager.fillReport(fis,parameterMap,connection);
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-Disposition","inline:filename=liste.pdf");
            httpServletResponse.getStatus();
            final OutputStream outputStream=httpServletResponse.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @GetMapping(value = "/printListeVenteXls")
    @Timed
    public void printListeVenteXls(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        JRXlsExporter exporter = new JRXlsExporter();
        try {
            // - Connexion à la base grace au fichier properties

            String url1 = context.getEnvironment().getProperty("spring.datasource.url");
            String login1 = context.getEnvironment().getProperty("spring.datasource.username");
            String password1 = context.getEnvironment().getProperty("spring.datasource.password");

            String lg = applicationProperties.getFacture().getCheminImage();
            connection = DriverManager.getConnection(url1, login1, password1);
            File file = new File(applicationProperties.getFacture().getCheminJasper());
            FileInputStream fis = new FileInputStream(new File(file, "Liste_vente.jasper"));
            Map<String, Object> parameterMap = new HashedMap();
            parameterMap.put("logo", lg);
            JasperPrint jasperPrint = JasperFillManager.fillReport(fis, parameterMap, connection);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, java.lang.Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, java.lang.Boolean.FALSE);

            httpServletResponse.setContentType("application/ms-excel");

            httpServletResponse.setHeader("Content-Disposition", "inline:filename=liste.xsls");
            httpServletResponse.getStatus();
            final OutputStream outputStream = httpServletResponse.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
            exporter.exportReport();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }
