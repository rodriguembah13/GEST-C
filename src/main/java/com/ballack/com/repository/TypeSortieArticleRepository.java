package com.ballack.com.repository;

import com.ballack.com.domain.TypeSortieArticle;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TypeSortieArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSortieArticleRepository extends JpaRepository<TypeSortieArticle, Long> {

}
