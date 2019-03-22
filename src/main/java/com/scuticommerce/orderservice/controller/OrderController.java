package com.scuticommerce.orderservice.controller;

import com.scuticommerce.orderservice.model.Order;
import com.scuticommerce.orderservice.model.OrderItem;
import com.scuticommerce.orderservice.repository.OrderItemRepository;
import com.scuticommerce.orderservice.repository.OrderRepository;
import com.scuticommerce.orderservice.service.DataIngestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value="/api/orderservice/")
public class OrderController {

    @Autowired
    DataIngestion dataIngestion;

    @Autowired
    OrderRepository repository;
    @Autowired
    OrderItemRepository orderItemRepository;


    @PostMapping(value="/create")
    public ResponseEntity<?> create(@RequestBody Order order){

        repository.save(order);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping(value="/batchCreate")
    public ResponseEntity<?> batchCreate(@RequestBody List<Order> orders){

        for (Order order : orders){
            repository.save(order);
        }

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    /**
     * This path is local path @Todo Convert to SFTP path
     * @param path
     * @return
     */
    @GetMapping(value="/load")
    public ResponseEntity<?> load(@RequestHeader String path, @RequestParam String type){

        String folderPath = path;

        try {
            dataIngestion.importData(folderPath,type.toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @GetMapping(value="/orders")
    public ResponseEntity<?> all(){

       List<Order> orders = repository.findAll();

       int i = 0;
       for (Order order : orders){

           i++;
           if(i == 50) break;
           List<OrderItem> orderItems = orderItemRepository.findByOrderNumber(order.getOrderNumber());
           order.setItems(orderItems);

       }

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(value="/orderItems")
    public ResponseEntity<?> allItems(){

        return new ResponseEntity<>(orderItemRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value="/status")
    public ResponseEntity<?> status(){

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
