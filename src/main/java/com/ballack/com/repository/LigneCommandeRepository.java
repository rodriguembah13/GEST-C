package com.ballack.com.repository;

import com.ballack.com.domain.LigneCommande;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the LigneCommande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {

    @Query("select ligne_commande from LigneCommande ligne_commande where ligne_commande.agent.login = ?#{principal.username}")
    List<LigneCommande> findByAgentIsCurrentUser();

}
