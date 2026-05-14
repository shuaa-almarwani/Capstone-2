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
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "business name can not be empty")
    @Column(columnDefinition = "varchar(50) not null")
    private String businessName;

    @Pattern(
            regexp = "HOME_KITCHEN|FOOD_TRUCK|CHEF",
            message = "type must be HOME_KITCHEN or FOOD_TRUCK or CHEF"
    )
    @Column(columnDefinition = "varchar(20) not null")
    private String type;

    @Column(columnDefinition = "varchar(100)")
    private String location;

    @Column(columnDefinition = "boolean default true")
    private Boolean available;

}