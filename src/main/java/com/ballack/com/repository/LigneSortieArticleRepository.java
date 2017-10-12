package com.ballack.com.repository;

import com.ballack.com.domain.LigneSortieArticle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LigneSortieArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneSortieArticleRepository extends JpaRepository<LigneSortieArticle, Long> {

}
