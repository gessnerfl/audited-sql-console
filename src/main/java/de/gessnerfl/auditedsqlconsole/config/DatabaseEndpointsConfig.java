package de.gessnerfl.auditedsqlconsole.config;

import javax.validation.constraints.NotNull;

public class DatabaseEndpointsConfig {
    @NotNull
    private String configFileLocation;

    public String getConfigFileLocation() {
        return configFileLocation;
    }

    public void setConfigFileLocation(String configFileLocation) {
        this.configFileLocation = configFileLocation;
    }
}
