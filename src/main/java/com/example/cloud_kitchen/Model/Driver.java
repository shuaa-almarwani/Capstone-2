package com.example.cloud_kitchen.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "name cannot be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String name;

    @NotEmpty(message = "phone cannot be empty")
    @Column(columnDefinition = "varchar(15) unique not null")
    private String phone;

    @NotEmpty(message = "vehicle type cannot be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String vehicleType;

    @Pattern(regexp = "AVAILABLE|BUSY")
    @Column(columnDefinition = "varchar(20) not null")
    private String status;
}
