package com.ballack.com.repository;

import com.ballack.com.domain.Etiquette;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Etiquette entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtiquetteRepository extends JpaRepository<Etiquette, Long> {

}
