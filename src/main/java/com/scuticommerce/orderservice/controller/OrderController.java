package com.scuticommerce.orderservice.controller;

import com.scuticommerce.model.order.Order;
import com.scuticommerce.model.order.OrderItem;
import com.scuticommerce.orderservice.repository.OrderItemRepository;
import com.scuticommerce.orderservice.repository.OrderRepository;
import com.scuticommerce.orderservice.service.DataIngestion;
import com.scuticommerce.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    OrderService orderService;


    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody Order order) {

        repository.save(order);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    @PostMapping(value = "/batchCreate")
    public ResponseEntity<?> batchCreate(@RequestBody List<Order> orders) {

        for (Order order : orders) {
            repository.save(order);
        }

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    /**
     * This path is local path @Todo Convert to SFTP path
     *
     * @param path
     * @return
     */
    @GetMapping(value = "/load")
    public ResponseEntity<?> load(@RequestHeader String path, @RequestParam String type) {

        String folderPath = path;

        try {
            dataIngestion.importData(folderPath, type.toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @GetMapping(value = "/orders")
    public ResponseEntity<?> all(@RequestParam(value="page", required = false) Integer page ,
                                 @RequestParam(value="size", required = false) Integer size) {

        Page<Order> orders = getOrders(page, size);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    private Page<Order> getOrders( Integer page,
                                   Integer size) {

        Pageable pageable = PageRequest.of(page != null ? page : 0, size != null ? size : 50,
                Sort.by("orderTotal").descending());

        return orderService.getOrders(pageable);

    }

    @GetMapping(value = "/orderItems")
    public ResponseEntity<?> allItems() {

        return new ResponseEntity<>(orderItemRepository.findAll(), HttpStatus.OK);
    }
    @GetMapping(value = "/orderWithItems")
    public ResponseEntity<?> orderWithItems() {

        return new ResponseEntity<>(repository.findByItemsNotNull(), HttpStatus.OK);
    }

    @GetMapping(value = "/orderGreaterThan")
    public ResponseEntity<?> orderTotalGreaterThan(@RequestParam BigDecimal amount) {

        return new ResponseEntity<>(repository.findByOrderTotalGreaterThan(amount), HttpStatus.OK);
    }

    @GetMapping(value = "/ordersIn")
    public ResponseEntity<?> ordersIn(@RequestParam String orderNumbers) {

        List<String> orderNums = Arrays.asList(orderNumbers.split(","));

        return new ResponseEntity<>(repository.findByOrderNumberIn(orderNums), HttpStatus.OK);
    }

    @GetMapping(value = "/status")
    public ResponseEntity<?> status() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/combo")
    public ResponseEntity<?> combine() {

        List<OrderItem> items = orderItemRepository.findAll();
        List<Order> orders = repository.findAll();

        orders.parallelStream().map(o -> updateOrderItems(o,items)).collect(Collectors.toList());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    private Order updateOrderItems(Order order, List<OrderItem> items) {

       Map<String,ArrayList<OrderItem>> map = new HashMap<>();

       for (OrderItem item: items) {
           if (item!= null && item.getOrderNumber() != null && order !=null
                   && order.getOrderNumber() != null) {
               if (item.getOrderNumber().equalsIgnoreCase(order.getOrderNumber())) {
                   putKeyToMapGeneric(map, item, order.getOrderNumber());
               }
           }
       }

       order.setItems(map.get(order.getOrderNumber()));
       return order;
    }

    public static <T> void putKeyToMapGeneric(Map<String, ArrayList<T>> map, T obj, String key) {

        ArrayList<T> value = map.containsKey(key) ? map.get(key) : new ArrayList<>();
        value.add(obj);
        map.put(key, value);
    }
}