package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Decomposition.
 */
@Entity
@Table(name = "decomposition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "decomposition")
public class Decomposition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "articledepart")
    private String articledepart;

    @Column(name = "articlefin")
    private String articlefin;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "etat")
    private Boolean etat;

    @ManyToOne
    private Article article;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticledepart() {
        return articledepart;
    }

    public Decomposition articledepart(String articledepart) {
        this.articledepart = articledepart;
        return this;
    }

    public void setArticledepart(String articledepart) {
        this.articledepart = articledepart;
    }

    public String getArticlefin() {
        return articlefin;
    }

    public Decomposition articlefin(String articlefin) {
        this.articlefin = articlefin;
        return this;
    }

    public void setArticlefin(String articlefin) {
        this.articlefin = articlefin;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public Decomposition quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Boolean isEtat() {
        return etat;
    }

    public Decomposition etat(Boolean etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public Article getArticle() {
        return article;
    }

    public Decomposition article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
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
        Decomposition decomposition = (Decomposition) o;
        if (decomposition.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), decomposition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Decomposition{" +
            "id=" + getId() +
            ", articledepart='" + getArticledepart() + "'" +
            ", articlefin='" + getArticlefin() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", etat='" + isEtat() + "'" +
            "}";
    }
}
