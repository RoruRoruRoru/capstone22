package com.example.capstone.domain;

import jakarta.persistence.*;

import java.util.List;

public class RestaurantRequest {

    private String restaurantName;
    private String restaurantLocation;
    private String restaurantOperatingTime;
    private String restaurantImg;


    public List<MenuRequest> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuRequest> menuList) {
        this.menuList = menuList;
    }

    private List<MenuRequest> menuList;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(String restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public String getRestaurantOperatingTime() {
        return restaurantOperatingTime;
    }

    public void setRestaurantOperatingTime(String restaurantOperatingTime) {
        this.restaurantOperatingTime = restaurantOperatingTime;

    }

    public String getRestaurantImg() {
        return restaurantImg;
    }

    public void setRestaurantImg(String restaurantImg) {
        this.restaurantImg = restaurantImg;
    }


}
