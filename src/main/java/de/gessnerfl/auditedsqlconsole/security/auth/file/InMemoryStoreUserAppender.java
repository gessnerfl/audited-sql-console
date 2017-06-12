package de.gessnerfl.auditedsqlconsole.security.auth.file;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class InMemoryStoreUserAppender {

    final InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryUserDetailsManagerConfigurer;

    public InMemoryStoreUserAppender(InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryUserDetailsManagerConfigurer) {
        this.inMemoryUserDetailsManagerConfigurer = inMemoryUserDetailsManagerConfigurer;
    }

    public void append(String username, String password, List<GrantedAuthority> authorities){
        inMemoryUserDetailsManagerConfigurer.withUser(username).password(password).authorities(authorities);
    }
}
