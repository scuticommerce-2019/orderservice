package com.scuticommerce.orderservice.repository;

import com.scuticommerce.orderservice.model.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderItemRepository extends MongoRepository<OrderItem,String> {
}
