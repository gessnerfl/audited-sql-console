package de.gessnerfl.auditedsqlconsole.config.security;

import javax.validation.constraints.NotNull;

public class AuthenticationConfig {

    @NotNull
    private AuthenticationType type = AuthenticationType.FILE;
    private FileAuthenticationConfig file;

    public AuthenticationType getType() {
        return type;
    }

    public void setType(AuthenticationType type) {
        this.type = type;
    }

    public FileAuthenticationConfig getFile() {
        return file;
    }

    public void setFile(FileAuthenticationConfig file) {
        this.file = file;
    }

}
