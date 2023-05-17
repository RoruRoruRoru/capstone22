package com.example.capstone.repository;

import com.example.capstone.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuJpaRepository extends JpaRepository<Menu, Long>, MenuRepository {
    @Override
    Optional<Menu> findByMenuId(Long menuId);
    @Override
    Optional<Menu> findByMenuPrice(String menuPrice);
    @Override
    Optional<Menu> findByMenuImgUrl(String menuImgUrl);
    @Override
    List<Menu> findListByRestaurantId(Long restaurantId);
    //Optional<RestaurantMenu> findByRestaurantId(String restaurantId);
}
