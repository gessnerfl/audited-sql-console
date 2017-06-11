package de.gessnerfl.auditedsqlconsole.service.security.auth.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gessnerfl.auditedsqlconsole.config.InvalidConfigurationException;
import de.gessnerfl.auditedsqlconsole.config.security.AuthenticationConfig;
import de.gessnerfl.auditedsqlconsole.config.security.AuthenticationType;
import de.gessnerfl.auditedsqlconsole.config.security.FileAuthenticationConfig;
import de.gessnerfl.auditedsqlconsole.service.security.auth.AuthenticationModuleConfigurer;
import de.gessnerfl.auditedsqlconsole.service.security.auth.RoleType;
import de.gessnerfl.auditedsqlconsole.service.security.auth.file.model.UserModel;
import de.gessnerfl.auditedsqlconsole.utils.json.ObjectMapperFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileAuthenticationModuleConfigurer implements AuthenticationModuleConfigurer {

    private final ResourceLoader resourceLoader;
    private final ObjectMapperFactory objectMapperFactory;
    private final InMemoryStoreUserAppenderFactory inMemoryStoreUserAppenderFactory;
    private final Logger logger;

    @Autowired
    public FileAuthenticationModuleConfigurer(ResourceLoader resourceLoader, ObjectMapperFactory objectMapperFactory, InMemoryStoreUserAppenderFactory inMemoryStoreUserAppenderFactory, Logger logger) {
        this.resourceLoader = resourceLoader;
        this.objectMapperFactory = objectMapperFactory;
        this.inMemoryStoreUserAppenderFactory = inMemoryStoreUserAppenderFactory;
        this.logger = logger;
    }

    @Override
    public AuthenticationType getType() {
        return AuthenticationType.FILE;
    }

    @Override
    public void configure(AuthenticationConfig authenticationConfig, AuthenticationManagerBuilder authenticationManagerBuilder) {
        logger.info("Configure authentication with authentication module FILE");
        try(InputStream inputStream = readFile(authenticationConfig)) {
            Stream<UserModel> userModels = mapFile(inputStream);

            InMemoryStoreUserAppender inMemoryStoreUserAppender = inMemoryStoreUserAppenderFactory.createFor(authenticationManagerBuilder);
            userModels.forEach(m -> configureUser(inMemoryStoreUserAppender, m));
        }catch (IOException e){
            throw new InvalidConfigurationException("Failed to read or map user file to data model", e);
        }
    }

    private InputStream readFile(AuthenticationConfig authenticationConfig) throws IOException {
        if (authenticationConfig.getFile() == null) {
            throw new InvalidConfigurationException("File authentication source configuration settings missing");
        }
        FileAuthenticationConfig fileAuthenticationConfig = authenticationConfig.getFile();
        String location = fileAuthenticationConfig.getLocation();
        logger.info("Read file from location {}", location);
        Resource resource = resourceLoader.getResource(location);
        if(resource.exists()){
            return resource.getInputStream();
        }
        throw new InvalidConfigurationException("No user file exists at the configured location "+location);
    }

    private Stream<UserModel> mapFile(InputStream is) throws IOException {
        ObjectMapper objectMapper = objectMapperFactory.createNew();
        UserModel[] users = objectMapper.readValue(is, UserModel[].class);
        if(users != null){
            return Stream.of(users);
        }
        return Stream.empty();
    }

    private void configureUser(InMemoryStoreUserAppender appender, UserModel m) {
        try {
            m.validate();
            logger.info("Add user {} to in memory store");
            Set<RoleType> roles = new HashSet<>(m.getRoles());
            roles.add(RoleType.USER);

            appender.append(m.getUsername(), m.getPasswordHash(), toGrantedAuthority(roles));
        }catch (ValidationException e){
            logger.error("User configuration of {} not valid; skip adding user; validation error = {}", m, e.getMessage());
        }
    }

    private List<GrantedAuthority> toGrantedAuthority(Collection<RoleType> roles){
        return roles.stream().map(t -> "ROLE_"+t.name()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
