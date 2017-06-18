package de.gessnerfl.auditedsqlconsole.domain.database.connection.model;

import org.springframework.data.annotation.Id;

public class DatabaseConnection {
    @Id
    private String id;
    private String name;
    private String jdbcConnectionUrl;
    private String username;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJdbcConnectionUrl() {
        return jdbcConnectionUrl;
    }

    public void setJdbcConnectionUrl(String jdbcConnectionUrl) {
        this.jdbcConnectionUrl = jdbcConnectionUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
