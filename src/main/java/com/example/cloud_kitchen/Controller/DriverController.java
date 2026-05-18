package com.example.cloud_kitchen.Controller;

import com.example.cloud_kitchen.Api.ApiResponse;
import com.example.cloud_kitchen.Model.Driver;
import com.example.cloud_kitchen.Service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @PostMapping("/add/{adminId}")
    public ResponseEntity<?> addDriver(@PathVariable Integer adminId,
                                       @RequestBody @Valid Driver driver,
                                       Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        driverService.addDriver(adminId, driver);
        return ResponseEntity.ok(new ApiResponse("Driver added successfully by Admin"));
    }

    @PutMapping("/update/{requesterId}/{driverId}")
    public ResponseEntity<?> updateDriver(@PathVariable Integer requesterId,
                                          @PathVariable Integer driverId,
                                          @RequestBody @Valid Driver driver,
                                          Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        driverService.updateDriver(requesterId, driverId, driver);
        return ResponseEntity.ok(new ApiResponse("Driver updated successfully"));
    }

    @DeleteMapping("/delete/{adminId}/{driverId}")
    public ResponseEntity<?> deleteDriver(@PathVariable Integer adminId, @PathVariable Integer driverId) {
        driverService.deleteDriver(adminId, driverId);
        return ResponseEntity.ok(new ApiResponse("Driver deleted successfully"));
    }




    // #22
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }
}

