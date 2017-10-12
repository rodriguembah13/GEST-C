package com.ballack.com.repository;

import com.ballack.com.domain.EntreeArticle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the EntreeArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntreeArticleRepository extends JpaRepository<EntreeArticle, Long> {

    @Query("select entree_article from EntreeArticle entree_article where entree_article.agent.login = ?#{principal.username}")
    List<EntreeArticle> findByAgentIsCurrentUser();

}
