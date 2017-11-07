package com.ballack.com.web.rest;

import com.ballack.com.config.ApplicationProperties;
import com.codahale.metrics.annotation.Timed;
import com.ballack.com.domain.Article;
import com.ballack.com.service.ArticleService;
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
import javax.validation.Valid;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Article.
 */
@RestController
@RequestMapping("/api")
public class ArticleResource {

    private final Logger log = LoggerFactory.getLogger(ArticleResource.class);

    private static final String ENTITY_NAME = "article";

    private final ArticleService articleService;
    private final ApplicationProperties applicationProperties;
    @Autowired
    ApplicationContext context;
    public ArticleResource(ArticleService articleService, ApplicationProperties applicationProperties) {
        this.articleService = articleService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /articles : Create a new article.
     *
     * @param article the article to create
     * @return the ResponseEntity with status 201 (Created) and with body the new article, or with status 400 (Bad Request) if the article has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/articles")
    @Timed
    public ResponseEntity<Article> createArticle(@Valid @RequestBody Article article) throws URISyntaxException {
        log.debug("REST request to save Article : {}", article);
        if (article.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new article cannot already have an ID")).body(null);
        }
        Article result = articleService.save(article);
        return ResponseEntity.created(new URI("/api/articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /articles : Updates an existing article.
     *
     * @param article the article to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated article,
     * or with status 400 (Bad Request) if the article is not valid,
     * or with status 500 (Internal Server Error) if the article couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/articles")
    @Timed
    public ResponseEntity<Article> updateArticle(@Valid @RequestBody Article article) throws URISyntaxException {
        log.debug("REST request to update Article : {}", article);
        if (article.getId() == null) {
            return createArticle(article);
        }
        Article result = articleService.save(article);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, article.getId().toString()))
            .body(result);
    }
    @PutMapping("/genererCodeArticle")
    @Timed
    public ResponseEntity<Article> genererCodeArticle(@Valid @RequestBody Article article) throws URISyntaxException {
        log.debug("REST request to update Article : {}", article);
        if (article.getId() == null) {
            return createArticle(article);
        }
        Article result = articleService.generecode(article);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, article.getId().toString()))
            .body(result);
    }

    /**
     * GET  /articles : get all the articles.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of articles in body
     */
    @GetMapping("/articles")
    @Timed
    public ResponseEntity<List<Article>> getAllArticles(@ApiParam Pageable pageable, @RequestParam(required = false) String filter) {
        if ("etiquette-is-null".equals(filter)) {
            log.debug("REST request to get all Articles where etiquette is null");
            return new ResponseEntity<>(articleService.findAllWhereEtiquetteIsNull(),
                HttpStatus.OK);
        }
        log.debug("REST request to get a page of Articles");
        Page<Article> page = articleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /articles/:id : get the "id" article.
     *
     * @param id the id of the article to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the article, or with status 404 (Not Found)
     */
    @GetMapping("/articles/{id}")
    @Timed
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        log.debug("REST request to get Article : {}", id);
        Article article = articleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(article));
    }

    /**
     * DELETE  /articles/:id : delete the "id" article.
     *
     * @param id the id of the article to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/articles/{id}")
    @Timed
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        log.debug("REST request to delete Article : {}", id);
        articleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/articles?query=:query : search for the article corresponding
     * to the query.
     *
     * @param query the query of the article search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/articles")
    @Timed
    public ResponseEntity<List<Article>> searchArticles(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Articles for query {}", query);
        Page<Article> page = articleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/articles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping(value = "/printListeArticlePdf")
    @Timed
    void printListePdf(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");

            String lg=applicationProperties.getFacture().getCheminImage();
            connection = DriverManager.getConnection(url1, login1, password1);
            File file = new File(applicationProperties.getFacture().getCheminJasper());
            FileInputStream fis = new FileInputStream(new File(file, "listeArticle.jasper"));
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
    @GetMapping(value = "/printListeArticleXls")
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
            FileInputStream fis = new FileInputStream(new File(file, "listeArticle.jasper"));
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
    @GetMapping(value = "/printListeArticleFamillePdf")
    @Timed
    void printListeFamillePdf(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
        Connection connection = null;
        try {
            // - Connexion à la base grace au fichier properties

            String url1 =context.getEnvironment().getProperty("spring.datasource.url");
            String login1 =context.getEnvironment().getProperty("spring.datasource.username");
            String password1 =context.getEnvironment().getProperty("spring.datasource.password");

            String lg=applicationProperties.getFacture().getCheminImage();
            connection = DriverManager.getConnection(url1, login1, password1);
            File file = new File(applicationProperties.getFacture().getCheminJasper());
            FileInputStream fis = new FileInputStream(new File(file, "listeArticleByFamille.jasper"));
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
    @GetMapping(value = "/printListeArticleFamilleXls")
    @Timed
    public void printListeArticleFamilleXls(HttpServletResponse httpServletResponse) throws SQLException, FileNotFoundException {
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
            FileInputStream fis = new FileInputStream(new File(file, "listeArticleByFamille.jasper"));
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
