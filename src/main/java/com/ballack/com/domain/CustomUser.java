package com.ballack.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CustomUser.
 */
@Entity
@Table(name = "custom_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customuser")
public class CustomUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isadu_user")
    private Boolean isaduUser;

    @Column(name = "is_vendre")
    private Boolean isVendre;

    @Column(name = "is_approv_stock")
    private Boolean isApprovStock;

    @Column(name = "commander")
    private Boolean commander;

    @Column(name = "is_print_fac")
    private Boolean isPrintFac;

    @Column(name = "isupdate_cmde")
    private Boolean isupdateCmde;

    @Column(name = "isupdate_stck")
    private Boolean isupdateStck;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "view_vente")
    private Boolean viewVente;

    @Column(name = "view_cmde")
    private Boolean viewCmde;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsaduUser() {
        return isaduUser;
    }

    public CustomUser isaduUser(Boolean isaduUser) {
        this.isaduUser = isaduUser;
        return this;
    }

    public void setIsaduUser(Boolean isaduUser) {
        this.isaduUser = isaduUser;
    }

    public Boolean isIsVendre() {
        return isVendre;
    }

    public CustomUser isVendre(Boolean isVendre) {
        this.isVendre = isVendre;
        return this;
    }

    public void setIsVendre(Boolean isVendre) {
        this.isVendre = isVendre;
    }

    public Boolean isIsApprovStock() {
        return isApprovStock;
    }

    public CustomUser isApprovStock(Boolean isApprovStock) {
        this.isApprovStock = isApprovStock;
        return this;
    }

    public void setIsApprovStock(Boolean isApprovStock) {
        this.isApprovStock = isApprovStock;
    }

    public Boolean isCommander() {
        return commander;
    }

    public CustomUser commander(Boolean commander) {
        this.commander = commander;
        return this;
    }

    public void setCommander(Boolean commander) {
        this.commander = commander;
    }

    public Boolean isIsPrintFac() {
        return isPrintFac;
    }

    public CustomUser isPrintFac(Boolean isPrintFac) {
        this.isPrintFac = isPrintFac;
        return this;
    }

    public void setIsPrintFac(Boolean isPrintFac) {
        this.isPrintFac = isPrintFac;
    }

    public Boolean isIsupdateCmde() {
        return isupdateCmde;
    }

    public CustomUser isupdateCmde(Boolean isupdateCmde) {
        this.isupdateCmde = isupdateCmde;
        return this;
    }

    public void setIsupdateCmde(Boolean isupdateCmde) {
        this.isupdateCmde = isupdateCmde;
    }

    public Boolean isIsupdateStck() {
        return isupdateStck;
    }

    public CustomUser isupdateStck(Boolean isupdateStck) {
        this.isupdateStck = isupdateStck;
        return this;
    }

    public void setIsupdateStck(Boolean isupdateStck) {
        this.isupdateStck = isupdateStck;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public CustomUser photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public CustomUser photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getTelephone() {
        return telephone;
    }

    public CustomUser telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Boolean isViewVente() {
        return viewVente;
    }

    public CustomUser viewVente(Boolean viewVente) {
        this.viewVente = viewVente;
        return this;
    }

    public void setViewVente(Boolean viewVente) {
        this.viewVente = viewVente;
    }

    public Boolean isViewCmde() {
        return viewCmde;
    }

    public CustomUser viewCmde(Boolean viewCmde) {
        this.viewCmde = viewCmde;
        return this;
    }

    public void setViewCmde(Boolean viewCmde) {
        this.viewCmde = viewCmde;
    }

    public User getUser() {
        return user;
    }

    public CustomUser user(User user) {
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
        CustomUser customUser = (CustomUser) o;
        if (customUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomUser{" +
            "id=" + getId() +
            ", isaduUser='" + isIsaduUser() + "'" +
            ", isVendre='" + isIsVendre() + "'" +
            ", isApprovStock='" + isIsApprovStock() + "'" +
            ", commander='" + isCommander() + "'" +
            ", isPrintFac='" + isIsPrintFac() + "'" +
            ", isupdateCmde='" + isIsupdateCmde() + "'" +
            ", isupdateStck='" + isIsupdateStck() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + photoContentType + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", viewVente='" + isViewVente() + "'" +
            ", viewCmde='" + isViewCmde() + "'" +
            "}";
    }
}
