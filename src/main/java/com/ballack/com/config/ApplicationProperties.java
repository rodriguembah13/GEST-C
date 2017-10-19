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

    public ApplicationProperties.commande getCommande() {
        return commande;
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
