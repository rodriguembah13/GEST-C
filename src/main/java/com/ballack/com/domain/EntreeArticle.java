package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A EntreeArticle.
 */
@Entity
@Table(name = "entree_article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "entreearticle")
public class EntreeArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "dateentre")
    private LocalDate dateentre;

    @Column(name = "montant_ht")
    private Double montant_ht;

    @Column(name = "montant_ttc")
    private Double montant_ttc;

    @Column(name = "observation")
    private String observation;

    @ManyToOne
    private User agent;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public EntreeArticle titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDateentre() {
        return dateentre;
    }

    public EntreeArticle dateentre(LocalDate dateentre) {
        this.dateentre = dateentre;
        return this;
    }

    public void setDateentre(LocalDate dateentre) {
        this.dateentre = dateentre;
    }

    public Double getMontant_ht() {
        return montant_ht;
    }

    public EntreeArticle montant_ht(Double montant_ht) {
        this.montant_ht = montant_ht;
        return this;
    }

    public void setMontant_ht(Double montant_ht) {
        this.montant_ht = montant_ht;
    }

    public Double getMontant_ttc() {
        return montant_ttc;
    }

    public EntreeArticle montant_ttc(Double montant_ttc) {
        this.montant_ttc = montant_ttc;
        return this;
    }

    public void setMontant_ttc(Double montant_ttc) {
        this.montant_ttc = montant_ttc;
    }

    public String getObservation() {
        return observation;
    }

    public EntreeArticle observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public User getAgent() {
        return agent;
    }

    public EntreeArticle agent(User user) {
        this.agent = user;
        return this;
    }

    public void setAgent(User user) {
        this.agent = user;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntreeArticle entreeArticle = (EntreeArticle) o;
        if (entreeArticle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entreeArticle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntreeArticle{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", dateentre='" + getDateentre() + "'" +
            ", montant_ht='" + getMontant_ht() + "'" +
            ", montant_ttc='" + getMontant_ttc() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
