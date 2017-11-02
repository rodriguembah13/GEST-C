package com.ballack.com.repository;

import com.ballack.com.domain.SortieArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the SortieArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SortieArticleRepository extends JpaRepository<SortieArticle, Long> {

    @Query("select sortie_article from SortieArticle sortie_article where sortie_article.agent.login = ?#{principal.username}")
    Page<SortieArticle> findByAgentIsCurrentUser(Pageable pageable);
    @Query("select sortie_article from SortieArticle sortie_article where sortie_article.datesortie >=:x and sortie_article.datesortie <:y")
    Page<SortieArticle> findByDate(Pageable pageable, @Param("x") LocalDate x, @Param("y") LocalDate y);
    @Query("select sortie_article from SortieArticle sortie_article where sortie_article.datesortie >=:x and sortie_article.datesortie <:y and sortie_article.agent.login = ?#{principal.username}")
    Page<SortieArticle> findByDateByAgentIsCurrentUser(Pageable pageable, @Param("x") LocalDate x, @Param("y") LocalDate y);
}
