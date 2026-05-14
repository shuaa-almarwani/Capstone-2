package com.example.cloud_kitchen.Repository;

import com.example.cloud_kitchen.Model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository   extends JpaRepository<Driver,Integer> {

    Driver findDriverById(Integer id);

}
