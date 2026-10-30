package com.example.demo.dto;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;

public class BookRequest {
    private Book book;
    private User user;

    public Book getBook() { return book; }
    public User getUser() { return user; }
    public void setBook(Book book) { this.book = book; }
    public void setUser(User user) { this.user = user; }

}