package de.gessnerfl.auditedsqlconsole.config.security;

import javax.validation.constraints.NotNull;

public class FileAuthenticationConfig {
    @NotNull
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
