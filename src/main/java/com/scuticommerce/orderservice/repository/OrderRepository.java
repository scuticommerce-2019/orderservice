package com.scuticommerce.orderservice.repository;

import com.scuticommerce.model.order.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order,String> {

    List<Order> findByOrderNumberBetween(int greaterThan , int lessThan);

    List<Order> findByItemsNotNull();

    List<Order> findByOrderTotalGreaterThan(BigDecimal greaterThan);

    List<Order> findByOrderNumberIn(List<String> orderNumbers);


}
