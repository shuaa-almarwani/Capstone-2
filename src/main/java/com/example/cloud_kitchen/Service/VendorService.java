package com.example.cloud_kitchen.Service;

import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.Order;
import com.example.cloud_kitchen.Model.User;
import com.example.cloud_kitchen.Model.Vendor;
import com.example.cloud_kitchen.Repository.OrderRepository;
import com.example.cloud_kitchen.Repository.UserRepository;
import com.example.cloud_kitchen.Repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Value("${apiKey}")
    private String apiKey;


    private void checkAccess(Integer requesterId, Integer vendorId) {
        User requester = userRepository.findUserById(requesterId);
        if (requester == null) throw new ApiException("User not found");


        boolean isAdmin = requester.getRole().equalsIgnoreCase("ADMIN");
        boolean isVendor = requesterId.equals(vendorId);

        if (!isAdmin && !isVendor) {
            throw new ApiException("You must be an Admin or Vendor");
        }
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public void addVendor(Integer adminId, Vendor vendor) {

        User requester = userRepository.findUserById(adminId);

        if (requester == null || !requester.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("Only Admin can add new vendors");
        }

        if (vendor.getCommercialRegistration() == null ||
                vendor.getCommercialRegistration().length() != 10) {
            throw new ApiException("Invalid Commercial Registration Number");
        }

        validateCommercialRegistration(vendor.getCommercialRegistration());

        if (vendor.getAvailable() == null) {
            vendor.setAvailable(true);
        }

        vendorRepository.save(vendor);
    }

    public void updateVendor(Integer requesterId, Integer id, Vendor vendor) {
        checkAccess(requesterId, id);

        Vendor oldVendor = vendorRepository.findVendorById(id);
        if (oldVendor == null) throw new ApiException("Vendor not found");

        oldVendor.setBusinessName(vendor.getBusinessName());
        oldVendor.setType(vendor.getType());
        oldVendor.setLocation(vendor.getLocation());
        oldVendor.setAvailable(vendor.getAvailable());

        vendorRepository.save(oldVendor);
    }

    public void deleteVendor(Integer adminId, Integer id) {
        User requester = userRepository.findUserById(adminId);
        if (requester == null || !requester.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("Only Admin can delete vendors");
        }
        Vendor vendor = vendorRepository.findVendorById(id);
        if (vendor == null) throw new ApiException("Vendor not found");
        vendorRepository.delete(vendor);
    }




    public List<Vendor> getVendorByType(String type) {
        if(vendorRepository.findVendorByType(type) == null) throw new ApiException("Vendor not found");
        return vendorRepository.findVendorByType(type.toUpperCase());
    }

    public List<Vendor> getVendorByLocation(String location) {
        if(vendorRepository.findVendorByLocation(location) == null) throw new ApiException("Vendor not found");
        return vendorRepository.findVendorByLocation(location);
    }

    public List<Vendor> getAvailableVendors() {
        return vendorRepository.findVendorByAvailable(true);
    }

    public void toggleAvailability(Integer requesterId, Integer id) {

        checkAccess(requesterId, id);

        Vendor vendor = vendorRepository.findVendorById(id);
        if (vendor == null) throw new ApiException("Vendor not found");

        if (vendor.getAvailable() == null) {
            vendor.setAvailable(true);
        } else {
            vendor.setAvailable(!vendor.getAvailable());
        }

        vendorRepository.save(vendor);
    }


    // للتحقق من رقم السجل التجاري
    private void validateCommercialRegistration(String cr) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://api.wathq.sa/sandbox/commercial-registration/status/"
                    + cr + "?language=ar";

            HttpHeaders headers = new HttpHeaders();
            headers.set("apiKey", apiKey);
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ApiException("Wathq API failed with status: " + response.getStatusCode());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.getBody());

            String status = json.get("name").asText();

            System.out.println("CR Status from Wathq: " + status);

            if (!"نشط".equals(status)) {
                throw new ApiException("Commercial Registration is not active: " + status);
            }

        } catch (Exception e) {
            throw new ApiException("Wathq API failed: " + e.getMessage());
        }
    }


    public String getDashboardSummary(Integer vendorId) {

        List<Order> orders = orderRepository.findAll()
                .stream()
                .filter(o -> o.getVendorId().equals(vendorId))
                .toList();

        int total = orders.size();

        int newOrders = 0;
        int activeOrders = 0;
        int deliveredOrders = 0;
        int cancelledOrders = 0;
        double revenue = 0;

        for (Order o : orders) {

            if (o.getStatus().equalsIgnoreCase("NEW")) newOrders++;

            if (o.getStatus().equalsIgnoreCase("NEW")
                    || o.getStatus().equalsIgnoreCase("ACCEPTED")
                    || o.getStatus().equalsIgnoreCase("ON_THE_WAY")) {
                activeOrders++;
            }

            if (o.getStatus().equalsIgnoreCase("DELIVERED")) {
                deliveredOrders++;
                revenue += o.getTotalPrice();
            }

            if (o.getStatus().equalsIgnoreCase("CANCELLED")) {
                cancelledOrders++;
            }
        }
        return "TOTAL: " + total +
                " | NEW: " + newOrders +
                " | ACTIVE: " + activeOrders +
                " | DELIVERED: " + deliveredOrders +
                " | CANCELLED: " + cancelledOrders +
                " | REVENUE: " + revenue;
    }

    public List<Vendor> getHighRatings() {

        List<Vendor> vendors = vendorRepository.getTopReviews();

        if (vendors.isEmpty()) {
            throw new ApiException("No top rated vendors found");
        }

        return vendors;
    }
}