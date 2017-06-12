package de.gessnerfl.auditedsqlconsole.security.auth.file.model;

import org.junit.Test;

import javax.validation.ValidationException;

public class UserModelTest {

    @Test
    public void shouldValidateConsistentModel(){
        UserModel sut = new UserModel();
        sut.setUsername("username");
        sut.setPasswordHash("passwordHash");

        sut.validate();
    }

    @Test(expected = ValidationException.class)
    public void shouldFailToValidateModelWhenUsernameIsMissing(){
        UserModel sut = new UserModel();
        sut.setPasswordHash("passwordHash");

        sut.validate();
    }

    @Test(expected = ValidationException.class)
    public void shouldFailToValidateModelWhenPasswordIsMissing(){
        UserModel sut = new UserModel();
        sut.setUsername("username");

        sut.validate();
    }

}