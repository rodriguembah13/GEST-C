package com.ballack.com.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to JHipster.
 * <p>
 * Properties are configured in the application.yml file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
public final commande commande= new commande();
    public final facture facture= new facture();
    public ApplicationProperties.commande getCommande() {
        return commande;
    }
    public ApplicationProperties.facture getFacture() {
        return facture;
    }
    public static class facture{
        private String cheminJasper;
        private String cheminImage;

        public facture() {
        }

        public String getCheminJasper() {
            return cheminJasper;
        }

        public void setCheminJasper(String cheminJasper) {
            this.cheminJasper = cheminJasper;
        }

        public String getCheminImage() {
            return cheminImage;
        }

        public void setCheminImage(String cheminImage) {
            this.cheminImage = cheminImage;
        }
    }
    public static class commande{
        private int datelimite;

        public commande() {
        }

        public int getDatelimite() {
            return datelimite;
        }

        public void setDatelimite(int datelimite) {
            this.datelimite = datelimite;
        }
    }

}
