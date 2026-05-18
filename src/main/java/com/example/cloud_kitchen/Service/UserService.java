package com.example.cloud_kitchen.Service;

import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.User;
import com.example.cloud_kitchen.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

private  final NotificationService notificationService;

    public Boolean isAdmin(Integer id) {
        User user = userRepository.findUserById(id);
        return user != null && user.getRole().equalsIgnoreCase("ADMIN");
    }

    public List<User> getAllUsers(Integer adminId) {
        if (!isAdmin(adminId)) {
            throw new ApiException("Only Admins can view all users");
        }
        return userRepository.findAll();
    }

    public void addUser(User user) {
        user.setRole("USER");
        userRepository.save(user);
        notificationService.sendWelcomeNotification(user.getId(), user.getRole());
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
        oldUser.setRole(oldUser.getRole());
        userRepository.save(oldUser);
    }
    public void deleteUser(Integer adminId, Integer userId) {
        if (!isAdmin(adminId)) {
            throw new ApiException("only admins can delete users");
        }
        User user = userRepository.findUserById(userId);
        if (user == null) throw new ApiException("User not found");

        notificationService.sendDeleteAccountNotification(userId);

        userRepository.delete(user);
    }




    public User getUserByEmail(Integer adminId, String email) {
        if (!isAdmin(adminId)) {
            throw new ApiException("Only admin can search by email");
        }
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new ApiException("email not found");
        }
        return user;
    }


    public List<User> getAdmins(Integer adminId) {
        if (!isAdmin(adminId)) {
            throw new ApiException("only admins can view all users");
        }
        List<User> admins = userRepository.findUserByRole("ADMIN");
        if (admins.isEmpty()) {
            throw new ApiException("no admins found");
        }
        return admins;
    }


    public List<User> getUsersByAddress(Integer adminId, String address) {
        if (!isAdmin(adminId)) {
            throw new ApiException("Only admin can view users by address");
        }
        List<User> users = userRepository.findUserByAddress(address);
        if (users.isEmpty()) {
            throw new ApiException("no users found");
        }
        return users;
    }


    // to creat the admin when program run
    @PostConstruct
    public void initAdmin() {
        if (userRepository.findUserByRole("ADMIN").isEmpty()) {
            User admin = new User();
            admin.setName("Main Admin");
            admin.setEmail("shuaa.almarwani@gmail.com");
            admin.setPassword("admin123");
            admin.setPhone("966546889463");
            admin.setAddress("Riyadh");
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }

}
