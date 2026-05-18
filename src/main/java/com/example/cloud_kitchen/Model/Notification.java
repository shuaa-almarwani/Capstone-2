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
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "userId is required")
    private Integer userId;

    //    @NotNull(message = "orderId is required")
    private Integer orderId;

    @NotNull(message = "message is required")
    private String message;

    @NotNull(message = "type is required")
    private String type;

    private Boolean sentByEmail = false;

    private Boolean sentByWhatsApp = false;
}
