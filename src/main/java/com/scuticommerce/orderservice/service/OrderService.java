package com.scuticommerce.orderservice.service;

import com.scuticommerce.model.order.Order;
import com.scuticommerce.model.order.OrderItem;
import com.scuticommerce.orderservice.repository.OrderItemRepository;
import com.scuticommerce.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Order> getOrders(Pageable pageable){

        Page<Order> orders = orderRepository.findAll(pageable);

        return orders;
    }

}
