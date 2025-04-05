package com.example.demo.controller;


import java.util.Optional;


import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class AuthController {

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index() {
        return "Login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute LoginRequest loginRequest, Model model, HttpSession session) {
        Optional<User> userOpt = userService.findByUserName(loginRequest.getUserName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                session.setAttribute("loggedInUser", user);
                return "redirect:/products/user?userId=" + user.getUserId();
            } else {
                model.addAttribute("error", "Mật khẩu sai!");
                return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Không tìm thấy tài khoản!");
            return "redirect:/";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/home")
    public String getHomePage(@RequestParam("userId") int userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "home";
    }

}

