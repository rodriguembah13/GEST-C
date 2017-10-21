package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Etiquette.
 */
@Entity
@Table(name = "etiquette")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "etiquette")
public class Etiquette implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "etiquette")
    private String etiquette;

    @Column(name = "code_bare")
    private String codeBare;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Column(name = "date_crea")
    private ZonedDateTime dateCrea;

    @OneToOne
    @JoinColumn(unique = true)
    private Article article;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtiquette() {
        return etiquette;
    }

    public Etiquette etiquette(String etiquette) {
        this.etiquette = etiquette;
        return this;
    }

    public void setEtiquette(String etiquette) {
        this.etiquette = etiquette;
    }

    public String getCodeBare() {
        return codeBare;
    }

    public Etiquette codeBare(String codeBare) {
        this.codeBare = codeBare;
        return this;
    }

    public void setCodeBare(String codeBare) {
        this.codeBare = codeBare;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public Etiquette dateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ZonedDateTime getDateCrea() {
        return dateCrea;
    }

    public Etiquette dateCrea(ZonedDateTime dateCrea) {
        this.dateCrea = dateCrea;
        return this;
    }

    public void setDateCrea(ZonedDateTime dateCrea) {
        this.dateCrea = dateCrea;
    }

    public Article getArticle() {
        return article;
    }

    public Etiquette article(Article article) {
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
        Etiquette etiquette = (Etiquette) o;
        if (etiquette.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), etiquette.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Etiquette{" +
            "id=" + getId() +
            ", etiquette='" + getEtiquette() + "'" +
            ", codeBare='" + getCodeBare() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateCrea='" + getDateCrea() + "'" +
            "}";
    }
}
