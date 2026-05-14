package com.example.cloud_kitchen.Service;

import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.Driver;
import com.example.cloud_kitchen.Repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public void addDriver(Driver driver) {

        if (driver.getStatus() == null) {
            driver.setStatus("AVAILABLE");
        }

        driverRepository.save(driver);
    }

    public void updateDriver(Integer id, Driver driver) {

        Driver oldDriver = driverRepository.findDriverById(id);

        if (oldDriver == null) {
            throw new ApiException("driver not found");
        }

        oldDriver.setName(driver.getName());
        oldDriver.setPhone(driver.getPhone());
        oldDriver.setVehicleType(driver.getVehicleType());
        oldDriver.setStatus(driver.getStatus());

        driverRepository.save(oldDriver);
    }

    public void deleteDriver(Integer id) {

        Driver driver = driverRepository.findDriverById(id);

        if (driver == null) {
            throw new ApiException("driver not found");
        }

        driverRepository.delete(driver);
    }

}
