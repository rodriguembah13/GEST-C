package com.ballack.com.repository;

import com.ballack.com.domain.FormeArticle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FormeArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormeArticleRepository extends JpaRepository<FormeArticle, Long> {

}
