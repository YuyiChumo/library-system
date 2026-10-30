package com.example.demo.controller;

import com.example.demo.dto.BorrowRequest;
import com.example.demo.service.BorrowService;
import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    public Result<Boolean> borrowOrReturn(@RequestBody BorrowRequest request) {
        if ("borrow".equals(request.getAction())) {
            return borrowService.borrowBook(request);
        } else if ("return".equals(request.getAction())) {
            return borrowService.returnBook(request);
        }
        return Result.fail("未知操作，请传 borrow 或 return", false);
    }
}