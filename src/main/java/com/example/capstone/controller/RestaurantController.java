package com.example.capstone.controller;

import com.example.capstone.domain.GetRestaurant;
import com.example.capstone.domain.RestaurantRequest;
import com.example.capstone.service.RestaurantService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestaurantController {
    private final RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    //식당등록
    @PostMapping("/RestaurantRegister")   //클라이언트에서 받아올 변수 GetRestaurant,restaurantImg,menuImgList
    public ResponseEntity<String> restaurantRegister(
                                                    @RequestParam(value = "restaurant") String restaurantRequestBody,
//                                                       @RequestParam(value = "restaurant") RestaurantRequest restaurantRequest,
                                                     @RequestParam(value = "restaurantImg" ,required = false)MultipartFile restaurantImg,
                                                     @RequestParam(value = "menuImgList", required = false)List<MultipartFile> menuImgList)throws IOException{
//        throws IOException
        System.out.println("들어옴");
        Gson gson = new Gson();
        GetRestaurant getRestaurant = gson.fromJson(restaurantRequestBody, GetRestaurant.class);
        if(restaurantService.RestaurantRegister(getRestaurant,restaurantImg,menuImgList)){
            return ResponseEntity.ok().body("success");  //식당등록 완료
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 식당 이미지,이름 리스트 전송
    @GetMapping(path = "/getRestaurantData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getRestaurantData() throws IOException {
        Map<String, Object> imgListAndRestaurantNameList = restaurantService.getImgListAndRestaurantNameList();

        return ResponseEntity.ok()
                .body(imgListAndRestaurantNameList);
    }




    @GetMapping("/{restaurantName}")
    public ResponseEntity<RestaurantRequest> getRestaurantByName(@PathVariable String restaurantName) throws IOException{
        return restaurantService.getRestaurantByName(restaurantName);
    }

    // 2023 05 20
    @GetMapping(path = "/getCategorySearchData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getNameAndImg(@RequestParam("category") String category) throws IOException {
        Map<String, Object> NameAndImg = restaurantService.getNameAndImg(category);

        return ResponseEntity.ok()
                .body(NameAndImg);
    }
}
