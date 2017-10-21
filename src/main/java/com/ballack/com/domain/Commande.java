package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "commande")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numcommande")
    private String numcommande;

    @Column(name = "montanttotalht")
    private Double montanttotalht;

    @Column(name = "codebarre")
    private String codebarre;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "montanttotalttc")
    private Double montanttotalttc;

    @Column(name = "datelimitlivraison")
    private LocalDate datelimitlivraison;

    @Column(name = "datecommande")
    private Instant datecommande;

    @Column(name = "etat")
    private Boolean etat;

    @Column(name = "soldee")
    private Boolean soldee;

    @ManyToOne
    private User agent;

    @ManyToOne
    private Fournisseur fournisseur;

    @ManyToOne
    private Magasin magasin;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumcommande() {
        return numcommande;
    }

    public Commande numcommande(String numcommande) {
        this.numcommande = numcommande;
        return this;
    }

    public void setNumcommande(String numcommande) {
        this.numcommande = numcommande;
    }

    public Double getMontanttotalht() {
        return montanttotalht;
    }

    public Commande montanttotalht(Double montanttotalht) {
        this.montanttotalht = montanttotalht;
        return this;
    }

    public void setMontanttotalht(Double montanttotalht) {
        this.montanttotalht = montanttotalht;
    }

    public String getCodebarre() {
        return codebarre;
    }

    public Commande codebarre(String codebarre) {
        this.codebarre = codebarre;
        return this;
    }

    public void setCodebarre(String codebarre) {
        this.codebarre = codebarre;
    }

    public String getLibelle() {
        return libelle;
    }

    public Commande libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getMontanttotalttc() {
        return montanttotalttc;
    }

    public Commande montanttotalttc(Double montanttotalttc) {
        this.montanttotalttc = montanttotalttc;
        return this;
    }

    public void setMontanttotalttc(Double montanttotalttc) {
        this.montanttotalttc = montanttotalttc;
    }

    public LocalDate getDatelimitlivraison() {
        return datelimitlivraison;
    }

    public Commande datelimitlivraison(LocalDate datelimitlivraison) {
        this.datelimitlivraison = datelimitlivraison;
        return this;
    }

    public void setDatelimitlivraison(LocalDate datelimitlivraison) {
        this.datelimitlivraison = datelimitlivraison;
    }

    public Instant getDatecommande() {
        return datecommande;
    }

    public Commande datecommande(Instant datecommande) {
        this.datecommande = datecommande;
        return this;
    }

    public void setDatecommande(Instant datecommande) {
        this.datecommande = datecommande;
    }

    public Boolean isEtat() {
        return etat;
    }

    public Commande etat(Boolean etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public Boolean isSoldee() {
        return soldee;
    }

    public Commande soldee(Boolean soldee) {
        this.soldee = soldee;
        return this;
    }

    public void setSoldee(Boolean soldee) {
        this.soldee = soldee;
    }

    public User getAgent() {
        return agent;
    }

    public Commande agent(User user) {
        this.agent = user;
        return this;
    }

    public void setAgent(User user) {
        this.agent = user;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public Commande fournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        return this;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public Commande magasin(Magasin magasin) {
        this.magasin = magasin;
        return this;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
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
        Commande commande = (Commande) o;
        if (commande.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commande.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", numcommande='" + getNumcommande() + "'" +
            ", montanttotalht='" + getMontanttotalht() + "'" +
            ", codebarre='" + getCodebarre() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", montanttotalttc='" + getMontanttotalttc() + "'" +
            ", datelimitlivraison='" + getDatelimitlivraison() + "'" +
            ", datecommande='" + getDatecommande() + "'" +
            ", etat='" + isEtat() + "'" +
            ", soldee='" + isSoldee() + "'" +
            "}";
    }
}
