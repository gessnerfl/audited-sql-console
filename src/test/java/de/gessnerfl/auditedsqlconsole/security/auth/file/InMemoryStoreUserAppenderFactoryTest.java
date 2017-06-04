package de.gessnerfl.auditedsqlconsole.security.auth.file;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryStoreUserAppenderFactoryTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryUserDetailsManagerConfigurer;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @InjectMocks
    private InMemoryStoreUserAppenderFactory sut;

    @Before
    public void init() throws Exception {
        when(authenticationManagerBuilder.inMemoryAuthentication()).thenReturn(inMemoryUserDetailsManagerConfigurer);
    }

    @Test
    public void shouldConfigureBCryptPasswordEncoderAndReturnThenAppenderInstance() throws Exception {
        InMemoryStoreUserAppender result = sut.createFor(authenticationManagerBuilder);

        assertNotNull(result);
        assertEquals(inMemoryUserDetailsManagerConfigurer, result.inMemoryUserDetailsManagerConfigurer);
        verify(inMemoryUserDetailsManagerConfigurer).passwordEncoder(bCryptPasswordEncoder);
    }

}