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
    @PostMapping("/add/{adminId}")
    public ResponseEntity<?> addVendor(@PathVariable Integer adminId,
                                       @RequestBody @Valid Vendor vendor,
                                       Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        vendorService.addVendor(adminId, vendor);
        return ResponseEntity.status(201).body(new ApiResponse("Vendor added successfully"));
    }
    @PutMapping("/update/{requesterId}/{vendorId}")
    public ResponseEntity<?> updateVendor(@PathVariable Integer requesterId,
                                          @PathVariable Integer vendorId,
                                          @RequestBody @Valid Vendor vendor,
                                          Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        vendorService.updateVendor(requesterId, vendorId, vendor);
        return ResponseEntity.status(200).body(new ApiResponse("Vendor updated successfully"));
    }
    @DeleteMapping("/delete/{adminId}/{vendorId}")
    public ResponseEntity<?> deleteVendor(@PathVariable Integer adminId, @PathVariable Integer vendorId) {
        vendorService.deleteVendor(adminId, vendorId);
        return ResponseEntity.status(200).body(new ApiResponse("Vendor deleted successfully"));
    }




    // #4
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getVendorByType(@PathVariable String type) {
        return ResponseEntity.status(200).body(vendorService.getVendorByType(type));
    }

    // #5
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableVendors() {
        return ResponseEntity.status(200).body(vendorService.getAvailableVendors());
    }

    // #6
    @GetMapping("/location/{location}")
    public ResponseEntity<?> getVendorByLocation(@PathVariable String location) {
        return ResponseEntity.status(200).body(vendorService.getVendorByLocation(location));
    }

    // #7
    @PutMapping("/toggle/{requesterId}/{vendorId}")
    public ResponseEntity<?> toggleAvailability(@PathVariable Integer requesterId,
                                                @PathVariable Integer vendorId) {
        vendorService.toggleAvailability(requesterId, vendorId);
        return ResponseEntity.status(200).body(new ApiResponse("Vendor availability updated"));
    }

    // #8
    @GetMapping("/dashboard-summary/{vendorId}")
    public ResponseEntity<?> getDashboard(@PathVariable Integer vendorId) {
        return ResponseEntity.ok(vendorService.getDashboardSummary(vendorId));
    }

    // #9
    @GetMapping("/top")
    public ResponseEntity<?> getTopRatedVendors() {
        return ResponseEntity.ok(vendorService.getHighRatings());
    }

}
