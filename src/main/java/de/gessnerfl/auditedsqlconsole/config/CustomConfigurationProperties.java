package de.gessnerfl.auditedsqlconsole.config;

import de.gessnerfl.auditedsqlconsole.config.security.SecurityConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "de.gessnerfl.audited.sql.console")
public class CustomConfigurationProperties {

    @NotNull
    private SecurityConfig security;

    public SecurityConfig getSecurity() {
        return security;
    }

    public void setSecurity(SecurityConfig security) {
        this.security = security;
    }
}
