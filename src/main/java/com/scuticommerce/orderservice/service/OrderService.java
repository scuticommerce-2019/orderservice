package com.scuticommerce.orderservice.service;

import com.scuticommerce.orderservice.model.Order;
import com.scuticommerce.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void createOrder(Order order){

        orderRepository.save(order);
    }

}
