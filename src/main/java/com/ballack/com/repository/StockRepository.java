package com.ballack.com.repository;

import com.ballack.com.domain.Article;
import com.ballack.com.domain.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;


/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("select p from Stock p where p.article =:x and p.actif =1")
    public Stock findStockactif(@Param("x") Article article);
    @Query("select p from Stock p where p.article.numArticle =:x and p.actif =1")
    public Stock findStockactifbynum(@Param("x") String num_article);
    @Query("select st from Stock st where st.quantite <= st.quantiteAlerte and st.closed=0")
    Page<Stock> findStockAlerte(Pageable pageable);
    @Query("select st from Stock st where st.dateperemption <=:x and st.closed=0")
    Page<Stock> findStockPeremp(Pageable pageable, @Param("x") LocalDate x);
    @Query("select st from Stock st where st.dateperemption <=:x and st.dateperemption >:y and st.closed=0")
    Page<Stock> findStockEvPeremp(Pageable pageable, @Param("x") LocalDate x, @Param("y") LocalDate y);
}
