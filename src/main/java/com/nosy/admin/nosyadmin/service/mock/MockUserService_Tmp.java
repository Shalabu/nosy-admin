package com.nosy.admin.nosyadmin.service.mock;

import com.google.common.collect.Lists;
import com.nosy.admin.nosyadmin.exceptions.GeneralException;
import com.nosy.admin.nosyadmin.model.User;
import com.nosy.admin.nosyadmin.service.UserService_Tmp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("mock_user_service")
public class MockUserService_Tmp implements UserService_Tmp {
    private static final List<User> DB = Lists.newArrayList();

    @Override
    public Optional<User> getUserByEmail(String email) {
        return DB.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return DB;
    }

    @Override
    public Optional<User> createUser(User user) {
        Optional<User> aUser = getUserByEmail(user.getEmail());
        if (aUser.isPresent()) {
            throw new GeneralException(String.format("User %s already exists", user.getEmail()));
        }
        DB.add(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(String email, User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> deleteUserByEmail(String email) {
        Optional<User> delUser = getUserByEmail(email);
        if (!delUser.isPresent())
            throw new GeneralException(String.format("User %s does not exists", email));

        if (!DB.remove(delUser.get())) {
            throw new GeneralException("Error removing user.");
        }
        return delUser;
    }
}
