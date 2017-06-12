package de.gessnerfl.auditedsqlconsole.security.auth.file.model;

import de.gessnerfl.auditedsqlconsole.security.auth.RoleType;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    private String username;
    private String passwordHash;
    private List<RoleType> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<RoleType> getRoles() {
        if(roles == null){
            roles = new ArrayList<>();
        }
        return roles;
    }

    public void setRoles(List<RoleType> roles) {
        this.roles = roles;
    }

    public void validate(){
        if(StringUtils.isBlank(username)){
            throw new ValidationException("Username is missing");
        }
        if(StringUtils.isBlank(passwordHash)){
            throw new ValidationException("Password hash is missing");
        }
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", roles=[" + StringUtils.join(getRoles(), ",") + "]"+
                '}';
    }
}
