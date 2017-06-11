package de.gessnerfl.auditedsqlconsole.service.security.auth;

import de.gessnerfl.auditedsqlconsole.config.security.AuthenticationConfig;
import de.gessnerfl.auditedsqlconsole.config.security.AuthenticationType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

public interface AuthenticationModuleConfigurer {
    AuthenticationType getType();
    void configure(AuthenticationConfig authenticationConfig, AuthenticationManagerBuilder auth);
}
