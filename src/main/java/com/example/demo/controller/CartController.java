package com.example.demo.controller;


import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carts")
public class CartController {
 @Autowired
    private CartService cartService;
 @Autowired
 ProductService productService;


}
