package com.example.cloud_kitchen.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Integer driverId; //

    @NotNull(message = "product name is required")
    private String productName;

    @NotNull(message = "price is required")
    private Double price;

    private String status; // NEW - ACCEPTED - ON_THE_WAY - DELIVERED - CANCELLED

    private String paymentMethod; // CASH - CARD

}