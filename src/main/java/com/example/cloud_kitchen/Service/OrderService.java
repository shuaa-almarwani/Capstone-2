package com.example.cloud_kitchen.Service;


import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.Driver;
import com.example.cloud_kitchen.Model.Menu;
import com.example.cloud_kitchen.Model.Order;
import com.example.cloud_kitchen.Model.Vendor;
import com.example.cloud_kitchen.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final DriverRepository driverRepository;
    private final NotificationService notificationService;
    private final MenuRepository menuRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void addOrder(Order order) {

        if (userRepository.findUserById(order.getUserId()) == null) {
            throw new ApiException("user not found");
        }

        Vendor vendor = vendorRepository.findVendorById(order.getVendorId());
        if (vendor == null) {
            throw new ApiException("Vendor not found");
        }

        if (!vendor.getAvailable()) {
            throw new ApiException("This vendor is not available right now");
        }

        double total = 0;

        if (order.getMenuItemIds() == null || order.getMenuItemIds().isEmpty()) {
            throw new ApiException("Menu items are required");
        }

        for (Integer menuId : order.getMenuItemIds()) {

            Menu menu = menuRepository.findMenuById(menuId);

            if (menu == null) {
                throw new ApiException("Menu not found: " + menuId);
            }

            // is the menu for the vendor?
            if (!menu.getVendorId().equals(vendor.getId())) {
                throw new ApiException("Menu item does not belong to this vendor");
            }

            total += menu.getPrice();
        }

        order.setTotalPrice(total);
        order.setStatus("NEW");

        orderRepository.save(order);
    }

    public void updateOrder(Integer id, Order order) {

        Order oldOrder = orderRepository.findOrderById(id);

        if (oldOrder == null) {
            throw new ApiException("Order not found");
        }

        if (order.getMenuItemIds() != null) {
            for (Integer itemId : order.getMenuItemIds()) {
                oldOrder.getMenuItemIds().add(itemId);
            }
        }
        if (order.getTotalPrice() != null)
            oldOrder.setTotalPrice(order.getTotalPrice());

        if (order.getPaymentMethod() != null)
            oldOrder.setPaymentMethod(order.getPaymentMethod());

        orderRepository.save(oldOrder);
    }

    public void deleteOrder(Integer id) {

        Order order = orderRepository.findOrderById(id);

        if (order == null) {
            throw new ApiException("order not found");
        }

        orderRepository.delete(order);
    }


     public List<Order> getOrdersByStatus(String status) {

        List<Order> orders = orderRepository.findOrderByStatus(status);

        if (orders.isEmpty()) {
            throw new ApiException("no orders found");
        }

        return orders;
    }

     public void assignDriver(Integer orderId, Integer driverId) {

        Order order = orderRepository.findOrderById(orderId);
        if (order == null) {
            throw new ApiException("order not found");
        }

        if (!order.getStatus().equals("NEW")) {
            throw new ApiException("Driver can only be assigned to NEW orders");
        }

        Driver driver = driverRepository.findDriverById(driverId);
        if (driver == null) {
            throw new ApiException("driver not found");
        }

        if (driver.getStatus().equalsIgnoreCase("BUSY")) {
            throw new ApiException("driver status is BUSY");
        }

        order.setDriverId(driverId);
        order.setStatus("ACCEPTED");

        driver.setStatus("BUSY");
        driverRepository.save(driver);

        orderRepository.save(order);
    }

     public void changeStatus(Integer orderId, String status) {

        Order order = orderRepository.findOrderById(orderId);
        if (order == null) {
            throw new ApiException("Order not found");
        }

        if (!(status.equalsIgnoreCase("NEW") ||
                status.equalsIgnoreCase("ACCEPTED") ||
                status.equalsIgnoreCase("ON_THE_WAY") ||
                status.equalsIgnoreCase("DELIVERED") ||
                status.equalsIgnoreCase("CANCELLED"))) {
            throw new ApiException("Invalid status");
        }

        String current = order.getStatus().toUpperCase();
        String next = status.toUpperCase();

        if (current.equals("NEW") &&
                !(next.equals("ACCEPTED") || next.equals("CANCELLED"))) {
            throw new ApiException("NEW orders can only become ACCEPTED or CANCELLED");
        }

        if (current.equals("ACCEPTED") &&
                !(next.equals("ON_THE_WAY") || next.equals("CANCELLED"))) {
            throw new ApiException("ACCEPTED orders can only become ON_THE_WAY or CANCELLED");
        }

        if (current.equals("ON_THE_WAY") &&
                !next.equals("DELIVERED")) {
            throw new ApiException("ON_THE_WAY orders can only become DELIVERED");
        }

        if (current.equals("DELIVERED") || current.equals("CANCELLED")) {
            throw new ApiException("Finalized orders cannot change status");
        }

        order.setStatus(status.toUpperCase());

        if (status.equalsIgnoreCase("DELIVERED") && order.getDriverId() != null) {

            Driver driver = driverRepository.findDriverById(order.getDriverId());

            if (driver != null) {
                driver.setStatus("AVAILABLE");
                driverRepository.save(driver);
            }
            notificationService.sendInvoiceNotification(order.getUserId(), order.getId(), order.getTotalPrice());
        }
        orderRepository.save(order);
        notificationService.sendOrderUpdate(
                order.getUserId(),
                orderId,
                "Order status changed to " + status.toUpperCase()
        );
    }


     public void cancelOrder(Integer userId, Integer orderId) {

        Order order = orderRepository.findOrderById(orderId);

        if (order == null) {
            throw new ApiException("Order not found");
        }
        if (!order.getUserId().equals(userId)) {
            throw new ApiException("You are not allowed to cancel this order");
        }
        if (order.getStatus().equalsIgnoreCase("NEW")) {

            order.setStatus("CANCELLED");
            if (order.getDriverId() != null) {
                Driver driver =
                        driverRepository.findDriverById(order.getDriverId());

                if (driver != null) {
                    driver.setStatus("AVAILABLE");
                    driverRepository.save(driver);
                }
            }

            orderRepository.save(order);
            return;
        }

        throw new ApiException(
                "Can't cancel this order, its status is " + order.getStatus()
        );
    }



    public List<Order> getActiveOrders(Integer userId) {
        if (userRepository.findUserById(userId) == null) {
            throw new ApiException("user not found");
        }

        return orderRepository.getActiveOrderByUserId(userId);
    }

     public List<Order> getPrevOrders(Integer userId) {
        if (userRepository.findUserById(userId) == null) {
            throw new ApiException("user not found");
        }

        return orderRepository.getPrevOrderByUserId(userId);
    }


}