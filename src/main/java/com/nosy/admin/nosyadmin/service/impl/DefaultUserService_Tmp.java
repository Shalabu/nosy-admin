package com.nosy.admin.nosyadmin.service.impl;

import com.nosy.admin.nosyadmin.config.security.KeycloakClient;
import com.nosy.admin.nosyadmin.exceptions.GeneralException;
import com.nosy.admin.nosyadmin.model.User;
import com.nosy.admin.nosyadmin.repository.UserRepository;
import com.nosy.admin.nosyadmin.service.UserService_Tmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("default_user_service")
public class DefaultUserService_Tmp implements UserService_Tmp {
    private final UserRepository userRepository;
    private final KeycloakClient keycloakClient;

    @Autowired
    public DefaultUserService_Tmp(UserRepository userRepository, KeycloakClient keycloakClient) {
        this.userRepository = userRepository;
        this.keycloakClient = keycloakClient;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findById(email);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public Optional<User> createUser(User user) {
        if (!isValidPassword(user.getPassword()))
            throw new GeneralException("Password is not valid.");
        if (!keycloakClient.registerNewUser(user))
            // The method registerNewUser must throw the concrete error.
            // This message is not always true, because if the keycloak is unavailable
            // this message does not reflect the real cause.
            throw new GeneralException("User already exists in database please try another email");

        // @TODO what should we do when the operation fails? The user was already added to keycloak.
        User newUser = userRepository.saveAndFlush(user);
        return (newUser != null) ? Optional.of(newUser) : Optional.empty();
    }

    @Override
    public Optional<User> updateUser(String email, User user) {
        // not implemented yet
        return Optional.empty();
    }

    @Override
    public Optional<User> deleteUserByEmail(String email) {
        Optional<User> deletedUser = getUserByEmail(email);
        // @TODO this method should return success or error.
        keycloakClient.deleteUsername(email);
        userRepository.deleteById(email);
        return deletedUser;
    }

    // @TODO Can we do this validation in another place?
    private boolean isValidPassword(String password) {
        return null != password && password.length() > 1;
    }
}
