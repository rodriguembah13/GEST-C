package com.ballack.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "num_article")
    private String numArticle;

    @Column(name = "poids")
    private Double poids;

    @Column(name = "codebarre")
    private String codebarre;

    @Column(name = "marque")
    private String marque;

    @Column(name = "datecreation")
    private LocalDate datecreation;

    @Column(name = "nomarticle")
    private String nomarticle;

    @Column(name = "prix_courant")
    private Double prixCourant;

    @ManyToOne
    private FamilleArticle familleArticle;

    @ManyToOne
    private FormeArticle formeArticle;

    @OneToOne(mappedBy = "article")
    @JsonIgnore
    private Etiquette etiquette;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumArticle() {
        return numArticle;
    }

    public Article numArticle(String numArticle) {
        this.numArticle = numArticle;
        return this;
    }

    public void setNumArticle(String numArticle) {
        this.numArticle = numArticle;
    }

    public Double getPoids() {
        return poids;
    }

    public Article poids(Double poids) {
        this.poids = poids;
        return this;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public String getCodebarre() {
        return codebarre;
    }

    public Article codebarre(String codebarre) {
        this.codebarre = codebarre;
        return this;
    }

    public void setCodebarre(String codebarre) {
        this.codebarre = codebarre;
    }

    public String getMarque() {
        return marque;
    }

    public Article marque(String marque) {
        this.marque = marque;
        return this;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public LocalDate getDatecreation() {
        return datecreation;
    }

    public Article datecreation(LocalDate datecreation) {
        this.datecreation = datecreation;
        return this;
    }

    public void setDatecreation(LocalDate datecreation) {
        this.datecreation = datecreation;
    }

    public String getNomarticle() {
        return nomarticle;
    }

    public Article nomarticle(String nomarticle) {
        this.nomarticle = nomarticle;
        return this;
    }

    public void setNomarticle(String nomarticle) {
        this.nomarticle = nomarticle;
    }

    public Double getPrixCourant() {
        return prixCourant;
    }

    public Article prixCourant(Double prixCourant) {
        this.prixCourant = prixCourant;
        return this;
    }

    public void setPrixCourant(Double prixCourant) {
        this.prixCourant = prixCourant;
    }

    public FamilleArticle getFamilleArticle() {
        return familleArticle;
    }

    public Article familleArticle(FamilleArticle familleArticle) {
        this.familleArticle = familleArticle;
        return this;
    }

    public void setFamilleArticle(FamilleArticle familleArticle) {
        this.familleArticle = familleArticle;
    }

    public FormeArticle getFormeArticle() {
        return formeArticle;
    }

    public Article formeArticle(FormeArticle formeArticle) {
        this.formeArticle = formeArticle;
        return this;
    }

    public void setFormeArticle(FormeArticle formeArticle) {
        this.formeArticle = formeArticle;
    }

    public Etiquette getEtiquette() {
        return etiquette;
    }

    public Article etiquette(Etiquette etiquette) {
        this.etiquette = etiquette;
        return this;
    }

    public void setEtiquette(Etiquette etiquette) {
        this.etiquette = etiquette;
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
        Article article = (Article) o;
        if (article.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", numArticle='" + getNumArticle() + "'" +
            ", poids='" + getPoids() + "'" +
            ", codebarre='" + getCodebarre() + "'" +
            ", marque='" + getMarque() + "'" +
            ", datecreation='" + getDatecreation() + "'" +
            ", nomarticle='" + getNomarticle() + "'" +
            ", prixCourant='" + getPrixCourant() + "'" +
            "}";
    }
}
