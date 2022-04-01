package com.griddynamics.mainservice.service;

import com.griddynamics.mainservice.dao.OrderSearchServiceDAO;
import com.griddynamics.mainservice.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class OrderSearchServiceImpl implements OrderSearchService {

    @Autowired
    private OrderSearchServiceDAO orderSearchServiceDAO;

    @Override
    public Flux<Order> getByPhone(Mono<String> phone, String requestId) {
        return orderSearchServiceDAO.getByPhone(phone, requestId);
    }
}
