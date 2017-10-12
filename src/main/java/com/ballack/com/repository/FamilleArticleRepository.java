package com.ballack.com.repository;

import com.ballack.com.domain.FamilleArticle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FamilleArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilleArticleRepository extends JpaRepository<FamilleArticle, Long> {

}
