package com.example.cloud_kitchen.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name can not be empty")
    @Size(min = 3 , message = "name must be more than 2 characters")
    @Column(columnDefinition = "varchar(20) not null")
    private String name;

    @NotEmpty(message = "email can not be empty")
    @Email(message = "invalid email format")
    @Column(columnDefinition = "varchar(50) unique")
    private String email;

    @NotEmpty(message = "password can not be empty")
    @Size(min = 6 , message = "password must be more than 5 characters")
    @Column(columnDefinition = "varchar(100) not null")
    private String password;

    @NotEmpty(message = "address can not be empty")
    @Column(columnDefinition = "varchar(100) not null")
    private String address;

    @Pattern(regexp = "USER|ADMIN", message = "role must be USER or ADMIN")
    @Column(columnDefinition = "varchar(10) not null")
    private String role;

}