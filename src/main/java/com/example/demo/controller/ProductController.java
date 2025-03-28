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
        // Lấy thông tin người dùng
        User user = userService.getUserById(userId);

        // Lấy danh sách các OrderItem của người dùng từ bảng OrderItem
        List<OrderItem> orderItems = orderItemRepository.findByOrdersUserUserId(userId);

        // Kiểm tra xem danh sách OrderItem có rỗng không
        if (orderItems.isEmpty()) {
            model.addAttribute("message", "Không có sản phẩm trong giỏ hàng.");
            return "Cart"; // Trả về trang giỏ hàng nếu không có sản phẩm
        }

        // Lấy orderId và userId từ OrderItem và Orders
        List<Integer> orderIds = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderIds.add(orderItem.getOrders().getOrderId()); // Lấy orderId từ Orders
        }

        // Lấy totalAmount cho mỗi OrderItem
        BigDecimal totalAmountItem = orderItems.stream()
                .map(orderItem -> orderItem.getPrize().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Đưa thông tin vào model để hiển thị trong view
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmountItem", totalAmountItem);
        model.addAttribute("userId", userId);
        model.addAttribute("orderIds", orderIds); // Truyền orderIds cho view

        return "Cart";  // Trả về trang Cart
    }




}








