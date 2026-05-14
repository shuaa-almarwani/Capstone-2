package com.example.cloud_kitchen.Service;

import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.User;
import com.example.cloud_kitchen.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {


        user.setRole("USER");
        userRepository.save(user);

    }

    public void updateUser(Integer id, User user) {

        User oldUser = userRepository.findUserById(id);

        if (oldUser == null) {
            throw new ApiException("user not found");
        }

        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setAddress(user.getAddress());
        oldUser.setPhone(user.getPhone());
        userRepository.save(oldUser);
    }

    public void deleteUser(Integer id) {

        User user = userRepository.findUserById(id);

        if (user == null) {
            throw new ApiException("user not found");
        }

        userRepository.delete(user);
    }

}
