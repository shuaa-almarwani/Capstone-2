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

    @PostMapping("/add")
    public ResponseEntity<?> addDriver(@RequestBody @Valid Driver driver, Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        driverService.addDriver(driver);
        return ResponseEntity.ok(new ApiResponse("driver added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDriver(@PathVariable Integer id,
                                          @RequestBody @Valid Driver driver,
                                          Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        driverService.updateDriver(id, driver);
        return ResponseEntity.ok(new ApiResponse("driver updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Integer id) {

        driverService.deleteDriver(id);
        return ResponseEntity.ok(new ApiResponse("driver deleted successfully"));
    }

}

