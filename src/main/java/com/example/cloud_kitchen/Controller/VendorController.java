package com.example.cloud_kitchen.Controller;

import com.example.cloud_kitchen.Api.ApiResponse;
import com.example.cloud_kitchen.Model.Vendor;
import com.example.cloud_kitchen.Service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor

public class VendorController {
    private final VendorService vendorService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllVendors() {

        return ResponseEntity.status(200).body(vendorService.getAllVendors());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addVendor(@RequestBody @Valid Vendor vendor,
                                       Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        vendorService.addVendor(vendor);

        return ResponseEntity.status(201).body(new ApiResponse("vendor added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVendor(@PathVariable Integer id,
                                          @RequestBody @Valid Vendor vendor,
                                          Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        vendorService.updateVendor(id, vendor);

        return ResponseEntity.status(200).body(new ApiResponse("vendor updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVendor(@PathVariable Integer id) {

        vendorService.deleteVendor(id);

        return ResponseEntity.status(200).body(new ApiResponse("vendor deleted successfully"));
    }



}
