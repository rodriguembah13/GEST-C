package com.ballack.com.repository;

import com.ballack.com.domain.SortieArticle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the SortieArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SortieArticleRepository extends JpaRepository<SortieArticle, Long> {

    @Query("select sortie_article from SortieArticle sortie_article where sortie_article.agent.login = ?#{principal.username}")
    List<SortieArticle> findByAgentIsCurrentUser();

}
