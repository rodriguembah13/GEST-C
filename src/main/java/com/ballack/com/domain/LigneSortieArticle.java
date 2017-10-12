package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LigneSortieArticle.
 */
@Entity
@Table(name = "ligne_sortie_article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lignesortiearticle")
public class LigneSortieArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "designation")
    private String designation;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "montantht")
    private Double montantht;

    @Column(name = "montanttva")
    private Double montanttva;

    @Column(name = "montantttc")
    private Double montantttc;

    @ManyToOne
    private SortieArticle sortieArticle;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Client client;

    @ManyToOne
    private TypeSortieArticle typeSortieArticle;

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

    public LigneSortieArticle designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public LigneSortieArticle quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getMontantht() {
        return montantht;
    }

    public LigneSortieArticle montantht(Double montantht) {
        this.montantht = montantht;
        return this;
    }

    public void setMontantht(Double montantht) {
        this.montantht = montantht;
    }

    public Double getMontanttva() {
        return montanttva;
    }

    public LigneSortieArticle montanttva(Double montanttva) {
        this.montanttva = montanttva;
        return this;
    }

    public void setMontanttva(Double montanttva) {
        this.montanttva = montanttva;
    }

    public Double getMontantttc() {
        return montantttc;
    }

    public LigneSortieArticle montantttc(Double montantttc) {
        this.montantttc = montantttc;
        return this;
    }

    public void setMontantttc(Double montantttc) {
        this.montantttc = montantttc;
    }

    public SortieArticle getSortieArticle() {
        return sortieArticle;
    }

    public LigneSortieArticle sortieArticle(SortieArticle sortieArticle) {
        this.sortieArticle = sortieArticle;
        return this;
    }

    public void setSortieArticle(SortieArticle sortieArticle) {
        this.sortieArticle = sortieArticle;
    }

    public Article getArticle() {
        return article;
    }

    public LigneSortieArticle article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Client getClient() {
        return client;
    }

    public LigneSortieArticle client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public TypeSortieArticle getTypeSortieArticle() {
        return typeSortieArticle;
    }

    public LigneSortieArticle typeSortieArticle(TypeSortieArticle typeSortieArticle) {
        this.typeSortieArticle = typeSortieArticle;
        return this;
    }

    public void setTypeSortieArticle(TypeSortieArticle typeSortieArticle) {
        this.typeSortieArticle = typeSortieArticle;
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
        LigneSortieArticle ligneSortieArticle = (LigneSortieArticle) o;
        if (ligneSortieArticle.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ligneSortieArticle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LigneSortieArticle{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", montantht='" + getMontantht() + "'" +
            ", montanttva='" + getMontanttva() + "'" +
            ", montantttc='" + getMontantttc() + "'" +
            "}";
    }
}
