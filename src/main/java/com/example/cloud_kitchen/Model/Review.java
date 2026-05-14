package com.example.cloud_kitchen.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "userId is required")
    private Integer userId;

    @NotNull(message = "orderId is required")
    private Integer orderId;

    @NotNull(message = "vendorId is required")
    private Integer vendorId;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}
