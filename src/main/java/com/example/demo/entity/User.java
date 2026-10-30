package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;    // 显示名称

    @Column(nullable = false, unique = true)
    private String username;    // 账户名称

    @Column(nullable = false)
    private String password;    // 现在存明文，以后要加密就改这里

    @Column(nullable = false)
    private Boolean isAdmin;

    public User() {}

    public User(Integer id, String name, String username, String password, Boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Boolean getIsAdmin() { return isAdmin; }
    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }
}
