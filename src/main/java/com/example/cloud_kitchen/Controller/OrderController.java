package com.example.cloud_kitchen.Controller;


import com.example.cloud_kitchen.Api.ApiResponse;
import com.example.cloud_kitchen.Model.Order;
import com.example.cloud_kitchen.Service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // @PostMapping("/add")
    // public ResponseEntity<?> addOrder(@RequestBody @Valid Order order, Errors errors) {

    //     if (errors.hasErrors()) {
    //         return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
    //     }

    //     orderService.addOrder(order);
    //     return ResponseEntity.ok(new ApiResponse("order created"));
    // }
    @PostMapping("/add/{userId}/{vendorId}")
public ResponseEntity<?> addOrder(@PathVariable Integer userId,
                                  @PathVariable Integer vendorId,
                                  @RequestBody @Valid Order order,
                                  Errors errors) {

    if (errors.hasErrors()) {
        return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
    }

    orderService.addOrder(userId, vendorId, order);
    return ResponseEntity.ok(new ApiResponse("order created"));
}

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer id,
                                         @RequestBody @Valid Order order,
                                         Errors errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        orderService.updateOrder(id, order);
        return ResponseEntity.ok(new ApiResponse("order updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {

        orderService.deleteOrder(id);
        return ResponseEntity.ok(new ApiResponse("order deleted"));
    }





    // #13
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    // #14
    @PutMapping("/assign/{orderId}/{driverId}")
    public ResponseEntity<?> assignDriver(@PathVariable Integer orderId,
                                          @PathVariable Integer driverId) {

        orderService.assignDriver(orderId, driverId);
        return ResponseEntity.ok(new ApiResponse("driver assigned"));
    }

    // #15
    @PutMapping("/status/{orderId}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable Integer orderId,
                                          @PathVariable String status) {
        orderService.changeStatus(orderId, status);
        return ResponseEntity.status(200).body( new ApiResponse("status updated"));
    }

    // #16
    @GetMapping("{userId}/active")
    public ResponseEntity<?> getActiveOrders(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.getActiveOrders(userId));
    }
    // #17
    @GetMapping("{userId}/prev")
    public ResponseEntity<?> getPrevOrders(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.getPrevOrders(userId));
    }

    // #18
    @PutMapping("/{userId}/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable Integer userId,
            @PathVariable Integer orderId
    ) {
        orderService.cancelOrder(userId, orderId);
        return ResponseEntity.ok("Order cancelled");
    }

}
