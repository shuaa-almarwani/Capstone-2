package com.example.cloud_kitchen.Controller;

import com.example.cloud_kitchen.Api.ApiResponse;
import com.example.cloud_kitchen.Model.Menu;
import com.example.cloud_kitchen.Service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;


    @GetMapping("/get")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    // @PostMapping("/add/{requesterId}")
    // public ResponseEntity<?> addMenuItem(@PathVariable Integer requesterId,
    //                                      @RequestBody @Valid Menu menu,
    //                                      Errors errors) {
    //     if (errors.hasErrors()) {
    //         return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
    //     }
    //     menuService.addMenuItem(requesterId, menu);
    //     return ResponseEntity.status(201).body(new ApiResponse("Menu item added successfully"));
    // }
    @PostMapping("/add/{requesterId}/{vendorId}")
public ResponseEntity<?> addMenuItem(@PathVariable Integer requesterId,
                                     @PathVariable Integer vendorId,
                                     @RequestBody @Valid Menu menu,
                                     Errors errors) {

    if (errors.hasErrors()) {
        return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
    }

    menuService.addMenuItem(requesterId, vendorId, menu);
    return ResponseEntity.status(201).body(new ApiResponse("Menu item added successfully"));
}

    @PutMapping("/update/{requesterId}/{itemId}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Integer requesterId,
                                            @PathVariable Integer itemId,
                                            @RequestBody @Valid Menu menu,
                                            Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        menuService.updateMenuItem(requesterId, itemId, menu);
        return ResponseEntity.status(200).body(new ApiResponse("Menu item updated successfully"));
    }

    @DeleteMapping("/delete/{requesterId}/{itemId}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Integer requesterId,
                                            @PathVariable Integer itemId) {
        menuService.deleteMenuItem(requesterId, itemId);
        return ResponseEntity.status(200).body(new ApiResponse("Menu item deleted successfully"));
    }


    // #20

    @GetMapping("/get/{vendorId}")
    public ResponseEntity<?> getMenuByVendor(@PathVariable Integer vendorId) {
        return ResponseEntity.status(200).body(menuService.getMenuByVendor(vendorId));
    }

    // #21
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getMenuByCategory(@PathVariable String category) {
        return ResponseEntity.status(200).body(menuService.getMenuByCategory(category));
    }
}
