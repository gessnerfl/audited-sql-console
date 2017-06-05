package de.gessnerfl.auditedsqlconsole.security.auth.file;

import de.gessnerfl.auditedsqlconsole.security.auth.AuthenticationConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStoreUserAppenderFactory {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public InMemoryStoreUserAppenderFactory(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public InMemoryStoreUserAppender createFor(AuthenticationManagerBuilder authenticationManagerBuilder) {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryUserDetailsManagerConfigurer = getInMemoryUserDetailsManagerConfigurer(authenticationManagerBuilder);
        inMemoryUserDetailsManagerConfigurer.passwordEncoder(bCryptPasswordEncoder);
        return new InMemoryStoreUserAppender(inMemoryUserDetailsManagerConfigurer);
    }

    private InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> getInMemoryUserDetailsManagerConfigurer(AuthenticationManagerBuilder authenticationManagerBuilder) {
        try {
            return authenticationManagerBuilder.inMemoryAuthentication();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get InMemoryUserDetailsManagerConfigurer", e);
        }
    }

}
