package com.drool.practice.controller;

import com.drool.practice.model.Order;
import com.drool.practice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/getDiscount")
    public Order getDiscount(@RequestBody Order order) {
        return orderService.getDiscountForOrderV2(order);
    }
}
