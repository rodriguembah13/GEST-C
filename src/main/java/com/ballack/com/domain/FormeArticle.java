package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FormeArticle.
 */
@Entity
@Table(name = "forme_article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "formearticle")
public class FormeArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_forme")
    private String nomForme;

    @Column(name = "quantite")
    private Integer quantite;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomForme() {
        return nomForme;
    }

    public FormeArticle nomForme(String nomForme) {
        this.nomForme = nomForme;
        return this;
    }

    public void setNomForme(String nomForme) {
        this.nomForme = nomForme;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public FormeArticle quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
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
        FormeArticle formeArticle = (FormeArticle) o;
        if (formeArticle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formeArticle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FormeArticle{" +
            "id=" + getId() +
            ", nomForme='" + getNomForme() + "'" +
            ", quantite='" + getQuantite() + "'" +
            "}";
    }
}
