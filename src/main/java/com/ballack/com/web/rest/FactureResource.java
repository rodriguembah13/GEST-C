package com.ballack.com.web.rest;

import com.ballack.com.domain.SortieArticle;
import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.Facture;
import com.ballack.com.service.FactureService;
import com.ballack.com.web.rest.util.HeaderUtil;
import com.ballack.com.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Facture.
 */
@RestController
@RequestMapping("/api")
public class FactureResource {

    private final Logger log = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    private final FactureService factureService;
    @Autowired
    ApplicationContext context;
    public FactureResource(FactureService factureService) {
        this.factureService = factureService;
    }

    /**
     * POST  /factures : Create a new facture.
     *
     * @param facture the facture to create
     * @return the ResponseEntity with status 201 (Created) and with body the new facture, or with status 400 (Bad Request) if the facture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/factures")
    @Timed
    public ResponseEntity<Facture> createFacture(@RequestBody Facture facture) throws URISyntaxException {
        log.debug("REST request to save Facture : {}", facture);
        if (facture.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new facture cannot already have an ID")).body(null);
        }
        Facture result = factureService.save(facture);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /factures : Updates an existing facture.
     *
     * @param facture the facture to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated facture,
     * or with status 400 (Bad Request) if the facture is not valid,
     * or with status 500 (Internal Server Error) if the facture couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/factures")
    @Timed
    public ResponseEntity<Facture> updateFacture(@RequestBody Facture facture) throws URISyntaxException {
        log.debug("REST request to update Facture : {}", facture);
        if (facture.getId() == null) {
            return createFacture(facture);
        }
        Facture result = factureService.save(facture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, facture.getId().toString()))
            .body(result);
    }

    /**
     * GET  /factures : get all the factures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of factures in body
     */
    @GetMapping("/factures")
    @Timed
    public ResponseEntity<List<Facture>> getAllFactures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Factures");
        Page<Facture> page = factureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/factures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /factures : get all the factures by user.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of factures in body
     */
    @GetMapping("/facturesuser")
    @Timed
    public ResponseEntity<List<Facture>> getAllFacturesUser(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Factures");
        Page<Facture> page = factureService.findAllbyUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/facturesuser");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /factures/:id : get the "id" facture.
     *
     * @param id the id of the facture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the facture, or with status 404 (Not Found)
     */
    @GetMapping("/factures/{id}")
    @Timed
    public ResponseEntity<Facture> getFacture(@PathVariable Long id) {
        log.debug("REST request to get Facture : {}", id);
        Facture facture = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(facture));
    }

    /**
     * DELETE  /factures/:id : delete the "id" facture.
     *
     * @param id the id of the facture to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/factures/{id}")
    @Timed
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        log.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/factures?query=:query : search for the facture corresponding
     * to the query.
     *
     * @param query the query of the facture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/factures")
    @Timed
    public ResponseEntity<List<Facture>> searchFactures(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Factures for query {}", query);
        Page<Facture> page = factureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/factures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping(value = "/PrintFacture/{id}")
    @Timed
    void printFacturePdf(@PathVariable Long id, HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");
            String lg="C:\\TempJasper\\imagepharma";
            //Resource jasper=context.getResource("application.path.reports");
            connection = DriverManager.getConnection(url1, login1, password1);
            //InputStream inputStream= new FileInputStream(new File("C:\\TempJasper\\bulletinT.jrxml"));

            File file = new File("C:\\Users\\ballack\\JaspersoftWorkspace\\gest-c");
            FileInputStream fis = new FileInputStream(new File(file, "facture_A4.jasper"));

            Map<String,Object> parameterMap= new HashedMap();

            //Long ida=id;
            parameterMap.put("idVente",id);
            parameterMap.put("logo",lg);
            parameterMap.put("codebare",id);
            //System.out.println(nom);
            JasperPrint jasperPrint= JasperFillManager.fillReport(fis,parameterMap,connection);
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-Disposition","inline:filename=bulletin.pdf");
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
    @GetMapping(value = "/PrintTicket/{id}")
    @Timed
    void printTicketPdf(@PathVariable Long id, HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");
            String lg="C:\\TempJasper\\imagepharma";
            connection = DriverManager.getConnection(url1, login1, password1);
            //InputStream inputStream= new FileInputStream(new File("C:\\TempJasper\\bulletinT.jrxml"));

            File file = new File("C:\\Users\\ballack\\JaspersoftWorkspace\\gest-c");
            FileInputStream fis = new FileInputStream(new File(file, "tickect.jasper"));

            Map<String,Object> parameterMap= new HashedMap();

            //Long ida=id;
            parameterMap.put("idVente",id);
            parameterMap.put("logo",lg);
            parameterMap.put("codebare",id);
            //System.out.println(nom);
            JasperPrint jasperPrint= JasperFillManager.fillReport(fis,parameterMap,connection);
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-Disposition","inline:filename=bulletin.pdf");
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
    @GetMapping(value = "/printBdReception/{id}")
    @Timed
    void printBdReceptionPdf(@PathVariable Long id, HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");
            String lg="C:\\TempJasper\\imagepharma";
            connection = DriverManager.getConnection(url1, login1, password1);
            //InputStream inputStream= new FileInputStream(new File("C:\\TempJasper\\bulletinT.jrxml"));

            File file = new File("C:\\Users\\ballack\\JaspersoftWorkspace\\gest-c");
            FileInputStream fis = new FileInputStream(new File(file, "bordereaureception.jasper"));

            Map<String,Object> parameterMap= new HashedMap();

            //Long ida=id;
            parameterMap.put("id_re",id);
           parameterMap.put("logo",lg);
         /*    parameterMap.put("codebare",id);*/
            //System.out.println(nom);
            JasperPrint jasperPrint= JasperFillManager.fillReport(fis,parameterMap,connection);
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-Disposition","inline:filename=bulletin.pdf");
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
    @GetMapping(value = "/printBdCmde/{id}")
    @Timed
    void printBdCmdePdf(@PathVariable Long id, HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");
            String lg="C:\\TempJasper\\imagepharma";
            connection = DriverManager.getConnection(url1, login1, password1);
            //InputStream inputStream= new FileInputStream(new File("C:\\TempJasper\\bulletinT.jrxml"));

            File file = new File("C:\\Users\\ballack\\JaspersoftWorkspace\\gest-c");
            FileInputStream fis = new FileInputStream(new File(file, "bordereaucmde.jasper"));

            Map<String,Object> parameterMap= new HashedMap();

            //Long ida=id;
            parameterMap.put("id_cmde",id);
            parameterMap.put("logo",lg);
          /*  parameterMap.put("codebare",id);*/
            //System.out.println(nom);
            JasperPrint jasperPrint= JasperFillManager.fillReport(fis,parameterMap,connection);
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setHeader("Content-Disposition","inline:filename=bulletin.pdf");
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
}
