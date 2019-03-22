package com.scuticommerce.orderservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Order {

    @Id
    String id;
    String email;
    String orderNumber;
    String dateEntered;
    String orderTotal;
    String itemTotal;
    String taxTotal;
    String shippingTotal;
    String handlingTotal;
    String status;
    String shipDate;
    String trackingNumber;
    String shippingMethod;
    String couponCode;
    String discountTotal;
    List<OrderItem> items;

}
