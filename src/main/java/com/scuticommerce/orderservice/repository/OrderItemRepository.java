package com.scuticommerce.orderservice.repository;

import com.scuticommerce.orderservice.model.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderItemRepository extends MongoRepository<OrderItem,String> {

    List<OrderItem> findByOrderNumber(String id);
}
