package com.scuticommerce.orderservice.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderItem {

    private List<OrderItem> orderItems;
    String orderNumber;
    String SKU;

}
