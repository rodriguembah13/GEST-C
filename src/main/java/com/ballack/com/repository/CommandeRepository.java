package com.ballack.com.repository;

import com.ballack.com.domain.Commande;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Commande entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    @Query("select commande from Commande commande where commande.agent.login = ?#{principal.username}")
    List<Commande> findByAgentIsCurrentUser();

}
