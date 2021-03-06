package com.ballack.com.repository;

import com.ballack.com.domain.LigneSortieArticle;
import com.ballack.com.domain.SortieArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


/**
 * Spring Data JPA repository for the LigneSortieArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneSortieArticleRepository extends JpaRepository<LigneSortieArticle, Long> {
    @Query("select p from LigneSortieArticle p,SortieArticle s where s.id=p.sortieArticle.id and s.datesortie <=:x and s.datesortie >:y")
    public List<LigneSortieArticle> findLigneByDate(@Param("y")LocalDate datedebut, @Param("x")LocalDate datefin);
    @Query("select p from LigneSortieArticle p where p.sortieArticle.id =:x")
    Page<LigneSortieArticle> findLineBySortie(Pageable pageable, @Param("x") Long id);

}
