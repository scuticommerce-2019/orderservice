package com.scuticommerce.orderservice.service;

import com.scuticommerce.orderservice.model.Order;
import com.scuticommerce.orderservice.model.OrderItem;
import com.scuticommerce.orderservice.repository.OrderItemRepository;
import com.scuticommerce.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public void createOrder(Order order){

        orderRepository.save(order);
    }

    public void createOrderItem(OrderItem orderItem){

        orderItemRepository.save(orderItem);
    }

}
