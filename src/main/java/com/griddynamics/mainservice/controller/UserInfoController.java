package com.griddynamics.mainservice.controller;

import com.griddynamics.mainservice.Repository.UserInfoRepository;
import com.griddynamics.mainservice.domain.Order;
import com.griddynamics.mainservice.domain.ProductInfo;
import com.griddynamics.mainservice.domain.User;
import com.griddynamics.mainservice.service.OrderSearchService;
import com.griddynamics.mainservice.service.ProductInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/userInfoService")
@RequiredArgsConstructor
public class UserInfoController {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    OrderSearchService orderSearchService;

    @Autowired
    ProductInfoService productInfoService;

    @GetMapping(value = "user/all")
    public ResponseEntity<Flux<User>> getAll(){
        System.out.println("yeah");

            return ResponseEntity.ok(userInfoRepository.findAll());
    }

    @GetMapping(value = "user/get", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<Flux<Order>> getProductInfoByUser(@RequestHeader("requestId") String requestId, @RequestParam String userId) {
        Mono<User> user = userInfoRepository.findById(userId);
        Flux<Order> orderFLux = orderSearchService.getByPhone(user.map(User::getPhone), requestId)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(order -> Mono.just(order).zipWith(user, (ord, usr) -> {
                    ord.setUserName(usr.getName());
                    ord.setPhoneNumber(usr.getPhone());
                    return ord;
                }))
                .flatMap(order -> {
                    Mono<ProductInfo> mostRelevant = productInfoService
                            .getMostRelevant(productInfoService
                                    .getByProductCode(order
                                            .getProductCode(), requestId), requestId);
                    return Mono.just(order).zipWith(mostRelevant, (ord, product) -> {
                        ord.setProductName(product.getProductName());
                        ord.setProductId(product.getProductId());
                        return ord;
                    });
                });

        return ResponseEntity.ok(orderFLux);
    }


}