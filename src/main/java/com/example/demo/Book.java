package com.example.demo;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // unique = true：数据库层面也不允许两本同名书，Service 里的 existsByName 只是提前拦一下
    @Column(nullable = false, unique = true)
    private String name;
    private String author;
    private Integer stock;
    private BigDecimal price;

    public Book() {}

    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public Integer getStock() { return stock; }
    public BigDecimal getPrice() { return price; }
    public void setName(String name) { this.name = name; }
    public void setId(Integer id) { this.id = id; }
    public void setAuthor(String author) { this.author = author; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
