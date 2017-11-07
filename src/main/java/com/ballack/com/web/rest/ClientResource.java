package com.ballack.com.web.rest;

import com.ballack.com.config.ApplicationProperties;
import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.Client;

import com.ballack.com.repository.ClientRepository;
import com.ballack.com.repository.search.ClientSearchRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Client.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private static final String ENTITY_NAME = "client";

    private final ClientRepository clientRepository;
    private final ApplicationProperties applicationProperties;
    @Autowired
    ApplicationContext context;
    private final ClientSearchRepository clientSearchRepository;
    public ClientResource(ClientRepository clientRepository, ApplicationProperties applicationProperties, ClientSearchRepository clientSearchRepository) {
        this.clientRepository = clientRepository;
        this.applicationProperties = applicationProperties;
        this.clientSearchRepository = clientSearchRepository;
    }

    /**
     * POST  /clients : Create a new client.
     *
     * @param client the client to create
     * @return the ResponseEntity with status 201 (Created) and with body the new client, or with status 400 (Bad Request) if the client has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clients")
    @Timed
    public ResponseEntity<Client> createClient(@RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to save Client : {}", client);
        if (client.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new client cannot already have an ID")).body(null);
        }
        Client result = clientRepository.save(client);
        clientSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clients : Updates an existing client.
     *
     * @param client the client to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated client,
     * or with status 400 (Bad Request) if the client is not valid,
     * or with status 500 (Internal Server Error) if the client couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/clients")
    @Timed
    public ResponseEntity<Client> updateClient(@RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to update Client : {}", client);
        if (client.getId() == null) {
            return createClient(client);
        }
        Client result = clientRepository.save(client);
        clientSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, client.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clients : get all the clients.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clients in body
     */
    @GetMapping("/clients")
    @Timed
    public ResponseEntity<List<Client>> getAllClients(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Clients");
        Page<Client> page = clientRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clients/:id : get the "id" client.
     *
     * @param id the id of the client to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the client, or with status 404 (Not Found)
     */
    @GetMapping("/clients/{id}")
    @Timed
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Client client = clientRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(client));
    }

    /**
     * DELETE  /clients/:id : delete the "id" client.
     *
     * @param id the id of the client to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/clients/{id}")
    @Timed
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientRepository.delete(id);
        clientSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/clients?query=:query : search for the client corresponding
     * to the query.
     *
     * @param query the query of the client search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/clients")
    @Timed
    public ResponseEntity<List<Client>> searchClients(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Clients for query {}", query);
        Page<Client> page = clientSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/clients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping(value = "/printListeclientPdf")
    @Timed
    void printListeclientPdf(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");

            String lg=applicationProperties.getFacture().getCheminImage();
            connection = DriverManager.getConnection(url1, login1, password1);
            File file = new File(applicationProperties.getFacture().getCheminJasper());
            FileInputStream fis = new FileInputStream(new File(file, "client.jasper"));
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
    @GetMapping(value = "/printListeclientXls")
    @Timed
    void printListeclientXls(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        JRXlsExporter exporter = new JRXlsExporter();
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");

            String lg=applicationProperties.getFacture().getCheminImage();
            connection = DriverManager.getConnection(url1, login1, password1);
            File file = new File(applicationProperties.getFacture().getCheminJasper());
            FileInputStream fis = new FileInputStream(new File(file, "client.jasper"));
            Map<String,Object> parameterMap= new HashedMap();
            parameterMap.put("logo",lg);
            JasperPrint jasperPrint= JasperFillManager.fillReport(fis,parameterMap,connection);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, java.lang.Boolean.TRUE);
            exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, java.lang.Boolean.FALSE);

            httpServletResponse.setContentType("application/ms-excel");

            httpServletResponse.setHeader("Content-Disposition","inline:filename=liste.xsls");
            httpServletResponse.getStatus();
            final OutputStream outputStream=httpServletResponse.getOutputStream();
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
