package com.ballack.com.repository;

import com.ballack.com.domain.TransfertMagasin;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the TransfertMagasin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransfertMagasinRepository extends JpaRepository<TransfertMagasin, Long> {

    @Query("select transfert_magasin from TransfertMagasin transfert_magasin where transfert_magasin.user.login = ?#{principal.username}")
    List<TransfertMagasin> findByUserIsCurrentUser();

}
