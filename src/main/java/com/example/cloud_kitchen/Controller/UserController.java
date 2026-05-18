package com.example.cloud_kitchen.Controller;

import com.example.cloud_kitchen.Api.ApiResponse;
import com.example.cloud_kitchen.Model.User;
import com.example.cloud_kitchen.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/get/{adminId}")
    public ResponseEntity<?> getAllUsers(@PathVariable Integer adminId) {
        return ResponseEntity.status(200).body(userService.getAllUsers(adminId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        userService.addUser(user);
        return ResponseEntity.status(201).body(new ApiResponse("User added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @RequestBody @Valid User user,
                                        Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        userService.updateUser(id, user);
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete/{adminId}/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer adminId, @PathVariable Integer userId) {
        userService.deleteUser(adminId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }




    // #1
    @GetMapping("/email/{adminId}/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable Integer adminId, @PathVariable String email) {
        return ResponseEntity.status(200).body(userService.getUserByEmail(adminId, email));
    }
    // #2
    @GetMapping("/admins/{adminId}")
    public ResponseEntity<?> getAdmins(@PathVariable Integer adminId) {
        return ResponseEntity.status(200).body(userService.getAdmins(adminId));
    }
    // #3
    @GetMapping("/address/{adminId}/{address}")
    public ResponseEntity<?> getUsersByAddress(@PathVariable Integer adminId, @PathVariable String address) {
        return ResponseEntity.status(200).body(userService.getUsersByAddress(adminId, address));
    }



}
