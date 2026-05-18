package com.example.cloud_kitchen.Service;

import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.Driver;
import com.example.cloud_kitchen.Model.User;
import com.example.cloud_kitchen.Repository.DriverRepository;
import com.example.cloud_kitchen.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;

    private final UserRepository userRepository;

    private void checkDriverAccess(Integer requesterId, Integer driverId) {
        User requester = userRepository.findUserById(requesterId);
        if (requester == null) throw new ApiException("User not found");

        boolean isAdmin = requester.getRole().equalsIgnoreCase("ADMIN");
        boolean isDriver = requesterId.equals(driverId);
        if (!isAdmin && !isDriver ) {
            throw new ApiException("Only Admin or the Driver himself can modify this");
        }
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public void addDriver(Integer adminId, Driver driver) {
        User requester = userRepository.findUserById(adminId);
        if (requester == null || !requester.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("Only admin can register new drivers");
        }
        if (driver.getStatus() == null) driver.setStatus("AVAILABLE");
        driverRepository.save(driver);
    }

    public void updateDriver(Integer requesterId, Integer driverId, Driver driver) {
        checkDriverAccess(requesterId, driverId);

        Driver oldDriver = driverRepository.findDriverById(driverId);
        if (oldDriver == null) throw new ApiException("Driver not found");

        oldDriver.setName(driver.getName());
        oldDriver.setPhone(driver.getPhone());
        oldDriver.setVehicleType(driver.getVehicleType());
        driverRepository.save(oldDriver);
    }

    public void deleteDriver(Integer adminId, Integer driverId) {

        User requester = userRepository.findUserById(adminId);
        if (requester == null || !requester.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("Only admin can delete drivers");
        }

        Driver driver = driverRepository.findDriverById(driverId);
        if (driver == null) throw new ApiException("Driver not found");

        driverRepository.delete(driver);
    }



    public List<Driver> getAvailableDrivers() {
        List<Driver> drivers = driverRepository.findDriverByStatus("AVAILABLE");
        if (drivers.isEmpty()) throw new ApiException("No available drivers");
        return drivers;
    }

}
