package com.example.capstone.service;

import com.example.capstone.domain.*;
import com.example.capstone.repository.MenuRepository;
import com.example.capstone.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Transactional
public class RestaurantService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private RestaurantRepository restaurantRepository;
    private MenuRepository menuRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

    //식당 + 메뉴 등록
    public boolean RestaurantRegister(GetRestaurant getRestaurant, MultipartFile restaurantImg, List<MultipartFile> menuImgList) throws IOException {
        log.debug("debug log={}", "RestaurantRegister메소드");
        if (validateDuplicateRestaurant(getRestaurant.getRestaurantName())) //식당 중복검증
            return false;
        // 식당 저장 객체
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantImgUrl(imgSave(restaurantImg, getRestaurant.getRestaurantName())); // 식당이미지 저장 및 url변환
        restaurant.setRestaurantName(getRestaurant.getRestaurantName());
        restaurant.setRestaurantLocation(getRestaurant.getRestaurantLocation());
        restaurant.setRestaurantOperatingTime(getRestaurant.getRestaurantOperatingTime());
        Restaurant restaurant1 = restaurantRepository.save(restaurant);

        /*
        getRestaurantId(restaurant1) 식당Id 활용
        메뉴 등록 구현 List<restaurantMenu>  완료
        */
        Long restaurantId = restaurant1.getRestaurantId();
        List<GetMenu> menuList = getRestaurant.getMenuList();

        // menuList,imgList 크기만큼 반복
        int count = menuList.size();
        for (int i = 0; i < count; i++) {
            menuRegister(menuList.get(i), menuImgList.get(i), restaurantId); //메뉴등록
        }
        return true;
    }

    //메뉴 등록
    public Long menuRegister(GetMenu getMenu, MultipartFile menuImg, Long restaurantId) throws IOException {
        log.debug("debug log={}", "menuRegister메소드");
        // 저장할 메뉴정보
        Menu menu = new Menu();
        menu.setMenuName(getMenu.getMenuName());
        menu.setMenuPrice(getMenu.getMenuPrice());
        menu.setMenuImgUrl(imgSave(menuImg, restaurantRepository.findByRestaurantId(restaurantId).get().getRestaurantName()));
        menu.setRestaurantId(restaurantId);
        //저장
        menuRepository.save(menu);
        return menu.getMenuId();
    }

    public Boolean validateDuplicateRestaurant(String restaurantName) {  ///식당 중복검증
        Optional<Restaurant> restaurant = restaurantRepository.findByRestaurantName(restaurantName);
        if (restaurant.isPresent()) {
            return true;  // 중복
        } else {
            return false; // 중복X
        }
    }

    // 이미지 저장
    public String imgSave(MultipartFile img, String restaurantName) throws IOException {
        log.debug("debug log={}", "imgSave메소드");
        String fileName = StringUtils.cleanPath(img.getOriginalFilename());
        String fileDirectory = "C:/Users/goddn/capstoneImg/" + restaurantName; // 식당 이름을 디렉토리 이름으로 사용
        String filePath = fileDirectory + "/" + fileName; // DB에 저장될 이미지파일의 최종주소
        try {
            // 디렉토리가 존재하지 않으면 생성
            Files.createDirectories(Paths.get(fileDirectory));
            // 파일 저장
            Files.write(Paths.get(filePath), img.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String fileName = UUID.randomUUID().toString() + ".jpg";
//        File targetFile = new File("C:/Users/goddn/capstoneImg", fileName); //저장되는 위치 (임시:원준집컴퓨터)
//        img.transferTo(targetFile);
//        String imageUrl = "http://localhost:8080/images/" + fileName;
        String imageUrl = filePath;
        log.debug("debug log={}", "imgSave저장완료");
        return imageUrl;
    }

    public Map<String, Object> getImgListAndRestaurantNameList() throws IOException {
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        List<String> imageUrlList = new ArrayList<>();
        List<String> restaurantNameList = new ArrayList<>();

        // 식당객체에서 필요한 태용 추출
        for (Restaurant restaurant : restaurantList) {
            restaurantNameList.add(restaurant.getRestaurantName());
            imageUrlList.add(restaurant.getRestaurantImgUrl());
        }
        // 이미지 리스트
        List<String> imageList = new ArrayList<>();
        // 이미지 byte 변환후 Sting으로 인코딩 후 리스트에 저장
        for (String imageUrl : imageUrlList) {
            File imageFile = new File(imageUrl);
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String imageData = Base64.getEncoder().encodeToString(imageBytes);
            imageList.add(imageData);
        }
        // 보낼정보 map에 저장
        Map<String, Object> data = new HashMap<>();
        data.put("imageList", imageList);
        data.put("restaurantNameList", restaurantNameList);
        return data;
    }



    public ResponseEntity<RestaurantRequest> getRestaurantByName(String restaurantName)throws IOException  {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByRestaurantName(restaurantName);

        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            RestaurantRequest restaurantRequest = new RestaurantRequest();


            restaurantRequest.setRestaurantName(restaurant.getRestaurantName());
            restaurantRequest.setRestaurantLocation(restaurant.getRestaurantLocation());
            restaurantRequest.setRestaurantOperatingTime(restaurant.getRestaurantOperatingTime());

            // 이미지 파일 URL에 GET 요청을 보내어, 이미지 파일을 받아옴
            File imageFile = new File(restaurant.getRestaurantImgUrl());
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String imageData = Base64.getEncoder().encodeToString(imageBytes);

            // 받아온 이미지 파일을 RestaurantRequest 객체의 restaurantIMG 필드에 넣어줌

            restaurantRequest.setRestaurantImg(imageData);

            // Restaurant에 해당하는 메뉴 리스트를 가져옴
            List<Menu> menuList = menuRepository.findListByRestaurantId(restaurant.getRestaurantId());
            // 메뉴 리스트를 MenuRequest 리스트로 변환
            List<MenuRequest> menuRequestList = new ArrayList<>();
            for (Menu menu : menuList) {
                MenuRequest menuRequest = new MenuRequest();
                menuRequest.setMenuName(menu.getMenuName());
                menuRequest.setMenuPrice(menu.getMenuPrice());
                // TODO: Menu의 이미지를 가져와서 MenuRequest에 넣어줌
                File imageMenuFile = new File(menu.getMenuImgUrl());
                byte[] imageMenuBytes = Files.readAllBytes(imageMenuFile.toPath());
                String imageMenuData = Base64.getEncoder().encodeToString(imageMenuBytes);
            }
            restaurantRequest.setMenuList(menuRequestList);

            return ResponseEntity.ok(restaurantRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

// 2023 05 20
    public Map<String, Object> getNameAndImg(String category) throws IOException {
        Map<String, Object> NameAndImg = new HashMap<>();

        List<String> restaurantName= new ArrayList<>();
        List<String> restaurantImg = new ArrayList<>();

        List<Restaurant> restaurants = restaurantRepository.findAllByRestaurantCategory(category);
        for (Restaurant restaurant : restaurants) {
            restaurantName.add(restaurant.getRestaurantName());
            restaurantImg.add(restaurant.getRestaurantImgUrl());
        }
        List<String> imageList = new ArrayList<>();

        for (String imageUrl : restaurantImg) {
            File imageFile = new File(imageUrl);
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String imageData = Base64.getEncoder().encodeToString(imageBytes);
            imageList.add(imageData);
        }
        // 보낼정보 map에 저장
        Map<String, Object> result = new HashMap<>();
        result.put("imageList", imageList);
        result.put("restaurantName", restaurantName);
        return result;
    }




}

