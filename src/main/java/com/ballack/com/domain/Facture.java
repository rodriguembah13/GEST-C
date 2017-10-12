package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "facture")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numfacture")
    private String numfacture;

    @Column(name = "montanttotalht")
    private Double montanttotalht;

    @Column(name = "montanttva")
    private Double montanttva;

    @Column(name = "reduction")
    private Double reduction;

    @Column(name = "codebarre")
    private String codebarre;

    @Column(name = "libellefacture")
    private String libellefacture;

    @Column(name = "dateedition")
    private LocalDate dateedition;

    @Column(name = "datefacturation")
    private LocalDate datefacturation;

    @Column(name = "montanttotalttc")
    private Double montanttotalttc;

    @Column(name = "observation")
    private String observation;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Caisse caisse;

    @ManyToOne
    private User user;

    @ManyToOne
    private Magasin magasin;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumfacture() {
        return numfacture;
    }

    public Facture numfacture(String numfacture) {
        this.numfacture = numfacture;
        return this;
    }

    public void setNumfacture(String numfacture) {
        this.numfacture = numfacture;
    }

    public Double getMontanttotalht() {
        return montanttotalht;
    }

    public Facture montanttotalht(Double montanttotalht) {
        this.montanttotalht = montanttotalht;
        return this;
    }

    public void setMontanttotalht(Double montanttotalht) {
        this.montanttotalht = montanttotalht;
    }

    public Double getMontanttva() {
        return montanttva;
    }

    public Facture montanttva(Double montanttva) {
        this.montanttva = montanttva;
        return this;
    }

    public void setMontanttva(Double montanttva) {
        this.montanttva = montanttva;
    }

    public Double getReduction() {
        return reduction;
    }

    public Facture reduction(Double reduction) {
        this.reduction = reduction;
        return this;
    }

    public void setReduction(Double reduction) {
        this.reduction = reduction;
    }

    public String getCodebarre() {
        return codebarre;
    }

    public Facture codebarre(String codebarre) {
        this.codebarre = codebarre;
        return this;
    }

    public void setCodebarre(String codebarre) {
        this.codebarre = codebarre;
    }

    public String getLibellefacture() {
        return libellefacture;
    }

    public Facture libellefacture(String libellefacture) {
        this.libellefacture = libellefacture;
        return this;
    }

    public void setLibellefacture(String libellefacture) {
        this.libellefacture = libellefacture;
    }

    public LocalDate getDateedition() {
        return dateedition;
    }

    public Facture dateedition(LocalDate dateedition) {
        this.dateedition = dateedition;
        return this;
    }

    public void setDateedition(LocalDate dateedition) {
        this.dateedition = dateedition;
    }

    public LocalDate getDatefacturation() {
        return datefacturation;
    }

    public Facture datefacturation(LocalDate datefacturation) {
        this.datefacturation = datefacturation;
        return this;
    }

    public void setDatefacturation(LocalDate datefacturation) {
        this.datefacturation = datefacturation;
    }

    public Double getMontanttotalttc() {
        return montanttotalttc;
    }

    public Facture montanttotalttc(Double montanttotalttc) {
        this.montanttotalttc = montanttotalttc;
        return this;
    }

    public void setMontanttotalttc(Double montanttotalttc) {
        this.montanttotalttc = montanttotalttc;
    }

    public String getObservation() {
        return observation;
    }

    public Facture observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Client getClient() {
        return client;
    }

    public Facture client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Caisse getCaisse() {
        return caisse;
    }

    public Facture caisse(Caisse caisse) {
        this.caisse = caisse;
        return this;
    }

    public void setCaisse(Caisse caisse) {
        this.caisse = caisse;
    }

    public User getUser() {
        return user;
    }

    public Facture user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public Facture magasin(Magasin magasin) {
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
        Facture facture = (Facture) o;
        if (facture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), facture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", numfacture='" + getNumfacture() + "'" +
            ", montanttotalht='" + getMontanttotalht() + "'" +
            ", montanttva='" + getMontanttva() + "'" +
            ", reduction='" + getReduction() + "'" +
            ", codebarre='" + getCodebarre() + "'" +
            ", libellefacture='" + getLibellefacture() + "'" +
            ", dateedition='" + getDateedition() + "'" +
            ", datefacturation='" + getDatefacturation() + "'" +
            ", montanttotalttc='" + getMontanttotalttc() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
