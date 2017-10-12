package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A SortieArticle.
 */
@Entity
@Table(name = "sortie_article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sortiearticle")
public class SortieArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numsortie")
    private String numsortie;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "datesortie")
    private LocalDate datesortie;

    @Column(name = "montanttotal")
    private Double montanttotal;

    @Column(name = "montanttva")
    private Double montanttva;

    @Column(name = "montantttc")
    private Double montantttc;

    @Column(name = "destinataire")
    private String destinataire;

    @ManyToOne
    private User agent;

    @ManyToOne
    private Magasin magasin;

    @ManyToOne
    private Client client;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumsortie() {
        return numsortie;
    }

    public SortieArticle numsortie(String numsortie) {
        this.numsortie = numsortie;
        return this;
    }

    public void setNumsortie(String numsortie) {
        this.numsortie = numsortie;
    }

    public String getLibelle() {
        return libelle;
    }

    public SortieArticle libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getDatesortie() {
        return datesortie;
    }

    public SortieArticle datesortie(LocalDate datesortie) {
        this.datesortie = datesortie;
        return this;
    }

    public void setDatesortie(LocalDate datesortie) {
        this.datesortie = datesortie;
    }

    public Double getMontanttotal() {
        return montanttotal;
    }

    public SortieArticle montanttotal(Double montanttotal) {
        this.montanttotal = montanttotal;
        return this;
    }

    public void setMontanttotal(Double montanttotal) {
        this.montanttotal = montanttotal;
    }

    public Double getMontanttva() {
        return montanttva;
    }

    public SortieArticle montanttva(Double montanttva) {
        this.montanttva = montanttva;
        return this;
    }

    public void setMontanttva(Double montanttva) {
        this.montanttva = montanttva;
    }

    public Double getMontantttc() {
        return montantttc;
    }

    public SortieArticle montantttc(Double montantttc) {
        this.montantttc = montantttc;
        return this;
    }

    public void setMontantttc(Double montantttc) {
        this.montantttc = montantttc;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public SortieArticle destinataire(String destinataire) {
        this.destinataire = destinataire;
        return this;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public User getAgent() {
        return agent;
    }

    public SortieArticle agent(User user) {
        this.agent = user;
        return this;
    }

    public void setAgent(User user) {
        this.agent = user;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public SortieArticle magasin(Magasin magasin) {
        this.magasin = magasin;
        return this;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public Client getClient() {
        return client;
    }

    public SortieArticle client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
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
        SortieArticle sortieArticle = (SortieArticle) o;
        if (sortieArticle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sortieArticle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SortieArticle{" +
            "id=" + getId() +
            ", numsortie='" + getNumsortie() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", datesortie='" + getDatesortie() + "'" +
            ", montanttotal='" + getMontanttotal() + "'" +
            ", montanttva='" + getMontanttva() + "'" +
            ", montantttc='" + getMontantttc() + "'" +
            ", destinataire='" + getDestinataire() + "'" +
            "}";
    }
}
