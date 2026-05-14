package com.example.cloud_kitchen.Service;

import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.Vendor;
import com.example.cloud_kitchen.Repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;


    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public void addVendor(Vendor vendor) {

        if (vendor.getAvailable() == null) {
            vendor.setAvailable(true);
        }

        vendorRepository.save(vendor);
    }

    public void updateVendor(Integer id, Vendor vendor) {

        Vendor oldVendor = vendorRepository.findVendorById(id);

        if (oldVendor == null) {
            throw new ApiException("vendor not found");
        }

        oldVendor.setBusinessName(vendor.getBusinessName());
        oldVendor.setType(vendor.getType());
        oldVendor.setLocation(vendor.getLocation());
        oldVendor.setAvailable(vendor.getAvailable());

        vendorRepository.save(oldVendor);
    }

    public void deleteVendor(Integer id) {

        Vendor vendor = vendorRepository.findVendorById(id);

        if (vendor == null) {
            throw new ApiException("vendor not found");
        }

        vendorRepository.delete(vendor);
    }
}