package com.griddynamics.mainservice.controller;

import com.griddynamics.mainservice.domain.Order;
import com.griddynamics.mainservice.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/userInfoService")
@RequiredArgsConstructor
public class UserInfoController {

    @Autowired
    private UserOrderService userOrderService;

    @GetMapping(value = "user/get", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public ResponseEntity<Flux<Order>> getProductInfoByUser(@RequestHeader("requestId") String requestId, @RequestParam String userId) {

        return ResponseEntity.ok(userOrderService.getUserOrderWithRelevantProduct(requestId, userId));
    }


}