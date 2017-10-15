package com.ballack.com.repository;

import com.ballack.com.domain.Facture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Facture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    @Query("select facture from Facture facture where facture.user.login = ?#{principal.username}")
    Page<Facture> findByUserIsCurrentUser(Pageable  pageable);

}
