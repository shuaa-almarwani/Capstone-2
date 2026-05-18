package com.example.cloud_kitchen.Repository;

import com.example.cloud_kitchen.Model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Menu findMenuById(Integer id);

    List<Menu> findMenuByVendorId(Integer vendorId);

    @Query("SELECT m FROM Menu m WHERE m.category = ?1")
    List<Menu> findMenuByCategory(String category);

}