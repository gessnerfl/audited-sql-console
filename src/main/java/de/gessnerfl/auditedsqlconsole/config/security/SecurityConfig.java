package de.gessnerfl.auditedsqlconsole.config.security;

import javax.validation.constraints.NotNull;

public class SecurityConfig {

    @NotNull
    private AuthenticationConfig auth;

    public AuthenticationConfig getAuth() {
        return auth;
    }

    public void setAuth(AuthenticationConfig auth) {
        this.auth = auth;
    }
}
