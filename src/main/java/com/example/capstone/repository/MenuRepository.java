package com.example.capstone.repository;

import com.example.capstone.domain.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    Menu save(Menu menu);
    Optional<Menu> findByMenuId(Long menuId);
    Optional<Menu> findByMenuPrice(String menuPrice);
    Optional<Menu> findByMenuImgUrl(String menuImgUrl);
   // Optional<RestaurantMenu> findByRestaurantId(String restaurantId);
    List<Menu> findAll();
    List<Menu> findListByRestaurantId(Long restaurantId);
}
