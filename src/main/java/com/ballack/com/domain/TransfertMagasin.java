package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TransfertMagasin.
 */
@Entity
@Table(name = "transfert_magasin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "transfertmagasin")
public class TransfertMagasin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "date_tranfert")
    private LocalDate date_tranfert;

    @ManyToOne
    private Magasin magasin1;

    @ManyToOne
    private Magasin magasin2;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public TransfertMagasin libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public TransfertMagasin quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDate_tranfert() {
        return date_tranfert;
    }

    public TransfertMagasin date_tranfert(LocalDate date_tranfert) {
        this.date_tranfert = date_tranfert;
        return this;
    }

    public void setDate_tranfert(LocalDate date_tranfert) {
        this.date_tranfert = date_tranfert;
    }

    public Magasin getMagasin1() {
        return magasin1;
    }

    public TransfertMagasin magasin1(Magasin magasin) {
        this.magasin1 = magasin;
        return this;
    }

    public void setMagasin1(Magasin magasin) {
        this.magasin1 = magasin;
    }

    public Magasin getMagasin2() {
        return magasin2;
    }

    public TransfertMagasin magasin2(Magasin magasin) {
        this.magasin2 = magasin;
        return this;
    }

    public void setMagasin2(Magasin magasin) {
        this.magasin2 = magasin;
    }

    public User getUser() {
        return user;
    }

    public TransfertMagasin user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        TransfertMagasin transfertMagasin = (TransfertMagasin) o;
        if (transfertMagasin.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transfertMagasin.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransfertMagasin{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", date_tranfert='" + getDate_tranfert() + "'" +
            "}";
    }
}
