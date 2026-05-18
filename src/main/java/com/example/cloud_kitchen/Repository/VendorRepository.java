package com.example.cloud_kitchen.Repository;

import com.example.cloud_kitchen.Model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Vendor findVendorById(Integer id);

    List<Vendor> findVendorByType(String type);

    List<Vendor> findVendorByAvailable(Boolean available);

    List<Vendor> findVendorByLocation(String location);

    @Query("""
    SELECT v
    FROM Vendor v
    JOIN Review r ON r.vendorId = v.id
    GROUP BY v.id, v.businessName, v.type, v.location, v.phone, v.available, v.commercialRegistration
    HAVING AVG(r.rating) >= 4
""")
    List<Vendor> getTopReviews();

}
