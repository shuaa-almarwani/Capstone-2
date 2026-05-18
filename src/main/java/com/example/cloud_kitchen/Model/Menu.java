package com.example.cloud_kitchen.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Item name is required")
    @Size(min = 3, message = "Name must be more than 3 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private Double price;

    private String description;

    @NotNull(message = "Vendor ID is required")
    private Integer vendorId;

    @NotNull(message = "category is required")
    @Pattern(
            regexp = "DRINK|MEAL|SWEET",
            message = "category must be DRINK, MEAL or SWEET"
    )
    private String category;

}