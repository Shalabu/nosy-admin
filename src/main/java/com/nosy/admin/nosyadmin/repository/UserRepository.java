package com.nosy.admin.nosyadmin.repository;

import com.nosy.admin.nosyadmin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
