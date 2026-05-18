package com.example.cloud_kitchen.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "userId is required")
    private Integer userId;

    @NotNull(message = "vendorId is required")
    private Integer vendorId;
    @NotNull(message = "menuItemIds is required")
    private List<Integer> menuItemIds;

    private Integer driverId;


    //    @NotNull(message = "totaPrice is required")
    private Double totalPrice;


    private String status;
    // NEW ، ACCEPTED ، ON_THE_WAY ، DELIVERED ، CANCELLED
    private String paymentMethod;

}