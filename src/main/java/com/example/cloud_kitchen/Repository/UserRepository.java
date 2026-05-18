package com.example.cloud_kitchen.Repository;

import com.example.cloud_kitchen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {


    User findUserById(Integer id);

    User findUserByEmail(String email);

    List<User> findUserByRole(String role);

    List<User> findUserByAddress(String address);
}
