package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.Repository.UserInfoRepository;
import com.griddynamics.mainservice.Util.Logger;
import com.griddynamics.mainservice.domain.Order;
import com.griddynamics.mainservice.domain.ProductInfo;
import com.griddynamics.mainservice.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserOrderServiceImpl implements UserOrderService {


    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    OrderSearchService orderSearchService;

    @Autowired
    ProductInfoService productInfoService;

    @Override
    public Flux<Order> getUserOrderWithRelevantProduct(String requestId, String userId) {

        Mono<User> user = userInfoRepository.findById(userId);

        return orderSearchService.getByPhone(user.map(User::getPhone), requestId)
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
                })
                .doOnEach(Logger
                        .logOnNext(v ->
                                log.info("RequestID[" + requestId + "]: Found user full order info: {}", v)));
    }
}
