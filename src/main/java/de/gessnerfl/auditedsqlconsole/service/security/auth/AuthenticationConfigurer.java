package de.gessnerfl.auditedsqlconsole.service.security.auth;

import de.gessnerfl.auditedsqlconsole.config.CustomConfigurationProperties;
import de.gessnerfl.auditedsqlconsole.config.security.AuthenticationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Component
public class AuthenticationConfigurer {

    private final CustomConfigurationProperties customConfigurationProperties;
    private final List<AuthenticationModuleConfigurer> moduleConfigurers;

    @Autowired
    public AuthenticationConfigurer(CustomConfigurationProperties customConfigurationProperties, List<AuthenticationModuleConfigurer> moduleConfigurers) {
        this.customConfigurationProperties = customConfigurationProperties;
        this.moduleConfigurers = moduleConfigurers;
    }

    public void configure(AuthenticationManagerBuilder auth) {
        AuthenticationModuleConfigurer moduleConfigurer = getModuleConfigurer();
        moduleConfigurer.configure(customConfigurationProperties.getSecurity().getAuth(), auth);
    }

    private AuthenticationModuleConfigurer getModuleConfigurer() {
        AuthenticationType type = customConfigurationProperties.getSecurity().getAuth().getType();
        return moduleConfigurers.stream()
                .filter(c -> c.getType() == type)
                .findFirst()
                .orElseThrow(noModuleConfigurer(type));
    }

    private Supplier<IllegalArgumentException> noModuleConfigurer(AuthenticationType type) {
        return () -> new IllegalArgumentException("No module authentication module available for type " + type);
    }
}
