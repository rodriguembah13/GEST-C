package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LigneCommande.
 */
@Entity
@Table(name = "ligne_commande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignecommande")
public class LigneCommande implements Serializable {

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

    @Column(name = "prix")
    private Double prix;

    @Column(name = "taxe_tva")
    private Double taxeTva;

    @ManyToOne
    private User agent;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Commande commande;

    @ManyToOne
    private Fournisseur fournisseur;

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

    public LigneCommande designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public LigneCommande quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getMontanttotalht() {
        return montanttotalht;
    }

    public LigneCommande montanttotalht(Double montanttotalht) {
        this.montanttotalht = montanttotalht;
        return this;
    }

    public void setMontanttotalht(Double montanttotalht) {
        this.montanttotalht = montanttotalht;
    }

    public Double getMontanttotalttc() {
        return montanttotalttc;
    }

    public LigneCommande montanttotalttc(Double montanttotalttc) {
        this.montanttotalttc = montanttotalttc;
        return this;
    }

    public void setMontanttotalttc(Double montanttotalttc) {
        this.montanttotalttc = montanttotalttc;
    }

    public Double getPrix() {
        return prix;
    }

    public LigneCommande prix(Double prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Double getTaxeTva() {
        return taxeTva;
    }

    public LigneCommande taxeTva(Double taxeTva) {
        this.taxeTva = taxeTva;
        return this;
    }

    public void setTaxeTva(Double taxeTva) {
        this.taxeTva = taxeTva;
    }

    public User getAgent() {
        return agent;
    }

    public LigneCommande agent(User user) {
        this.agent = user;
        return this;
    }

    public void setAgent(User user) {
        this.agent = user;
    }

    public Article getArticle() {
        return article;
    }

    public LigneCommande article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Commande getCommande() {
        return commande;
    }

    public LigneCommande commande(Commande commande) {
        this.commande = commande;
        return this;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public LigneCommande fournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        return this;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
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
        LigneCommande ligneCommande = (LigneCommande) o;
        if (ligneCommande.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ligneCommande.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", montanttotalht='" + getMontanttotalht() + "'" +
            ", montanttotalttc='" + getMontanttotalttc() + "'" +
            ", prix='" + getPrix() + "'" +
            ", taxeTva='" + getTaxeTva() + "'" +
            "}";
    }
}
