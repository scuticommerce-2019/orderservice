package com.scuticommerce.orderservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.scuticommerce.orderservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Component
public class DataIngestion {

    @Autowired
    OrderService orderService;

    public void importData(String folderPath) throws IOException {

        try (Stream<Path> paths = Files.walk(Paths.get(
                folderPath))) {
              paths.filter(Files::isRegularFile).forEach(p -> readFiles(p, false));
        }

    }

    private Consumer<? super Path> readFiles(Path path, boolean createFile) {

        String filePath = path.toString();
        String fileName = path.getFileName().toString();
        String jsonFile = fileName.substring(0,fileName.length() - 4 )+ ".json";
        System.out.println("JSON " + jsonFile);
        System.out.println("CSV  " + filePath);

        try {

            ObjectMapper mapper = new ObjectMapper();

            String jsonPayload = convertCSVToJson(filePath,jsonFile, createFile);

            List<?> myObjects =
                    mapper.readValue(jsonPayload, new TypeReference<List<?>>(){});

            System.out.println(myObjects);
            for (Object map :myObjects) {

                LinkedHashMap data = (LinkedHashMap)map;

                Order order = getOrder(data);

                orderService.createOrder(order);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private Order getOrder(LinkedHashMap data) {

        Order order = new Order();
        order.setEmail((String)data.get("Email"));
        order.setOrderNumber((String)data.get("OrderNumber"));
        order.setOrderTotal((String)data.get("OrderTotal"));
        order.setItemTotal((String)data.get("ItemTotal"));
        order.setTaxTotal((String)data.get("TaxTotal"));
        order.setShippingTotal((String)data.get("ShippingTotal"));
        order.setHandlingTotal((String)data.get("HandlingTotal"));
        order.setStatus((String)data.get("Status"));
        order.setShipDate((String)data.get("ShipDate"));
        order.setTrackingNumber((String)data.get("TrackingNumber"));
        order.setShippingMethod((String)data.get("ShippingMethod"));
        order.setCouponCode((String)data.get("CouponCode"));
        order.setDiscountTotal((String)data.get("DiscountTotal"));
        return order;
    }

    private static String convertCSVToJson(String csvFile, String jsonFile, boolean createFile) throws IOException {

        File input = new File(csvFile);
        File output = new File(jsonFile);

        CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
        CsvMapper csvMapper = new CsvMapper();

        // Read data from CSV file
        List<?> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();

        ObjectMapper mapper = new ObjectMapper();

        // Write JSON formated data to output.json file
        if(createFile) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(output, readAll);
        }

        // Write JSON formated data to stdout
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readAll);
    }

}
