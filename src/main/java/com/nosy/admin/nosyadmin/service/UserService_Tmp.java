package com.nosy.admin.nosyadmin.service;

import com.nosy.admin.nosyadmin.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService_Tmp {

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    Optional<User> createUser(User user);

    Optional<User> updateUser(String email, User user);

    Optional<User> deleteUserByEmail(String email);
}
