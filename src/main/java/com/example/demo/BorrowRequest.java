package com.example.demo;

public class BorrowRequest {
    private String username;
    private String password;
    private String bookName;
    private String action; // "borrow" 或 "return"

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getBookName() { return bookName; }
    public String getAction() { return action; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public void setAction(String action) { this.action = action; }
}