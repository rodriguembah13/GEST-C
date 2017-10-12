package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A LigneEntreeArticle.
 */
@Entity
@Table(name = "ligne_entree_article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ligneentreearticle")
public class LigneEntreeArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "designation")
    private String designation;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "montanttotalht")
    private Double montanttotalht;

    @Column(name = "montanttotalttc")
    private Double montanttotalttc;

    @Column(name = "prix_unitaire")
    private Double prix_unitaire;

    @Column(name = "taxe_tva")
    private Double taxeTVA;

    @Column(name = "dateperemption")
    private LocalDate dateperemption;

    @Column(name = "prix_achat")
    private Double prixAchat;

    @ManyToOne
    private User agent;

    @ManyToOne
    private Article article;

    @ManyToOne
    private EntreeArticle entreeArticle;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public LigneEntreeArticle designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public LigneEntreeArticle quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getMontanttotalht() {
        return montanttotalht;
    }

    public LigneEntreeArticle montanttotalht(Double montanttotalht) {
        this.montanttotalht = montanttotalht;
        return this;
    }

    public void setMontanttotalht(Double montanttotalht) {
        this.montanttotalht = montanttotalht;
    }

    public Double getMontanttotalttc() {
        return montanttotalttc;
    }

    public LigneEntreeArticle montanttotalttc(Double montanttotalttc) {
        this.montanttotalttc = montanttotalttc;
        return this;
    }

    public void setMontanttotalttc(Double montanttotalttc) {
        this.montanttotalttc = montanttotalttc;
    }

    public Double getPrix_unitaire() {
        return prix_unitaire;
    }

    public LigneEntreeArticle prix_unitaire(Double prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
        return this;
    }

    public void setPrix_unitaire(Double prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }

    public Double getTaxeTVA() {
        return taxeTVA;
    }

    public LigneEntreeArticle taxeTVA(Double taxeTVA) {
        this.taxeTVA = taxeTVA;
        return this;
    }

    public void setTaxeTVA(Double taxeTVA) {
        this.taxeTVA = taxeTVA;
    }

    public LocalDate getDateperemption() {
        return dateperemption;
    }

    public LigneEntreeArticle dateperemption(LocalDate dateperemption) {
        this.dateperemption = dateperemption;
        return this;
    }

    public void setDateperemption(LocalDate dateperemption) {
        this.dateperemption = dateperemption;
    }

    public Double getPrixAchat() {
        return prixAchat;
    }

    public LigneEntreeArticle prixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
        return this;
    }

    public void setPrixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public User getAgent() {
        return agent;
    }

    public LigneEntreeArticle agent(User user) {
        this.agent = user;
        return this;
    }

    public void setAgent(User user) {
        this.agent = user;
    }

    public Article getArticle() {
        return article;
    }

    public LigneEntreeArticle article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public EntreeArticle getEntreeArticle() {
        return entreeArticle;
    }

    public LigneEntreeArticle entreeArticle(EntreeArticle entreeArticle) {
        this.entreeArticle = entreeArticle;
        return this;
    }

    public void setEntreeArticle(EntreeArticle entreeArticle) {
        this.entreeArticle = entreeArticle;
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
        LigneEntreeArticle ligneEntreeArticle = (LigneEntreeArticle) o;
        if (ligneEntreeArticle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ligneEntreeArticle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LigneEntreeArticle{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", montanttotalht='" + getMontanttotalht() + "'" +
            ", montanttotalttc='" + getMontanttotalttc() + "'" +
            ", prix_unitaire='" + getPrix_unitaire() + "'" +
            ", taxeTVA='" + getTaxeTVA() + "'" +
            ", dateperemption='" + getDateperemption() + "'" +
            ", prixAchat='" + getPrixAchat() + "'" +
            "}";
    }
}
