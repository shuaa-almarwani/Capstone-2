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
    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }


    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        userService.addUser(user);

        return ResponseEntity.status(201).body(new ApiResponse("user added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @RequestBody @Valid User user,
                                        Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        userService.updateUser(id, user);

        return ResponseEntity.status(200)
                .body(new ApiResponse("user updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {

        userService.deleteUser(id);

        return ResponseEntity.status(200)
                .body(new ApiResponse("user deleted successfully"));
    }


}
