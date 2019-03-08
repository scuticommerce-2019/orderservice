package com.scuticommerce.orderservice.repository;

import com.scuticommerce.orderservice.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order,String> {
}
