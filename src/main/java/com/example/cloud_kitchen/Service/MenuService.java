package com.example.cloud_kitchen.Service;

import com.example.cloud_kitchen.Api.ApiException;
import com.example.cloud_kitchen.Model.Menu;
import com.example.cloud_kitchen.Model.User;
import com.example.cloud_kitchen.Repository.MenuRepository;
import com.example.cloud_kitchen.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    private void checkMenuAccess(Integer requesterId, Integer vendorId) {
        User requester = userRepository.findUserById(requesterId);
        if (requester == null) throw new ApiException("User not found");

        boolean isAdmin = requester.getRole().equalsIgnoreCase("ADMIN");
        boolean isVendor = requesterId.equals(vendorId);

        if (!isAdmin && !isVendor) {
            throw new ApiException(" Only Admin or the Vendor owner can manage this menu");
        }
    }
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public void addMenuItem(Integer requesterId, Menu menu) {
        checkMenuAccess(requesterId, menu.getVendorId());
        menuRepository.save(menu);
    }

    public void updateMenuItem(Integer requesterId, Integer itemId, Menu menu) {
        Menu oldItem = menuRepository.findMenuById(itemId);
        if (oldItem == null) throw new ApiException("Menu item not found");

        checkMenuAccess(requesterId, oldItem.getVendorId());

        oldItem.setName(menu.getName());
        oldItem.setPrice(menu.getPrice());
        oldItem.setDescription(menu.getDescription());
        oldItem.setCategory(menu.getCategory());

        menuRepository.save(oldItem);
    }
    public void deleteMenuItem(Integer requesterId, Integer itemId) {
        Menu item = menuRepository.findMenuById(itemId);
        if (item == null) throw new ApiException("Menu item not found");

        checkMenuAccess(requesterId, item.getVendorId());
        menuRepository.delete(item);
    }



     public List<Menu> getMenuByVendor(Integer vendorId) {
        List<Menu> items = menuRepository.findMenuByVendorId(vendorId);
        if (items.isEmpty()) throw new ApiException("No menu found for this vendor");
        return items;
    }

    public List<Menu> getMenuByCategory(String category) {
        List<Menu> items = menuRepository.findMenuByCategory(category);
        if (items.isEmpty()) throw new ApiException("No items found for this category");
        return items;
    }
}