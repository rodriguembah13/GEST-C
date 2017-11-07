package com.ballack.com.web.rest;

import com.ballack.com.config.ApplicationProperties;
import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.Fournisseur;

import com.ballack.com.repository.FournisseurRepository;
import com.ballack.com.repository.search.FournisseurSearchRepository;
import com.ballack.com.web.rest.util.HeaderUtil;
import com.ballack.com.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Fournisseur.
 */
@RestController
@RequestMapping("/api")
public class FournisseurResource {

    private final Logger log = LoggerFactory.getLogger(FournisseurResource.class);

    private static final String ENTITY_NAME = "fournisseur";

    private final FournisseurRepository fournisseurRepository;
    private final ApplicationProperties applicationProperties;
    @Autowired
    ApplicationContext context;
    private final FournisseurSearchRepository fournisseurSearchRepository;
    public FournisseurResource(FournisseurRepository fournisseurRepository, ApplicationProperties applicationProperties, FournisseurSearchRepository fournisseurSearchRepository) {
        this.fournisseurRepository = fournisseurRepository;
        this.applicationProperties = applicationProperties;
        this.fournisseurSearchRepository = fournisseurSearchRepository;
    }

    /**
     * POST  /fournisseurs : Create a new fournisseur.
     *
     * @param fournisseur the fournisseur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fournisseur, or with status 400 (Bad Request) if the fournisseur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fournisseurs")
    @Timed
    public ResponseEntity<Fournisseur> createFournisseur(@RequestBody Fournisseur fournisseur) throws URISyntaxException {
        log.debug("REST request to save Fournisseur : {}", fournisseur);
        if (fournisseur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fournisseur cannot already have an ID")).body(null);
        }
        Fournisseur result = fournisseurRepository.save(fournisseur);
        fournisseurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fournisseurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fournisseurs : Updates an existing fournisseur.
     *
     * @param fournisseur the fournisseur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fournisseur,
     * or with status 400 (Bad Request) if the fournisseur is not valid,
     * or with status 500 (Internal Server Error) if the fournisseur couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fournisseurs")
    @Timed
    public ResponseEntity<Fournisseur> updateFournisseur(@RequestBody Fournisseur fournisseur) throws URISyntaxException {
        log.debug("REST request to update Fournisseur : {}", fournisseur);
        if (fournisseur.getId() == null) {
            return createFournisseur(fournisseur);
        }
        Fournisseur result = fournisseurRepository.save(fournisseur);
        fournisseurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fournisseur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fournisseurs : get all the fournisseurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fournisseurs in body
     */
    @GetMapping("/fournisseurs")
    @Timed
    public ResponseEntity<List<Fournisseur>> getAllFournisseurs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Fournisseurs");
        Page<Fournisseur> page = fournisseurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fournisseurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fournisseurs/:id : get the "id" fournisseur.
     *
     * @param id the id of the fournisseur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fournisseur, or with status 404 (Not Found)
     */
    @GetMapping("/fournisseurs/{id}")
    @Timed
    public ResponseEntity<Fournisseur> getFournisseur(@PathVariable Long id) {
        log.debug("REST request to get Fournisseur : {}", id);
        Fournisseur fournisseur = fournisseurRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fournisseur));
    }

    /**
     * DELETE  /fournisseurs/:id : delete the "id" fournisseur.
     *
     * @param id the id of the fournisseur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fournisseurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        log.debug("REST request to delete Fournisseur : {}", id);
        fournisseurRepository.delete(id);
        fournisseurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fournisseurs?query=:query : search for the fournisseur corresponding
     * to the query.
     *
     * @param query the query of the fournisseur search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/fournisseurs")
    @Timed
    public ResponseEntity<List<Fournisseur>> searchFournisseurs(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Fournisseurs for query {}", query);
        Page<Fournisseur> page = fournisseurSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fournisseurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping(value = "/printListefournisseursPdf")
    @Timed
    void printListefournisseursPdf(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        //JRPdfExporter jrPdfExporter=new JRPdfExporter();

        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");

            String lg=applicationProperties.getFacture().getCheminImage();
            connection = DriverManager.getConnection(url1, login1, password1);
            File file = new File(applicationProperties.getFacture().getCheminJasper());
            FileInputStream fis = new FileInputStream(new File(file, "fournisseurs.jasper"));
            Map<String,Object> parameterMap= new HashedMap();
            parameterMap.put("logo",lg);
            JasperPrint jasperPrint= JasperFillManager.fillReport(fis,parameterMap,connection);
/*            jrPdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            jrPdfExporter.setParameter(JRPdfExporter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
            jrPdfExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, java.lang.Boolean.TRUE);
            jrPdfExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, java.lang.Boolean.FALSE);*/
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-Disposition","inline:filename=liste.pdf");
            httpServletResponse.getStatus();
            final OutputStream outputStream=httpServletResponse.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
/*            jrPdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
            jrPdfExporter.exportReport();*/
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @GetMapping(value = "/printListefournisseursXls")
    @Timed
    public void printListefournisseursXls(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
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
            FileInputStream fis = new FileInputStream(new File(file, "fournisseurs.jasper"));
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
