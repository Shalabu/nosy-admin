package com.nosy.admin.nosyadmin.service.impl;

import com.nosy.admin.nosyadmin.config.security.KeycloakClient;
import com.nosy.admin.nosyadmin.exceptions.GeneralException;
import com.nosy.admin.nosyadmin.model.User;
import com.nosy.admin.nosyadmin.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestDefaultService_Tmp {
    @InjectMocks
    DefaultUserService_Tmp userService;

    @Mock
    UserRepository userRepository;
    @Mock
    KeycloakClient keycloakClient;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createGetUserByIdEmptyTest() {
        String email = "an@email.com";

        // mock
        when(userRepository.findById(email)).thenReturn(Optional.empty());

        // test
        Optional<User> resUser = userService.getUserByEmail(email);

        assertFalse(resUser.isPresent());
        verify(userRepository, times(1)).findById(email);
    }

    @Test
    public void createGetUserByEmailTest() {
        String email = "an@email.com";
        User aUser = new User() {{
            setEmail(email);
            setFirstName("first");
            setLastName("last");
            setInfo("info");
            setPassword("password");
        }};

        // mock
        when(userRepository.findById(email)).thenReturn(Optional.of(aUser));

        // test
        Optional<User> resUser = userService.getUserByEmail(email);

        assertNotNull(resUser.get());
        assertTrue(resUser.isPresent());
        assertEquals(aUser.toString(), resUser.get().toString());
        verify(userRepository, times(1)).findById(email);
    }

    @Test
    public void getAllUsersEmptyTest() {
        // mock
        when(userRepository.findAll()).thenReturn(Lists.emptyList());

        // test
        List<User> listUser = userService.getAllUsers();
        assertTrue(listUser.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getAllUsersTest() {
        List<User> aList = Lists.list(
                new User() {{
                    setEmail("an@email.com");
                    setFirstName("first");
                    setLastName("last");
                    setInfo("info");
                    setPassword("password");
                }},
                new User() {{
                    setEmail("an1@email.com");
                    setFirstName("first1");
                    setLastName("last1");
                    setInfo("info1");
                    setPassword("password1");
                }}
        );

        // mock
        when(userRepository.findAll()).thenReturn(aList);

        // test
        List<User> listUser = userService.getAllUsers();
        assertFalse(listUser.isEmpty());
        assertEquals(2, listUser.size());
        assertEquals(aList, listUser);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void createUserInvalidPassTest() {
        User aUser = new User();
        expectedException.expect(GeneralException.class);
        expectedException.expectMessage("Password is not valid.");

        userService.createUser(aUser);
    }

    @Test
    public void createUserKeycloakFailTest() {
        User aUser = new User() {{
            setPassword("password");
        }};
        expectedException.expect(GeneralException.class);
        expectedException.expectMessage("try another email");

        // mock
        when(keycloakClient.registerNewUser(aUser)).thenReturn(false);

        userService.createUser(aUser);
        verify(keycloakClient, times(1)).registerNewUser(aUser);
    }

    @Test
    public void createUserDbFailTest() {
        User aUser = new User() {{
            setPassword("password");
        }};

        // mock
        when(keycloakClient.registerNewUser(aUser)).thenReturn(false);
        when(userRepository.saveAndFlush(aUser)).thenReturn(null);

        Optional<User> newUser = userService.createUser(aUser);

        assertFalse(newUser.isPresent());
        assertNull(newUser.get());

        verify(keycloakClient, times(1)).registerNewUser(aUser);
        verify(userRepository, times(1)).saveAndFlush(aUser);
    }

    @Test
    public void createUserTest() {
        User aUser = new User() {{
            setEmail("an@email.com");
            setPassword("password");
            setFirstName("first");
            setLastName("last");
            setInfo("info");
        }};
        expectedException.expect(GeneralException.class);
        expectedException.expectMessage("try another email");

        // mock
        when(keycloakClient.registerNewUser(aUser)).thenReturn(false);
        when(userRepository.saveAndFlush(aUser)).thenReturn(aUser);

        Optional<User> newUser = userService.createUser(aUser);

        assertTrue(newUser.isPresent());
        assertNotNull(newUser.get());
        assertEquals(aUser, newUser.get());
        verify(keycloakClient, times(1)).registerNewUser(aUser);
        verify(userRepository, times(1)).saveAndFlush(aUser);
    }

    @Test
    public void deleteUserFailTest() {
        String email = "an@email.com";

        // mock
        when(userRepository.findById(email)).thenReturn(Optional.empty());

        // test
        Optional<User> delUser = userService.deleteUser(email);

        assertFalse(delUser.isPresent());
        verify(userRepository, times(1)).findById(email);
        doNothing().when(keycloakClient).deleteUsername(email);
        doNothing().when(userRepository).deleteById(email);
    }

    @Test
    public void deleteUserTest() {
        String email = "an@email.com";
        User aUser = new User() {{
            setEmail("an@email.com");
            setPassword("password");
            setFirstName("first");
            setLastName("last");
            setInfo("info");
        }};

        // mock
        when(userRepository.findById(email)).thenReturn(Optional.of(aUser));

        // test
        Optional<User> delUser = userService.deleteUser(email);

        assertTrue(delUser.isPresent());
        assertEquals(aUser, delUser.get());
        verify(userRepository, times(1)).findById(email);
        verify(userRepository, times(1)).deleteById(email);
        verify(keycloakClient, times(1)).deleteUsername(email);
    }
}