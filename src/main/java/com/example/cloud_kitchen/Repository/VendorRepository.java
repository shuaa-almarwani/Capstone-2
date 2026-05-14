package com.example.cloud_kitchen.Repository;

import com.example.cloud_kitchen.Model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Vendor findVendorById(Integer id);

}
