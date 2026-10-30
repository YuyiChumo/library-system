package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BorrowService {
    private final BookRepository bookRepository;
    private final UserService userService;
    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowService(BookRepository bookRepository, UserService userService, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional
    public Result<Boolean> borrowBook(BorrowRequest request) {
        // 1. 校验用户身份（普通用户即可，不需要管理员）
        User dbUser = checkUser(request);
        if (dbUser == null) return Result.fail("账号或密码错误", false);

        // 2. 查书
        Book requestBook = request.getBook();
        if (requestBook == null) return Result.fail("书籍信息不能为空", false);
        Book dbBook = findBook(requestBook);
        if (dbBook == null) return Result.fail("书籍不存在", false);

        // 3. 判断库存（必须 > 0）
        if (dbBook.getStock() <= 0) {
            return Result.fail("库存不足，无法借阅", false);
        }

        // 4. 扣库存
        dbBook.setStock(dbBook.getStock() - 1);
        bookRepository.save(dbBook);

        // 5. 生成借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setUser(dbUser);
        record.setBook(dbBook);
        record.setBorrowTime(LocalDateTime.now());
        borrowRecordRepository.save(record);

        return Result.success("借书成功", true);
    }

    @Transactional
    public Result<Boolean> returnBook(BorrowRequest request) {
        // 1. 校验用户身份
        User dbUser = checkUser(request);
        if (dbUser == null) return Result.fail("账号或密码错误", false);

        // 2. 查书
        Book requestBook = request.getBook();
        if (requestBook == null) return Result.fail("书籍信息不能为空", false);
        Book dbBook = findBook(requestBook);
        if (dbBook == null) return Result.fail("书籍不存在", false);

        // 3. 查未归还的借阅记录
        BorrowRecord record = borrowRecordRepository.findByUserAndBookAndReturnTimeIsNull(dbUser, dbBook);
        if (record == null) {
            return Result.fail("您并没有借阅这本书，或者已经归还了", false);
        }

        // 4. 恢复库存
        dbBook.setStock(dbBook.getStock() + 1);
        bookRepository.save(dbBook);

        // 5. 更新记录归还时间
        record.setReturnTime(LocalDateTime.now());
        borrowRecordRepository.save(record);

        return Result.success("还书成功", true);
    }

    // 用请求里的 User 去库里核对账号密码，成功返回库里的 User，失败返回 null
    private User checkUser(BorrowRequest request) {
        if (request == null || request.getUser() == null) return null;
        User user = request.getUser();
        return userService.checkUser(user.getUsername(), user.getPassword());
    }

    // 请求里的 Book 只当“线索”：优先按 id 找，没给 id 再按书名找
    private Book findBook(Book requestBook) {
        if (requestBook.getId() != null) {
            Book byId = bookRepository.getBookById(requestBook.getId());
            if (byId != null) return byId;
        }
        String name = requestBook.getName();
        if (name == null || name.trim().isEmpty()) return null;
        return bookRepository.findByName(name.trim());
    }
}