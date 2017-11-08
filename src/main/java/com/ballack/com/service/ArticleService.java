package com.ballack.com.service;

import com.ballack.com.config.ApplicationProperties;
import com.ballack.com.domain.Article;
import com.ballack.com.repository.ArticleRepository;
import com.ballack.com.repository.search.ArticleSearchRepository;
import com.ballack.com.service.util.StringTab;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Article.
 */
@Service
@Transactional
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final ApplicationProperties applicationProperties;
    private final ArticleSearchRepository articleSearchRepository;

    public ArticleService(ArticleRepository articleRepository, ApplicationProperties applicationProperties, ArticleSearchRepository articleSearchRepository) {
        this.articleRepository = articleRepository;
        this.applicationProperties = applicationProperties;
        this.articleSearchRepository = articleSearchRepository;
    }

    /**
     * Save a article.
     *
     * @param article the entity to save
     * @return the persisted entity
     */
    public Article save(Article article) {

        log.debug("Request to save Article : {}", article);
        article.setDatecreation(LocalDate.now());
        article.setNumArticle("");
        Article result = articleRepository.save(article);
        result.setNumArticle("" + (applicationProperties.getFacture().getIndice() + result.getId()));
        //articleSearchRepository.save(result);
        return articleRepository.saveAndFlush(result);
    }

    public Article saveAndFluch(Article article) {
        log.debug("Request to save Article : {}", article);
        Article result = articleRepository.saveAndFlush(article);
        //articleSearchRepository.save(result);
        return result;
    }

    public Article generecode(Article article) {
        StringTab stringTab = new StringTab();
        log.debug("Request to save Article : {}", article);
        article.setCodebarre(stringTab.getString(article.getNomarticle(), 12));
        Article result = articleRepository.saveAndFlush(article);
        //articleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Article> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable);
    }


    /**
     * get all the articles where Etiquette is null.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Article> findAllWhereEtiquetteIsNull() {
        log.debug("Request to get all articles where Etiquette is null");
        return StreamSupport
            .stream(articleRepository.findAll().spliterator(), false)
            .filter(article -> article.getEtiquette() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one article by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Article findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findOne(id);
    }

    /**
     * Delete the  article by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.delete(id);
        articleSearchRepository.delete(id);
    }

    /**
     * Search for the article corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Article> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Articles for query {}", query);
        Page<Article> result = articleSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    public void traitementArticleExel(File file) throws IOException {
        //String fileName1 = "E:\\persne\\SYsTETAB\\imp\\test.xls";
        String fileName1 = "C:\\Users\\ballack\\Documents\\article.xlsx";
        FileInputStream fichier = new FileInputStream(new File(fileName1));
        // XSSFWorkbook wb = new XSSFWorkbook(fichier);
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet sheet = wb.getSheetAt(0);
        FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
        Set<Article> articles = new HashSet<>();
        for (Row ligne : sheet) {//parcourir les lignes
            Article article = new Article();
            for (Cell cell : ligne) {
                //parcourir les colonnes
                if (cell.getColumnIndex() == 0) {
                    System.out.print(cell.getColumnIndex() + "\t\t");
                    System.out.print("N)1" + cell.getStringCellValue() + "\t\t");
                    article.setNumArticle(cell.getStringCellValue());
                    System.out.print(article.getNumArticle());
                } else if (cell.getColumnIndex() == 1) {
                    System.out.print(cell.getColumnIndex() + "\t\t");
                    System.out.print("N)2" + cell.getStringCellValue() + "\t\t");
                    article.setCodebarre(cell.getStringCellValue());
                    System.out.print(article.getCodebarre());
                } else if (cell.getColumnIndex() == 2) {
                    System.out.print(cell.getColumnIndex() + "\t\t");
                    System.out.print("N)3" + cell.getStringCellValue() + "\t\t");
                    article.setNomarticle(cell.getStringCellValue());
                    System.out.print(article.getNomarticle());
                } else if (cell.getColumnIndex() == 3) {
                    System.out.print(cell.getColumnIndex() + "\t\t");
                    System.out.print("N)4" + cell.getStringCellValue() + "\t\t");
                    article.setMarque(cell.getStringCellValue());
                }

            }
            articles.add(article);
            articleRepository.save(article);
        }
        System.out.print(articles.size());
    }
}
