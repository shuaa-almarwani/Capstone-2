package com.example.cloud_kitchen.Repository;


import com.example.cloud_kitchen.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

    public interface OrderRepository extends JpaRepository<Order, Integer> {

        Order findOrderById(Integer id);

        List<Order> findOrderByStatus(String status);

        @Query("SELECT o FROM Order o WHERE o.userId = ?1 AND o.status IN ('NEW', 'ACCEPTED', 'ON_THE_WAY')")
        List<Order> getActiveOrderByUserId(Integer userId);

        @Query("SELECT o FROM Order o WHERE o.userId = ?1 AND o.status IN ('DELIVERED', 'CANCELLED')")
        List<Order> getPrevOrderByUserId(Integer userId);


        @Query("SELECT o FROM Order o WHERE o.userId = ?1 AND o.vendorId = ?2 AND o.status = ?3")
        Order findFirstByUserIdAndVendorIdAndStatus(Integer userId,
                                                    Integer vendorId,
                                                    String status);
    }
