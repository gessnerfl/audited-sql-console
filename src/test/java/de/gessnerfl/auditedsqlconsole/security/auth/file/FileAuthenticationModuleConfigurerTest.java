package de.gessnerfl.auditedsqlconsole.security.auth.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gessnerfl.auditedsqlconsole.config.InvalidConfigurationException;
import de.gessnerfl.auditedsqlconsole.config.security.AuthenticationConfig;
import de.gessnerfl.auditedsqlconsole.config.security.FileAuthenticationConfig;
import de.gessnerfl.auditedsqlconsole.security.auth.RoleType;
import de.gessnerfl.auditedsqlconsole.security.auth.file.model.UserModel;
import de.gessnerfl.auditedsqlconsole.utils.json.ObjectMapperFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileAuthenticationModuleConfigurerTest {
    private final static String FILE_PATH = "/content/security/users.json";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    @Mock
    private ResourceLoader resourceLoader;
    @Mock
    private ObjectMapperFactory objectMapperFactory;
    @Mock
    private Logger logger;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AuthenticationConfig authenticationConfig;
    @Mock
    private FileAuthenticationConfig fileAuthenticationConfig;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private InMemoryStoreUserAppenderFactory inMemoryStoreUserAppenderFactory;
    @Mock
    private InMemoryStoreUserAppender inMemoryStoreUserAppender;

    @InjectMocks
    private FileAuthenticationModuleConfigurer sut;

    @Before
    public void init() throws Exception {
        when(inMemoryStoreUserAppenderFactory.createFor(authenticationManagerBuilder)).thenReturn(inMemoryStoreUserAppender);
        when(objectMapperFactory.createNew()).thenReturn(objectMapper);
    }

    @Test
    public void shouldConfigureUsersInInMemoryStore() throws Exception {
        final UserModel user = mock(UserModel.class);
        final InputStream is = mock(InputStream.class);
        final Resource resource = mock(Resource.class);

        when(user.getUsername()).thenReturn(USERNAME);
        when(user.getPasswordHash()).thenReturn(PASSWORD);
        when(user.getRoles()).thenReturn(Arrays.asList(RoleType.USER, RoleType.APPROVER));

        when(authenticationConfig.getFile()).thenReturn(fileAuthenticationConfig);
        when(fileAuthenticationConfig.getLocation()).thenReturn(FILE_PATH);
        when(resourceLoader.getResource(FILE_PATH)).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(is);
        when(objectMapper.readValue(is, UserModel[].class)).thenReturn(new UserModel[]{user});

        sut.configure(authenticationConfig, authenticationManagerBuilder);

        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(inMemoryStoreUserAppender).append(eq(USERNAME), eq(PASSWORD), listArgumentCaptor.capture());
        List<GrantedAuthority> value = listArgumentCaptor.getValue();
        assertThat(value, hasItems(new SimpleGrantedAuthority("ROLE_APPROVER"), new SimpleGrantedAuthority("ROLE_USER")));

        verifyNoMoreInteractions(inMemoryStoreUserAppender);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void shouldThrowExceptionWhenNoFileAuthenticationConfigurationIsAvailable() throws Exception {
        when(authenticationConfig.getFile()).thenReturn(null);

        sut.configure(authenticationConfig, authenticationManagerBuilder);
    }

    @Test(expected = InvalidConfigurationException.class)
    public void shouldThrowExceptionWhenUserFileDoesNotExists() throws Exception{
        final Resource resource = mock(Resource.class);

        when(authenticationConfig.getFile()).thenReturn(fileAuthenticationConfig);
        when(fileAuthenticationConfig.getLocation()).thenReturn(FILE_PATH);
        when(resourceLoader.getResource(FILE_PATH)).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        sut.configure(authenticationConfig, authenticationManagerBuilder);
    }

    @Test
    public void shouldSilentlyContinueWhenNoUserIsDefined() throws Exception {
        final InputStream is = mock(InputStream.class);
        final Resource resource = mock(Resource.class);

        when(authenticationConfig.getFile()).thenReturn(fileAuthenticationConfig);
        when(fileAuthenticationConfig.getLocation()).thenReturn(FILE_PATH);
        when(resourceLoader.getResource(FILE_PATH)).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(is);
        when(objectMapper.readValue(is, UserModel[].class)).thenReturn(null);

        sut.configure(authenticationConfig, authenticationManagerBuilder);

        verify(inMemoryStoreUserAppender, never()).append(anyString(), anyString(), anyListOf(GrantedAuthority.class));
    }

    @Test
    public void shouldSilentlyContinueWhenWhenEmptyUserArrayMapped() throws Exception {
        final InputStream is = mock(InputStream.class);
        final Resource resource = mock(Resource.class);

        when(authenticationConfig.getFile()).thenReturn(fileAuthenticationConfig);
        when(fileAuthenticationConfig.getLocation()).thenReturn(FILE_PATH);
        when(resourceLoader.getResource(FILE_PATH)).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(is);
        when(objectMapper.readValue(is, UserModel[].class)).thenReturn(new UserModel[0]);

        sut.configure(authenticationConfig, authenticationManagerBuilder);

        verify(inMemoryStoreUserAppender, never()).append(anyString(), anyString(), anyListOf(GrantedAuthority.class));
    }
}