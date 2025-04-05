package com.example.demo.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "[User]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(unique = true, nullable = false)
    private String password;
    private String userName;
    private boolean deleteFlg;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Orders> orders;

    public User(String userName, int userId, String password, boolean deleteFlg, List<Orders> orders) {
        this.userName = userName;
        this.userId = userId;
        this.password = password;
        this.deleteFlg = deleteFlg;
        this.orders = orders;
    }

    public User() {
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(boolean deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", deleteFlg=" + deleteFlg +
                ", orders=" + orders +
                '}';
    }
}
