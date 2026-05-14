package com.example.cloud_kitchen.Repository;

import com.example.cloud_kitchen.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {


    User findUserById(Integer id);
}
