package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "quantite_alerte")
    private Integer quantiteAlerte;

    @Column(name = "prix_article")
    private Double prixArticle;

    @Column(name = "dateperemption")
    private LocalDate dateperemption;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "prix_achat")
    private Double prixAchat;

    @Column(name = "observation")
    private String observation;

    @Column(name = "taxe_tva")
    private Double taxeTVA;

    @Column(name = "quantite_gros")
    private Integer quantiteGros;

    @Column(name = "quantite_alerte_gros")
    private Integer quantiteAlerteGros;

    @Column(name = "closed")
    private Boolean closed;

    @ManyToOne
    private Article article;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public Stock quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Integer getQuantiteAlerte() {
        return quantiteAlerte;
    }

    public Stock quantiteAlerte(Integer quantiteAlerte) {
        this.quantiteAlerte = quantiteAlerte;
        return this;
    }

    public void setQuantiteAlerte(Integer quantiteAlerte) {
        this.quantiteAlerte = quantiteAlerte;
    }

    public Double getPrixArticle() {
        return prixArticle;
    }

    public Stock prixArticle(Double prixArticle) {
        this.prixArticle = prixArticle;
        return this;
    }

    public void setPrixArticle(Double prixArticle) {
        this.prixArticle = prixArticle;
    }

    public LocalDate getDateperemption() {
        return dateperemption;
    }

    public Stock dateperemption(LocalDate dateperemption) {
        this.dateperemption = dateperemption;
        return this;
    }

    public void setDateperemption(LocalDate dateperemption) {
        this.dateperemption = dateperemption;
    }

    public Boolean isActif() {
        return actif;
    }

    public Stock actif(Boolean actif) {
        this.actif = actif;
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Double getPrixAchat() {
        return prixAchat;
    }

    public Stock prixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
        return this;
    }

    public void setPrixAchat(Double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public String getObservation() {
        return observation;
    }

    public Stock observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Double getTaxeTVA() {
        return taxeTVA;
    }

    public Stock taxeTVA(Double taxeTVA) {
        this.taxeTVA = taxeTVA;
        return this;
    }

    public void setTaxeTVA(Double taxeTVA) {
        this.taxeTVA = taxeTVA;
    }

    public Integer getQuantiteGros() {
        return quantiteGros;
    }

    public Stock quantiteGros(Integer quantiteGros) {
        this.quantiteGros = quantiteGros;
        return this;
    }

    public void setQuantiteGros(Integer quantiteGros) {
        this.quantiteGros = quantiteGros;
    }

    public Integer getQuantiteAlerteGros() {
        return quantiteAlerteGros;
    }

    public Stock quantiteAlerteGros(Integer quantiteAlerteGros) {
        this.quantiteAlerteGros = quantiteAlerteGros;
        return this;
    }

    public void setQuantiteAlerteGros(Integer quantiteAlerteGros) {
        this.quantiteAlerteGros = quantiteAlerteGros;
    }

    public Boolean isClosed() {
        return closed;
    }

    public Stock closed(Boolean closed) {
        this.closed = closed;
        return this;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Article getArticle() {
        return article;
    }

    public Stock article(Article article) {
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
        Stock stock = (Stock) o;
        if (stock.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stock.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", quantite='" + getQuantite() + "'" +
            ", quantiteAlerte='" + getQuantiteAlerte() + "'" +
            ", prixArticle='" + getPrixArticle() + "'" +
            ", dateperemption='" + getDateperemption() + "'" +
            ", actif='" + isActif() + "'" +
            ", prixAchat='" + getPrixAchat() + "'" +
            ", observation='" + getObservation() + "'" +
            ", taxeTVA='" + getTaxeTVA() + "'" +
            ", quantiteGros='" + getQuantiteGros() + "'" +
            ", quantiteAlerteGros='" + getQuantiteAlerteGros() + "'" +
            ", closed='" + isClosed() + "'" +
            "}";
    }
}
