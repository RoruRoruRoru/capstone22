package com.example.capstone.repository;

import com.example.capstone.domain.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findByRestaurantId(Long restaurantId);
    Optional<Restaurant> findByRestaurantImgUrl(String RestaurantImgUrl);
    Optional<Restaurant> findByRestaurantName(String restaurantName);
    Optional<Restaurant> findByRestaurantLocation(String restaurantLocation);
    Optional<Restaurant> findByRestaurantOperatingTime(String operatingTime);
    List<Restaurant> findAll();
    List<Restaurant> findAllByRestaurantCategory(String category);

}
