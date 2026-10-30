package com.example.demo;

public class BorrowRequest {
    private User user;      // 谁在操作：至少要有 username 和 password
    private Book book;      // 借/还哪本书：传 id 或 name 都行
    private String action;  // "borrow" 或 "return"

    public User getUser() { return user; }
    public Book getBook() { return book; }
    public String getAction() { return action; }
    public void setUser(User user) { this.user = user; }
    public void setBook(Book book) { this.book = book; }
    public void setAction(String action) { this.action = action; }
}