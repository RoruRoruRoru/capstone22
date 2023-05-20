package com.example.capstone.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// 저장할 클래스
@Entity
public class Restaurant{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long restaurantId;  //id
    private String restaurantImgUrl;   //식당 이미지 주소
    private String restaurantName;   // 식당이름 정보
    private String restaurantLocation;// 식당 위치 정보
    private String restaurantOperatingTime; // 운영시간 정보
    private String restaurantCategory;


    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantImgUrl() {
        return restaurantImgUrl;
    }

    public void setRestaurantImgUrl(String restaurantImgUrl) {
        this.restaurantImgUrl = restaurantImgUrl;
    }

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
    public String getRestaurantCategory() {return restaurantCategory;
    }

    public void setRestaurantCategory(String restaurantCategory) {this.restaurantCategory = restaurantCategory;
    }

//    private List<RestaurantMenu> menuList; // 추가할메뉴리스트 저장시 위에 값들만 저장

}
