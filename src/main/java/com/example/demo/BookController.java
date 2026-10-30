package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    BookController(BookService bookService) { this.bookService = bookService; }

    @PostMapping("/add")
    public Result<Boolean> addBook(@RequestBody BookRequest bookRequest) { return bookService.addBook(bookRequest); }

    // 获取所有图书
    @GetMapping("/all")
    public Result<List<Book>> getAllBooks() {
        return bookService.getAllBooks();
    }

    // 根据 ID 查图书
    @GetMapping("/{id}")
    public Result<Book> getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id);
    }

}
