package com.ballack.com.repository;

import com.ballack.com.domain.LigneEntreeArticle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the LigneEntreeArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneEntreeArticleRepository extends JpaRepository<LigneEntreeArticle, Long> {

    @Query("select ligne_entree_article from LigneEntreeArticle ligne_entree_article where ligne_entree_article.agent.login = ?#{principal.username}")
    List<LigneEntreeArticle> findByAgentIsCurrentUser();

}
