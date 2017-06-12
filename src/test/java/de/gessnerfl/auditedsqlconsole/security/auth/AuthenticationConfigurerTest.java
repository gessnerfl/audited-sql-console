package de.gessnerfl.auditedsqlconsole.security.auth;

import de.gessnerfl.auditedsqlconsole.config.CustomConfigurationProperties;
import de.gessnerfl.auditedsqlconsole.config.security.AuthenticationConfig;
import de.gessnerfl.auditedsqlconsole.config.security.AuthenticationType;
import de.gessnerfl.auditedsqlconsole.config.security.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationConfigurerTest {

    @Mock
    private CustomConfigurationProperties customConfigurationProperties;
    @Mock
    private SecurityConfig securityConfig;
    @Mock
    private AuthenticationConfig authenticationConfig;
    @Mock
    private AuthenticationModuleConfigurer authenticationModuleConfigurer;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private AuthenticationConfigurer sut;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        sut = new AuthenticationConfigurer(customConfigurationProperties, Collections.singletonList(authenticationModuleConfigurer));

        when(customConfigurationProperties.getSecurity()).thenReturn(securityConfig);
        when(securityConfig.getAuth()).thenReturn(authenticationConfig);
        when(authenticationConfig.getType()).thenReturn(AuthenticationType.FILE);
    }

    @Test
    public void shouldTriggerModuleConfigurationWhenAvailableForGivenType() throws Exception {
        when(authenticationModuleConfigurer.getType()).thenReturn(AuthenticationType.FILE);

        sut.configure(authenticationManagerBuilder);

        verify(authenticationModuleConfigurer).configure(authenticationConfig, authenticationManagerBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoModuleConfigurerIsAvailableForTheGivenType() throws Exception {
        when(authenticationModuleConfigurer.getType()).thenReturn(null);

        sut.configure(authenticationManagerBuilder);
    }

}