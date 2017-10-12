package com.ballack.com.repository;

import com.ballack.com.domain.Decomposition;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Decomposition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecompositionRepository extends JpaRepository<Decomposition, Long> {

}
