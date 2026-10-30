package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 谁借的
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 借了哪本书
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private LocalDateTime borrowTime;
    private LocalDateTime returnTime; // 为空表示还没还，有值表示已归还

    public BorrowRecord() {}

    public User getUser() { return user; }
    public Integer getId() { return id; }
    public Book getBook() { return book; }
    public LocalDateTime getBorrowTime() { return borrowTime; }
    public LocalDateTime getReturnTime() { return returnTime; }
    public void setUser(User user) { this.user = user; }
    public void setId(Integer id) { this.id = id; }
    public void setBook(Book book) { this.book = book; }
    public void setBorrowTime(LocalDateTime borrowTime) { this.borrowTime = borrowTime; }
    public void setReturnTime(LocalDateTime returnTime) { this.returnTime = returnTime; }
}