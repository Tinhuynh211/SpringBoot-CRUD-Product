package com.example.demo.controller;


import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderItemService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Map<Integer, List<Product>> userCarts = new HashMap<>();
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "productList";
    }


    @GetMapping("/user")
    public String getProductsPage(@RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("products", productService.findAll());
        return "productList";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("userId") int userId, @RequestParam("productId") int productId, Model model) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        List<Product> cart = userCarts.getOrDefault(userId, new ArrayList<>());
        cart.add(product);
        userCarts.put(userId, cart);
        System.out.println(userCarts);
        return "redirect:/products/user?userId=" + userId;
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        List<Product> cart = userCarts.getOrDefault(userId, new ArrayList<>());

        if (cart.isEmpty()) {
            List<OrderItem> orderItems = orderItemRepository.findByOrdersUserUserId(userId);

            if (orderItems.isEmpty()) {
                model.addAttribute("message", "Giỏ hàng của bạn trống!");
                return "Cart";
            }

            int currentOrderId = orderItems.get(0).getOrders().getOrderId();
            List<OrderItem> currentOrderItems = orderItemRepository.findByOrdersOrderId(currentOrderId);
            BigDecimal totalAmountItem = currentOrderItems.stream()
                    .map(orderItem -> orderItem.getPrize().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);


            model.addAttribute("orderItems", currentOrderItems);
            model.addAttribute("totalAmountItem", totalAmountItem);
            model.addAttribute("message", "Giỏ hàng của bạn trống! Vui lòng thêm sản phẩm vào giỏ.");
            return "Cart";
        }

        BigDecimal totalAmount = cart.stream()
                .map(product -> product.getProductPrice().multiply(BigDecimal.valueOf(1))) 
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);  

        for (Product product : cart) {
            OrderItem existingOrderItem = orderItemRepository.findByOrdersAndProduct(order, product);

            if (existingOrderItem != null) {
                existingOrderItem.setQuantity(existingOrderItem.getQuantity() + 1);
                existingOrderItem.setPrize(product.getProductPrice().multiply(BigDecimal.valueOf(existingOrderItem.getQuantity())));
                orderItemRepository.save(existingOrderItem); 
            } else {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(1);  
                orderItem.setPrize(product.getProductPrice());
                orderItem.setDeleteFlg(false);  
                orderItem.setOrders(order);
                orderItemRepository.save(orderItem); 
            }
        }

        userCarts.put(userId, new ArrayList<>());  

        List<OrderItem> orderItems = orderItemRepository.findByOrdersOrderId(order.getOrderId());
        model.addAttribute("orderItems", orderItems);

        BigDecimal totalAmountItem = orderItems.stream()
                .map(orderItem -> orderItem.getPrize().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalAmountItem", totalAmountItem);

        return "Cart"; 
    }






}








