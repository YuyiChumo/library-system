package com.example.demo;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserService userService;

    public BookService(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
    }

    public Result<List<Book>> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        if (bookList.isEmpty()) {
            return Result.success("当前还没有图书", bookList);   // 空列表也是成功结果
        }
        return Result.success("查找成功", bookList);
    }

    public Result<Book> getBookById(Integer id) {
        Book book =  bookRepository.getBookById(id);
        if(book == null) {
            return Result.fail("书籍不存在", null);
        }
        return Result.success("查找成功", book);
    }

    public Result<Boolean> addBook(BookRequest bookRequest) {
        // 1. 请求本身不能为空
        if (bookRequest == null || bookRequest.getBook() == null || bookRequest.getUser() == null) {
            return Result.fail("数据不完整", false);
        }
        Book book = bookRequest.getBook();
        User user = bookRequest.getUser();

        // 2. 校验操作人：账号存在、密码正确、并且是管理员（具体逻辑在 UserService 里）
        Result<Boolean> auth = userService.checkAdmin(user.getUsername(), user.getPassword());
        if (!Boolean.TRUE.equals(auth.getData())) {
            return Result.fail(auth.getMessage(), false);   // 用户不存在 / 密码错误 / 不是管理员
        }

        // 3. 校验书籍信息（注意：新增时 id 由数据库自动生成，所以不检查 id）
        if (book.getName() == null || book.getName().trim().isEmpty()) {
            return Result.fail("书名不能为空", false);
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            return Result.fail("作者不能为空", false);
        }
        if (book.getStock() == null || book.getStock() < 0) {
            return Result.fail("库存不能为空，也不能是负数", false);
        }
        // signum() 返回正负号：<= 0 就表示价格是 0 或负数
        if (book.getPrice() == null || book.getPrice().signum() <= 0) {
            return Result.fail("价格必须大于 0", false);
        }

        // 书名统一用去掉首尾空格后的值，省得后面反复 trim
        String name = book.getName().trim();

        // 4. 同名的书不要重复添加
        if (bookRepository.existsByName(name)) {
            return Result.fail("《" + name + "》已经存在了", false);
        }

        // 5. 保存：new 一个新的 Book，只复制需要的字段，忽略前端传进来的 id
        Book newBook = new Book();
        newBook.setName(name);
        newBook.setAuthor(book.getAuthor().trim());
        newBook.setStock(book.getStock());
        newBook.setPrice(book.getPrice());
        try {
            bookRepository.save(newBook);
        } catch (DataIntegrityViolationException e) {
            // 两个人同时加同一本书时，上面的 existsByName 可能都通过了，
            // 这时由 Book.name 上的唯一约束兜底，这里接住异常返回友好的提示
            return Result.fail("《" + name + "》已经存在了", false);
        }

        return Result.success("添加成功", true);
    }

}
