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
 * A Caisse.
 */
@Entity
@Table(name = "caisse")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "caisse")
public class Caisse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numcaisse")
    private String numcaisse;

    @Column(name = "date_ouverture")
    private Instant dateOuverture;

    @Column(name = "date_fermeture")
    private Instant dateFermeture;

    @Column(name = "fondcaisse")
    private Double fondcaisse;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumcaisse() {
        return numcaisse;
    }

    public Caisse numcaisse(String numcaisse) {
        this.numcaisse = numcaisse;
        return this;
    }

    public void setNumcaisse(String numcaisse) {
        this.numcaisse = numcaisse;
    }

    public Instant getDateOuverture() {
        return dateOuverture;
    }

    public Caisse dateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
        return this;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Instant getDateFermeture() {
        return dateFermeture;
    }

    public Caisse dateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
        return this;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public Double getFondcaisse() {
        return fondcaisse;
    }

    public Caisse fondcaisse(Double fondcaisse) {
        this.fondcaisse = fondcaisse;
        return this;
    }

    public void setFondcaisse(Double fondcaisse) {
        this.fondcaisse = fondcaisse;
    }

    public Boolean isActive() {
        return active;
    }

    public Caisse active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public Caisse user(User user) {
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
        Caisse caisse = (Caisse) o;
        if (caisse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), caisse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Caisse{" +
            "id=" + getId() +
            ", numcaisse='" + getNumcaisse() + "'" +
            ", dateOuverture='" + getDateOuverture() + "'" +
            ", dateFermeture='" + getDateFermeture() + "'" +
            ", fondcaisse='" + getFondcaisse() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
